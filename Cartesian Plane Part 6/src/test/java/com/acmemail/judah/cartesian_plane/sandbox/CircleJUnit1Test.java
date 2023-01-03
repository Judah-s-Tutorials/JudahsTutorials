package com.acmemail.judah.cartesian_plane.sandbox;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CircleJUnit1Test
{
    private static final double epsilon = .001;
    
    @Test
    void testCircle()
    {
        double  xco         = 10;
        double  yco         = 20;
        double  radius      = 30;
        Circle  circle      = new Circle( xco, yco, radius );
        
        double  actXco      = circle.getXco();
        double  actYco      = circle.getYco();
        double  actRadius   = circle.getRadius();
        
        assertEquals( xco, actXco );
        assertEquals( yco, actYco );
        assertEquals( radius, actRadius, epsilon );
    }

    @Test
    void testGetCircumference()
    {
        double  radius      = 30;
        double  expCircum   = Math.PI * 2 * radius;
        Circle  circle      = new Circle( 0, 0, radius );
        double  actCircum   = circle.getCircumference();
        
        assertEquals( expCircum, actCircum, epsilon );
    }

    @Test
    void testGetArea()
    {
        double  radius      = 30;
        double  expArea     = Math.PI * radius * radius;
        Circle  circle      = new Circle( 0, 0, radius );
        double  actArea     = circle.getArea();
        
        assertEquals( expArea, actArea, epsilon );
    }

    @Test
    void testGetXco()
    {
        Circle  circle      = new Circle( 0, 0, 0 );
        double  expVal      = 10;
        circle.setXco( expVal );
        double  actVal      = circle.getXco();
        
        assertEquals( expVal, actVal, epsilon );
    }

    @Test
    void testSetXco()
    {
        Circle  circle      = new Circle( 0, 0, 0 );
        double  expVal      = 10;
        circle.setXco( expVal );
        double  actVal      = circle.getXco();
        
        assertEquals( expVal, actVal, epsilon );
    }

    @Test
    void testGetYco()
    {
        Circle  circle      = new Circle( 0, 0, 0 );
        double  expVal      = 10;
        circle.setYco( expVal );
        double  actVal      = circle.getYco();
        
        assertEquals( expVal, actVal, epsilon );
    }

    @Test
    void testSetYco()
    {
        Circle  circle      = new Circle( 0, 0, 0 );
        double  expVal      = 10;
        circle.setYco( expVal );
        double  actVal      = circle.getYco();
        
        assertEquals( expVal, actVal, epsilon );
    }

    @Test
    void testGetRadius()
    {
        Circle  circle      = new Circle( 0, 0, 0 );
        double  expVal      = 10;
        circle.setRadius( expVal );
        double  actVal      = circle.getRadius();
        
        assertEquals( expVal, actVal, epsilon );
    }

    @Test
    void testSetRadius()
    {
        Circle  circle      = new Circle( 0, 0, 0 );
        double  expVal      = 10;
        circle.setRadius( expVal );
        double  actVal      = circle.getRadius();
        
        assertEquals( expVal, actVal, epsilon );
    }
}
