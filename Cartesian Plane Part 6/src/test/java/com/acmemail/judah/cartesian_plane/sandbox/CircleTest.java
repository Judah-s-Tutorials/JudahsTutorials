package com.acmemail.judah.cartesian_plane.sandbox;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CircleTest
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
        fail("Not yet implemented");
    }

    @Test
    void testGetXco()
    {
        fail("Not yet implemented");
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
        fail("Not yet implemented");
    }

    @Test
    void testSetYco()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetRadius()
    {
        fail("Not yet implemented");
    }

    @Test
    void testSetRadius()
    {
        fail("Not yet implemented");
    }

}
