package com.gmail.johnstraub1954.penrose.utils;

import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.List;

import com.gmail.johnstraub1954.penrose.PShape;
import com.gmail.johnstraub1954.penrose.Vertex;

/**
 * This class contains miscellaneous utilities
 * for support of the Penrose Aperiodic tiling project.
 * Most of the utilities are associated with rounding
 * and approximation issues
 * ({@linkplain #EPSILON}, {@linkplain #PRECISION}
 * related to handling floating point rounding errors
 * when comparing coordinates.
 */
public class Utils
{
    /**
     * The number of decimal points to which floating point numbers
     * are rounded;
     * 0 indicates the nearest integer,
     * -1 the nearest one-tenth of a unit, 
     * -2 the nearest one-hundredth of a unit,
     * etc.
     */
    public static final int     PRECISION   = -1;
    /**
     * Value to use when testing the distance 
     * between two floating point values
     * after allowing for rounding error; based on the established {@linkplain #PRECISION}.
     */
    public static final double  EPSILON     = Math.pow( 10, PRECISION ) / 2;
    /**
     * Environment dependent line separator for use producing reports.
     */
    private static final String  newl   = System.lineSeparator();
    /**
     * Default constructor; not used.
     */
    private Utils()
    {
        // not used
    }
    
    /**
     * Indicates whether a given point 
     * lies on a given line segment.
     * A point is determined to lie on a line segment
     * if its distance from the line segment
     * is below a fixed threshold;
     * see {@linkplain #EPSILON}.
     * 
     * @param point the given point
     * @param line  the given line segment
     *  
     * @return  
     *      true, if the given point is determined
     *      to lie on the given line segment
     */
    public static boolean liesOn( Point2D point, Line2D line )
    {
        double  distance    = line.ptSegDist( point );
        boolean result      = distance < EPSILON;
        
        return result;
    }
      
    /**
     * Indicates whether two given line segments intersect
     * at one or more points.
     * 
     * @param line1 one given line segments
     * @param line2 the other given line segment
     * 
     * @return  true if the given line segments intersect
     */
    public static boolean intersect( Line2D line1, Line2D line2 )
    {
        boolean result  = line1.intersectsLine( line2 );
        return result;
    }
    
    /**
     * Determine the orientation of three given points.
     * Orientation is indicated by the return value as follows:
     * <ol>
     *      <li>-1: counterclockwise</li>
     *      <li>1: clockwise</li>
     *      <li>0: collinear</li>
     * </ol>
     * <p>
     * Orientation is calculated with respect to the 
     * usual definition of the Cartesian plane.
     * 
     * @param pointP    the first given point
     * @param pointQ    the second given point
     * @param pointR    the third given point
     * 
     * @return  
     *      -1, 0, or 1, describing an orientation that is
     *      counterclockwise, collinear, or clockwise, respectively.
     *      
     * @see <a href="https://www.geeksforgeeks.org/dsa/orientation-3-ordered-points/">
     *          Orientation of 3 ordered points
     *     </a>
     *     on GeeksforGeeks.
     */
    public static int 
    orientation( Point2D pointP, Point2D pointQ, Point2D pointR )
    {
        double  xcoP        = pointP.getX();
        double  ycoP        = pointP.getY();
        double  xcoQ        = pointQ.getX();
        double  ycoQ        = pointQ.getY();
        double  xcoR        = pointR.getX();
        double  ycoR        = pointR.getY();
        double  crossA      = (xcoQ - xcoP) * (ycoR - ycoP);
        double  crossB      = (xcoR - xcoP) * (ycoQ - ycoP);
        double  diff        = crossA - crossB;
        // - sign accounts for the difference between Cartesian plane
        // coordinates (y-coordinates increase to the North) and
        // display coordinates (y-coordintates increase to the South.
        int     orientation = -(int)Math.signum( diff );
        return orientation;
    }
    
    /**
     * Compiles a report of the details of all vertices in a list.
     * 
     * @param label     label to begin the report
     * @param vertices  the list of vertices to report
     * 
     * @return the detailed report
     */
    public static String print( String label, List<Vertex> vertices )
    {
        StringBuilder   bldr    = new StringBuilder();
        int             size    = vertices.size();
        for ( int inx = 0 ; inx < size ; ++inx )
        {
            Vertex  vertex      = vertices.get( inx );
            Point2D coords      = vertex.getCoords();
            String  strCoords   = 
                String.format( "(%5.1f, %5.1f) ", coords.getX(), coords.getY() );
            String  strSlope    = String.format( "m=%4.1f ", vertex.getSlope() );
            String  strLength   = String.format( "l=%4.1f", vertex.getLength() );
            bldr.append( label ).append( " " ).append( inx ).append( ": " )
                .append( strCoords ).append( strSlope ).append( strLength )
                .append( newl );
        }
        return bldr.toString();
    }
    
    /**
     * Create a new point with coordinates obtained
     * by subtracting one point from another.
     * The new point will have coordinates
     * equivalent to the difference of the x- and y-coordinates
     * of the two arguments.
     * 
     * @param pointA    the subtrahend (right-hand side) of the operation
     * @param pointB    the minuend (left-hand side) of the operation
     * 
     * @return  the result of the operation pointB - pointA
     */
    public static Point2D subtract( Point2D pointA, Point2D pointB )
    {
        double  xDiff   = pointB.getX() - pointA.getX();
        double  yDiff   = pointB.getY() - pointA.getY();
        Point2D diff    = new Point2D.Double( xDiff, yDiff );
        return diff;
    }
    
    /**
     * Create a new line with coordinates copied and rounded
     * from a given line.
     * 
     * @param lineIn    the given line
     * 
     * @return  the new line
     * 
     * @see Utils
     */
    public static Line2D round( Line2D lineIn )
    {
        Point2D point1Rounded   = round( lineIn.getP1() );
        Point2D point2Rounded   = round( lineIn.getP2() );
        Line2D  lineRounded     = 
            new Line2D.Double( point1Rounded, point2Rounded );
        return lineRounded;
    }
    
    /**
     * Create a new point with coordinates copied and rounded
     * from a given point.
     * 
     * @param pointIn   the given point
     * 
     * @return  
     *      a new point with coordinates copied and rounded
     *      from the given point
     *      
     * @see Utils
     */
    public static Point2D round( Point2D pointIn )
    {
        double  xcoRounded      = round( pointIn.getX() );
        double  ycoRounded      = round( pointIn.getY() );
        Point2D pointRounded    = new Point2D.Double( xcoRounded, ycoRounded );
        return pointRounded;
    }
    
    /**
     * Round a given number As described in the
     * {@linkplain Utils} documentation.
     * Negative numbers round away from 0;
     * e.g. -3.35 rounds to 3.4.
     * 
     * @param val   the given number
     * @return  the given number rounded to the nearest tenth.
     */
    public static double round( double val )
    {
        double  rounded     = val;
        if ( Math.abs( rounded ) != Double.POSITIVE_INFINITY )
        {
            double  multiplied  = Math.abs( val ) * 10;
            double  incremented = multiplied +.5;
            rounded = (int)incremented; 
            rounded /= 10;
            rounded *= Math.signum( val );
        }
        return rounded;
    }
    
    /**
     * Calculate the slope of a given line.
     * 
     * @param line  the given line
     * 
     * @return  the slope of the given line
     */
    public static double slope( Line2D line )
    {
        double  slope  = slope( line.getP1(), line.getP2() );
        return slope;
    }
    
    /**
     * Calculates the length of a given line.
     * 
     * @param line  the given line
     * 
     * @return  the length of the given line
     */
    public static double length( Line2D line )
    {
        Point2D point1  = line.getP1();
        double  length  = point1.distance( line.getP2() );
        return length;
    }
    
    /**
     * Calculate the slope of the line drawn through two given points.
     * 
     * @param point1    the first given point
     * @param point2    the second given point
     * 
     * @return  the slope of the line drawn through the given points
     */
    public static double slope( Point2D point1, Point2D point2 )
    {
        double  deltaX  = point2.getX() - point1.getX();
        double  deltaY  = point2.getY() - point1.getY();
        double  slope   = deltaY / deltaX;
        // return slope relative to Cartesian plane
        slope = -slope;
        return slope;
    }
    
    /**
     * Determine if two given lines have the same endpoints
     * after accounting for rounding error.
     * 
     * @param line1 the first given line
     * @param line2 the second given line
     * 
     * @return  true, if the given lines have approximately the same coordinates
     * 
     * @see Utils
     */
    public static boolean match( Line2D line1, Line2D line2 )
    {
        boolean result      = false;
        Point2D line1_p1    = line1.getP1();
        Point2D line2_p1    = line2.getP1();
        if ( match( line1_p1, line2_p1 ) )
        {
            // p1 matches for both lines, test p2 for both lines
            Point2D line1_p2 = line1.getP2();
            Point2D line2_p2 = line2.getP2();
            result = match( line1_p2, line2_p2 );
        }
        else
        {
            Point2D line1_p2    = line1.getP2();
            if ( match( line1_p2, line2_p1 ) )
            {
                // line1-2 matches line2-1
                // does line 1-1 math line-2
                Point2D line2_p2    = line2.getP2();
                result = match( line1_p1, line2_p2 );
            }
        }
        return result;
    }
    
    /**
     * Determine if two given points have the same endpoints
     * after accounting for rounding error.
     * 
     * @param point1 the first given point
     * @param point2 the second given point
     * 
     * @return  
     *      true, if the given points have approximately the same coordinates
     * 
     * @see Utils
     */
    public static boolean match( Point2D point1, Point2D point2 )
    {
        double  xDiff   = Math.abs( point1.getX() - point2.getX() );
        double  yDiff   = Math.abs( point1.getY() - point2.getY() );
        boolean result  = xDiff < EPSILON && yDiff < EPSILON;
        return result;
    }
    
    /**
     * Determine if two floating point value are approximately equal
     * after accounting for rounding error.
     * 
     * @param val1  the first value
     * @param val2  the second value
     * 
     * @return  true, if the given values are approximately equal
     */
    public static boolean match( double val1, double val2 )
    {
        double  diff    = 0;
        if ( Math.abs( val2 ) == Double.POSITIVE_INFINITY 
            && Math.abs( val1 ) == Double.POSITIVE_INFINITY
        )
            diff = 0;
        else
            diff = Math.abs( val1 - val2 );
        boolean result  = diff < EPSILON;
        return result;
    }

    /**
     * Creates a formatted report
     * describing the intersection of two PShapes.
     * This method is mainly useful for debugging.
     * 
     * @param pShapeA   the first PShape
     * @param pShapeB   the second PShape
     * 
     * @return  a formatted report describing the intersection of two PShapes
     */
    public static String formatIntersection( PShape pShapeA, PShape pShapeB )
    {
        String  result  = "";
        Area    areaA   = new Area( pShapeA.getWorkShape() );
        Area    areaB   = new Area( pShapeB.getWorkShape() );
        areaA.intersect(areaB);
        if ( !areaA.isEmpty() )
            result = formatPathIterator( areaA.getPathIterator( null ) );
        return result;
    }
    
    /**
     * Creates a formatted report describing the properties
     * of a given PathIterator.
     * 
     * @param iter  the given PathIterator
     * 
     * @return  
     *      a formatted report describing the properties
     *      of the given PathIterator
     */
    public static String formatPathIterator( PathIterator iter )
    {
        StringBuilder   bldr    = new StringBuilder();
        while ( !iter.isDone() )
        {
            double[]        coords  = new double[6];
            int             type    = iter.currentSegment( coords );
            String          strType = decodeIteratorType( type );
            bldr.append( strType ).append( ":" );
            for ( int inx = 0 ; inx < coords.length ; inx += 2 )
            {
                bldr.append( ' ' )
                    .append( decodeCoords( coords, inx ) )
                    .append( newl );
            }
            iter.next();
        }
        return bldr.toString();
    }
    
    /**
     * Converts a given line's slope to radians,
     * describing the angle that the line makes
     * with respect to the x-axis.
     * 
     * @param line  the given line
     * 
     * @return  the slope of the given line converted to radians
     */
    public static double radians( Line2D line )
    {
        double  radians = radians( line.getP1(), line.getP2() );
        return radians;
    }
    
    /**
     * Describes the angle of a line drawn through two points.
     * 
     * @param point1    the first point
     * @param point2    the second point
     * 
     * @return  the angle of a line drawn through two points
     */
    public static double radians( Point2D point1, Point2D point2 )
    {
        double  deltaX  = point2.getX() - point1.getX();
        // choosing y1 - y2 (instead of y2 - y1) will convert the
        // factor from screen coordinates to Cartesian coordinates 
        double  deltaY  = point1.getY() - point2.getY();
        double  radians = Math.atan( deltaY / deltaX );
        return radians;
    }
    
    /**
     * Creates a formatted string describing the coordinates
     * stored in consecutive cells of an array.
     *  
     * @param coords    the array containing the coordinates to format
     * @param from      the index with the array of the first coordinate
     * 
     * @return
     *      formatted string describing the coordinates
     *      stored in consecutive cells of an array
     */
    private static String decodeCoords( double[] coords, int from )
    {
        String          strXco  = String.format( "%6.2f", coords[from] );
        String          strYco  = String.format( "%6.2f", coords[from + 1] );
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( '(' ).append( strXco ).append( ',')
            .append( strYco ).append( ')' );
        return bldr.toString();
    }
    
    /**
     * Returns a string describing the type 
     * of a segment of a PathIterator,
     * for example "moveTo" or "lineTo."
     * 
     * @param type  the type to describe
     * 
     * @return  a string describing the type of a PathIterator segment
     */
    private static String decodeIteratorType( int type )
    {
        String  strType = "???";
        switch ( type )
        {
        case PathIterator.SEG_MOVETO:
            strType = "moveTo";
            break;
        case PathIterator.SEG_LINETO:
            strType = "lineTo";
            break;
        case PathIterator.SEG_CLOSE:
            strType = "close";
            break;
        }
        return strType;
    }
}
