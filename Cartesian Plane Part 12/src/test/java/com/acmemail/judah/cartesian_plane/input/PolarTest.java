package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.geom.Point2D;

import org.junit.jupiter.api.Test;

class PolarTest
{
    private static final double testRadius  = 2;
    private static final double testTheta   = Math.PI / 3;

    private static final double testXco     = 
        testRadius * Math.cos( testTheta );
    private static final double testYco     = 
        testRadius * Math.sin( testTheta );
    
    private static final Polar      testPolar   = 
        Polar.of( testRadius, testTheta );
    private static final Point2D    testPoint   = 
        new Point2D.Double( testXco, testYco );

    @Test
    void testToPoint()
    {
        Point2D point   = testPolar.toPoint();
        assertEquals( testXco, point.getX(), .00001 );
        assertEquals( testYco, point.getY(), .00001 );
    }

    @Test
    void testGetRadius()
    {
        assertEquals( testRadius, testPolar.getRadius() );
    }

    @Test
    void testGetTheta()
    {
        assertEquals( testTheta, testPolar.getTheta() );
    }

    @Test
    void testToString()
    {
        int     maxLen          = 4;
        String  testRadiusStr   = Double.toString( testRadius );
        if ( testRadiusStr.length() > maxLen )
            testRadiusStr = testRadiusStr.substring( 0, maxLen );
        String  testThetaStr    = Double.toString( testTheta );
        if ( testThetaStr.length() > maxLen )
            testThetaStr = testThetaStr.substring( 0, maxLen );
        
        String  testStr = testPolar.toString();
        assertTrue( testStr.contains( testRadiusStr ), testRadiusStr );
        assertTrue( testStr.contains( testThetaStr ), testThetaStr );
    }

    @Test
    void testOfPoint2D()
    {
        Polar   actPolar    = Polar.of( testPoint );
        assertEquals( testPolar.getRadius(), actPolar.getRadius(), .0001 );
        assertEquals( testPolar.getTheta(), actPolar.getTheta(), .0001 );
    }

    @Test
    void testOfDoubleDouble()
    {
        Polar   actPolar    = Polar.of( testRadius, testTheta );
        assertEquals( testRadius, actPolar.getRadius() );
        assertEquals( testTheta, actPolar.getTheta() );
    }

    @Test
    void testOfXY()
    {
        Polar   actPolar    = Polar.of( testRadius, testTheta );
        assertEquals( testRadius, actPolar.getRadius() );
        assertEquals( testTheta, actPolar.getTheta() );
    }

    @Test
    void testRadiusOfXY()
    {
        Double  actRadius   = Polar.radiusOfXY( testXco, testYco );
        assertEquals( testRadius, actRadius );
    }

    @Test
    void testThetaOfXY()
    {
        Double  actTheta    = Polar.thetaOfXY( testXco, testYco );
        assertEquals( testTheta, actTheta );
    }
}
