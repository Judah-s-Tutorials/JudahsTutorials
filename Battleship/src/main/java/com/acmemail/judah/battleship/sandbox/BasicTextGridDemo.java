package com.acmemail.judah.battleship.sandbox;

import java.util.List;

import javax.swing.JOptionPane;

import com.acmemail.judah.battleship.Configurator;
import com.acmemail.judah.battleship.Fleet;
import com.acmemail.judah.battleship.Result;
import com.acmemail.judah.battleship.artwork.text.BasicTextGrid;
import com.acmemail.judah.battleship.model.Grid2D;
import com.acmemail.judah.battleship.model.GridCoords;
import com.acmemail.judah.battleship.model.Orientation;
import com.acmemail.judah.battleship.model.Ship2D;
import com.acmemail.judah.battleship.model.ShipTypes;
import com.acmemail.judah.battleship.model.default_ship_types.Battleship;
import com.acmemail.judah.battleship.model.default_ship_types.Destroyer;
import com.acmemail.judah.battleship.model.default_ship_types.Submarine;

/**
 * This is a simple application
 * that configures a grid
 * and visualizes if via {@link BasicTextGrid}.
 */
public class BasicTextGridDemo
{
    /**
     * Default constructor.
     * Not used.
     */
    private BasicTextGridDemo()
    {
        // not used
    }
    
    /**
     * Application entry point.
     * 
     * @param args  command-line arguments, not used
     */
    public static void main( String[] args )
    {
        Grid2D              grid            = Grid2D.getHomeGrid();
        
        ShipTypes.registerDefaultTypes();
        Fleet   fleet   = new Fleet();
        fleet.addToBeDeployed( Battleship.getType(), "ship 1" );
        fleet.addToBeDeployed( Destroyer.getType(), "ship 2" );
        fleet.addToBeDeployed( Submarine.getType(), "ship 3" );
        
        Configurator.nextState();
        
        List<Fleet.Proto>   toBeDeployed    = fleet.getToBeDeployedProtos();
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
        grid.attack( new GridCoords( 4, 4 ) );
        
        BasicTextGrid   textGrid    = new BasicTextGrid( grid );
        List<String>    artWork     = textGrid.toPrintString();
        artWork.forEach( System.out::println );
    }
        
    /**
     * Format and print the contents
     * of a given Result object.
     * 
     * @param result    the given Result object
     */
    private static void showResult( Result result )
    {
        String  messages    = String.join( "\n", result.getMessages() );
        JOptionPane.showMessageDialog( null, messages );
        System.exit( 1 );
    }
}
