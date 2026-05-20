package com.acmemail.judah.cartesian_plane.math;

import java.awt.geom.Point2D;
import java.util.Objects;

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
    private static final double TWO_PI          = 2 * Math.PI;
    private static final String INVALID_RADIUS  = "radius: not a valid value";
    private static final String INVALID_THETA   = "theta: not a valid value";
    
    /** The radius of this object. */
    private final double    radius;
    /** The angle of this object. */
    private final double    theta;
    
    /**
     * Constructor.
     * Creates a new Polar object
     * from a given radius and angle.
     * Radius and angle must be
     * valid, finite quantities.
     * Internal form will be normalized,
     * so r >= 0 and 0 <= theta < 2pi;
     * see {@link #ofRTheta(double, double)}
     * for details.
     *  
     * @param radiusP    the given radius
     * @param thetaP     the given angle in radians
     * 
     * @throws  
     *      IllegalArgumentException if radius or theta is not
     *      a valid, finite double
     */
    private Polar( double radiusP, double thetaP )
    {
        double  tempR   = radiusP;
        double  tempTh  = thetaP % TWO_PI;
        if ( Double.isNaN( radiusP ) || Double.isInfinite( radiusP ) )
            throw new IllegalArgumentException( INVALID_RADIUS );
        if ( Double.isNaN( thetaP ) || Double.isInfinite( thetaP ) )
            throw new IllegalArgumentException( INVALID_THETA );
        if ( radiusP == 0)
        {
            tempR = 0;
            tempTh = 0;
        }
        else
        {
            if ( radiusP < 0 )
            {
                tempR = -tempR;
                tempTh += Math.PI;
            }
            if ( tempTh < 0 )
                tempTh += TWO_PI;
        }
        
        radius = tempR;
        theta = tempTh;
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
        Complex     complex = new Complex( values[0], values[1] );
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
    public static Polar ofComplex( Complex zed )
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
     * Internally, coordinates are normalized
     * so that radius &ge; 0
     * and 0 &le; &theta; &lt; 2&pi;.
     * The normalization algorithm is:
     * <pre style="padding-left:2em;">
     * if radius == 0 
     *     &theta; = 0
     * else
     * {
     *     if radius &lt; 0 :
     *         radius = -radius, &theta; += &pi;
     *     &theta; = &theta; % 2&pi;
     *     if &theta; &lt; 0:
     *         &theta; += 2&pi;
     * }</pre>
     * <p>
     * Examples:
     * <ul>
     *     <li>(0, &pi;) &rarr; (0,0)</li> 
     *     <li>(2, &pi;/4) &rarr; (2,&pi;/4)</li> 
     *     <li>(-2, &pi;/4) &rarr; (2,5&pi;/4;)</li> 
     *     <li>(1, 3&pi;) &rarr; (1,&pi;)</li> 
     *     <li>(1, -&pi;) &rarr; (1,&pi;)</li> 
     *     <li>(-1, -&pi;) &rarr; (1,0)</li> 
     * </ul>
     * 
     * @param radius    the given radius;
     *                  must be a valid, finite value
     * @param theta     the given angle in radians
     *                  must be a valid, finite value
     * 
     * @return  the created Polar object
     * 
     * @throws  
     *      IllegalArgumentException if radius or theta is not
     *      a valid, finite double
     */
    public static Polar ofRTheta( double radius, double theta )
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
        double  theta   = xco == 0 && yco == 0 ?
            0 : Math.atan2( yco, xco );
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
        double      xco     = 0;
        double      yco     = 0;
        if ( radius != 0 )
        {
            xco = radius * Math.cos( theta );
            yco = radius * Math.sin( theta );
        }
        double[]    rect    = new double[] { xco, yco };
        return rect;
    }
}
