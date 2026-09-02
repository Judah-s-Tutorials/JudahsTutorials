package com.acmemail.judah.battleship.sandbox;

import java.util.List;
import java.util.stream.IntStream;

import javax.swing.JOptionPane;

import com.acmemail.judah.battleship.Configurator;
import com.acmemail.judah.battleship.Fleet;
import com.acmemail.judah.battleship.Result;
import com.acmemail.judah.battleship.artwork.awt.GridFrame;
import com.acmemail.judah.battleship.artwork.awt.GridWindowParent;
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
    private static GridFrame        gridFrame;
    private static GridWindowParent parent; 

    public static void main(String[] args)
    {
        Grid2D  grid    = new Grid2D();
        IntStream.range( 1, 2 )
            .mapToObj( i -> "Opponent " + i )
            .forEach( Grid2D::new );
        gridFrame = GridFrame.getFrame( () -> new GridWindowParent() );
        parent = (GridWindowParent)gridFrame.getClient();

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
        parent.repaint();
    }
    
    private static void showResult( Result result )
    {
        String  messages    = String.join( "\n", result.getMessages() );
        JOptionPane.showMessageDialog( null, messages );
        System.exit( 1 );
    }
}
