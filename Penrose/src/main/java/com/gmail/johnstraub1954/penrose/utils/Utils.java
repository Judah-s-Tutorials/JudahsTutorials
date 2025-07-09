package com.gmail.johnstraub1954.penrose.utils;

import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import com.gmail.johnstraub1954.penrose.PShape;
import com.gmail.johnstraub1954.penrose.Vertex;

/**
 * 
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
     * Allowance is made for floating point rounding error;
     * {@linkplain Utils}.
     * 
     * @param point the given point
     * @param line  the given line segment
     * @return
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
        Rectangle2D bounds1     = line1.getBounds2D();
        Rectangle2D bounds2     = line2.getBounds2D();
        // Pretest: if bounding boxes do not intersect, the line segments
        // do not intersect. If bounding boxes intersect, more inspection
        // if required.
        boolean     result      = bounds1.intersects( bounds2 );
        if ( result )
        {
            double      xco1Left    = bounds1.getX();
            double      xco1Right   = xco1Left + bounds1.getWidth();
            double      yco1Top     = bounds1.getY();
            double      yco1Bottom  = yco1Top + bounds1.getHeight();
            double      xco2Left    = bounds2.getX();
            double      xco2Right   = xco2Left + bounds2.getWidth();
            double      yco2Top     = bounds2.getY();
            double      yco2Bottom  = yco2Top + bounds2.getHeight();
        }
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
     * @return  -1 if 
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
     * Compiles a report
     * @param label
     * @param vertices
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
    
    public static Line2D round( Line2D lineIn )
    {
        Point2D point1Rounded   = round( lineIn.getP1() );
        Point2D point2Rounded   = round( lineIn.getP2() );
        Line2D  lineRounded     = 
            new Line2D.Double( point1Rounded, point2Rounded );
        return lineRounded;
    }
    
    // Subtract pointA from pointB
    public static Point2D subtract( Point2D pointA, Point2D pointB )
    {
        double  xDiff   = pointB.getX() - pointA.getX();
        double  yDiff   = pointB.getY() - pointA.getY();
        Point2D diff    = new Point2D.Double( xDiff, yDiff );
        return diff;
    }
    
    public static boolean lineContains( Line2D line, Point2D point )
    {
        boolean result  = false;
        double  slope1  = slope( line );
        Point2D point1  = line.getP1();
        Point2D point2  = line.getP2();
        double  slope2  = slope( point1, point );
        if ( Utils.match( point, point1 ) )
            result = true;
        else if ( Utils.match( point, point2  ) )
            result = true;
        else if ( !match( slope1, slope2 ) )
            result = false;
        else
        {
            double  pointXco    = point.getX();
            double  pointYco    = point.getY();
            double  point1Xco   = point1.getX();
            double  point1Yco   = point1.getY();
            double  point2Xco   = point2.getX();
            double  point2Yco   = point2.getY();
            if ( slope1 < 0 )
            {
                result = 
                    pointXco <= point1Xco && pointYco <= point1Yco
                    && pointXco >= point2Xco && pointYco >= point2Yco;
            }
            else
            {
                result = 
                    pointXco >= point1Xco && pointYco >= point1Yco
                    && pointXco <= point2Xco && pointYco <= point2Yco;
            }
        }
        
        return result;
    }
    
    public static Point2D round( Point2D pointIn )
    {
        double  xcoRounded      = round( pointIn.getX() );
        double  ycoRounded      = round( pointIn.getY() );
        Point2D pointRounded    = new Point2D.Double( xcoRounded, ycoRounded );
        return pointRounded;
    }
    
    /**
     * Round a given number to the nearest tenth.
     * Negative numbers round away from 0;
     * e.g. -3.5 rounds to 4.
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
    
    public static double slope( Line2D line )
    {
        double  slope  = slope( line.getP1(), line.getP2() );
        return slope;
    }
    
    public static double length( Line2D line )
    {
        Point2D point1  = line.getP1();
        double  length  = point1.distance( line.getP2() );
        return length;
    }
    
    public static double slope( Point2D point1, Point2D point2 )
    {
        double  deltaX  = point1.getX() - point2.getX();
        double  deltaY  = point1.getY() - point2.getY();
        double  slope   = deltaY / deltaX;
        // return slope relative to Cartesian plane
        slope = -slope;
        return slope;
    }
    
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
    
    public static boolean match( Point2D point1, Point2D point2 )
    {
        double  xDiff   = Math.abs( point1.getX() - point2.getX() );
        double  yDiff   = Math.abs( point1.getY() - point2.getY() );
        boolean result  = xDiff < EPSILON && yDiff < EPSILON;
        return result;
    }
    
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
    
    public static double radians( Line2D line )
    {
        double  radians = radians( line.getP1(), line.getP2() );
        return radians;
    }
    
    public static double radians( Point2D point1, Point2D point2 )
    {
        double  slope   = slope( point1, point2 );
        double  radians = Math.atan( slope );
        return radians;
    }
    
    private static String decodeCoords( double[] coords, int from )
    {
        String          strXco  = String.format( "%6.2f", coords[from] );
        String          strYco  = String.format( "%6.2f", coords[from + 1] );
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( '(' ).append( strXco ).append( ',')
            .append( strYco ).append( ')' );
        return bldr.toString();
    }
    
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
        case PathIterator.SEG_QUADTO:
            strType = "quadTo";
            break;
        case PathIterator.SEG_CUBICTO:
            strType = "cubicTo";
            break;
        case PathIterator.SEG_CLOSE:
            strType = "close";
            break;
        }
        return strType;
    }
}
