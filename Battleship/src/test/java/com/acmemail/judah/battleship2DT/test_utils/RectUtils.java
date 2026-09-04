package com.acmemail.judah.battleship2DT.test_utils;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.acmemail.judah.battleship.model.GridCoords;

/**
 * Set of utilities for performing operations on Rectangles.
 * By design, 
 * they contain no dependencies on ships, ship types, or grid.
 */
public class RectUtils
{
    /**
     * Default constructor, not used.
     */
    private RectUtils()
    {
        // not used
    }

    /**
     * Returns a stream of Points
     * for every point in a given rectangle.
     * 
     * @param rect  the given rectangle
     * 
     * @return  a stream of Points for every point in rect
     * 
     * @throws NullPointerException if rect is null
     */
    public static Stream<Point> getInteriorPoints( Rectangle rect )
    {
        Objects.requireNonNull( rect, "rect" );
        
        int maxXco  = (int)rect.getMaxX();
        int minXco  = rect.x;
        int maxYco  = (int)rect.getMaxY();
        int minYco  = rect.y;
        
        Stream<Point>   points  = IntStream.range( minYco, maxYco )
            .boxed()
            .flatMap( yco -> IntStream.range( minXco, maxXco )
                .mapToObj( xco -> new Point( xco, yco ) )
            );
        return points;
    }
    
    /** Get a stream containing
     * the exterior points on the perimeter of a given rectangle.
     * 
     * @param rect  the given rectangle.
     * 
     * @return  a Stream<Point> 
     *      containing the exterior points on the perimeter of rect
     *      
     * @throws NullPointerException if rect is null
     */
    public static Stream<Point> getPerimeterPoints( Rectangle rect )
    {
        Objects.requireNonNull( rect, "rect" );
        Rectangle   outer   = getSuperRect( rect, 1 );
        Stream<Point>   stream  = getInteriorPoints( outer )
            .filter( p -> !rect.contains( p ) );
        return stream;
    }

    /**
     * Given a reference rectangle and increase-by parameter,
     * create a rectangle that contains the reference rectangle
     * and includes additional number of rows and columns.
     *
     * @param refRect   reference rectangle
     * @param increase  given increase-by parameter
     *
     * @return the created rectangle
     *
     * @throws NullPointerException if refRect is null
     * @throws IllegalArgumentException if increase is not positive
     */
    public static Rectangle getSuperRect( Rectangle refRect, int increase )
    {
        Objects.requireNonNull( refRect, "refRect" );
        Rectangle   rect    =
            new Rectangle(
                refRect.x - increase,
                refRect.y - increase,
                refRect.width + 2 * increase,
                refRect.height + 2 * increase
            );
        if ( rect.width <= refRect.width || rect.height <= refRect.height )
        {
            String  message = "increase must be positive: " + increase;
            throw new IllegalArgumentException( message );
        }
        return rect;
    }

    /**
     * Given a reference rectangle and exclusion parameter,
     * create a rectangle that is contained in the reference rectangle,
     * and excludes the given number of rows and columns.
     *
     * @param refRect   reference rectangle
     * @param exclude   exclusion parameter
     *
     * @return the created rectangle
     *
     * @throws NullPointerException if refRect is null
     * @throws IllegalArgumentException
     *      if exclude is too large for refRect's dimensions
     */
    public static Rectangle getSubrect( Rectangle refRect, int exclude )
    {
        Objects.requireNonNull( refRect, "refRect" );
        Rectangle   rect    =
            new Rectangle(
                refRect.x + exclude,
                refRect.y + exclude,
                refRect.width - 2 * exclude,
                refRect.height - 2 * exclude
            );
        if ( rect.width <= 1 || rect.height <= 1 )
        {
            String  message = "exclude too large: " + exclude;
            throw new IllegalArgumentException( message );
        }
        return rect;
    }

    /**
     * Returns a stream of GridCoords
     * for every point in a given rectangle.
     * 
     * @param rect  the given rectangle
     * 
     * @return  a stream of GridCoords for every point in rect
     * 
     * @throws NullPointerException if rect is null
     */
    public static Stream<GridCoords> getInteriorCoords( Rectangle rect )
    {
        Objects.requireNonNull( rect, "rect" );
        
        Stream<GridCoords>  coords  = getInteriorPoints( rect )
            .map( p -> new GridCoords( p.x, p.y ) );
        return coords;
    }
    
    /**
     * Given a test rectangle and a reference rectangle,
     * get a stream of all coordinates for which
     * the test rectangle will be fully contained
     * in the reference rectangle.
     * 
     * @param testRect  the given test rectangle
     * @param refRect  the given reference rectangle
     * 
     * @return
     *      a stream of coordinates at which
     *      the test rectangle will be fully contained
     *      in the reference rectangle
     *      
     * @throws NullPointerException if testRect is null
     * @throws NullPointerException if refRect is null
     */
    public static Stream<Point> getAllValidPoints( 
        Rectangle refRect, 
        Rectangle testRect 
    )
    {
        Objects.requireNonNull( testRect, "testRect" );
        Objects.requireNonNull( refRect, "refRect" );
        Dimension   dim = new Dimension( testRect.width, testRect.height );
        Stream<Point>  stream  = getInteriorPoints( refRect )
            .map( p -> new Rectangle( p, dim ) )
            .filter( r -> refRect.contains( r ) )
            .map( r -> new Point( r.x, r.y ) );
        return stream;
    }
    
    /**
     * Subtract one rectangle from another;
     * return the difference as a stream of gridCoords.
     * 
     * @param subtractFrom  the rectangle to subtract from
     * @param rect          the the rectangle to subtract
     * 
     * @return
     *      a Stream<GridCoords>
     *      containing the difference between the two rectangles
     *      
     * @throws NullPointerException if subtractFrom is null
     * @throws NullPointerException if rect is null
     */
    public static Stream<GridCoords> subtract( 
        Rectangle subtractFrom, 
        Rectangle rect 
    )
    {
        Objects.requireNonNull( subtractFrom, "subtractFrom" );
        Objects.requireNonNull( rect, "rect" );
        
        Stream<GridCoords> stream   =
            difference( subtractFrom, rect )
                .map( p -> coordsOf( p ) );
        return stream;
    }
    
    /**
     * Subtract one rectangle from another;
     * return the difference as a stream of Points.
     * 
     * @param subtractFrom  the rectangle to subtract from
     * @param rect          the the rectangle to subtract
     * 
     * @return
     *      a Stream<Point> containing the difference between the two rectangles
     *      
     * @throws NullPointerException if subtractFrom is null
     * @throws NullPointerException if rect is null
     */
    public static Stream<Point> difference( Rectangle subtractFrom, Rectangle rect )
    {
        Objects.requireNonNull( subtractFrom, "subtractFrom" );
        Objects.requireNonNull( rect, "rect" );
        
        Stream<Point> stream   =
            getInteriorPoints( subtractFrom )
                .filter( p -> !rect.contains( p ) );
        return stream;
    }
    
    /**
     * Converts a given GridCoordinate to a Point.
     * 
     * @param coords    the given GridCoordinate
     * 
     * @return  the derived point
     * 
     * @throws NullPointerException if coords is null
     */
    public static Point pointOf( GridCoords coords )
    {
        Objects.requireNonNull( coords, "coords" );
        Point   point   = new Point( coords.xco(), coords.yco() );
        return point;
    }
    
    /**
     * Converts a given point to a GridCoords object.
     * 
     * @param point the given point
     * 
     * @return  the derived GridCoordinates object
     * 
     * @throws NullPointerException if point is null
     */
    public static GridCoords coordsOf( Point point )
    {
        Objects.requireNonNull( point, "point" );
        GridCoords  coords  = new GridCoords( point.x, point.y );
        return coords;
    }

    /**
     * Gets the location of a given Rectangle as a GridCoord.
     * 
     * @param rect  the given rectangle
     * 
     * @return  the derived GridCoords
     *
     * @throws NullPointerException if rect is null
     */
    public static GridCoords coordsOf( Rectangle rect )
    {
        Objects.requireNonNull( rect, "rect" );
        GridCoords  coords  = new GridCoords( rect.x, rect.y );
        return coords;
    }
    
    /**
     * Return true if the given coordinates are contained
     * in any Rectangle in a given array of Rectangles.
     * 
     * @param coords    the given coordinates
     * @param rects     the given array
     * 
     * @return  true if coords is contained in any element of rects
     * 
     * @throws NullPointerException if coords is null
     * @throws NullPointerException if rects is null
     */
    public static boolean contains( GridCoords coords, Rectangle[] rects )
    {
        Objects.requireNonNull( coords, "coords" );
        Objects.requireNonNull( rects, "rects" );
        Point   point   = RectUtils.pointOf( coords );
        boolean result  =
            Arrays.stream( rects )
                .anyMatch( r -> r.contains( point ) );
        return result;
    }
    
    /**
     * Return true if the given Rectangle intersects
     * any Rectangle in a given array of Rectangles.
     * 
     * @param rect      the given rectangle
     * @param rects     the given array
     * 
     * @return  true if rect intersects any element of rects
     */
    public static boolean intersects( Rectangle rect, Rectangle[] rects )
    {
        Objects.requireNonNull( rect, "rect" );
        Objects.requireNonNull( rects, "rects" );
        boolean result  =
            Arrays.stream( rects )
                .anyMatch( r -> r.intersects( rect ) );
        return result;
    }
}
