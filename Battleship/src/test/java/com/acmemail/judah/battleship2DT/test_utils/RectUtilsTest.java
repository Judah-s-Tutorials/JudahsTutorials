package com.acmemail.judah.battleship2DT.test_utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.acmemail.judah.battleship2D.GridCoords;

class RectUtilsTest
{
    @Test
    public void testSubtract()
    {
        Rectangle   subtractFrom    = new Rectangle( 10, 15, 20, 25 );
        Rectangle   subtractor      = 
            new Rectangle(
                subtractFrom.x + 2,
                subtractFrom.y + 2,
                subtractFrom.width - 4,
                subtractFrom.height - 4
            );
        // sanity check
        assertTrue( subtractFrom.contains( subtractor ) );
        assertFalse( subtractFrom.equals( subtractor ) );
        
        int         lastXco = subtractFrom.x + subtractFrom.width;
        int         lastYco = subtractFrom.y + subtractFrom.height;
        Set<Point>  expSet  = new HashSet<>();
        for ( int yco = subtractFrom.y ; yco < lastYco ; ++yco )
            for ( int xco = subtractFrom.x ; xco < lastXco ; ++xco )
            {
                Point   point   = new Point( xco, yco );
                // sanity check
                assertTrue( subtractFrom.contains( point ) );
                if ( !subtractor.contains( point ) )
                    expSet.add( point );
            }
        
        Set<Point>  actSet  =
            RectUtils.subtract( subtractFrom, subtractor )
            .map( RectUtils::pointOf )
            .collect( Collectors.toSet() );
        
        assertEquals( expSet, actSet );
    }
    
    @Test
    public void testContainsGridCoordsArrayOfRectangles()
    {
        Corners     corners     = getDefaultCorners();
        Rectangle   bigRect     = corners.bigRect;
        Rectangle[] rects       = corners.corners;
        RectUtils.getInteriorPoints( bigRect )
            .forEach( p -> {
                boolean     expResult   =
                    Arrays.stream( rects )
                        .anyMatch( r -> r.contains( p ) );
                GridCoords  coords      = RectUtils.coordsOf( p );
                boolean     actResult   = 
                    RectUtils.contains( coords, rects );
                assertEquals( expResult, actResult, coords.toString() );
            });
    }
    
    @Test
    public void testIntersectsArrayOfRectangles()
    {
        Corners     corners     = getDefaultCorners();
        Arrays.stream( corners.corners )
            .forEach( r -> testIntersectsOneRect( r, corners.corners ) );
    }
    
    @Test
    public void testPointOfGridCoords()
    {
        int         expXco      = 10;
        int         expYco      = 15;
        GridCoords  coords      = new GridCoords( expXco, expYco );
        Point       expPoint    = new Point( expXco, expYco );
        Point       actPoint    = RectUtils.pointOf( coords );
        assertEquals( expPoint, actPoint );
    }

    @Test
    public void testCoordsOfPoint()
    {
        int         expXco      = 10;
        int         expYco      = 15;
        Point       expPoint    = new Point( expXco, expYco );
        GridCoords  expCoords   = new GridCoords( expXco, expYco );
        GridCoords  actCoords   = RectUtils.coordsOf( expPoint );
        assertEquals( expCoords, actCoords );
    }

    @Test
    public void testCoordsOfRectangle()
    {
        int         expXco      = 10;
        int         expYco      = 15;
        Rectangle   rect        = new Rectangle( expXco, expYco, 1, 1 );
        GridCoords  expCoords   = new GridCoords( expXco, expYco );
        GridCoords  actCoords   = RectUtils.coordsOf( rect );
        assertEquals( expCoords, actCoords );
    }

    @Test
    public void testGetInteriorPoints()
    {
        Rectangle   rect    = new Rectangle( 10, 10, 20, 25 );
        int         lastXco = rect.x + rect.width;
        int         lastYco = rect.y + rect.height;
        Set<Point>  expSet  = new HashSet<>();
        for ( int yco = rect.y ; yco < lastYco ; ++yco )
            for ( int xco = rect.x ; xco < lastXco ; ++xco )
                expSet.add( new Point( xco, yco ) );
        
        Set<Point>  actSet  =
            RectUtils.getInteriorPoints( rect )
                .collect( Collectors.toSet() );
        assertEquals( expSet, actSet );
    }
    
    @Test
    public void testDifference()
    {
        int         fromXco         = 3;
        int         fromYco         = 5;
        int         fromWidth       = 50;
        int         fromHeight      = 40;
        Rectangle   subtractFrom    = 
            new Rectangle( fromXco, fromYco, fromWidth, fromHeight );
        int         subXco          = fromXco + 2;
        int         subYco          = fromYco + 2;
        int         subWidth        = fromWidth - 4;
        int         subHeight       = fromHeight - 4;
        // sanity check
        assertTrue( subXco < fromHeight );
        assertTrue( subYco < fromWidth );
        assertTrue( subWidth > 1 );
        assertTrue( subHeight > 1 );
        Rectangle   rect            =
            new Rectangle( subXco, subYco, subWidth, subHeight );
        
        Set<Point>  diffSet =
            RectUtils.difference( subtractFrom, rect )
            .collect( Collectors.toSet() );
        
        RectUtils.getInteriorPoints( subtractFrom )
            .forEach( p -> {
                String  comment = p.toString();
                if ( rect.contains( p ) )
                    assertFalse( diffSet.contains( p ), comment );
                else
                    assertTrue( diffSet.contains( p ), comment );
            });
    }

    @Test
    public void testGetPerimeterPoints()
    {
        int         testXco     = 0;
        int         testYco     = 0;
        int         testWidth   = 5;
        int         testHeight  = 3;
        Rectangle   rect        = 
            new Rectangle( testXco, testYco, testWidth, testHeight );
        
        int minXco  = rect.x - 1;
        int maxXco  = (int)rect.getMaxX() + 1;
        int minYco  = rect.y - 1;
        int maxYco  = (int)rect.getMaxY() + 1;
        
        List<Point> expList     = new ArrayList<>();
        for ( int xco = minXco ; xco < maxXco ; ++ xco )
        {
            expList.add( new Point( xco, minYco ) );
            expList.add( new Point( xco, maxYco - 1 ) );
        }
        // The corners are already present; don't add them again
        for ( int yco = minYco + 1 ; yco < maxYco - 1 ; ++yco )
        {
            expList.add( new Point( minXco, yco ) );
            expList.add( new Point( maxXco - 1, yco ) );
        }
        
        List<Point> actList =
            RectUtils.getPerimeterPoints( rect ).toList();
        
        List<Point> workingList = new ArrayList<>();
        workingList.addAll( expList );
        for ( Point point : actList )
            assertTrue( workingList.remove( point ), point.toString() );
        
        assertTrue( workingList.isEmpty(), workingList.toString() );
    }

    @Test
    public void testGetSuperRect()
    {
        int         xco         = 10;
        int         yco         = 15;
        int         width       = 20;
        int         height      = 25;
        int         increase    = 3;
        Rectangle   refRect     = new Rectangle( xco, yco, width, height );

        Rectangle   expRect     =
            new Rectangle(
                xco - increase,
                yco - increase,
                width + 2 * increase,
                height + 2 * increase
            );
        Rectangle   actRect     = RectUtils.getSuperRect( refRect, increase );
        assertEquals( expRect, actRect );

        assertThrows(
            IllegalArgumentException.class,
            () -> RectUtils.getSuperRect( refRect, 0 )
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> RectUtils.getSuperRect( refRect, -1 )
        );
    }

    @Test
    public void testGetSubrect()
    {
        int         xco         = 10;
        int         yco         = 15;
        int         width       = 20;
        int         height      = 25;
        int         exclude     = 3;
        Rectangle   refRect     = new Rectangle( xco, yco, width, height );

        Rectangle   expRect     =
            new Rectangle(
                xco + exclude,
                yco + exclude,
                width - 2 * exclude,
                height - 2 * exclude
            );
        Rectangle   actRect     = RectUtils.getSubrect( refRect, exclude );
        assertEquals( expRect, actRect );

        int         tooLarge    = width / 2;
        assertThrows(
            IllegalArgumentException.class,
            () -> RectUtils.getSubrect( refRect, tooLarge )
        );
    }

    @Test
    public void testGetInteriorCoords()
    {
        Rectangle       rect    = new Rectangle( 10, 10, 20, 25 );
        int             lastXco = rect.x + rect.width;
        int             lastYco = rect.y + rect.height;
        Set<GridCoords> expSet  = new HashSet<>();
        for ( int yco = rect.y ; yco < lastYco ; ++yco )
            for ( int xco = rect.x ; xco < lastXco ; ++xco )
                expSet.add( new GridCoords( xco, yco ) );
        
        Set<GridCoords> actSet  =
            RectUtils.getInteriorCoords( rect )
                .collect( Collectors.toSet() );
        assertEquals( expSet, actSet );
    }

    @Test
    public void testGetAllValidPoints()
    {
        int         refXco      = 10;
        int         refYco      = 15;
        int         refWidth    = 20;
        int         refHeight   = 25;
        Rectangle   refRect     = 
            new Rectangle( refXco, refYco, refWidth, refHeight );
        Rectangle   rect    = 
            new Rectangle( 0, 0, refRect.width - 2, refRect.height - 2 );
        int         lastXco     = refRect.x + rect.width;
        int         lastYco     = refRect.y + rect.height;
        
        Set<Point>  expPoints   = new HashSet<>();
        for ( int yco = refYco ; yco < lastYco ; ++yco )
            for ( int xco = refRect.x ; xco < lastXco ; ++xco )
            {
                Point   loc     = new Point( xco, yco );
                rect.setLocation( loc );
                if ( refRect.contains( rect ) )
                    expPoints.add( loc );
            }
        
        Set<Point>  actPoints   =
            RectUtils.getAllValidPoints( refRect, rect )
                .collect( Collectors.toSet() );
        
        assertEquals( expPoints, actPoints );
    }
    
    /**
     * Creates the default Corners fixture shared by
     * testContainsGridCoordsArrayOfRectangles,
     * testContainsRectangleArrayOfRectangles,
     * and testIntersectsArrayOfRectangles.
     * Returns a fresh instance on every call.
     *
     * @return  the default Corners fixture
     *
     * @see Corners
     */
    private static Corners getDefaultCorners()
    {
        int         side        = 4;
        int         xco         = 10;
        int         yco         = 15;
        int         width       = 8 * side;
        int         height      = 10 * side;
        Corners     corners     = new Corners( xco, yco, width, height, side );
        return corners;
    }

    /**
     * Helper method for testIntersectsRectangleArrayOfRectangles,
     * which validates RectUtils.intersects(Rectangle, Rectangle[]).
     * The caller passes a rectangle,
     * and an array that contains the rectangle.
     * It is assumed that none of the rectangles in the array
     * intersect one another.
     * It is further assumed that
     * there is unoccupied space between the left and right columns,
     * and the top and bottom rows of the array.
     * We confirm that: 
     * a) the intersect method return true 
     * for rectangles that overlap the given rectangle; and
     * b) the intersect method returns false
     * for rectangles positioned immediately to the right and left,
     * and immediately above/below the given rectangle
     * 
     * @param rect      the given rectangle
     * @param rects     the array of rectangles
     * 
     * @see Corners
     */
    private static void testIntersectsOneRect( Rectangle rect, Rectangle[] rects )
    {
        Rectangle   tester  = new Rectangle( 0, 0, 2, 2 );
        int         lastXco = rect.x + rect.width;
        int         lastYco = rect.y + rect.height;
        for ( int yco = rect.y - 1 ; yco < lastYco ; ++yco )
            for ( int xco = rect.x - 1 ; xco < lastXco ; ++xco )
            {
                tester.setLocation( xco, yco );
                assertTrue( RectUtils.intersects( tester, rects ) );
            }
        
        tester.setLocation( rect.x + rect.width, rect.y );
        assertFalse( RectUtils.intersects( tester, rects ) );
        tester.setLocation( rect.x, rect.y + rect.height );
        assertFalse( RectUtils.intersects( tester, rects ) );
        
        tester.setLocation( rect.x - tester.width, rect.y );
        assertFalse( RectUtils.intersects( tester, rects ) );
        tester.setLocation( rect.x, rect.y - tester.height );
        assertFalse( RectUtils.intersects( tester, rects ) );
    }

    /**
     * Creates a large rectangle,
     * then a square rectangle overlaying each corner
     * of the large rectangle.
     * The user provides the properties of the large rectangle,
     * and the length of a side of the square rectangle.
     * The expectation is that all the corners
     * will have width and height of at least one,
     * will be properly nested inside the large rectangle,
     * and will not overlap each other.
     */
    private static class Corners
    {
        /** The large rectangle.  */
        public final Rectangle      bigRect;
        /** The square rectangles in the corners of the large rectangle.  */
        public final Rectangle[]    corners;
        
        /**
         * Constructor.
         * Construct a large rectangle
         * and an array of four rectangles
         * overlaying the corners of the large rectangle.
         * An assertion will be raised
         * if the result violates the conditions stated
         * in the class header.
         * 
         * @param xco       x-coordinate of the large rectangle
         * @param yco       y-coordinate of the large rectangle
         * @param width     width of the large rectangle
         * @param height    height of the large rectangle
         * @param side      width/height of the nested rectangles
         * 
         * @see Corners
         */
        public Corners( int xco, int yco, int width, int height, int side )
        {
            int         rightXco    = xco + width - side;
            int         bottomYco   = yco + height - side;
            bigRect = new Rectangle( xco, yco, width, height );
            corners = new Rectangle[]
            {
                new Rectangle( xco, yco, side, side ),
                new Rectangle( rightXco, yco, side, side ),
                new Rectangle( xco, bottomYco, side, side ),
                new Rectangle( rightXco, bottomYco, side, side )
            };
            // sanity check
            for ( Rectangle rect : corners )
            {
                assertTrue( bigRect.contains( rect ) );
                assertTrue( rect.width > 1 );
                assertTrue( rect.height > 1 );
                for ( Rectangle other : corners )
                    if ( rect != other )
                        assertFalse( rect.intersects( other ) );
            }
        }
    }
}
