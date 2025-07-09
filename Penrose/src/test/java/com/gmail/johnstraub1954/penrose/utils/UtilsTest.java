package com.gmail.johnstraub1954.penrose.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.junit.jupiter.api.Test;

public class UtilsTest
{
    @Test
    public void testRoundLine2D()
    {
        testRoundLine2D( 3.14, 2.14, 3.1, 2.1 );
        testRoundLine2D( 3.15, 2.15, 3.2, 2.2 );
        testRoundLine2D( -3.14, -2.14, -3.1, -2.1 );
        testRoundLine2D( -3.15, -2.15, -3.2, -2.2 );
        testRoundLine2D(
            Double.POSITIVE_INFINITY,
            Double.NEGATIVE_INFINITY,
            Double.POSITIVE_INFINITY,
            Double.NEGATIVE_INFINITY
        );
    }
    
    private static void 
    testRoundLine2D( double inXco, double inYco, double expXco, double expYco )
    {
        double  inXco2  = (Math.abs( inXco ) + 5) * Math.signum( inXco );
        double  inYco2  = (Math.abs( inYco ) + 5) * Math.signum( inYco );
        double  expXco2 = (Math.abs( expXco ) + 5) * Math.signum( expXco );
        double  expYco2 = (Math.abs( expYco ) + 5) * Math.signum( inYco );
        Line2D  input   = new Line2D.Double( inXco, inYco, inXco2, inYco2 );
        Line2D  output  = Utils.round( input );
        Line2D  exp     = new Line2D.Double( expXco, expYco, expXco2, expYco2 );
        assertLineEquals( exp, output );
    }

    @Test
    public void testRoundPoint2D()
    {
        Point2D input1  = new Point2D.Double( 3.14, 2.14 );
        Point2D output1 = Utils.round( input1 );
        Point2D exp1    = new Point2D.Double( 3.1, 2.1 );
        assertEquals( exp1, output1 );

        Point2D input2  = new Point2D.Double( 3.15, 2.15 );
        Point2D output2 = Utils.round( input2 );
        Point2D exp2    = new Point2D.Double( 3.2, 2.2 );
        assertEquals( exp2, output2 );

        Point2D input3  = new Point2D.Double( -3.15, -2.15 );
        Point2D output3 = Utils.round( input3 );
        Point2D exp3    = new Point2D.Double( -3.2, -2.2 );
        assertEquals( exp3, output3 );

        Point2D input4  = 
            new Point2D.Double( 
                Double.POSITIVE_INFINITY,
                Double.POSITIVE_INFINITY
        );
        Point2D output4 = Utils.round( input4 );
        Point2D exp4    = 
            new Point2D.Double( 
                Double.POSITIVE_INFINITY,
                Double.POSITIVE_INFINITY
        );
        assertEquals( exp4, output4 );

        Point2D input5  = 
            new Point2D.Double( 
                Double.NEGATIVE_INFINITY,
                Double.NEGATIVE_INFINITY
        );
        Point2D output5 = Utils.round( input5 );
        Point2D exp5    = 
            new Point2D.Double( 
                Double.NEGATIVE_INFINITY,
                Double.NEGATIVE_INFINITY
        );
        assertEquals( exp5, output5 );
    }

    @Test
    public void testRoundDouble()
    {
        double  input1  = 3.14999;
        double  output1 = Utils.round( input1 );
        double  exp1    = 3.1;
        assertEquals( exp1, output1 );

        double  input2  = 3.15000;
        double  output2 = Utils.round( input2 );
        double  exp2    = 3.2;
        assertEquals( exp2, output2 );
        
        double  input3  = -3.14999;
        double  output3 = Utils.round( input3 );
        double  exp3    = -3.1;
        assertEquals( exp3, output3 );

        double  input4  = -3.15000;
        double  output4 = Utils.round( input4 );
        double  exp4    = -3.2;
        assertEquals( exp4, output4 );
        
        double  input5  = Double.POSITIVE_INFINITY;
        double  output5 = Utils.round( input5 );
        double  exp5    = Double.POSITIVE_INFINITY;
        assertEquals( exp5, output5 );
        
        double  input6  = Double.NEGATIVE_INFINITY;
        double  output6 = Utils.round( input6 );
        double  exp6    = Double.NEGATIVE_INFINITY;
        assertEquals( exp6, output6 );
    }

    @Test
    public void testOrientationPoint2DPoint2DPoint2D()
    {
        Point2D pointA  = new Point2D.Double( 11, 25 );
        Point2D pointB  = new Point2D.Double( 21, 5 );
        Point2D pointC  = new Point2D.Double( 22, 11 );
        testOrientation( pointA, pointB, pointC, -1 );
        
        pointA  = new Point2D.Double( 1.1, 2.5 );
        pointB  = new Point2D.Double( 1.2, 2.4 );
        pointC  = new Point2D.Double( 1.15, 2.45 );
        testOrientation( pointA, pointB, pointC, -1 );
        
        pointA  = new Point2D.Double( 11, 25 );
        pointB  = new Point2D.Double( 9, 27 );
        pointC  = new Point2D.Double( 8, 30 );
        testOrientation( pointA, pointB, pointC, 1 );
        
        pointA  = new Point2D.Double( 1.1, 2.5 );
        pointB  = new Point2D.Double( .9, 2.7 );
        pointC  = new Point2D.Double( .8, 3.0 );
        testOrientation( pointA, pointB, pointC, 1 );
        
        pointA  = new Point2D.Double( 11, 25 );
        pointB  = new Point2D.Double( 20, 25 );
        pointC  = new Point2D.Double( 8, 25 );
        testOrientation( pointA, pointB, pointC, 0 );
        
        pointA  = new Point2D.Double( 11, 25 );
        pointB  = new Point2D.Double( 11, 20 );
        pointC  = new Point2D.Double( 11, 8 );
        testOrientation( pointA, pointB, pointC, 0 );
    }
    
    private void 
    testOrientation( 
        Point2D pointA, 
        Point2D pointB, 
        Point2D pointC, 
        int expResult
    )
    {
        String  feedback    = 
            format( pointA) + "//" + format( pointB) + "//" + format( pointC );
        int     actResult   = Utils.orientation( pointA, pointB, pointC );
        assertEquals( expResult, actResult, feedback );
    }
    
    private static void assertLineEquals( Line2D line1, Line2D line2 )
    {
        assertEquals( line1.getP1(), line2.getP1() );
        assertEquals( line2.getP2(), line2.getP2() );
    }
    
    private static String format( Point2D point )
    {
        String  strPoint    = 
            String.format( "(%.2f,%.2f", point.getX(), point.getY() );
        return strPoint;
    }
}
