package com.acmemail.judah.battleship.sandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.List;
import java.util.stream.IntStream;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.acmemail.judah.battleship.Configurator;
import com.acmemail.judah.battleship.Fleet;
import com.acmemail.judah.battleship.Result;
import com.acmemail.judah.battleship2D.Cell2DView;
import com.acmemail.judah.battleship2D.Grid2D;
import com.acmemail.judah.battleship2D.GridCoords;
import com.acmemail.judah.battleship2D.Orientation;
import com.acmemail.judah.battleship2D.Ship2D;
import com.acmemail.judah.battleship2D.ShipTypes;
import com.acmemail.judah.battleship2D.default_ship_types.Battleship;
import com.acmemail.judah.battleship2D.default_ship_types.Destroyer;
import com.acmemail.judah.battleship2D.default_ship_types.Submarine;

public class GraphGridController
{
    private static GridFrame    gridFrame;
    private static Controller   controller; 

    public static void main(String[] args)
    {
        Grid2D  grid    = new Grid2D();
        gridFrame = GridFrame.getFrame( () -> new Controller( grid ) );
        controller = (Controller)gridFrame.getClient();

        ShipTypes.registerDefaultTypes();
        Fleet   fleet   = new Fleet();
        fleet.addToBeDeployed( Battleship.getType(), "ship 1" );
        fleet.addToBeDeployed( Destroyer.getType(), "ship 2" );
        fleet.addToBeDeployed( Submarine.getType(), "ship 3" );
        
        Configurator.nextState();
        
        List<Fleet.Proto>   toBeDeployed    = fleet.getToBeDeployed();
        Fleet.Proto         proto           = toBeDeployed.get( 0 );
        GridCoords          coords          = new GridCoords( 0, 0 );
        Ship2D              ship            =
            fleet.getShip( coords, "", Orientation.HORIZONTAL, proto );
        Result              result          = fleet.deploy( ship, proto );
        if ( !result.getStatus() )
            showResult( result );
        proto = toBeDeployed.get( 0 );
        coords = new GridCoords( 1, 2 );
        ship = fleet.getShip( coords, "", Orientation.VERTICAL, proto );
        result = fleet.deploy( ship, proto );
        if ( !result.getStatus() )
            showResult( result );
        proto = toBeDeployed.get( 0 );
        coords = new GridCoords( 2, 4 );
        ship = fleet.getShip( coords, "", Orientation.HORIZONTAL, proto );
        result = fleet.deploy( ship, proto );
        if ( !result.getStatus() )
            showResult( result );
        
        Configurator.nextState();
        
        grid.attack( new GridCoords( 1, 1 ) );
        grid.attack( new GridCoords( 1, 2 ) );
        grid.attack( new GridCoords( 5, 3 ) );
        grid.attack( new GridCoords( 2, 8 ) );
        controller.repaint();
    }
    
    private static void showResult( Result result )
    {
        String  messages    = String.join( "\n", result.getMessages() );
        JOptionPane.showMessageDialog( null, messages );
        System.exit( 1 );
    }
    
    public static class Controller extends JPanel
    {
        private final Color     bgColor     = Color.LIGHT_GRAY;
        private final Color     shipColor   = Color.DARK_GRAY;
        private final Color     gridColor   = Color.BLACK;
        private final Color     splatColor  = Color.RED;
        private final int       cellSide    = 30;
        private final Grid2D    grid;
        
        private final Rectangle gridBounds  = new Rectangle();
        private final Rectangle shipRect    = new Rectangle();
        
        public Controller( Grid2D grid )
        {
            this.grid = grid;
            Dimension   dim     = new Dimension( 500, 500 );
            setPreferredSize( dim );
        }
        
        @Override
        public void paint( Graphics graphics )
        {
            Graphics2D  gtx         = (Graphics2D)graphics;
            int         winWidth    = getWidth();
            int         winHeight   = getHeight();
            gtx.setColor( bgColor );
            gtx.fillRect( 0,  0, winWidth, winHeight );
            
            int         numRows         = Grid2D.getNumRows();
            int         numCols         = Grid2D.getNumCols();
            int         boundsWidth     = numCols * cellSide;
            int         boundsHeight    = numRows * cellSide;
            
            gtx.translate( 10, 10 );
            gtx.setColor( shipColor );
            for ( Ship2D ship : grid.getAllShips() )
            {
                Rectangle   bounds  = ship.getBounds();
                shipRect.x = cellSide * bounds.x;
                shipRect.y = cellSide * bounds.y;
                shipRect.width = cellSide * bounds.width;
                shipRect.height = cellSide * bounds.height;
                gtx.fill( shipRect );
            }
            
            gtx.setColor( gridColor );
            IntStream.iterate( 0, i -> i <= boundsWidth , i -> i += cellSide )
                .mapToObj( i -> new Line2D.Double( i, 0, i, boundsHeight ) )
                .forEach( gtx::draw );
            IntStream.iterate( 0, i -> i <= boundsHeight , i -> i += cellSide )
                .mapToObj( i -> new Line2D.Double( 0, i, boundsWidth, i ) )
                .forEach( gtx::draw );
            
            gtx.setColor( splatColor );
            grid.getCells()
                .filter( Cell2DView::isSplatted )
                .map ( c -> 
                    new Rectangle(
                        c.getCoords().xco() * cellSide + 3,
                        c.getCoords().yco() * cellSide + 3,
                        cellSide - 6,
                        cellSide - 6
                    )
                )
                .forEach( gtx::fill );
                    
        }
    }

}
