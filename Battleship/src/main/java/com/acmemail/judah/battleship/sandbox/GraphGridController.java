package com.acmemail.judah.battleship.sandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.stream.IntStream;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.acmemail.judah.battleship.Configurator;
import com.acmemail.judah.battleship.Fleet;
import com.acmemail.judah.battleship.Label;
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
        private final Color     labelColor  = Color.BLACK;
        private final int       cellSide    = 20;
        private final int       padding     = 10;
        private final Grid2D    grid;
        private final int       numRows;
        private final int       numCols;
        private final Rectangle gridBounds;
        
        private final Rectangle shipRect    = new Rectangle();
        private Dimension       labelBounds = null;
        
        public Controller( Grid2D grid )
        {
            this.grid = grid;
            Dimension   dim     = new Dimension( 500, 500 );
            setPreferredSize( dim );
            
            numRows = Grid2D.getNumRows();
            numCols = Grid2D.getNumCols();
            int     gridWidth   = numCols * cellSide;
            int     gridHeight  = numRows * cellSide;
            gridBounds = new Rectangle( 0, 0, gridWidth, gridHeight );
        }
        
        @Override
        public void paint( Graphics graphics )
        {
            Graphics2D  gtx         = (Graphics2D)graphics;
            int         winWidth    = getWidth();
            int         winHeight   = getHeight();
            gtx.setColor( bgColor );
            gtx.fillRect( 0,  0, winWidth, winHeight );
            
            Font        font        = gtx.getFont();
            int         size        = font.getSize();
            Font        fixedFont   =
                new Font( Font.MONOSPACED, Font.BOLD, size );
            gtx.setFont( fixedFont );
            
            paintShips( gtx );
            paintGridLines( gtx );
            paintSplats( gtx );
            paintLabels( gtx );
        }

        private void paintShips( Graphics2D gtx )
        {
            Color           saveColor       = gtx.getColor();
            AffineTransform saveTransform   = gtx.getTransform();
            int             translate       = padding + labelBounds.width;
            gtx.translate( translate, translate );
            
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

            gtx.setTransform( saveTransform );
            gtx.setColor( saveColor );
        }

        private void paintGridLines( Graphics2D gtx )
        {
            Color           saveColor       = gtx.getColor();
            AffineTransform saveTransform   = gtx.getTransform();
            int             translate       = padding + labelBounds.width;
            gtx.translate( translate, translate );
            gtx.setColor( gridColor );

            int             gridWidth       = gridBounds.width;
            int             gridHeight      = gridBounds.height;
            IntStream.iterate( 0, i -> i <= gridWidth , i -> i += cellSide )
                .mapToObj( i -> new Line2D.Double( i, 0, i, gridHeight ) )
                .forEach( gtx::draw );
            IntStream.iterate( 0, i -> i <= gridHeight , i -> i += cellSide )
                .mapToObj( i -> new Line2D.Double( 0, i, gridWidth, i ) )
                .forEach( gtx::draw );

            gtx.setTransform( saveTransform );
            gtx.setColor( saveColor );
        }
        
        private void paintSplats( Graphics2D gtx )
        {
            Color           saveColor       = gtx.getColor();
            AffineTransform saveTransform   = gtx.getTransform();
            int             translate       = padding + labelBounds.width;
            gtx.translate( translate, translate );
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

            gtx.setTransform( saveTransform );
            gtx.setColor( saveColor );
        }

        private void paintLabels( Graphics2D gtx )
        {
            Color           saveColor       = gtx.getColor();
            AffineTransform saveTransform   = gtx.getTransform();
            int             translate       = padding + labelBounds.width;
            gtx.translate( translate, translate );
            gtx.setColor( labelColor );

            FontRenderContext   context     = gtx.getFontRenderContext();
            Font                font        = gtx.getFont();
            
            for ( int row = 0 ; row < numRows ; ++row )
            {
                Label       label       = new Label( 0, row );
                String      strLabel    = label.getRowStr();
                Rectangle2D strBounds   = 
                    font.getStringBounds( strLabel, context );
                int         iWidth      = (int)strBounds.getWidth();
                int         iHeight     = (int)strBounds.getHeight();
                int         xco         = -iWidth - 5;
                int         yco         = 
                    cellSide * (row + 1) - cellSide / 2 + iHeight / 2 - 1;
                gtx.drawString( strLabel, xco, yco );
            }
            
//            gtx.rotate( Math.PI / 2 );
            for ( int col = 0 ; col < numCols ; ++col )
            {
                Label       label       = new Label( col, 0 );
                String      strLabel    = label.getColStr();
                Rectangle2D strBounds   = 
                    font.getStringBounds( strLabel, context );
                int         iWidth      = (int)strBounds.getWidth();
                int         iHeight     = (int)strBounds.getHeight();
                int         xco         = 
                    cellSide * col + cellSide / 2 - iWidth / 2;
                int         yco         = -5;
                gtx.drawString( strLabel, xco, yco );
            }
            
            gtx.setTransform( saveTransform );
            gtx.setColor( saveColor );
        }
        
        @Override
        public void addNotify()
        {
            super.addNotify();
            Font        font        = getFont();
            FontMetrics fontMetrics = getFontMetrics( font );
            labelBounds = getMaxLabelBounds( font, fontMetrics );
        }
        
        private static Dimension getMaxLabelBounds( 
            Font font, 
            FontMetrics fontMetrics 
        )
        {
            int         height      = 
                fontMetrics.getMaxAscent() + fontMetrics.getMaxDescent();
            int         width       = 2 * fontMetrics.getMaxAdvance();
            Dimension   dim         = new Dimension( width, height );
            return dim;
        }
    }
}
