package com.acmemail.judah.cartesian_plane.input;

import java.awt.geom.Point2D;
import java.util.Objects;

import com.acmemail.judah.cartesian_plane.math.Complex;

/**
 * An instance of this class
 * stores the polar coordinates
 * of a point on the Cartesian plane.
 * It can translate between polar coordinates
 * and Cartesian coordinates.
 * All angle values are in radians.
 * 
 * @author Jack Straub
 */
public final class Polar
{
    /** The radius of this object. */
    private final double    radius;
    /** The angle of this object. */
    private final double    theta;
    
    /**
     * Constructor.
     * Creates a new Polar object
     * from a given radius and angle.
     *  
     * @param radius    the given radius
     * @param theta     the given angle in radians
     */
    private Polar( double radius, double theta )
    {
        this.radius = radius;
        this.theta = theta;
    }

    /**
     * Converts this object
     * to a Point2D object
     * 
     * @return Point2D object equivalent to this object
     */
    public Point2D toPoint()
    {
        double[]    values  = toRectangle();
        Point2D     point   = new Point2D.Double( values[0], values[1] );
        return point;
    }

    /**
     * Converts this object
     * to a Complex object
     * 
     * @return Complex object equivalent to this object
     */
    public Complex toComplex()
    {
        double[]    values  = toRectangle();
        Complex     complex   = new Complex( values[0], values[1] );
        return complex;
    }
    
    /**
     * Returns the radius of this object.
     * 
     * @return  the radius of this object
     */
    public double getRadius()
    {
        return radius;
    }
    
    /**
     * Returns the angle of this object in radians.
     * 
     * @return  the angle of this object in radians
     */
    public double getTheta()
    {
        return theta;
    }
    
    @Override
    public String toString()
    {
        StringBuilder   bldr    = new StringBuilder( "Polar[" )
            .append( "radius=" ).append( radius ).append( ", " )
            .append( "theta=").append( theta ).append( " radians]" 
        );
        return bldr.toString();
    }
    
    @Override
    public int hashCode()
    {
        int hash    = Double.hashCode( radius );
        hash = 31 * hash + Double.hashCode( theta );
        return hash;
    }
    
    @Override
    public boolean equals( Object other )
    {
        boolean result  = false;
        if ( this == other )
            result = true;
        else if ( other instanceof Polar that )
        {
            result = 
                Double.compare( this.radius, that.radius ) == 0 &&
                Double.compare( this.theta, that.theta ) == 0;
        }
        else
            ;
        return result;
    }

    /**
     * Creates a Polar object from a given
     * Complex object.
     * 
     * @param zed   the given Complex object; must be non-null
     * 
     * @return  the created Polar object
     * 
     * @throws NullPointerException if zed is null
     */
    public static Polar of( Complex zed )
    {
        Objects.requireNonNull( zed, "zed" );
        Polar  pzed     = ofXY( zed.re(), zed.im() );
        return pzed;
    }
    
    /**
     * Creates a Polar object
     * from a given Point2D object.
     * 
     * @param point the given Point2D object; must be non-null
     * 
     * @return  the created Polar object
     * 
     * @throws NullPointerException if point is null
     */
    public static Polar of( Point2D point )
    {
        Objects.requireNonNull( point, "point" );
        Polar  pzed    = ofXY( point.getX(), point.getY() );
        return pzed;
    }
    
    /**
     * Creates a Polar object
     * from a given radius and angle.
     * 
     * @param radius    the given radius
     * @param theta     the given angle in radians
     * 
     * @return  the created Polar object
     */
    public static Polar of( double radius, double theta )
    {
        Polar   pzed    = new Polar( radius, theta );
        return pzed;
    }
    
    /**
     * Creates a Polar object
     * from a given x- and y-coordinate.
     * 
     * @param xco    the given x-coordinate
     * @param yco    the given y-coordinate
     * 
     * @return  the created Polar object
     */
    public static Polar ofXY( double xco, double yco )
    {
        double  radius  = radiusOfXY( xco, yco );
        double  theta   = thetaOfXY( xco, yco );
        Polar   pzed    = new Polar( radius, theta );
        return pzed;
    }
    
    /**
     * Calculates the radius,
     * in Polar coordinates,
     * given the Cartesian coordinates
     * of a point.
     * 
     * @param xco    the given x-coordinate
     * @param yco    the given y-coordinate
     * 
     * @return  the calculated radius
     */
    public static double radiusOfXY( double xco, double yco )
    {
        double  radius  = Math.hypot( xco, yco );
        return radius;
    }
    
    /**
     * Calculates the angle,
     * in Polar coordinates,
     * given the Cartesian coordinates
     * of a point.
     * 
     * @param xco    the given x-coordinate
     * @param yco    the given y-coordinate
     * 
     * @return  the calculated angle
     */
    public static double thetaOfXY( double xco, double yco )
    {
        double  theta   = Math.atan2( yco, xco );
        return theta;
    }
    
    /**
     * Converts polar form to rectangular form.
     * Works for Point2D and Complex.
     * Constructs and returns a double[]
     * containing (xco, yco) or, equivalently
     * (real coefficient, imaginary coefficient).
     * 
     * @return the constructed double[]
     */
    private double[] toRectangle()
    {
        double      xco = radius * Math.cos( theta );
        double      yco = radius * Math.sin( theta );
        double[]    rect    = new double[] { xco, yco };
        return rect;
    }
}
