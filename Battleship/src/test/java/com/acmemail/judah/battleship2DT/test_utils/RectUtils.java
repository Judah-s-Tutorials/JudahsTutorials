package com.acmemail.judah.battleship2DT.test_utils;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.acmemail.judah.battleship2D.Grid2D;
import com.acmemail.judah.battleship2D.GridCoords;

public class RectUtils
{
    /** Grid bounds, for calculations requiring Grid2D. */
    private static final Rectangle gridBounds  = 
        new Rectangle( 0, 0, Grid2D.getNumCols(), Grid2D.getNumRows() );
    
    /**
     * Default constructor, not used.
     */
    private RectUtils()
    {
        // not used
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
    public static Point ofGridCoords( GridCoords coords)
    {
        Objects.requireNonNull( coords, "coords" );
        Point   point   = new Point( coords.xco(), coords.yco() );
        return point;
    }
    
    /**
     * Converts a given point to a GridCoords object.
     * 
     * @return  the derived point
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
     * @return  the derived point
     * 
     * @throws NullPointerException if point is null
     */
    public static GridCoords coordsOf( Rectangle rect )
    {
        Objects.requireNonNull( rect, "rect" );
        GridCoords  coords  = new GridCoords( rect.x, rect.y );
        return coords;
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
    
    public static Stream<Point> getPerimeterPoints( Rectangle rect )
    {
        Rectangle   outer   = new Rectangle(
            rect.x - 1, rect.y - 1,
            rect.width + 2, rect.height + 2
        );
//        int minXco  = outer.x;
//        int minYco  = outer.y;
//        int maxXco  = (int)outer.getMaxX();
//        int maxYco  = (int)outer.getMaxY();
//        Stream<Point>   stream  =
//            IntStream.range( minXco, maxXco )
//            .boxed()
//            .flatMap( x -> IntStream.range( minYco, maxYco )
//                .mapToObj( y -> new Point( x, y ) )
//            )
//            .filter( p -> !rect.contains( p ) );
        Stream<Point>   stream  = getInteriorPoints( outer )
            .filter( p -> !rect.contains( p ) );
        return stream;
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
    public static Stream<GridCoords> getAllCoords( Rectangle rect )
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
     * in the reference rectangle;
     * 
     * @param testRect  the the given test rectangle
     * @param refRect  the the given test rectangle
     * 
     * @return
     *      a stream of coordinates at which
     *      the rest rectangle will be full contained
     *      in the reference rectangle
     *      
     * @throws NullPointerException if testRect is null
     * @throws NullPointerException if refRect is null
     */
    public static Stream<Point> getAllValidPoints( 
        Rectangle testRect, 
        Rectangle refRect 
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
}
