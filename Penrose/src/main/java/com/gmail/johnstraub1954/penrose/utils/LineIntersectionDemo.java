package com.gmail.johnstraub1954.penrose.utils;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class LineIntersectionDemo
{
    private static final String format  = "(%5.0f, %5.0f)";
    private static final String endl    = System.lineSeparator();
    
    public LineIntersectionDemo()
    {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args)
    {
        // Lines intersecting at an interior point
        Line2D  intersectLine1  = new Line2D.Double( -1000, -1000, 1000, 1000 );
        Line2D  intersectLine2  = new Line2D.Double( 1000, 1000, -1000, -1000 );
        testIntersect( "interior intersect", intersectLine1, intersectLine2 );
        
        // Lines intersecting at an endpoint 1
        Line2D  endpoint1Line1  = new Line2D.Double( 10, 10, 100, 10 );
        Line2D  endpoint1Line2  = new Line2D.Double( 10, 10, 10, 100 );
        testIntersect( "endpoint 1 intersect", endpoint1Line1, endpoint1Line2 );
        
        // Lines intersecting at an endpoint 2
        Line2D  endpoint2Line1  = new Line2D.Double( 10, 100, 10, 10 );
        Line2D  endpoint2Line2  = new Line2D.Double( 10, 100, 100, 100 );
        testIntersect( "endpoint 2 intersect", endpoint2Line1, endpoint2Line2 );
        
        // Lines that don't intersect
        Line2D  parallelLine1   = new Line2D.Double( 10, 10, 100, 10 );
        Line2D  parallelLine2   = new Line2D.Double( 100, 100, 200, 10 );
        testIntersect( "parallel lines", parallelLine1, parallelLine2 );
        
        // Lines that overlap
        Line2D  overlapLine1    = new Line2D.Double( 10, 10, 100, 10 );
        Line2D  overlapLine2    = new Line2D.Double( 10, 10, 200, 10 );
        testIntersect( "overlap", overlapLine1, overlapLine2 );
    }
    
    private static void 
    testIntersect( String comment, Line2D line1, Line2D line2 )
    {
        boolean         intersect   = line1.intersectsLine( line2 );
        String          strLine1    = formatLine( line1 );
        String          strLine2    = formatLine( line2 );
        StringBuilder   bldr        = new StringBuilder();
        bldr.append( comment ).append(endl)
            .append( strLine1 ).append( "  ...  ").append( strLine2 )
            .append( ": " ).append( intersect )
            .append( endl ).append( "#################" );
        System.out.println( bldr );
    }
    
    private static String formatLine( Line2D line )
    {
        String  end1    = formatCoordinate( line.getP1() );
        String  end2    = formatCoordinate( line.getP2() );
        String  str     = "[" + end1 + "->" + end2 + "]";
        return str;
    }
    
    private static String formatCoordinate( Point2D point )
    {
        String  formatted   = 
            String.format( format, point.getX(),  point.getY() );
        return formatted;
    }
}
