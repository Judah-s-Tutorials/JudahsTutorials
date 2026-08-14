package com.acmemail.judah.battleship2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class Cell2DTest
{
    private static final String         testTypeName    = "test type";
    private static final String         testShipName    = "test ship";
    private static final ShipType2D     testType        =
        new ShipType2D( testTypeName, 10, 5, null );
    private static final int            defXco          = 10;
    private static final int            defYco          = 9;
    private static final GridCoords     defCoords       = 
        new GridCoords( defXco, defYco );
    private static final Orientation    defOrientation  = Orientation.HORIZONTAL;
    
    @BeforeAll
    public static void beforeAll()
    {
        ShipTypes.register( testType );
    }
    
    @Test
    void testCell2DShip()
    {
        Ship2D  ship    = getShip();
        Cell2D  cell    = new Cell2D( defCoords, ship );
        assertEquals( ship, cell.getShip() );
        assertEquals( defCoords, ship.getCoords() );
        assertEquals( defCoords, cell.getCoords() );
        
        Ship2D      nullShip    = null;
        GridCoords  nullCoords  = null;
        assertThrows( NullPointerException.class, () -> 
            new Cell2D( defCoords, nullShip ) 
        );
        assertThrows( NullPointerException.class, () -> 
            new Cell2D( nullCoords, ship ) 
        );
    }
    
    @Test
    void testCell2DCoords()
    {
        Cell2D  cell    = new Cell2D( defCoords );
        assertNull( cell.getShip() );
        assertEquals( defCoords, cell.getCoords() );
        
        GridCoords  nullCoords  = null;
        assertThrows( NullPointerException.class, 
            () -> new Cell2D( nullCoords ) 
        );
    }

    @Test
    void testIsSplatted()
    {
        Cell2D  cellNoShip  = new Cell2D( defCoords );
        Cell2D  cellShip    = new Cell2D( defCoords, getShip() );
        assertFalse( cellNoShip.isSplatted() );
        assertFalse( cellShip.isSplatted() );

        cellNoShip.setSplatted();
        cellShip.setSplatted();
        assertTrue( cellNoShip.isSplatted() );
        assertTrue( cellShip.isSplatted() );
    }

    @Test
    void testSetSplatted()
    {
        Cell2D  cell    = new Cell2D( defCoords, getShip() );
        assertFalse( cell.isSplatted() );
        cell.setSplatted();
        assertTrue( cell.isSplatted() );
    }

    @Test
    void testGetCoords()
    {
        GridCoords  coords  = new GridCoords( defXco + 1, defYco + 1 );
        Cell2D  cellNoShip  = new Cell2D( coords );
        Cell2D  cellShip    = new Cell2D( defCoords, getShip() );
        assertEquals( coords, cellNoShip.getCoords() );
        assertEquals( defCoords, cellShip.getCoords() );
    }

    @Test
    void testGetShip()
    {
        Ship2D  testShip    = getShip();
        Cell2D  cellNoShip  = new Cell2D( defCoords );
        Cell2D  cellShip    = new Cell2D( defCoords, getShip() );
        assertNull( cellNoShip.getShip() );
        assertEquals( testShip, cellShip.getShip() );
    }

    @Test
    void testToString()
    {
        Ship2D  unNamedShip     = 
            new Ship2D( testType, defCoords, defOrientation );
        Cell2D  cellNoShip      = new Cell2D( defCoords );
        Cell2D  cellNamedShip   = new Cell2D( defCoords, getShip() );
        Cell2D  cellunNamedShip = new Cell2D( defCoords, unNamedShip );
        
        String  noShipStr       = cellNoShip.toString();
        String  namedShipStr    = cellNamedShip.toString();
        String  unNamedShipStr  = cellunNamedShip.toString();
        
        String  coordsXcoPhrase = "xco=" + defXco;
        String  coordsYcoPhrase = "yco=" + defYco;
        String  shipPhrase      = "ship=";
        String  splattedPhrase  = "splatted=";
        
        assertTrue( noShipStr.contains( coordsXcoPhrase ) );
        assertTrue( namedShipStr.contains( coordsXcoPhrase ) );
        assertTrue( unNamedShipStr.contains( coordsXcoPhrase ) );
        
        assertTrue( noShipStr.contains( coordsYcoPhrase ) );
        assertTrue( namedShipStr.contains( coordsYcoPhrase ) );
        assertTrue( unNamedShipStr.contains( coordsYcoPhrase ) );
        
        assertTrue( noShipStr.contains( shipPhrase + "null" ) );
        assertTrue( namedShipStr.contains( shipPhrase + testShipName ) );
        assertTrue( unNamedShipStr.contains( shipPhrase ) );
        
        assertTrue( noShipStr.contains( splattedPhrase + "false" ) );
        assertTrue( namedShipStr.contains( splattedPhrase + "false" ) );
        assertTrue( unNamedShipStr.contains( splattedPhrase + "false" ) );
        
        cellNoShip.setSplatted();
        cellNamedShip.setSplatted();
        cellunNamedShip.setSplatted();

        noShipStr = cellNoShip.toString();
        namedShipStr = cellNamedShip.toString();
        unNamedShipStr = cellunNamedShip.toString();

        assertTrue( noShipStr.contains( splattedPhrase + "true" ) );
        assertTrue( namedShipStr.contains( splattedPhrase + "true" ) );
        assertTrue( unNamedShipStr.contains( splattedPhrase + "true" ) );
    }

    @Test
    void testToStringNullNamedShip()
    {
        Ship2D  nullNamedShip   =
            new Ship2D( testType, null, defCoords, defOrientation );
        Cell2D  cellNoShip      = new Cell2D( defCoords );
        Cell2D  cellNullNamedShip  = new Cell2D( defCoords, nullNamedShip );

        String  noShipStr       = cellNoShip.toString();
        String  nullNamedStr    = cellNullNamedShip.toString();

        assertTrue( noShipStr.contains( "ship=null" ) );
        assertTrue( nullNamedStr.contains( "ship=<unnamed>" ) );
        assertNotEquals( noShipStr, nullNamedStr );
    }

    @Test
    void testEqualsObject()
    {
        GridCoords  coordsA = new GridCoords( defXco, defYco );
        GridCoords  coordsB = new GridCoords( defXco + 1, defYco + 1 );
        Ship2D      shipA   = new Ship2D( testType, coordsA, defOrientation );
        Ship2D      shipB   = new Ship2D( testType, coordsB, defOrientation );
        
        Cell2D      cell1   = new Cell2D( coordsA, shipA );
        Cell2D      cell2   = new Cell2D( coordsA, shipA );        
        assertNotEquals( cell1, null );
        assertNotEquals( cell1, new Object() );
        
        assertEquals( cell1, cell2 );
        assertEquals( cell2, cell1 );
        assertEquals( cell1, cell1 );
        assertEquals( cell1.hashCode(), cell2.hashCode() );

        cell1 = new Cell2D( coordsA );
        cell2 = new Cell2D( coordsB );
        assertNotEquals( cell1, cell2 );
        assertNotEquals( cell2, cell1 );

        cell1 = new Cell2D( coordsA, shipA );
        cell2 = new Cell2D( coordsB, shipB );        
        assertNotEquals( cell1, cell2 );
        assertNotEquals( cell2, cell1 );
        
        // special test for coordinates equal but ships not equal...
        // should not happen in real life
        Cell2D  cell3   = new Cell2D( coordsA, shipA );
        Cell2D  cell4   = new Cell2D( shipA.getCoords() );
        assertNotEquals( cell3, cell4 );
}

    private Ship2D getShip()
    {
        Ship2D  ship    = 
            new Ship2D( testType, testShipName, defCoords, defOrientation );
        return ship;
    }
}
