package com.acmemail.judah.battleship2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.battleship.BattleshipException;

class Grid2DTest
{
    private static final int    numRows = Grid2D.getNumRows();
    private static final int    numCols = Grid2D.getNumCols();
    
    @BeforeEach
    public void beforeEach()
    {
        Grid2D.reset();
    }

    @Test
    void testGrid2D()
    {
        Grid2D  grid    = new Grid2D();
        Grid2D  home    = Grid2D.getHomeGrid();
        assertEquals( grid, home );
    }

    @Test
    void testGrid2DString()
    {
        String  name1   = "Name 1";
        String  name2   = "Name 2";
        String  name3   = "Name 3";
        Grid2D  grid1   = new Grid2D( name1 );
        Grid2D  grid2   = new Grid2D( name2 );
        Grid2D  grid3   = new Grid2D( name3 );
        
        assertEquals( name1, grid1.getName() );
        assertEquals( name2, grid2.getName() );
        assertEquals( name3, grid3.getName() );
        
        assertEquals( grid1, Grid2D.getGrid( name1 ) );
        assertEquals( grid2, Grid2D.getGrid( name2 ) );
        assertEquals( grid3, Grid2D.getGrid( name3 ) );
        
        for ( String name : new String[] { name1, name2, name3 } )
        {
            try
            {
                new Grid2D( name );
                fail( "Expected exception not thrown: " + name );
            }
            catch ( BattleshipException exc )
            {
                String  message = exc.getMessage();
                assertNotNull( message );
                assertTrue( message.contains( name ) );
            }
        }
    }

    @Test
    void testGetName()
    {
        String  name1   = "Name 1";
        Grid2D  grid1   = new Grid2D( name1 );
        
        assertEquals( name1, grid1.getName() );
    }

    @Test
    void testAttackIsSplattedIntInt()
    {
        Grid2D  grid    = new Grid2D();
        for ( int yco = 0 ; yco < numRows ; ++yco )
            for ( int xco = 0 ; xco < numCols ; ++xco )
                assertFalse( grid.isSplatted( xco, yco ) );
        
        for ( int yco = 0 ; yco < numRows ; ++yco )
        {
            if ( yco < numCols )
                grid.attack( yco, yco );
        }

        for ( int yco = 0 ; yco < numRows ; ++yco )
            for ( int xco = 0 ; xco < numCols ; ++xco )
            {
                if ( yco == xco )
                    assertTrue( grid.isSplatted( xco, yco ) );
                else
                    assertFalse( grid.isSplatted( xco, yco ) );
            }
    }

    @Test
    void testGetShip()
    {
        fail("Not yet implemented");
    }

    @Test
    void testAttackGridCoords()
    {
        Grid2D  grid    = new Grid2D();
        for ( int yco = 0 ; yco < numRows ; ++yco )
            for ( int xco = 0 ; xco < numCols ; ++xco )
            {
                GridCoords  coords  = new GridCoords( xco, yco );
                assertFalse( grid.isSplatted( coords ) );
            }
        
        for ( int yco = 0 ; yco < numRows ; ++yco )
        {
            if ( yco < numCols )
            {
                GridCoords  coords  = new GridCoords( yco, yco );
                grid.attack( coords );
            }
        }

        for ( int yco = 0 ; yco < numRows ; ++yco )
            for ( int xco = 0 ; xco < numCols ; ++xco )
            {
                GridCoords  coords  = new GridCoords( xco, yco );
                if ( yco == xco )
                    assertTrue( grid.isSplatted( coords ) );
                else
                    assertFalse( grid.isSplatted( coords ) );
            }
    }

    @Test
    void testContainsGridCoords()
    {
        fail("Not yet implemented");
    }

    @Test
    void testContainsIntInt()
    {
        Grid2D  grid    = new Grid2D();
        for ( int yco = 0 ; yco < numRows ; ++yco )
            for ( int xco = 0 ; xco < numCols ; ++xco )
                assertTrue( grid.contains( xco, yco ) );
        
        for ( int xco = -1 ; xco <= numCols ; ++xco )
        {
            assertFalse( grid.contains( xco, -1 ) );
            assertFalse( grid.contains( xco, numRows ) );
        }
        
        for ( int yco = -1 ; yco <= numRows ; ++yco )
        {
            assertFalse( grid.contains( -1, yco ) );
            assertFalse( grid.contains( numCols, yco ) );
        }
    }

    @Test
    void testContainsShip2D()
    {
        fail("Not yet implemented");
    }

    @Test
    void testIntersectsExisting()
    {
        fail("Not yet implemented");
    }

    @Test
    void testPutShip()
    {
        fail("Not yet implemented");
    }

    @Test
    void testRemove()
    {
        fail("Not yet implemented");
    }

    @Test
    void testIsSplattedGridCoords()
    {
        fail("Not yet implemented");
    }

    @Test
    void testIsSunk()
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
    void testGetHomeGrid()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetGrid()
    {
        String  name1   = "Name 1";
        String  name2   = "Name 2";
        String  name3   = "Name 3";
        Grid2D  grid1   = new Grid2D( name1 );
        Grid2D  grid2   = new Grid2D( name2 );
        Grid2D  grid3   = new Grid2D( name3 );
        
        assertEquals( grid1, Grid2D.getGrid( name1 ) );
        assertEquals( grid2, Grid2D.getGrid( name2 ) );
        assertEquals( grid3, Grid2D.getGrid( name3 ) );
        
        String  name1X  = name1 + 'X';
        String  name2X  = name2 + 'X';
        String  name3X  = name3 + 'X';
        assertNull( Grid2D.getGrid( name1X ) );
        assertNull( Grid2D.getGrid( name2X ) );
        assertNull( Grid2D.getGrid( name3X ) );
    }

}
