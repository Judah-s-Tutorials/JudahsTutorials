package com.gmail.johnstraub1954.penrose.utils;

import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;

import com.gmail.johnstraub1954.penrose.PShape;

public class Utils
{
    public static final double  EPSILON = .05;
    
    private static final String  newl   = System.lineSeparator();
    /**
     * Default constructor; not used.
     */
    private Utils()
    {
        // not used
    }
    
//    public static boolean liesOn( )
    
    public static boolean liesOn( Point2D point, Line2D line )
    {
        double  xco         = point.getX();
        double  yco         = point.getY();
        double  slope       = slope( line );
        double  yIntercept  = xco;
        if ( slope != Double.POSITIVE_INFINITY )
            yIntercept = yco - slope * xco;
        double  diff        = yco - (slope * xco + yIntercept );
        boolean result      = match( diff, 0 );
        return result;
    }
    
    public static double slope( Line2D line )
    {
        double  slope  = slope( line.getP1(), line.getP2() );
        return slope;
    }
    
    public static double slope( Point2D point1, Point2D point2 )
    {
        double  deltaX  = point1.getX() - point2.getX();
        double  deltaY  = point1.getY() - point2.getY();
        double  slope   = deltaY / deltaX;
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
        double  diff    = Math.abs( val1 - val2 );
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
