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
    private static final String INVALID_XCO     = "x-coordinate: not a valid value";
    private static final String INVALID_YCO     = "y-coordinate: not a valid value";
    private static final String INVALID_REAL    = "real part: not a valid value";
    private static final String INVALID_IMAG    = "imag part: not a valid value";
    
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
        if ( Double.isNaN( radiusP ) || Double.isInfinite( radiusP ) )
            throw new IllegalArgumentException( INVALID_RADIUS );
        if ( Double.isNaN( thetaP ) || Double.isInfinite( thetaP ) )
            throw new IllegalArgumentException( INVALID_THETA );

        double  tempR   = radiusP;
        double  tempTh  = thetaP;
        if ( tempR == 0 )
        {
            tempTh = 0;
        }
        else
        {
            if ( tempR < 0 )
            {
                tempR = -tempR;
                tempTh += Math.PI;
            }
            tempTh = ( ( tempTh % TWO_PI ) + TWO_PI ) % TWO_PI;
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
        double      xco     = radius * Math.cos( theta );
        double      yco     = radius * Math.sin( theta );
        Point2D     point   = new Point2D.Double( xco, yco );
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
        double      real    = radius * Math.cos( theta );
        double      imag    = radius * Math.sin( theta );
        Complex     complex = new Complex( real, imag );
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
            result = false;
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
     * @throws IllegalArgumentException
     *      if a component of zed is not a valid, finite double
     */
    public static Polar ofComplex( Complex zed )
    {
        if ( Double.isNaN( zed.re() ) || Double.isInfinite( zed.re() ) )
            throw new IllegalArgumentException( INVALID_REAL );
        if ( Double.isNaN( zed.im() ) || Double.isInfinite( zed.im() ) )
            throw new IllegalArgumentException( INVALID_IMAG );
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
     * @throws IllegalArgumentException
     *      if a component of point is not a valid, finite double
     */
    public static Polar ofPoint( Point2D point )
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
     *     &theta; = ( ( &theta; % 2&pi; ) + 2&pi; ) % 2&pi;
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
        if ( Double.isNaN( radius ) || Double.isInfinite( radius ) )
            throw new IllegalArgumentException( INVALID_RADIUS );
        if ( Double.isNaN( theta ) || Double.isInfinite( theta ) )
            throw new IllegalArgumentException( INVALID_THETA );
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
     * 
     * @throws IllegalArgumentException
     *      if xco or yco is not a valid, finite double
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
     * @param xco    the given x-coordinate; must be a valid, finite double
     * @param yco    the given y-coordinate; must be a valid, finite double
     * 
     * @return  the calculated radius
     * 
     * @throws IllegalArgumentException
     *      if xco or yco is not a valid, finite double
     */
    public static double radiusOfXY( double xco, double yco )
    {
        if ( Double.isNaN( xco ) || Double.isInfinite( xco ) )
            throw new IllegalArgumentException( INVALID_XCO );
        if ( Double.isNaN( yco ) || Double.isInfinite( yco ) )
            throw new IllegalArgumentException( INVALID_YCO );
        double  radius  = Math.hypot( xco, yco );
        return radius;
    }
    
    /**
     * Calculates the angle,
     * in Polar coordinates,
     * given the Cartesian coordinates
     * of a point.
     * The returned coordinates will be normalized
     * as discussed {@link #ofRTheta(double, double)};
     * 0 &le; &theta; &lt; 2&pi;.
     * 
     * @param xco    the given x-coordinate
     * @param yco    the given y-coordinate
     * 
     * @return  the calculated angle
     * 
     * @throws IllegalArgumentException
     *      if xco or yco is not a valid, finite value
     */
    public static double thetaOfXY( double xco, double yco )
    {
        if ( Double.isNaN( xco ) || Double.isInfinite( xco ) )
            throw new IllegalArgumentException( INVALID_XCO );
        if ( Double.isNaN( yco ) || Double.isInfinite( yco ) )
            throw new IllegalArgumentException( INVALID_YCO );
        double  theta   = xco == 0 && yco == 0 ?
            0 : Math.atan2( yco, xco );
        if ( theta < 0 )
            theta += TWO_PI;
        return theta;
    }
}
