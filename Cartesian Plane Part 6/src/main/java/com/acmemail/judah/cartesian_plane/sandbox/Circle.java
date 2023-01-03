/**
 * 
 */
package com.acmemail.judah.cartesian_plane.sandbox;

/**
 * This is a simple class to encapsulate a circle.
 * It is intended to be used a part of an example
 * of unit testing.
 * 
 * @author Jack Straub
 *
 */
public class Circle
{
    private double  xco;
    private double  yco;
    private double  radius;
    
    /**
     * Construct a circle with a given center and radius;
     * 
     * @param xco       x-coordinate of the given center
     * @param yco       y-coordinate of the given center
     * @param radius    given radius
     */
    public Circle( double xco, double yco, double radius )
    {
        super();
        this.xco = xco;
        this.yco = yco;
        this.radius = radius;
    }
    
    /**
     * Gets the circumference of this circle.
     * 
     * @return the area of this circle
     */
    public double getCircumference()
    {
        double circumference = 2 * radius * Math.PI;
        return circumference;
    }
    
    /**
     * Gets the area of this circle.
     * 
     * @return the area of this circle
     */
    public double getArea()
    {
        double area = radius * radius * Math.PI;
        return area;
    }

    /**
     * Gets the x-coordinate of the center of the circle.
     * 
     * @return the x-coordinate of the center of the circle
     */
    public double getXco()
    {
        return xco;
    }

    /**
     * Sets the x-coordinate of the center of the circle.
     * 
     * @param xco the new x-coordinate of the center of the circle
     */
    public void setXco(double xco)
    {
        this.xco = xco;
    }

    /**
     * Gets the y-coordinate of the center of the circle.
     * 
     * @return the y-coordinate of the center of the circle
     */
    public double getYco()
    {
        return yco;
    }

    /**
     * Sets the y-coordinate of the center of the circle.
     * 
     * @param yco the new y-coordinate of the center of the circle
     */
    public void setYco(double yco)
    {
        this.yco = yco;
    }

    /**
     * Gets the radius of the circle.
     * 
     * @return the radius of the circle
     */
    public double getRadius()
    {
        return radius;
    }

    /**
     * Sets the radius of the circle.
     * 
     * @param radius the new radius of the circle
     */
    public void setRadius(double radius)
    {
        this.radius = radius;
    }
}
