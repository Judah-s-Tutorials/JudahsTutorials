package com.acmemail.judah.battleship.sandbox;

import static com.acmemail.judah.battleship.Orientation.HORIZONTAL;
import static com.acmemail.judah.battleship.Orientation.VERTICAL;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.battleship.Fleet;
import com.acmemail.judah.battleship.Grid;
import com.acmemail.judah.battleship.GridCoords;
import com.acmemail.judah.battleship.Orientation;
import com.acmemail.judah.battleship.Ship;
import com.acmemail.judah.battleship.ShipType;
import com.acmemail.judah.battleship.artwork.GraphicalGrid;

public class AppRunnerDemo
{
    private static GraphicalGrid    graphicalGrid;
    public static void main(String[] args)
    {
        double  d1  = 0xba;
        double  d2  = 0x8e;
        double  d3  = 0x23;
        System.out.printf( "%f, %f, %f%n", d1/255, d2/255, d3/255 );
        Grid    grid    = Grid.getHomeGrid();
        
        Ship        ship;
        GridCoords  coords;
        ShipType.registerDefaultTypes();
        ShipType    battleship  = ShipType.getShipType( "Battleship" );
        ShipType    destroyer   = ShipType.getShipType( "Destroyer" );
        ShipType    submarine   = ShipType.getShipType( "Submarine" );
        
        coords = new GridCoords( 1, 1 );
        ship = new Ship( battleship, coords, HORIZONTAL );
        Fleet.add( ship );
        
        coords = new GridCoords( 3, 3 );
        ship = new Ship( destroyer, coords, VERTICAL );
        Fleet.add( ship );
        
        coords = new GridCoords( 5, 4 );
        ship = new Ship( submarine, coords, HORIZONTAL );
        Fleet.add( ship );
        
        grid.attack( 10, 8 );
        grid.attack( 4, 3 );
        grid.attack( 5, 7 );
        grid.attack( 7, 4 );
        grid.attack( 2, 1 );
        grid.attack( 3, 1 );

        SwingUtilities.invokeLater( () -> {
            JFrame  frame   = new JFrame();
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            JPanel  pane    = new JPanel( new BorderLayout() );
            frame.setContentPane( pane );
            graphicalGrid = new GraphicalGrid( grid );
            pane.add( graphicalGrid, BorderLayout.CENTER );
            frame.pack();
            frame.setVisible( true );
        });
        
        SwingUtilities.invokeLater( () -> playGhost() );
    }
    
    private static void playGhost()
    {
        int         xco = 0;
        int         yco = 1;
        Orientation orientation = HORIZONTAL;
        for ( int inx = 0 ; inx < 5 ; ++inx )
        {
            ShipType    shipType    = ShipType.getShipType( "Carrier" );
            GridCoords  gridCoords  = new GridCoords( xco, yco );
            Ship        ship        = 
                new Ship( shipType, gridCoords, orientation );
            orientation = orientation == HORIZONTAL ? VERTICAL : HORIZONTAL; 
            graphicalGrid.setGhostShip( ship );
//            graphicalGrid.update();
//            pause();
//            graphicalGrid.setGhostShip( null );
//            graphicalGrid.update();
//            pause();
//            xco += 1;
//            yco += 1;
        }
    }
    
    private static void pause()
    {
        JOptionPane.showMessageDialog( null, "next" );
    }
}
