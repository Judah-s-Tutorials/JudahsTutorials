package com.acmemail.judah.battleship.sandbox;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.acmemail.judah.battleship.Cell;
import com.acmemail.judah.battleship.GridCoords;
import com.acmemail.judah.battleship.Orientation;
import com.acmemail.judah.battleship.Ship;
import com.acmemail.judah.battleship.ShipType;
import com.acmemail.judah.battleship.default_ship_types.Battleship;

public class UnmodifiableMapDemo
{
    private static int                  colLen  = 100;
    private static int                  rowLen  = 100;
    private static Map<GridCoords,Cell> testMap = new HashMap<>();
    public static void main(String[] args)
    {
        for ( int yco = 0  ; yco < rowLen ; ++yco )
            for ( int xco = 0 ; xco < colLen ; ++xco )
            {
                GridCoords  coords  = new GridCoords( xco, yco );
                Cell        cell    = new Cell( coords );
                testMap.put( coords, cell );
            }
        Map<GridCoords,Cell> unmodifiableMap    =
            Collections.unmodifiableMap( testMap );
        try
        {
            test1( unmodifiableMap );
            System.out.println( "Test1: No exception thrown" );
        }
        catch ( UnsupportedOperationException exc )
        {
            System.out.println( "Test1: " + exc );
        }

        try
        {
            test2( unmodifiableMap );
            System.out.println( "Test2: No exception thrown" );
        }
        catch ( UnsupportedOperationException exc )
        {
            System.out.println( "Test2: " + exc );
        }
    }
    
    private static void test1( Map<GridCoords,Cell> unmodifiableMap )
    {
        int         xco     = colLen / 2;
        int         yco     = rowLen / 2;
        GridCoords  coords  = new GridCoords( xco, yco );
        Cell        cell    = new Cell( coords );
        unmodifiableMap.put( coords, cell );
    }
    
    private static void test2( Map<GridCoords,Cell> unmodifiableMap )
    {
        int         xco         = colLen / 2;
        int         yco         = rowLen / 2;
        GridCoords  coords      = new GridCoords( xco, yco );
        Cell        cell        = unmodifiableMap.get( coords );
        ShipType    type        = new Battleship();
        Orientation orientation = Orientation.HORIZONTAL;
        Ship        ship    = new Ship( type, coords, orientation );
        cell.setShip( ship );
    }
}
