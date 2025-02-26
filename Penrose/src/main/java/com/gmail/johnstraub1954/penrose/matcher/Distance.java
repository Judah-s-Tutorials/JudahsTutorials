package com.gmail.johnstraub1954.penrose.matcher;

import java.awt.geom.Point2D;

/**
 * Encapsulates the distance between two points
 * in the Cartesian plane.
 * 
 * @author Jack Straub
 */
public class Distance
{
    /** For calculating the approximate equality of two doubles. */
    private static final double epsilon     = .00001;
    /** The encapsulated distance. */
    private final double        distance;
    
    /**
     * Instantiates a Distance object
     * based on two given points.
     * 
     * @param pointA    the first given point
     * @param pointB    the second given point
     */
    public Distance( Point2D pointA, Point2D pointB )
    {
        distance = pointA.distance( pointB );
    }
    
    /**
     * Returns the encapsulated distance.
     * 
     * @return the encapsulated distance
     */
    public double getDistance()
    {
        return distance;
    }
    
    /**
     * Calculates a hash code for this object.
     * It's doesn't use a particularly good hash function,
     * but we don't expect use the hash code often,
     * if at all.
     * This method is present because it is required
     * when writing an equals(Object) method.
     */
    public int hashCode()
    {
        return (int)(1000 * distance);
    }
    
    /**
     * Compares this object to another
     * and returns true if they are
     * approximately equal.
     * The objects are equal
     * if the other object is a non-null Distance object
     * with a value equal to this
     * within a few decimal points of precision.
     * 
     * @param   other   the object to compare to
     * 
     * @return true if this object is equal to other
     * 
     * @see Distance#epsilon
     */
    public boolean equals( Object other )
    {
        boolean result  = false;
        if ( other == null )
            result = false;
        else if ( other == this )
            result = true;
        else if ( !(other instanceof Distance) )
            result = false;
        else
        {
            Distance    that    = (Distance)other;
            double      diff    = distance - that.distance;
            double      absDiff = Math.abs( diff );
            result = absDiff < epsilon;
        }
        return result;
    }
}
