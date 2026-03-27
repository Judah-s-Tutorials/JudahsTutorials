package com.acmemail.judah.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.acmemail.judah.battleship.default_ship_types.Battleship;
import com.acmemail.judah.battleship.default_ship_types.Destroyer;

class CellTest
{
    private static final ShipType   battleshipType   = new Battleship();
    private static final ShipType   destroyerType   = new Destroyer();
    
    @Test
    void testCellGridCoords()
    {
        int         expXco  = 10;
        int         expYco  = expXco + 5;
        GridCoords  coords  = new GridCoords( expXco, expYco );
        Cell        cell    = new Cell( coords );
        testCellGridCoords( cell, expXco, expYco );
    }

    @Test
    void testCellIntInt()
    {
        int         expXco  = 10;
        int         expYco  = expXco + 5;
        Cell        cell    = new Cell( expXco, expYco );
        testCellGridCoords( cell, expXco, expYco );
    }
    
    private void testCellGridCoords( Cell cell, int expXco, int expYco )
    {
        GridCoords  expGridCoords   = new GridCoords( expXco, expYco );
        assertEquals( expXco, cell.getXco() );
        assertEquals( expYco, cell.getYco() );
        assertEquals( expGridCoords, cell.getCoords() );
        assertNull( cell.getShip() );
        assertFalse( cell.isSplatted() );
    }

    @Test
    void testGetXco()
    {
        int     expXco  = 10;
        Cell    cell    = new Cell( expXco, 0 );
        assertEquals( expXco, cell.getXco() );
    }

    @Test
    void testGetYco()
    {
        int     expYco  = 10;
        Cell    cell    = new Cell( 0, expYco );
        assertEquals( expYco, cell.getYco() );
    }

    @Test
    void testSet_IsSplatted()
    {
        Cell    cell    = new Cell( 0, 0 );
        assertFalse( cell.isSplatted() );
        cell.setSplatted( true );
        assertTrue( cell.isSplatted() );
    }

    @Test
    void testGetCoords()
    {
        int         expXco      = 10;
        int         expYco      = expXco + 5;
        GridCoords  expCoords   = new GridCoords( expXco, expYco );
        Cell        cell        = new Cell( expXco, expYco );
        assertEquals( expCoords, cell.getCoords() );
    }

    @Test
    void testHasShip()
    {
        Cell        cell    = new Cell( 0, 0 );
        assertFalse( cell.hasShip() );
        
        GridCoords  coords      = new GridCoords( 10, 10 );
        Orientation orientation = Orientation.HORIZONTAL;
        Ship        ship        = 
            new Ship( battleshipType, coords, orientation ); 
        cell.setShip( ship );
        assertTrue( cell.hasShip() );
    }

    @Test
    void testSetGetShip()
    {
        Cell        cell    = new Cell( 0, 0 );
        assertNull( cell.getShip() );
        
        GridCoords  coords      = new GridCoords( 10, 10 );
        Orientation orientation = Orientation.HORIZONTAL;
        Ship        expShip     = 
            new Ship( battleshipType, coords, orientation ); 
        cell.setShip( expShip );
        assertEquals( expShip, cell.getShip() );
    }

    @Test
    void testToString()
    {
        Cell        cell    = new Cell( 0, 0 );
        System.out.println( cell );
    }

    @Test
    public void testEquals()
    {
        GridCoords  shipLoc     = new GridCoords( 0, 0 );
        Orientation orientation = Orientation.HORIZONTAL;
        Ship        shipA       = 
            new Ship( battleshipType, shipLoc, orientation );
        Ship        shipB       = 
            new Ship( destroyerType, shipLoc, orientation );
        GridCoords  coordsA     = new GridCoords( 1, 10 );
        GridCoords  coordsB     = new GridCoords( 2, 11 );
        Object      obj         = new Object();
        
        Cell        cellA       = new Cell( coordsA );
        Cell        cellB       = new Cell( coordsB );
        
        assertTrue( cellA.equals( cellA ) );
        assertFalse( cellA.equals( null ) );
        assertFalse( cellA.equals( obj ) );
        assertFalse( cellA.equals( cellB ) );
        assertFalse( cellB.equals( cellA ) );
        
        cellB = new Cell( coordsA );
        assertTrue( cellA.equals( cellB ) );
        assertTrue( cellB.equals( cellA ) );
        assertEquals( cellA.hashCode(), cellB.hashCode() );
        
        cellA.setShip( shipA );
        assertFalse( cellA.equals( cellB ) );
        assertFalse( cellB.equals( cellA ) );
        
        cellB.setShip( shipB );
        assertFalse( cellA.equals( cellB ) );
        assertFalse( cellB.equals( cellA ) );
        
        cellB.setShip( shipA );
        assertTrue( cellA.equals( cellB ) );
        assertTrue( cellB.equals( cellA ) );
        assertEquals( cellA.hashCode(), cellB.hashCode() );
        
        cellA.setSplatted( true );
        assertFalse( cellA.equals( cellB ) );
        assertFalse( cellB.equals( cellA ) );
        
        cellB.setSplatted( true );
        assertTrue( cellA.equals( cellB ) );
        assertTrue( cellB.equals( cellA ) );
        assertEquals( cellA.hashCode(), cellB.hashCode() );
    }
}
