package com.acmemail.judah.battleship2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Objects;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.acmemail.judah.battleship.BattleshipException;
import com.acmemail.judah.battleship2DT.test_utils.RectUtils;

class Grid2DTest
{
    private static final int        width       = Grid2D.getNumCols();
    private static final int        height      = Grid2D.getNumRows();
    private static final Rectangle  gridBounds  = 
        new Rectangle( 0, 0, width, height );
    
    private static final ShipType2D squareType  =
        new ShipType2D( "Square type", 2, 2, null );
    private static final ShipType2D twoDType  =
        new ShipType2D( "2D type", 5, 3, null );
    private static final ShipType2D oneDType  =
        new ShipType2D( "1D type", 3, 1, null );
    
    @BeforeAll
    public static void beforeAll()
    {
        ShipTypes.register( squareType );
    }
    
    @BeforeEach
    public void beforeEach()
    {
        Grid2D.reset();
    }

    @Test
    public void testGrid2D()
    {
        Grid2D  grid    = new Grid2D();
        Grid2D  home    = Grid2D.getHomeGrid();
        assertEquals( grid, home );
    }

    @Test
    public void testGrid2DString()
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
    public void testGetName()
    {
        String  name1   = "Name 1";
        Grid2D  grid1   = new Grid2D( name1 );
        
        assertEquals( name1, grid1.getName() );
    }

    @Test
    public void testAttackIsSplattedIntInt()
    {
        Grid2D  grid    = new Grid2D();
        for ( int yco = 0 ; yco < height ; ++yco )
            for ( int xco = 0 ; xco < width ; ++xco )
                assertFalse( grid.isSplatted( xco, yco ) );
        
        for ( int yco = 0 ; yco < height ; ++yco )
        {
            if ( yco < width )
                grid.attack( yco, yco );
        }

        for ( int yco = 0 ; yco < height ; ++yco )
            for ( int xco = 0 ; xco < width ; ++xco )
            {
                if ( yco == xco )
                    assertTrue( grid.isSplatted( xco, yco ) );
                else
                    assertFalse( grid.isSplatted( xco, yco ) );
            }
    }

    @Test
    public void testGetShip()
    {
        int         xco     = 3;
        int         yco     = 5;
        GridCoords  coords  = new GridCoords( xco, yco ); 
        Ship2D      ship    = 
            new Ship2D( twoDType, coords, Orientation.VERTICAL );
        Grid2D      grid    = new Grid2D();
        grid.put( ship );
        
        Rectangle   bounds  = ship.getBounds();
        RectUtils.getAllCoords( bounds )
            .map( c -> grid.getShip( c.xco(), c.yco() ) )
            .forEach( s -> assertEquals( ship, s ) );
        
        RectUtils.getInteriorPoints( bounds )
            .filter( p -> !bounds.contains( p ) )
            .map( RectUtils::coordsOf )
            .map( c -> grid.getShip( c.xco(), c.yco() ) )
            .forEach( s -> assertNull( s ) );
    }

    @Test
    public void testAttackGridCoords()
    {
        Grid2D  grid    = new Grid2D();
        for ( int yco = 0 ; yco < height ; ++yco )
            for ( int xco = 0 ; xco < width ; ++xco )
            {
                GridCoords  coords  = new GridCoords( xco, yco );
                assertFalse( grid.isSplatted( coords ) );
            }
        
        for ( int yco = 0 ; yco < height ; ++yco )
        {
            if ( yco < width )
            {
                GridCoords  coords  = new GridCoords( yco, yco );
                grid.attack( coords );
            }
        }

        for ( int yco = 0 ; yco < height ; ++yco )
            for ( int xco = 0 ; xco < width ; ++xco )
            {
                GridCoords  coords  = new GridCoords( xco, yco );
                if ( yco == xco )
                    assertTrue( grid.isSplatted( coords ) );
                else
                    assertFalse( grid.isSplatted( coords ) );
            }
    }

    @Test
    public void testContainsGridCoords()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testContainsIntInt()
    {
        Grid2D  grid    = new Grid2D();
        for ( int yco = 0 ; yco < height ; ++yco )
            for ( int xco = 0 ; xco < width ; ++xco )
                assertTrue( grid.contains( xco, yco ) );
        
        for ( int xco = -1 ; xco <= width ; ++xco )
        {
            assertFalse( grid.contains( xco, -1 ) );
            assertFalse( grid.contains( xco, height ) );
        }
        
        for ( int yco = -1 ; yco <= height ; ++yco )
        {
            assertFalse( grid.contains( -1, yco ) );
            assertFalse( grid.contains( width, yco ) );
        }
    }

    @ParameterizedTest
    @MethodSource( "allProtoShips" )
    public void testContainsShip2DPos( Ship2D ship )
    {
        // For each prototype ship, validate that containsShip
        // returns true for every valid coordinate.
        Grid2D      grid        = new Grid2D();
        getAllValidShips( ship ).forEach( 
            s -> assertTrue( grid.contains( s ), s.toString() ) 
        );
    }

    @Test
    public void testIntersectsExisting()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testPutShip()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testRemove()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testIsSplattedGridCoords()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testIsSunk()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testGetNumRows()
    {
        int expNumRows  = 
            parseIntProperty( 
                Constants.KEY_NUM_ROWS, 
                Constants.DEF_NUM_ROWS
            );
        int actNumRows  = Grid2D.getNumRows();
        assertEquals( expNumRows, actNumRows );
    }

    @Test
    public void testGetNumCols()
    {
        int expNumCols  = 
            parseIntProperty( 
                Constants.KEY_NUM_COLS, 
                Constants.DEF_NUM_COLS
            );
        int actNumCols  = Grid2D.getNumCols();
        assertEquals( expNumCols, actNumCols );
    }

    @Test
    public void testGetHomeGrid()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testGetGrid()
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
    
    @Test
    public void testGetBounds()
    {
        Grid2D      grid        = new Grid2D();
        Rectangle   expBounds   = new Rectangle( 0, 0, width, height );
        Rectangle   actBounds   = grid.getBounds();
        assertNotNull( actBounds );
        assertEquals( expBounds, actBounds );
    }

    /**
     * Gets a stream of prototype ships.
     * A "prototype ship" is a ship
     * with a fixed type and orientation,
     * but an unspecified position.
     * The output stream includes horizontal and vertical instances
     * of {@code twoDType} and {@code oneDType} ships.
     * 
     * @return  a stream of prototype ships
     */
    private static Stream<Ship2D> allProtoShips()
    {
        GridCoords  tempCoords  = new GridCoords( 0, 0 );
        Ship2D  ship2DH = 
            new Ship2D( twoDType, tempCoords, Orientation.HORIZONTAL );
        Ship2D  ship2DV = 
            new Ship2D( twoDType, tempCoords, Orientation.VERTICAL );
        Ship2D  ship1DH = 
            new Ship2D( oneDType, tempCoords, Orientation.HORIZONTAL );
        Ship2D  ship1DV = 
            new Ship2D( oneDType, tempCoords, Orientation.VERTICAL );

        Stream<Ship2D>  stream  = Stream.of( ship2DH, ship2DV, ship1DH, ship1DV );
        return stream;
    }
    
    /**
     * Generate a stream of ships for each coordinate of the grid
     * that is valid for the ship.
     * The type and orientation of the ship
     * is based on the prototype
     * passed by the caller.
     * 
     * @param protoShip the ship from which to derive type and orientation
     * 
     * @return
     *      a stream of ships for each coordinate of the grid
     *      that is valid for the ship
     */
    private Stream<Ship2D> getAllValidShips( Ship2D protoShip )
    {
        Rectangle   bounds      = protoShip.getBounds();
        Dimension   dim         = new Dimension( bounds.width, bounds.height );
        ShipType2D  type        = protoShip.getType();
        Orientation orient      = protoShip.getOrientation();
        Stream<Ship2D>  stream  = RectUtils.getAllCoords( gridBounds )
            .map( c -> new Rectangle( RectUtils.ofGridCoords( c ), dim ) )
            .filter( r -> gridBounds.contains( r ) )
            .map( r -> RectUtils.coordsOf( r ) )
            .map( c -> new Ship2D( type, c, orient) );
        return stream;
    }

    /**
     * Get the integer value of a system property.
     * If the property is not present, a default is applied.
     * 
     * @param key       the property key
     * @param defVal    the value returned 
     *                  if the attempt to read the property fails
     *                  
     * @return the value of the property, or defVal
     *         if fetching the property value fails
     *         
     * @throws  NullPointerException    if key is null
     * @throws  NumberFormatException   if the value is present but invalid    
     */
    private static int parseIntProperty( String key, int defVal )
    {
        Objects.requireNonNull( key, "key" );
        int     val;
        String  strVal   = System.getProperty( key );
        if ( strVal == null )
            val = defVal;
        else
            val = Integer.parseInt( strVal );
        return val;
    }
}
