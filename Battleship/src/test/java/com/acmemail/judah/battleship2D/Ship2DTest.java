package com.acmemail.judah.battleship2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.acmemail.judah.battleship2DT.test_utils.RectUtils;

class Ship2DTest
{
    private static final String TEST_TYPE_NAME2D        = "testType2D";
    private static final String TEST_TYPE_NAME1D        = "testType1D";
    private static final int    TEST_TYPE_LEN           = 10;
    private static final int    TEST_TYPE_BREADTH       = 5;
    private static final Image  TEST_TYPE_IMAGE         =
        new BufferedImage( 10, 10, BufferedImage.TYPE_INT_RGB );
    private static final ShipType2D TEST_TYPE2D         =
        new ShipType2D(
            TEST_TYPE_NAME2D, 
            TEST_TYPE_LEN, 
            TEST_TYPE_BREADTH, 
            TEST_TYPE_IMAGE
        );
    private static final ShipType2D TEST_TYPE1D         =
        new ShipType2D(
            TEST_TYPE_NAME1D, 
            TEST_TYPE_LEN, 
            1, 
            TEST_TYPE_IMAGE
        );
    private static final int        SQUARE_SIDE         = 2;
    private static final ShipType2D TEST_TYPE_SQUARE    =
        new ShipType2D( "Test Square", SQUARE_SIDE, SQUARE_SIDE, null );
    
    private static final int        ship2DHXco  = 3;
    private static final int        ship2DHYco  = 4;  
    private static final GridCoords ship2DHCo   =
        new GridCoords( ship2DHXco, ship2DHYco );
    private static final String     ship2DHName = "ship2DH";
    private static final Ship2D     ship2DH     =
        new Ship2D( TEST_TYPE2D, ship2DHName, ship2DHCo, Orientation.HORIZONTAL );
    
    private static final int        ship2DVXco  = 3;
    private static final int        ship2DVYco  = 4;  
    private static final GridCoords ship2DVCo   =
        new GridCoords( ship2DVXco, ship2DVYco );
    private static final String     ship2DVName = "ship2DV";
    private static final Ship2D     ship2DV     =
        new Ship2D( TEST_TYPE2D, ship2DVName, ship2DVCo, Orientation.VERTICAL );
    
    private static final int        ship1DHXco  = 3;
    private static final int        ship1DHYco  = 4;  
    private static final GridCoords ship1DHCo   =
        new GridCoords( ship1DHXco, ship1DHYco );
    private static final String     ship1DHName = "ship1DH";
    private static final Ship2D     ship1DH     =
        new Ship2D( TEST_TYPE1D, ship1DHName, ship1DHCo, Orientation.HORIZONTAL );
    
    private static final int        ship1DVXco  = 3;
    private static final int        ship1DVYco  = 4;  
    private static final GridCoords ship1DVCo   =
        new GridCoords( ship1DVXco, ship1DVYco );
    private static final String     ship1DVName = "ship1DV";
    private static final Ship2D     ship1DV     =
        new Ship2D( TEST_TYPE1D, ship1DVName, ship1DVCo, Orientation.VERTICAL );
    
    @BeforeAll
    public static void beforeAll()
    {
        ShipTypes.register( TEST_TYPE2D );
        ShipTypes.register( TEST_TYPE1D );
        ShipTypes.register( TEST_TYPE_SQUARE );
    }
    
    @AfterAll
    public static void afterAll()
    {
        ShipTypes.reset();
    }

    @BeforeEach
    public void setUp() throws Exception
    {
    }

    @Test
    public void testShipCtorNullName()
    {
        Ship2D  test    = 
            new Ship2D( TEST_TYPE2D, null, ship2DVCo, Orientation.VERTICAL );
        assertNull( test.getName() );
        
        test = new Ship2D( TEST_TYPE2D, ship2DVCo, Orientation.VERTICAL );
        assertNotNull( test.getName() );
    }

    @Test
    public void testShipShipTypeGridCoordsHorizontal1D()
    {
        Corners expCorners  = new Corners( 
            ship1DHXco, 
            ship1DHYco, 
            TEST_TYPE1D, 
            Orientation.HORIZONTAL
        );
        Corners actCorners  = new Corners( ship1DH );
        assertEquals( TEST_TYPE1D, ship1DH.getType() );
        assertEquals( Orientation.HORIZONTAL, ship1DH.getOrientation() );
        expCorners.test( actCorners );
    }

    @Test
    public void testShipShipTypeGridCoordsVertical1D()
    {
        Corners expCorners  = new Corners( 
            ship1DVXco, 
            ship1DVYco, 
            TEST_TYPE1D, 
            Orientation.VERTICAL
        );
        Corners actCorners  = new Corners( ship1DV );
        assertEquals( TEST_TYPE1D, ship1DV.getType() );
        assertEquals( Orientation.VERTICAL, ship1DV.getOrientation() );
        expCorners.test( actCorners );
    }
    
    @Test
    public void testShip2DNPEs()
    {
        Class<NullPointerException> npeClass    = NullPointerException.class;
        assertThrows( npeClass, 
            () -> new Ship2D( 
                null, 
                ship2DVName, 
                ship2DVCo, 
                Orientation.VERTICAL
            )
        );

        assertThrows( npeClass, 
            () -> new Ship2D( 
                TEST_TYPE2D, 
                ship2DVName, 
                null, 
                Orientation.VERTICAL
            )
        );

        assertThrows( npeClass, 
            () -> new Ship2D( 
                TEST_TYPE2D, 
                ship2DVName, 
                ship2DVCo, 
                null
            )
        );
    }

    @Test
    public void testShipShipTypeGridCoordsHorizontal2D()
    {
        Corners expCorners  = new Corners( 
            ship2DHXco, 
            ship2DHYco, 
            TEST_TYPE2D, 
            Orientation.HORIZONTAL
        );
        Corners actCorners  = new Corners( ship2DH );
        assertEquals( TEST_TYPE2D, ship2DH.getType() );
        assertEquals( Orientation.HORIZONTAL, ship2DH.getOrientation() );
        expCorners.test( actCorners );
    }

    @Test
    public void testShipShipTypeGridCoordsVertical2D()
    {
        Corners expCorners  = new Corners( 
            ship2DVXco, 
            ship2DVYco, 
            TEST_TYPE2D, 
            Orientation.VERTICAL
        );
        Corners actCorners  = new Corners( ship2DV );
        assertEquals( TEST_TYPE2D, ship2DV.getType() );
        assertEquals( Orientation.VERTICAL, ship2DV.getOrientation() );
        expCorners.test( actCorners );
    }

    @ParameterizedTest
    @MethodSource( "allShips" )
    public void testContainsIntInt( Ship2D ship )
    {
        RectUtils.getInteriorPoints( ship.getBounds() )
            .forEach( p -> 
                assertTrue( ship.contains( p.x, p.y ), p.toString() )
            );
        
        RectUtils.getPerimeterPoints( ship.getBounds() )
            .forEach( p -> 
                assertFalse( ship.contains( p.x, p.y ), p.toString() )
            );

    }

    @ParameterizedTest
    @MethodSource( "allShips" )
    public void testContainsGridCoordsPos( Ship2D ship )
    {
        RectUtils.getInteriorPoints( ship.getBounds() )
            .map( p -> new GridCoords( p.x, p.y ) )
            .forEach( g -> assertTrue( ship.contains( g ) ) );
    }

    @ParameterizedTest
    @MethodSource( "allShips" )
    public void testContainsGridCoordsNeg( Ship2D ship )
    {
        RectUtils.getPerimeterPoints( ship.getBounds() )
            .map( p -> new GridCoords( p.x, p.y ) )
            .forEach( g -> assertFalse( ship.contains( g ) ) );
    }

    @ParameterizedTest
    @MethodSource( "allShips" )
    public void testIntersectsShip( Ship2D ship )
    {
        testIntersectsULC( ship );
        testIntersectsURC( ship );
        testIntersectsLLC( ship );
        testIntersectsLRC( ship );
    }
    
    @Test
    public void testGetTypeName()
    {
        assertEquals( TEST_TYPE_NAME2D, ship2DH.getTypeName() );
    }
    
    @Test
    public void testGetName()
    {
        assertEquals( ship2DHName, ship2DH.getName() );
    }

    @Test
    public void testToString()
    {
        Ship2D      ship            = ship1DH;
        Rectangle   bounds          = ship.getBounds();
        int         width           = bounds.width;
        int         height          = bounds.height;
        
        String      str             = ship1DH.toString();
        String      nameClause      = "name=" + ship.getName();
        String      typeClause      = "type=" + ship.getTypeName();
        String      posClause       = "start=";
        String      xcoClause       = "xco=" + bounds.x;
        String      ycoClause       = "yco=" + bounds.y;
        String      orientClause    = "orient=" + ship.getOrientation();
        String      widthClause     = "width=" + width;
        String      heightClause    = "height=" + height;
        assertTrue( str.contains( nameClause ), nameClause );
        assertTrue( str.contains( typeClause ), typeClause );
        assertTrue( str.contains( posClause ), posClause );
        assertTrue( str.contains( xcoClause ), xcoClause );
        assertTrue( str.contains( ycoClause ), ycoClause );
        assertTrue( str.contains( orientClause ), orientClause );
        assertTrue( str.contains( widthClause ), widthClause );
        assertTrue( str.contains( heightClause ), heightClause );
    }

    @Test
    public void testEqualsHash()
    {
        // In tests for equality where both objects use firstA, equality
        // will be determined using identity test for firstA. Introduce
        // firstA_alt, which is equal but not identical to firstA.
        ShipType2D  typeA       = TEST_TYPE2D;
        ShipType2D  typeB       = TEST_TYPE1D;
        String      nameA       = "nameA";
        String      nameB       = "nameB";
        GridCoords  firstA      = new GridCoords( 1, 2 );
        GridCoords  firstA_Alt  = new GridCoords( 1, 2 );
        GridCoords  firstB      = new GridCoords( 3, 4 );
        Orientation orientA     = Orientation.HORIZONTAL;
        Orientation orientB     = Orientation.VERTICAL;
        Ship2D      ship1       = null;
        Ship2D      ship2       = null;
        
        ship1 = new Ship2D(typeA, nameA, firstA, orientA );
        ship2 = new Ship2D(typeA, nameA, firstA, orientA );
        assertEquals( ship1, ship2 );
        assertEquals( ship2, ship1 );
        assertEquals( ship1, ship1 );
        assertEquals( ship1.hashCode(), ship2.hashCode() );
        assertNotEquals( ship1, null );
        assertNotEquals( ship1, new Object() );
        
        ship1 = new Ship2D(typeA, nameA, firstA, orientA );
        ship2 = new Ship2D(typeA, nameA, firstA_Alt, orientA );
        assertEquals( ship1, ship2 );
        assertEquals( ship1.hashCode(), ship2.hashCode() );
        
        ship1 = new Ship2D(typeA, firstA, orientA );
        ship2 = new Ship2D(typeA, firstA, orientA );
        assertEquals( ship1, ship2 );
        assertEquals( ship1.hashCode(), ship2.hashCode() );
        
        ship1 = new Ship2D(typeA, nameA, firstA, orientA );
        ship2 = new Ship2D(typeA, firstA, orientA );
        assertNotEquals( ship1, ship2 );
        
        ship1 = new Ship2D(typeA, nameA, firstA, orientA );
        ship2 = new Ship2D(typeB, nameA, firstA, orientA );
        assertNotEquals( ship1, ship2 );

        ship2 = new Ship2D(typeA, nameB, firstA, orientA );
        assertNotEquals( ship1, ship2 );

        ship2 = new Ship2D(typeA, nameA, firstB, orientA );
        assertNotEquals( ship1, ship2 );

        ship2 = new Ship2D(typeA, nameA, firstA, orientB );
        assertNotEquals( ship1, ship2 );
    }
    
    /**
     * Gets a square ship with the given coordinates
     * and a horizontal orientation.
     * 
     * @param xco   the given x-coordinate
     * @param yco   the given y-coordinate
     * 
     * @return  the instantiated ship
     */
    private Ship2D getSquareShip( int xco, int yco )
    {
        GridCoords  coords  = new GridCoords( xco, yco );
        Ship2D      test    = 
            new Ship2D(TEST_TYPE_SQUARE, coords, Orientation.HORIZONTAL );
        return test;        
    }
    
    /**
     * Verifies intersects(Ship2D) is true
     * if a given ship intersects another ship
     * in its upper-left corner.
     * 
     * @param ship  the given ship
     */
    private void testIntersectsULC( Ship2D ship )
    {
        Corners corners = new Corners( ship );
        // Want lrc of square to coincide with ulc of ship
        int         xco     = corners.ulc.x - SQUARE_SIDE + 1;
        int         yco     = corners.ulc.y - SQUARE_SIDE + 1;
        Ship2D      square  = getSquareShip( xco, yco );
        assertTrue( ship.intersects( square ) );
        
        square = getSquareShip( xco - 1, yco );
        assertFalse( ship.intersects( square ) );
        
        square = getSquareShip( xco, yco - 1 );
        assertFalse( ship.intersects( square ) );
    }
    
    /**
     * Verifies intersects(Ship2D) is true
     * if a given ship intersects another ship
     * in its upper-right corner.
     * 
     * @param ship  the given ship
     */
    private void testIntersectsURC( Ship2D ship )
    {
        Corners corners = new Corners( ship );
        // Want llc of square to coincide with urc of ship
        int         xco     = corners.urc.x;
        int         yco     = corners.urc.y - SQUARE_SIDE + 1;
        Ship2D      square  = getSquareShip( xco, yco );
        assertTrue( ship.intersects( square ) );
        
        square = getSquareShip( xco + 1, yco );
        assertFalse( ship.intersects( square ) );
        
        square = getSquareShip( xco, yco - 1 );
        assertFalse( ship.intersects( square ) );
    }
    
    /**
     * Verifies intersects(Ship2D) is true
     * if a given ship intersects another ship
     * in its lower-left corner.
     * 
     * @param ship  the given ship
     */
    private void testIntersectsLLC( Ship2D ship )
    {
        Corners corners = new Corners( ship );
        // Want urc of square to coincide with llc of ship
        int         xco     = corners.llc.x - SQUARE_SIDE + 1;
        int         yco     = corners.llc.y;
        Ship2D      square  = getSquareShip( xco, yco );
        assertTrue( ship.intersects( square ) );
        
        square = getSquareShip( xco - 1, yco );
        assertFalse( ship.intersects( square ) );
        
        square = getSquareShip( xco, yco + 1 );
        assertFalse( ship.intersects( square ) );
    }
    
    /**
     * Verifies intersects(Ship2D) is true
     * if a given ship intersects another ship
     * in its lower-right corner.
     * 
     * @param ship  the given ship
     */
    private void testIntersectsLRC( Ship2D ship )
    {
        Corners corners = new Corners( ship );
        // Want ulc of square to coincide with lrc of ship
        int         xco     = corners.lrc.x;
        int         yco     = corners.lrc.y;
        Ship2D      square  = getSquareShip( xco, yco );
        assertTrue( ship.intersects( square ) );
        
        square = getSquareShip( xco + 1, yco );
        assertFalse( ship.intersects( square ) );
        
        square = getSquareShip( xco, yco + 1 );
        assertFalse( ship.intersects( square ) );
    }

    /**
     * Returns a stream of all test ships.
     * 
     * @return a stream of all test ships
     */
    private static Stream<Ship2D> allShips()
    {
        Stream<Ship2D>  ships   =
            Stream.of( ship2DH, ship2DV, ship1DH, ship1DV );
        return ships;
    }

    /**
     * An instance of this class encapsulates
     * the points at the corners of a ship.
     */
    private static class Corners
    {
        public final Rectangle  bounds;
        public final Point      ulc;
        public final Point      urc;
        public final Point      llc;
        public final Point      lrc;
        
        /**
         * Get the points at the corners
         * of a given ship.
         * 
         * @param ship  the given ship
         */
        public Corners( Ship2D ship )
        {
            bounds = ship.getBounds();
            int maxX    = (int)bounds.getMaxX() - 1;
            int minX    = (int)bounds.getMinX();
            int maxY    = (int)bounds.getMaxY() - 1;
            int minY    = (int)bounds.getMinY();
            ulc = new Point( minX, minY );
            urc = new Point( maxX, minY );
            llc = new Point( minX, maxY );
            lrc = new Point( maxX, maxY );
        }
        
        /**
         * Calculates the expected corners of a ship
         * of the given coordinates, type, and orientation.
         * 
         * @param minX          the given x-coordinate
         * @param minY          the given y-coordinate
         * @param type          the given type
         * @param orientation   the given orientation
         */
        public Corners( 
            int minX, 
            int minY, 
            ShipType2D type, 
            Orientation orientation 
        )
        {
            int length  = type.length();
            int breadth = type.breadth();
            int width  = orientation == Orientation.HORIZONTAL ?
                length : breadth;
            int height  = orientation == Orientation.HORIZONTAL ?
                breadth : length;
            int maxX    = minX + width - 1;
            int maxY    = minY + height -1;
            ulc = new Point( minX, minY );
            urc = new Point( maxX, minY );
            llc = new Point( minX, maxY );
            lrc = new Point( maxX, maxY );
            bounds = new Rectangle( minX, minY, width, height );
        }
        
        /**
         * Verifies that this object encapsulates the same corners
         * as a given Corners object.
         * 
         * @param that  the given object
         */
        public void test( Corners that )
        {
            assertEquals( this.ulc, that.ulc, ulc.toString() );
            assertEquals( this.urc, that.urc, urc.toString() );
            assertEquals( this.llc, that.llc, llc.toString() );
            assertEquals( this.lrc, that.lrc, lrc.toString() );
            assertEquals( this.bounds, that.bounds, bounds.toString() );
        }
    }
}
