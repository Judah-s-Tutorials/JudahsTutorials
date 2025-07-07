package com.gmail.johnstraub1954.penrose.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.junit.jupiter.api.Test;

public class UtilsTestIntersectLine2Line2D
{
    private static TwoLineTestFeedback    feedbackPanel = 
        new TwoLineTestFeedback();
    
    private static Point2D  point1End1  = null;
    private static Point2D  point1End2  = null;
    private static Point2D  point2End1  = null;
    private static Point2D  point2End2  = null;
    private static Point2D  endPoint    = null;
    private static Line2D   line1       = null;
    private static Line2D   line2       = null;  
    
    @Test
    public void test()
    {
        testIntersectTrue();
    }
    
    @Test
    public static void testIntersectTrue()
    {
        // Intersect at left endpoint
        // Intersect at one endpoint
        point1End1  = new Point2D.Double( 10, 20 );
        endPoint    = new Point2D.Double( 40, 80 );
        point2End2  = new Point2D.Double( 80, 160 );
        line1   = new Line2D.Double( point1End1, endPoint );
        line2   = new Line2D.Double( endPoint, point2End2 );  
        test( line1, line2, true );
        test( line2, line1, true );
        
        // Intersect at overlap
        point1End1  = new Point2D.Double( 10, 30 );
        point1End2  = new Point2D.Double( 40, 120 );
        point2End1  = new Point2D.Double( 30, 90 );
        point2End2  = new Point2D.Double( 120, 360 );
        line1   = new Line2D.Double( point1End1, point1End2 );
        line2   = new Line2D.Double( point2End1, point2End2 );  
        test( line1, line2, true );
        test( line2, line1, true );
        
        // Intersect at one internal point
        point1End1  = new Point2D.Double( 0, 30 );
        point1End2  = new Point2D.Double( 150, 150 );
        point2End1  = new Point2D.Double( 150, 30 );
        point2End2  = new Point2D.Double( 0, 150 );
        line1   = new Line2D.Double( point1End1, point1End2 );
        line2   = new Line2D.Double( point2End1, point2End2 );  
        test( line1, line2, true );
        test( line2, line1, true );
    }

    private static void test( Line2D line1, Line2D line2, boolean expResult )
    {
        Point2D line1End1   = line1.getP1(); 
        Point2D line1End2   = line1.getP2(); 
        Point2D line2End1   = line2.getP1(); 
        Point2D line2End2   = line2.getP2();
        test( line1End1, line1End2, line2End1, line2End2, expResult );
        test( line1End1, line1End2, line2End2, line2End1, expResult );
        test( line1End2, line1End1, line2End1, line2End2, expResult );
        test( line1End2, line1End1, line2End2, line2End1, expResult );
    }
    
    private static void test( 
        Point2D line1End1, 
        Point2D line1End2, 
        Point2D line2End1, 
        Point2D line2End2,
        boolean expResult
    )
    {
        Line2D  line1       = new Line2D.Double( line1End1, line1End2 );
        Line2D  line2       = new Line2D.Double( line2End1, line2End2 );
        String  strLine1    = format( line1 );
        String  strLine2    = format( line2 );
        String  comment     = strLine1 + " // " + strLine2;
        boolean actResult   = Utils.intersect( line1, line2 );
        if ( feedbackPanel != null )
            feedbackPanel.show( line1, line2 );
        assertEquals( expResult, actResult, comment );
    }
    
    private static String format( Line2D line )
    {
        String  point1  = format( line.getP1() );
        String  point2  = format( line.getP2() );
        String  result  = "[" + point1 + " -> " + point2 + "]";
        return result;
    }
    
    private static String format( Point2D point )
    {
        String  result  = "(" + point.getX() + "," + point.getY() + ")";
        return result;
    }
}
