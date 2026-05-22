package com.acmemail.judah.java_records;

import java.awt.geom.Point2D;
import java.util.Objects;

public record CircleRecord( double diameter, Point2D center )
{
    // Overridden canonical constructor disallows null center
    public CircleRecord
    {
        Objects.requireNonNull( center, "center" );
    }
    
    // Non-canonical constructor defaults center to (0,0)
    public CircleRecord( double diameter )
    {
        this( diameter, new Point2D.Double( 0, 0 ) );
    }
    

    /**
     * Gets the center of the encapsulated circle.
     * @return the area
     */
    public double area()
    {
        double  radius  = diameter / 2;
        double  area    = Math.PI * radius * radius;
        return area;
    }
    
    /**
     * Gets the circumference of the encapsulated circle.
     * @return the circumference
     */
    public double circumference()
    {
        double  circum  = Math.PI * diameter;
        return circum;
    }
}
