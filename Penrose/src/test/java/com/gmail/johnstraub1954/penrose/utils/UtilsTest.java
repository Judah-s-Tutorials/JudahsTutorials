package com.gmail.johnstraub1954.penrose.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.gmail.johnstraub1954.penrose.PDart;
import com.gmail.johnstraub1954.penrose.Vertex;

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
    
    @Test
    public void testLiesOnPoint2DLine2D()
    {
        Point2D endPoint1   = new Point2D.Double( -2, -2 );
        Point2D midPoint1   = new Point2D.Double( -1, -1 );
        Point2D midPoint2   = new Point2D.Double( 1, 1 );
        Point2D endPoint2   = new Point2D.Double( 2, 2 );
        Point2D miscPoint   = new Point2D.Double();
        Line2D  testLine    = new Line2D.Double( endPoint1, endPoint2 );
        assertTrue( Utils.liesOn( endPoint1, testLine ) );
        assertTrue( Utils.liesOn( midPoint1, testLine ) );
        assertTrue( Utils.liesOn( midPoint2, testLine ) );
        assertTrue( Utils.liesOn( endPoint2, testLine ) );
        
        // On the same virtual (infinitely long) line, but not on 
        // the given line segment.
        miscPoint.setLocation( -2.1, -2.1 );
        assertFalse( Utils.liesOn( miscPoint, testLine ) );
        miscPoint.setLocation( 2.1, 2.1 );
        assertFalse( Utils.liesOn( miscPoint, testLine ) );
        
        // A little below, a little above the given line segment.
        miscPoint.setLocation( -2, -2.1 );
        assertFalse( Utils.liesOn( miscPoint, testLine ) );
        miscPoint.setLocation( -2, -1.9 );
        assertFalse( Utils.liesOn( miscPoint, testLine ) );
        miscPoint.setLocation( 2, 2.1 );
        assertFalse( Utils.liesOn( miscPoint, testLine ) );
        miscPoint.setLocation( 2, 1.9 );
        assertFalse( Utils.liesOn( miscPoint, testLine ) );
        
        // A little to the left, a little to the right 
        // of the given line segment.
        miscPoint.setLocation( -2.1, -2 );
        assertFalse( Utils.liesOn( miscPoint, testLine ) );
        miscPoint.setLocation( 2.1, 2 );
        assertFalse( Utils.liesOn( miscPoint, testLine ) );
    }
    
    @Test
    public void testIntersectLine2DLine2D()
    {
        UtilsTestIntersectLine2Line2D.testIntersectTrue();
        UtilsTestIntersectLine2Line2D.testIntersectFalse();
    }
    
    @Test
    public void testSubtractPoint2DPoint2D()
    {
        double  xcoA    = 5;
        double  ycoA    = 10;
        double  xcoB    = 1;
        double  ycoB    = 6;
        double  expXco  = xcoB - xcoA;
        double  expYco  = ycoB - ycoA;
        Point2D pointA  = new Point2D.Double( xcoA, ycoA );
        Point2D pointB  = new Point2D.Double( xcoB, ycoB );
        Point2D act     = Utils.subtract( pointA, pointB );
        assertEquals( expXco, act.getX() );
        assertEquals( expYco, act.getY() );
        
    }
    
    @Test
    public void testSlopeLine2D()
    {
        Line2D  line    = new Line2D.Double( 2, 3, 6, 7 );
        double  slope   = Utils.slope( line );
        assertEquals( -1, slope );
        
        line.setLine( 2, 1, 1, 2 );
        slope   = Utils.slope( line );
        assertEquals( 1, slope );
        
        line.setLine( 2, 1, 5, 1 );
        slope   = Utils.slope( line );
        // don't distinguish between 0.0 and -0.0
        slope = Math.abs( slope );
        assertEquals( 0, slope );
        
        line.setLine( 1, 1, 1, 10 );
        slope   = Utils.slope( line );
        assertEquals( Double.NEGATIVE_INFINITY, slope );
        
        line.setLine( 1, 10, 1, 1 );
        slope   = Utils.slope( line );
        assertEquals( Double.POSITIVE_INFINITY, slope );
    }
    
    @Test
    public void testLengthLine2D()
    {
        Line2D  line    = new Line2D.Double( 3, 3, 6, 7 );
        double  len     = Utils.length( line );
        assertEquals( 5, len );
        
        line.setLine( 3, 3, 3, 3 );
        len = Utils.length( line );
        assertEquals( 0, len );
    }
     
    @Test
    public void testMatchLine2DLine2D()
    {
        Line2D  line1   = new Line2D.Double( 5.13, 10.11, 10.51, 11.61 );
        Line2D  line2   = new Line2D.Double( 5.11, 10.12, 10.50, 11.64 );
        assertTrue( Utils.match( line1, line2 ) );

        line2.setLine( 5.16, 10.17, 10.55, 11.66 );
        assertFalse( Utils.match( line1, line2 ) );
        
        line1.setLine( 5.15, 10.16, 10.57, 11.65 );
        assertTrue( Utils.match( line1, line2 ) );
        
        line2.setLine( line1 );
        assertTrue( Utils.match( line1, line2 ) );
        
        line2.setLine( line1.getP2(), line1.getP1() );
        assertTrue( Utils.match( line1, line2 ) );
    }
    
    @Test
    public void testMatchPoint2DPoint2D()
    {
        Point2D point1  = new Point2D.Double( 5.13, 10.11 );
        Point2D point2  = new Point2D.Double( 5.10, 10.14 );
        assertTrue( Utils.match( point1, point2 ) );
        
        point2.setLocation( 5.18, 10.16 );
        assertFalse( Utils.match( point1, point2 ) );

        point1.setLocation( 5.15, 10.15 );
        assertTrue( Utils.match( point1, point2 ) );
    }

    
    @Test
    public void testMatchDoubleDouble()
    {
        double  posInfinity = Double.POSITIVE_INFINITY;
        double  negInfinity = Double.NEGATIVE_INFINITY;
        assertTrue( Utils.match( 10.19, 10.21 ) );
        assertFalse( Utils.match( 10.19, 10.11 ) );
        assertTrue( Utils.match( posInfinity, posInfinity ) );
        assertTrue( Utils.match( negInfinity, negInfinity ) );
        assertTrue( Utils.match( posInfinity, negInfinity ) );
    }
    
    @Test
    public void testRadiansPoint2DPoint2D()
    {
        Point2D pointA      = new Point2D.Double( 1, 1 );
        Point2D pointB      = new Point2D.Double( 5, 5 );
        double  radians     = Utils.radians( pointA, pointB );
        double  expected    = -Math.PI / 4;
        assertEquals( expected, radians, .0001 );
        
        radians = Utils.radians( pointB, pointA );
        assertEquals( expected, radians, .0001 );
        
        pointB.setLocation( 0, 2 );
        radians = Utils.radians( pointA, pointB );
        expected = Math.PI / 4;
        assertEquals( expected, radians, .0001 );
        
        pointB.setLocation( 1,5 );
        radians = Utils.radians( pointA, pointB );
        expected = -Math.PI / 2;
        assertEquals( expected, radians, .0001 );
        
        pointB.setLocation( 0, 0 );
        radians = Utils.radians( pointA, pointB );
        expected = -Math.PI / 4;
        assertEquals( expected, radians, .0001 );
        
        pointB.setLocation( 0, 1 );
        radians = Utils.radians( pointA, pointB );
        // don't distinguish between 0.0 and -0.0
        radians = Math.abs( radians );
        expected = 0;
        assertEquals( expected, radians, .0001 );
    }
    
    @Test
    public void testRadiansLine2D()
    {
        Point2D pointA      = new Point2D.Double( 1, 1 );
        Point2D pointB      = new Point2D.Double( 5, 5 );
        Line2D  line        = new Line2D.Double( pointA, pointB );
        double  radians     = Math.abs( Utils.radians( line ) );
        double  expected    = Math.PI / 4;
        assertEquals( expected, radians, .0001 );
        
        pointB.setLocation( 0, 0 );
        line.setLine( pointA, pointB );
        radians = Math.abs( Utils.radians( line ) );
        expected = Math.PI / 4;
        assertEquals( expected, radians, .0001 );
        
        pointB.setLocation( 1, 5 );
        line.setLine( pointA, pointB );
        radians = Math.abs( Utils.radians( line ) );
        expected = Math.PI / 2;
        assertEquals( expected, radians, .0001 );
    }
    
    @Test
    public  void testFormatPathIteratorPathIterator()
    {
        // This test is just to improve test coverage.
        // "formatPathIterator" is a debugging aid, and should
        // be deleted in the near future.
        Line2D  line    = new Line2D.Double( 0, 0, 10, 10 );
        Utils.formatPathIterator( line.getPathIterator( null ) );
    }
    
    @Test
    public  void testPrintStringList()
    {
        // This test is just to improve test coverage.
        // "print" is a debugging aid, and should
        // be deleted in the near future.
        PDart           dart    = new PDart( 100 );
        List<Vertex>    list    = dart.getVertices();
        Utils.print( "", list );
    }
    
    @Test
    public  void testFormatIntersectionPShapePShape()
    {
        // This test is just to improve test coverage.
        // "formatIntersection" is a debugging aid, and should
        // be deleted in the near future.
        PDart           dart1   = new PDart( 100 );
        PDart           dart2   = new PDart( 100 );
        Utils.formatIntersection( dart1, dart2 );
        
        dart2.moveTo( 500, 500 );
        Utils.formatIntersection( dart1, dart2 );
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
