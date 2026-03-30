package com.acmemail.judah.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Rectangle;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GridTest
{
    private static final int    numRows     = Grid.getNumRows();
    private static final int    numCols     = Grid.getNumCols();
    private static final Grid   homeGrid    = Grid.getHomeGrid();
    private static final Grid   namedGrid   = new Grid( "Test" );
    
    private static final Class<BattleshipException>  excClass   = 
        BattleshipException.class;

    
    @BeforeAll
    public static void beforeAll()
    {
        ShipType.registerDefaultTypes();
    }
    
    @BeforeEach
    public void beforeEach()
    {
        Fleet.removeAllShips();
        homeGrid.clean();
    }
        
    @Test
    void testGrid()
    {
        // The default constructor is private in Grid.java.
        // It is automatically called when the class is loaded.
        // No separate test is needed.
    }
    
    @Test
    public void testGridString()
    {
        String  name    = "test name";
        Grid    grid    = new Grid( name );
        assertEquals( name, grid.getName() );
        
        assertThrows( excClass, () -> new Grid( name ) );
    }
    
    @Test
    public void testGetGrid()
    {
        String  validName       = "this is the name of a grid";
        String  invalidName     = "this is not the name of a grid";
        new Grid( validName );
        Grid    actGrid         = Grid.getGrid( validName );
        assertNotNull( actGrid );
        assertEquals( validName, actGrid.getName() );
        assertNull( Grid.getGrid( invalidName ) );
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
        testAttackGridCoords( homeGrid );
        testAttackGridCoords( namedGrid );
    }

    @Test
    void testPutShipGoRight()
    {
        ShipType    type    = 
            ShipType.getShipType( Constants.DEF_BATTLESHIP_NAME );
        int         hXco    = 0;
        int         hYco    = 0;
        GridCoords  hCoords = new GridCoords( hXco, hYco );
        int         vXco    = hXco + 1;
        int         vYco    = hYco + 1;
        GridCoords  vCoords = new GridCoords( vXco, vYco );
        Ship        hShip   = 
            new Ship( type, hCoords, Orientation.HORIZONTAL );
        Ship        vShip   = 
            new Ship( type, vCoords, Orientation.VERTICAL );
        
        homeGrid.put( hShip );
        homeGrid.put( vShip );
        verifyGridRect( hShip );
        verifyGridRect( vShip );
    }

    @Test
    void testPutShipOutOfBounds()
    {
        ShipType    type    = 
            ShipType.getShipType( Constants.DEF_BATTLESHIP_NAME );
        int         len     = type.getLength();
        int         hXco    = numCols - len + 1;
        int         hYco    = 0;
        GridCoords  hCoords = new GridCoords( hXco, hYco );
        int         vXco    = 0;
        int         vYco    = numRows - len + 1;
        GridCoords  vCoords = new GridCoords( vXco, vYco );
        Ship        hShip   = 
            new Ship( type, hCoords, Orientation.HORIZONTAL );
        Ship        vShip   = 
            new Ship( type, vCoords, Orientation.VERTICAL );
        
        assertThrows( excClass, () -> homeGrid.put( hShip ) );
        assertThrows( excClass, () -> homeGrid.put( vShip ) );
    }

    @Test
    void testPutShipIntersection()
    {
        ShipType    type    = 
            ShipType.getShipType( Constants.DEF_BATTLESHIP_NAME );
        int         hXco    = numCols / 2;
        int         hYco    = numRows / 2;
        GridCoords  hCoords = new GridCoords( hXco, hYco );
        int         vXco    = hXco + 1;
        int         vYco    = hYco - 1;
        GridCoords  vCoords = new GridCoords( vXco, vYco );
        Ship        hShip   = 
            new Ship( type, hCoords, Orientation.HORIZONTAL );
        Ship        vShip   = 
            new Ship( type, vCoords, Orientation.VERTICAL );
        
        Fleet.add( hShip );
        assertThrows( excClass, () -> Fleet.add( vShip ) );
        Fleet.remove( hShip );
        Fleet.add( vShip );
        assertThrows( excClass, () -> homeGrid.put( hShip ) );
    }

    @Test
    void testRemoveShip()
    {
        ShipType    type    = 
            ShipType.getShipType( Constants.DEF_BATTLESHIP_NAME );
        int         hXco    = 0;
        int         hYco    = 0;
        GridCoords  hCoords = new GridCoords( hXco, hYco );
        int         vXco    = hXco + 1;
        int         vYco    = hYco + 1;
        GridCoords  vCoords = new GridCoords( vXco, vYco );
        Ship        hShip   = 
            new Ship( type, hCoords, Orientation.HORIZONTAL );
        Ship        vShip   = 
            new Ship( type, vCoords, Orientation.VERTICAL );
        
        homeGrid.put( hShip );
        homeGrid.put( vShip );
        
        verifyGridRect( hShip );
        Fleet.remove( hShip );
        verifyNotInGrid( hShip );

        verifyGridRect( vShip );
        Fleet.remove( vShip );
        verifyNotInGrid( vShip );
    }

    @Test
    void testIsSplattedGridCoords()
    {
        for ( int yco = 0 ; yco < numRows ; ++yco )
            for ( int xco = 0 ; xco < numCols ; ++xco )
            {
                GridCoords  coords  = new GridCoords( xco, yco );
                assertFalse( homeGrid.isSplatted( coords ) );
                homeGrid.attack( coords );
                assertTrue( homeGrid.isSplatted( coords ) );
            }
    }

    /**
     * @see #testIsValidCoord()
     */
    @Test
    void testIsValidCoordIntInt()
    {
        // testing this method is integrated with testIsValidCoord
    }

    @Test
    void testIsValidCoord()
    {
        for ( int yco = 0 ; yco < numRows ; ++yco )
            for ( int xco = 0 ; xco < numCols ; ++xco )
            {
                GridCoords  coords  = new GridCoords( xco, yco );
                assertTrue( Grid.isValidCoord( coords ) );
                assertTrue( Grid.isValidCoord( xco, yco ) );
            }
        GridCoords[]    invalidCoords   =
        {
            new GridCoords( 0, -1 ),
            new GridCoords( -1, 0 ),
            new GridCoords( 0, numRows ),
            new GridCoords( numCols, 0 ),
        };
        for ( GridCoords coords : invalidCoords )
        {
            int xco = coords.getXco();
            int yco = coords.getYco();
            assertFalse( Grid.isValidCoord( coords ) );
            assertFalse( Grid.isValidCoord( xco, yco ) );
        }
    }

    @Test
    void testGetIntInt()
    {
        for ( int yco = 0 ; yco < numRows ; ++yco )
            for ( int xco = 0 ; xco < numCols ; ++xco )
            {
                Cell    cell    = homeGrid.get( xco, yco );
                assertEquals( xco, cell.getXco() );
                assertEquals( yco, cell.getYco() );
            }
    }

    @Test
    void testGetObject()
    {
        for ( int yco = 0 ; yco < numRows ; ++yco )
            for ( int xco = 0 ; xco < numCols ; ++xco )
            {
                GridCoords  coords  = new GridCoords( xco, yco );
                Cell        cell    = homeGrid.get( coords );
                assertEquals( xco, cell.getXco() );
                assertEquals( yco, cell.getYco() );
            }
        GridCoords[]    invalidCoords   =
        {
            new GridCoords( 0, -1 ),
            new GridCoords( -1, 0 ),
            new GridCoords( 0, numRows ),
            new GridCoords( numCols, 0 ),
        };
        for ( GridCoords coords : invalidCoords )
            assertThrows( excClass, () -> homeGrid.get( coords ) );
    }

    @Test
    void testPutCell()
    {
        int     xco     = numCols / 2;
        int     yco     = numCols / 2;
        Cell    cell    = homeGrid.get( xco, yco );
        assertFalse( cell.isSplatted() );
        cell.setSplatted( true );
        homeGrid.put( cell );
        cell = homeGrid.get( xco, yco );
        assertTrue( cell.isSplatted() );
    }

    @Test
    void testPutGridCoordsCell()
    {
        int         xco     = numCols / 2;
        int         yco     = numCols / 2;
        GridCoords  coords  = new GridCoords( xco, yco );
        Cell    cell    = homeGrid.get( coords );
        assertFalse( cell.isSplatted() );
        cell.setSplatted( true );
        homeGrid.put( coords, cell );
        cell = homeGrid.get( coords );
        assertTrue( cell.isSplatted() );
    }

    @Test
    void testGetNumRows()
    {
        int numRows = Grid.getNumRows();
        assertEquals( GridTest.numRows, numRows );
    }

    @Test
    void testGetNumCols()
    {
        int numCols = Grid.getNumCols();
        assertEquals( GridTest.numCols, numCols );
    }

    @Test
    void testEvaluateBoundsGridCoords()
    {
        for ( int yco = 0 ; yco < numRows ; ++yco )
            for ( int xco = 0 ; xco < numCols ; ++xco )
            {
                GridCoords      coords  = new GridCoords( xco, yco );
                List<String>    errors  = Grid.evaluateBounds( coords );
                assertEquals( 0, errors.size() );
            }
        GridCoords[]    invalidCoords   =
        {
            new GridCoords( 0, -1 ),
            new GridCoords( -1, 0 ),
            new GridCoords( 0, numRows ),
            new GridCoords( numCols, 0 ),
        };
        for ( GridCoords coords : invalidCoords )
        {
            List<String>    errors  = Grid.evaluateBounds( coords );
            assertTrue( errors.size() > 0 );
        }
    }

    @Test
    void testEvaluateBoundsShipHorizontal()
    {
        ShipType    type        = 
            ShipType.getShipType( Constants.DEF_BATTLESHIP_NAME );
        int         shipLen     = type.getLength();
        int         lastCol     = numCols - shipLen + 1;
        Orientation orientation = Orientation.HORIZONTAL;
        
        // Verify all possible valid cells in the grid
        for ( int yco = 0 ; yco < numRows ; ++yco )
            for ( int xco = 0 ; xco < lastCol ; ++xco )
            {
                GridCoords      coords  = new GridCoords( xco, yco );
                Ship            ship    = 
                    new Ship( type, coords, orientation );
                List<String>    errors  = Grid.evaluateBounds( ship );
                assertEquals( 0, errors.size() );
            }
        // Test the tail of every row, starting with the column at which
        // the ship goes out of bounds.
        for ( int yco = 0 ; yco < numRows ; ++yco )
            for ( int xco = lastCol + 1 ; xco < numCols ; ++xco )
            {
                GridCoords      coords  = new GridCoords( xco, yco );
                Ship            ship    = 
                    new Ship( type, coords, orientation );
                List<String>    errors  = Grid.evaluateBounds( ship );
                assertTrue( errors.size() > 0 );
            }
    }

    @Test
    void testEvaluateBoundsShipVertical()
    {
        ShipType    type        = 
            ShipType.getShipType( Constants.DEF_BATTLESHIP_NAME );
        int         shipLen     = type.getLength();
        int         lastRow     = numRows - shipLen + 1;
        Orientation orientation = Orientation.VERTICAL;
        
        // Verify all possible valid cells in the grid
        for ( int yco = 0 ; yco < lastRow ; ++yco )
            for ( int xco = 0 ; xco < numCols ; ++xco )
            {
                GridCoords      coords  = new GridCoords( xco, yco );
                Ship            ship    = 
                    new Ship( type, coords, orientation );
                List<String>    errors  = Grid.evaluateBounds( ship );
                assertEquals( 0, errors.size() );
            }
        // Test the tail of every column, starting with the row at which
        // the ship goes out of bounds.
        for ( int yco = lastRow + 1 ; yco < numRows ; ++yco )
            for ( int xco = 0 ; xco < numCols ; ++xco )
            {
                GridCoords      coords  = new GridCoords( xco, yco );
                Ship            ship    = 
                    new Ship( type, coords, orientation );
                List<String>    errors  = Grid.evaluateBounds( ship );
                assertTrue( errors.size() > 0 );
            }
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
        
        assertThrows( excClass, () -> grid.attack( numCols, numRows ) );
    }
    
    private void testAttackGridCoords( Grid grid )
    {
        for ( int yco = 0 ; yco < numRows ; ++yco )
            for ( int xco = 0 ; xco < numCols ; ++xco )
            {
                Cell        cell    = grid.get( xco, yco );
                GridCoords  coords  = new GridCoords( xco, yco );
                assertFalse( cell.isSplatted() );
                grid.attack( coords );
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
                assertFalse( homeGrid.isSplatted( xco, yco ) );
                cell.setSplatted( true );
                assertTrue( cell.isSplatted() );
                assertTrue( grid.isSplatted( xco, yco ) );
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
    
    private void verifyGridRect( Ship ship )
    {
        Rectangle   rect    = ship.getRect();
        for ( int yco = 0 ; yco < numRows ; yco++ )
            for ( int xco = 0 ; xco < numCols ; xco ++ )
            {
                Cell    cell    = homeGrid.get( xco, yco );
                Ship    actShip = cell.getShip();
                if ( rect.contains( xco, yco ) )
                    assertEquals( ship, actShip );
                else
                    assertNotEquals( ship, actShip );
            }
    }
    
    /**
     * Verify that a given ship is not represented in the Grid.
     * 
     * @param ship  the given ship
     */
    private void verifyNotInGrid( Ship ship )
    {
        for ( int yco = 0 ; yco < numRows ; yco++ )
            for ( int xco = 0 ; xco < numCols ; xco ++ )
            {
                Cell    cell    = homeGrid.get( xco, yco );
                Ship    actShip = cell.getShip();
                assertNotEquals( ship, actShip );
            }
    }
}
