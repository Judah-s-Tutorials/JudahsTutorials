package com.acmemail.judah.battleship.sandbox;

import static com.acmemail.judah.battleship.Orientation.HORIZONTAL;
import static com.acmemail.judah.battleship.Orientation.VERTICAL;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.battleship.Fleet;
import com.acmemail.judah.battleship.Grid;
import com.acmemail.judah.battleship.GridCoords;
import com.acmemail.judah.battleship.Ship;
import com.acmemail.judah.battleship.ShipType;
import com.acmemail.judah.battleship.artwork.GraphicalGrid;

public class AppRunnerDemo
{
    public static void main(String[] args)
    {
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
            pane.add( new GraphicalGrid( grid ), BorderLayout.CENTER );
            frame.pack();
            frame.setVisible( true );
        });
    }
}
