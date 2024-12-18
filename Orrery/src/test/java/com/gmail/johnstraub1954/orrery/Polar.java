package com.gmail.johnstraub1954.orrery;

import java.awt.geom.Point2D;

import org.nfunk.jep.type.Complex;

/**
 * Polar coordinates
 * of a point on the Cartesian plane.
 * 
 * @author Jack Straub
 */
public class Polar
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
     * @param theta     the given angle
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
        double  xco = radius * Math.cos( theta );
        double  yco = radius * Math.sin( theta );

        Point2D point   = new Point2D.Double( xco, yco );
        return point;
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
     * Returns the angle of this object.
     * 
     * @return  the angle of this object
     */
    public double getTheta()
    {
        return theta;
    }
    
    @Override
    public String toString()
    {
        StringBuilder   bldr    = new StringBuilder( "PolarZ(" )
            .append( radius )
            .append( "," )
            .append( theta )
            .append( ")" 
        );
        return bldr.toString();
    }
    
    /**
     * Creates a Polar object from a given
     * Complex object.
     * 
     * @param zed   the given Complex object
     * 
     * @return  the created Polar object
     */
    public static Polar of( Complex zed )
    {
        Polar  pzed     = ofXY( zed.re(), zed.im() );
        return pzed;
    }
    
    /**
     * Creates a Polar object
     * from a given Point2D object.
     * 
     * @param point the given Point2D object
     * 
     * @return  the created Polar object
     */
    public static Polar of( Point2D point )
    {
        Polar  pzed    = ofXY( point.getX(), point.getY() );
        return pzed;
    }
    
    /**
     * Creates a Polar object
     * from a given radius and angle.
     * 
     * @param radius    the given radius
     * @param theta     the given angle
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
        double  sum     = xco * xco + yco * yco;
        double  radius  = Math.sqrt( sum );
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
}
