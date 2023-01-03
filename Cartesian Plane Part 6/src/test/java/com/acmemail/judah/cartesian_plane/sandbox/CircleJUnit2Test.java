package com.acmemail.judah.cartesian_plane.sandbox;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CircleJUnit2Test
{
    private static final double epsilon = .001;
    private static final double defXco      = 10;
    private static final double defYco      = 2 * defXco;
    private static final double defRadius   = 2 * defYco;
    private static final double defTestVal  = 2 * defRadius;
    
    private Circle  circle;
  
    @BeforeEach
    public void beforeEach()
    {
        circle = new Circle( defXco, defYco, defRadius );
    }
    
    @Test
    void testCircle()
    {
        double  actXco      = circle.getXco();
        double  actYco      = circle.getYco();
        double  actRadius   = circle.getRadius();
        
        assertEquals( defXco, actXco );
        assertEquals( defYco, actYco );
        assertEquals( defRadius, actRadius, epsilon );
    }

    @Test
    void testGetCircumference()
    {
        double  expCircum   = Math.PI * 2 * defRadius;
        double  actCircum   = circle.getCircumference();
        
        assertEquals( expCircum, actCircum, epsilon );
    }

    @Test
    void testGetArea()
    {
        double  expArea     = Math.PI * defRadius * defRadius;
        double  actArea     = circle.getArea();
        
        assertEquals( expArea, actArea, epsilon );
    }

    @Test
    void testGetXco()
    {
        circle.setXco( defTestVal );
        double  actVal      = circle.getXco();
        
        assertEquals( defTestVal, actVal, epsilon );
    }

    @Test
    void testSetXco()
    {
        circle.setXco( defTestVal );
        double  actVal      = circle.getXco();
        
        assertEquals( defTestVal, actVal, epsilon );
    }

    @Test
    void testGetYco()
    {
        circle.setYco( defTestVal );
        double  actVal      = circle.getYco();
        
        assertEquals( defTestVal, actVal, epsilon );
    }

    @Test
    void testSetYco()
    {
        circle.setYco( defTestVal );
        double  actVal      = circle.getYco();
        
        assertEquals( defTestVal, actVal, epsilon );
    }

    @Test
    void testGetRadius()
    {
        circle.setRadius( defTestVal );
        double  actVal      = circle.getRadius();
        
        assertEquals( defTestVal, actVal, epsilon );
    }

    @Test
    void testSetRadius()
    {
        circle.setRadius( defTestVal );
        double  actVal      = circle.getRadius();
        
        assertEquals( defTestVal, actVal, epsilon );
    }
}
