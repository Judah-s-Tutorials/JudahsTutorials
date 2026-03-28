package com.acmemail.judah.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class GridTest
{
    private static final int    numRows     = Grid.getNumRows();
    private static final int    numCols     = Grid.getNumCols();
    private static final Grid   homeGrid    = Grid.getHomeGrid();
    private static final Grid   namedGrid   = new Grid( "Test" );
    
    @BeforeAll
    public static void beforeAll()
    {
        ShipType.registerDefaultTypes();
        Fleet.removeAllShips();
        for ( int yco = 0 ; yco < numRows ; ++yco )
            for ( int xco = 0 ; xco < numCols ; ++xco )
            {
                Cell    cell    = homeGrid.get( xco, yco );
                assertNull( cell.getShip() );
                cell.setSplatted( false );
            }
    }
        
    @Test
    void testGrid()
    {
        // The default constructor is private in Grid.java.
        // It is automatically called when the class is loaded.
        // No separate test is needed.
    }

    @Test
    void testGetHomeGrid()
    {
        assertNotNull( homeGrid );
    }

    @Test
    void testIsSplattedIntInt()
    {
        testIsSplattedIntInt( homeGrid );
        testIsSplattedIntInt( namedGrid );
    }

    @Test
    void testGetShip()
    {
        ShipType    type        = 
            ShipType.getShipType( Constants.DEF_BATTLESHIP_NAME );
        assertNotNull( type );
        GridCoords  coords      = new GridCoords( 1, 1 );
        Orientation orientation = Orientation.HORIZONTAL;
        Ship        expShip     = new Ship( type, coords, orientation );
        
        int         minXco      = expShip.getMinX();
        int         maxXco      = expShip.getMaxX();
        int         minYco      = expShip.getMinY();
        int         maxYco      = expShip.getMaxY();
        for ( int yco = minYco ; yco < maxYco ; ++yco )
            for ( int xco = minXco ; xco < maxXco ; ++xco )
            {
                Ship    ship    = homeGrid.getShip( xco, yco );
                assertNull( ship );
            }

        Fleet.add( expShip );
        for ( int yco = minYco ; yco < maxYco ; ++yco )
            for ( int xco = minXco ; xco < maxXco ; ++xco )
                assertEquals( expShip, homeGrid.getShip( xco, yco ) );
        
        Fleet.remove( expShip );
        for ( int yco = minYco ; yco < maxYco ; ++yco )
            for ( int xco = minXco ; xco < maxXco ; ++xco )
            {
                Ship    ship    = homeGrid.getShip( xco, yco );
                assertNull( ship );
            }
    }

    @Test
    void testAttackIntInt()
    {
        testAttackIntInt( homeGrid );
        testAttackIntInt( namedGrid );
    }

    @Test
    void testAttackGridCoords()
    {
        fail("Not yet implemented");
    }

    @Test
    void testPutShip()
    {
        fail("Not yet implemented");
    }

    @Test
    void testRemoveShip()
    {
        fail("Not yet implemented");
    }

    @Test
    void testIsSplattedGridCoords()
    {
        fail("Not yet implemented");
    }

    @Test
    void testIsValidCoordIntInt()
    {
        fail("Not yet implemented");
    }

    @Test
    void testIsValidCoordGridCoords()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetIntInt()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetObject()
    {
        fail("Not yet implemented");
    }

    @Test
    void testPutCell()
    {
        fail("Not yet implemented");
    }

    @Test
    void testPutGridCoordsCell()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetNumRows()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetNumCols()
    {
        fail("Not yet implemented");
    }

    @Test
    void testEvaluateBoundsGridCoords()
    {
        fail("Not yet implemented");
    }

    @Test
    void testEvaluateBoundsShip()
    {
        fail("Not yet implemented");
    }
    
    private void testAttackIntInt( Grid grid )
    {
        for ( int yco = 0 ; yco < numRows ; ++yco )
            for ( int xco = 0 ; xco < numCols ; ++xco )
            {
                Cell    cell    = grid.get( xco, yco );
                assertFalse( cell.isSplatted() );
                grid.attack( xco, yco );
            }

        for ( int yco = 0 ; yco < numRows ; ++yco )
            for ( int xco = 0 ; xco < numCols ; ++xco )
            {
                Cell    cell    = grid.get( xco, yco );
                assertTrue( cell.isSplatted() );
                cell.setSplatted( false );
            }
    }

    private void testIsSplattedIntInt( Grid grid )
    {
        for ( int yco = 0 ; yco < numRows ; ++yco )
            for ( int xco = 0 ; xco < numCols ; ++xco )
            {
                Cell    cell    = grid.get( xco, yco );
                assertFalse( cell.isSplatted() );
                cell.setSplatted( true );
                assertTrue( cell.isSplatted() );
            }
        for ( int yco = 0 ; yco < numRows ; ++yco )
            for ( int xco = 0 ; xco < numCols ; ++xco )
            {
                Cell    cell    = grid.get( xco, yco );
                assertTrue( cell.isSplatted() );
                cell.setSplatted( false );
                assertFalse( cell.isSplatted() );
            }
    }

}
