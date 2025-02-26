package com.gmail.johnstraub1954.penrose2.matcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.awt.geom.Point2D;

import org.junit.jupiter.api.Test;

import com.gmail.johnstraub1954.penrose.matcher.Distance;

class DistanceTest
{
    @Test
    public void testGetDistance1()
    {
        Point2D     pointA  = new Point2D.Double( 0, 3 );
        Point2D     pointB  = new Point2D.Double( 4, 0 );
        Distance    dist    = new Distance( pointA, pointB );
        assertEquals( 5, dist.getDistance() );
    }
    
    @Test
    public void testGetDistance2()
    {
        Point2D     pointA  = new Point2D.Double( 0, -3 );
        Point2D     pointB  = new Point2D.Double( -4, 0 );
        Distance    dist    = new Distance( pointA, pointB );
        assertEquals( 5, dist.getDistance() );
    }
    
    @Test
    void testEquals()
    {
        Point2D     pointA  = new Point2D.Double();
        Point2D     pointB  = new Point2D.Double();
        Distance    distA   = null;
        Distance    distB   = null;
        
        pointA.setLocation( 0, 0 );
        pointB.setLocation( 1, 1 );
        distA = new Distance( pointA, pointB );
        pointA.setLocation( 3, 3 );
        pointB.setLocation( 4, 4 );
        distB = new Distance( pointA, pointB );
        
        assertEquals( distA, distA );
        assertNotEquals( distA, null );
        assertNotEquals( distA, new Object() );
        assertEquals( distA, distB );
        assertEquals( distB, distA );
        assertEquals( distA.hashCode(), distB.hashCode() );
        
        pointA.setLocation( 0.0000001, 0.0000001 );
        pointB.setLocation( 1, 1 );
        distA = new Distance( pointA, pointB );
        pointA.setLocation( 3, 3 );
        pointB.setLocation( 4, 4 );
        distB = new Distance( pointA, pointB );
        
        assertEquals( distA, distA );
        assertEquals( distA, distB );
        assertEquals( distB, distA );
        assertEquals( distA.hashCode(), distB.hashCode() );
        
        pointA.setLocation( 0.1, 0.1 );
        pointB.setLocation( 1, 1 );
        distA = new Distance( pointA, pointB );
        pointA.setLocation( 3, 3 );
        pointB.setLocation( 4, 4 );
        distB = new Distance( pointA, pointB );
        
        assertNotEquals( distA, distB );
        assertNotEquals( distB, distA );
    }

}
