package com.acmemail.judah.battleship.sandbox;

import com.acmemail.judah.battleship.Configurator;
import com.acmemail.judah.battleship.artwork.text.BasicTextGrid;
import com.acmemail.judah.battleship.model.Grid2D;
import com.acmemail.judah.battleship.model.GridCoords;
import com.acmemail.judah.battleship.model.Orientation;
import com.acmemail.judah.battleship.model.Ship2D;
import com.acmemail.judah.battleship.model.ShipType2D;

public class ConsoleDemo2
{
    public static void main(String[] args)
    {
        ShipType2D      testType    = new ShipType2D( "test", 4, 2, null );
        GridCoords      coords1     = new GridCoords( 0, 0 );
        Ship2D          ship1       = 
            new Ship2D( testType, coords1, Orientation.HORIZONTAL );
        GridCoords      coords2     = new GridCoords( 8, 0 );
        Ship2D          ship2       = 
            new Ship2D( testType, coords2, Orientation.VERTICAL );
        GridCoords      coords3     = new GridCoords( 6, 8 );
        Ship2D          ship3       = 
            new Ship2D( testType, coords3, Orientation.HORIZONTAL );
        GridCoords      coords4     = new GridCoords( 5, 0 );
        
        Grid2D          grid        = Grid2D.getHomeGrid();
        grid.put( ship1 );
        grid.put( ship2 );
        grid.put( ship3 );
        Configurator.nextState();
        Configurator.nextState();
        grid.attack( coords1 );
        grid.attack( coords2 );
        grid.attack( coords4 );
        BasicTextGrid   textGrid    = new BasicTextGrid( grid );
        textGrid.printGrid();
    }
}
