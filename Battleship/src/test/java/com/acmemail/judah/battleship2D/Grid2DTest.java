package com.acmemail.judah.battleship2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        ShipTypes.register( twoDType );
        ShipTypes.register( oneDType );
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
    public void testIsSplattedIntInt()
    {
        Grid2D      grid        = new Grid2D();
        Rectangle   gridBounds  = grid.getBounds();

        RectUtils.getInteriorCoords( gridBounds )
            .forEach( c -> assertFalse( grid.isSplatted( c ) ) );
        
        // Make a rectangle that is a proper subset of the grid bounds
        Rectangle   rect    = getSubrect( gridBounds, 2 );
        RectUtils.getInteriorCoords( rect )
            .forEach( grid::attack );
        
        RectUtils.getInteriorCoords( rect )
            .forEach( c -> assertTrue( grid.isSplatted( c ), c.toString() ) );
        
        RectUtils.subtract( gridBounds, rect )
            .forEach( c -> assertFalse( grid.isSplatted( c ), c.toString() ) );
        
    }

    @Test
    public void testGetShipIntInt()
    {
        int         xco         = 3;
        int         yco         = 5;
        GridCoords  coords      = new GridCoords( xco, yco ); 
        Ship2D      ship        = 
            new Ship2D( twoDType, coords, Orientation.VERTICAL );
        Grid2D      grid        = new Grid2D();
        Rectangle   gridBounds  = grid.getBounds();
        grid.put( ship );
        
        Rectangle   shipBounds  = ship.getBounds();
        RectUtils.getInteriorCoords( shipBounds )
            .map( c -> grid.getShip( c.xco(), c.yco() ) )
            .forEach( s -> assertEquals( ship, s ) );
        
        RectUtils.difference( gridBounds, shipBounds )
            .filter( p -> !shipBounds.contains( p ) )
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
        Grid2D      grid        = new Grid2D();
        Rectangle   bounds      = grid.getBounds();
        Rectangle   superBounds =
            new Rectangle( 
                bounds.x - 1, 
                bounds.y - 1, 
                bounds.width + 2, 
                bounds.height + 2 
            );
        RectUtils.getInteriorPoints( superBounds )
            .forEach( point -> {
                GridCoords  coords  = RectUtils.coordsOf( point );
                boolean     hasPoint    = bounds.contains( point );
                boolean     hasCoords   = grid.contains( coords );
                String      comment = point.toString();
                assertEquals( hasPoint, hasCoords, comment );
            });
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

    @ParameterizedTest
    @MethodSource( "allProtoShips" )
    public void testIntersectsExistingTrivial( Ship2D ship )
    {
        // When the grid is empty, no ship will intersect any other ship
        Grid2D      grid        = new Grid2D();
        Rectangle   gridBounds  = grid.getBounds();
        RectUtils.getAllValidPoints( gridBounds, ship.getBounds() )
            .map( RectUtils::coordsOf )
            .map( c -> getNewShip( ship, c ) )
            .forEach( s -> assertFalse( grid.intersectsExisting( s ) ) );
    }

    @ParameterizedTest
    @MethodSource( "allProtoShips" )
    public void testIntersectsExisting( Ship2D ship )
    {
        Grid2D      grid        = new Grid2D();
        Rectangle   gridBounds  = grid.getBounds();
        int         squareSide  = squareType.breadth();
        int         centerXco   = (int)gridBounds.getCenterX();
        int         centerYco   = (int)gridBounds.getCenterY();
        
        // Position two ships, approximately in the center of the grid,
        // horizontal ship on the left, vertical ship on the right.
        int         shipYco     = centerYco - squareSide / 2;
        int         leftXco     = centerXco - squareSide - 2;
        int         rightXco    = centerXco + 2;
        GridCoords  leftCoords  = new GridCoords( leftXco, shipYco );
        GridCoords  rightCoords = new GridCoords( rightXco, shipYco );
        
        // Note: ship is square; horizontal/vertical not relevant
        Ship2D      leftShip    = 
            new Ship2D( squareType, "square", leftCoords, Orientation.HORIZONTAL );
        Rectangle   leftBounds  = leftShip.getBounds();
        Ship2D      rightShip   = 
            new Ship2D( squareType, rightCoords, Orientation.VERTICAL );
        Rectangle   rightBounds = rightShip.getBounds();
        // sanity check
        assertTrue( gridBounds.contains( leftBounds ) );
        assertTrue( gridBounds.contains( rightBounds ) );
        assertFalse( leftBounds.intersects( rightBounds ) );
        assertTrue( grid.contains( leftShip ) );
        assertTrue( grid.contains( rightShip ) );

        grid.put( leftShip );
        grid.put( rightShip );
        
        Rectangle   shipBounds  = ship.getBounds();
        RectUtils.getAllValidPoints( gridBounds, shipBounds )
            .map( p -> RectUtils.coordsOf( p ) )
            .map( c -> getNewShip( ship, c ) )
            .filter( s -> !leftBounds.intersects( s.getBounds()) )
            .filter( s -> !rightBounds.intersects( s.getBounds()) )
            .forEach( s -> assertFalse( grid.intersectsExisting( s ) ) );
        
        RectUtils.getAllValidPoints( gridBounds, ship.getBounds() )
            .map( p -> RectUtils.coordsOf( p ) )
            .map( c -> getNewShip( ship, c ) )
            .filter( s -> 
                leftBounds.intersects( s.getBounds() ) || rightBounds.intersects( s.getBounds() )
            )
            .forEach( s -> assertTrue( grid.intersectsExisting( s ) ) );
    }

    @ParameterizedTest
    @MethodSource( "allProtoShips" )
    public void testPutShipTrivial( Ship2D ship )
    {
        // 
        Grid2D      grid        = new Grid2D();
        Rectangle   gridBounds  = grid.getBounds();
        Rectangle   shipBounds  = ship.getBounds();
        RectUtils.getAllValidPoints( gridBounds, shipBounds)
            .map( RectUtils::coordsOf )
            .map( c -> getNewShip( ship, c ) )
            .forEach( s -> { 
                grid.put( s );
                GridCoords  coords      = s.getCoords();
                Ship2D      testShip    = 
                    grid.getShip( coords.xco(), coords.yco() );
                assertEquals( s, testShip );
                grid.clear();
            });
    }

    @Test
    public void testRemove()
    {
        Grid2D          grid        = new Grid2D();
        Rectangle       bounds      = grid.getBounds();
        ShipType2D      type        = squareType;
        int             rightXco    = bounds.x + bounds.width - type.length();
        int             bottomYco   = bounds.y + bounds.height - type.breadth();
        
        // Generate coordinates to place ship at corners of grid
        GridCoords[]    gridCoords  = 
        {
            new GridCoords( bounds.x, bounds.y ),
            new GridCoords( rightXco, bounds.y ),
            new GridCoords( bounds.x, bottomYco ),
            new GridCoords( rightXco, bottomYco )
        };
        // Sanity check
        for ( GridCoords coords : gridCoords )
            assertTrue( grid.contains( coords ), coords.toString() );
        
        List<Ship2D>    shipSource  = new ArrayList<>();
        for ( GridCoords coords : gridCoords )
        {
            Ship2D  ship    = new Ship2D( type, coords, Orientation.HORIZONTAL );
            //// Begin sanity check
            assertTrue( grid.contains( ship ), ship.toString() );
            for ( Ship2D testShip : shipSource )
                assertFalse( ship.intersects( testShip ), testShip.toString() );
            //// End sanity check
            shipSource.add( ship );
            grid.put( ship );
        }
        
        for ( Ship2D ship : shipSource )
        {
            assertEquals( ship, grid.getShip( ship.getCoords() ) );
            grid.remove( ship );
            RectUtils.getInteriorCoords( ship.getBounds() )
                .forEach( c -> assertNull( grid.getShip( c ), c.toString() ) );
            // Verify that a ship can be added at the vacated coordinates
            grid.put( ship );
        }
    }

    @Test
    public void testIsSplattedGridCoords()
    {
        Grid2D      grid        = new Grid2D();
        Rectangle   gridBounds  = grid.getBounds();
        
        // None of the coords should be splatted yet
        RectUtils.getInteriorCoords( gridBounds )
            .forEach( c -> assertFalse( grid.isSplatted( c ) ) );
        
        // Splat all the corners of the grid bounds
        Rectangle[] corners = getCorners( gridBounds, 2, 2, true );
        for ( Rectangle corner : corners )
        {
            RectUtils.getInteriorCoords( corner )
                .forEach( grid::attack );
        }
        
        // Make sure rects, and only rects, are seen as splatted
        RectUtils.getInteriorCoords( gridBounds )
            .forEach( c -> 
                assertEquals( contains( c, corners ), grid.isSplatted( c ) )
            );
    }

    @ParameterizedTest
    @MethodSource( "allProtoShips" )
    public void testIsSunk( Ship2D ship )
    {
        Grid2D  grid    = new Grid2D();
        getAllValidShips( ship ).forEach( s -> {
            grid.clear();
            grid.put( ship );
            testIsSunk( ship, grid );
        });
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
        Grid2D  home    = Grid2D.getHomeGrid();
        assertNull( home );
        Grid2D  grid    = new Grid2D();
        home = Grid2D.getHomeGrid();
        assertEquals( grid, home );
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
     * Given a reference rectangle and exclusion parameter,
     * create a rectangle that is contained in the reference rectangle,
     * and excludes the given number of rows and columns.
     * Raises an assertion if the new rectangle
     * does not contain at least one point.
     * 
     * @param refRect   reference rectangle
     * @param exclude   exclusion parameter
     * 
     * @return the created rectangle
     */
    private static Rectangle getSubrect( Rectangle refRect, int exclude )
    {
        Rectangle   rect    =
            new Rectangle(
                refRect.x + exclude,
                refRect.y + exclude,
                refRect.width - 2 * exclude,
                refRect.height - 2 * exclude
            );
        // sanity check
        assert( rect.x < gridBounds.width );
        assert( rect.y < gridBounds.height );
        assert( rect.width > 1 );
        assert( rect.height > 1 );

        return rect;
    }
    
    /**
     * Attack the cells in a ship until it is sunk.
     * Verify that grid.isSunk() returns false
     * until the last cell is attacked,
     * then returns true.
     * 
     * @param ship  the ship to attack
     * @param grid  the grid that the ship lives in
     */
    private static void testIsSunk( Ship2D ship, Grid2D grid )
    {
        Rectangle           bounds  = ship.getBounds();
        List<GridCoords>    list    = 
            RectUtils.getInteriorCoords( bounds ).toList();
        int                 lastInx = list.size() - 1;
        for ( int inx = 0 ; inx < lastInx ; ++inx )
        {
            GridCoords  coords  = list.get( inx );
            assertFalse( grid.isSplatted( coords ) );
            grid.attack( list.get( inx ) );
            assertTrue( grid.isSplatted( coords ) );
            assertFalse( grid.isSunk( ship ) );
        }
        grid.attack( list.get( lastInx ) );
        assertTrue( grid.isSunk( ship ) );
    }

    /**
     * Given a reference ship and grid coordinates,
     * create a new ship with the same properties 
     * as the reference ship,
     * but with new grid coordinates.
     * 
     * @param refShip   the reference ship
     * @param coords    the new coordinates
     * @return
     */
    private static Ship2D getNewShip( Ship2D refShip, GridCoords coords )
    {
        ShipType2D  type    = refShip.getType();
        String      name    = refShip.getName();
        Orientation orient  = refShip.getOrientation();
        Ship2D      newShip = new Ship2D( type, name, coords, orient );
        return newShip;
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
        Stream<Ship2D>  stream  = RectUtils.getInteriorCoords( gridBounds )
            .map( c -> new Rectangle( RectUtils.ofGridCoords( c ), dim ) )
            .filter( r -> gridBounds.contains( r ) )
            .map( r -> RectUtils.coordsOf( r ) )
            .map( c -> new Ship2D( type, c, orient) );
        return stream;
    }
    
    /**
     * Get Rectangles nested in the corners of a given rectangle.
     * If validate is true,
     * we validate that each corner is properly nested
     * in the given rectangle,
     * and that none of the rectangles 
     * intersect each other.
     * 
     * @param rect      the given rectangle
     * @param width     the width of each nested rectangle
     * @param height    the height of each nested rectangle
     * @param validate  true if validation is required
     * 
     * @return  the generated array of rectangles
     */
    private static Rectangle[] getCorners( 
        Rectangle rect, 
        int width, 
        int height,
        boolean validate
    )
    {
        int         rightXco    = rect.x + rect.width - width;
        int         lowerYco    = rect.y + rect.height - height;
        Rectangle[] corners     =
        {
            new Rectangle( rect.x, rect.y, width, height ),
            new Rectangle( rightXco, rect.y, width, height ),
            new Rectangle( rect.x, lowerYco, width, height ),
            new Rectangle( rightXco, lowerYco, width, height )
        };
        
        if ( validate )
        {
            for ( Rectangle corner : corners )
            {
                assertTrue( rect.contains( corner ) );
                for ( Rectangle test : corners )
                {
                    if ( corner != test )
                        assertFalse( corner.intersects( test ) );
                }
            }
                
        }
        return corners;
    }
    
    /**
     * Return true if the given coordinates are contained
     * in any Rectangle in a given array of Rectangles.
     * 
     * @param coords    the given coordinates
     * @param rects     the given array
     * 
     * @return  true if coords is contained in any element of rects
     */
    private static boolean contains( GridCoords coords, Rectangle[] rects )
    {
        Point   point   = RectUtils.ofGridCoords( coords );
        Boolean result  =
            Arrays.stream( rects )
                .filter( r -> r.contains( point ) )
                .findFirst().orElse( null ) != null;
        return result;
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
