package com.acmemail.judah.java_records;

import java.awt.geom.Point2D;
import java.util.Objects;

/**
 * Traditional implementation of a Circle class,
 * for comparison to a record class implementation.
 * 
 * @see CircleRecord
 */
public class CircleTraditional
{
    private final   double  diameter;
    private final   Point2D center;
    
    /**
     * Constructor.
     * Initializes the diameter and center of a circle.
     * 
     * @param diameter  the diameter of the circle
     * @param center    the center of the circle
     */
    public CircleTraditional( double diameter, Point2D center )
    {
        this.diameter = diameter;
        this.center = center;
    }

    /**
     * Gets the center of the encapsulated circle.
     * @return the diameter
     */
    public double getDiameter()
    {
        return diameter;
    }

    /**
     * Gets the center of the encapsulated circle.
     * @return the center
     */
    public Point2D getCenter()
    {
        return center;
    }

    /**
     * Gets the center of the encapsulated circle.
     * @return the area
     */
    public double getArea()
    {
        double  radius  = diameter / 2;
        double  area    = Math.PI * radius * radius;
        return area;
    }
    
    /**
     * Gets the circumference of the encapsulated circle.
     * @return the circumference
     */
    public double getCircumference()
    {
        double  circum  = Math.PI * diameter;
        return circum;
    }

    @Override
    public String toString()
    {
        String  str = 
            "CircleTraditional [diameter=" + diameter + ", "
            + "center=" + center + "]";
        return str;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(center, diameter);
    }

    @Override
    public boolean equals(Object obj)
    {
        boolean rcode   = false;
        if (this == obj)
            rcode = true;
        else if (obj == null)
            rcode = false;
        else if ( obj instanceof CircleTraditional that )
        {
            
            if ( !Objects.equals( this, that ) )
                rcode = false;
            else if ( Double.compare( diameter, that.diameter ) != 0 )
                rcode = false;
            else
                rcode = true;
        }
        return rcode;
    }
}
