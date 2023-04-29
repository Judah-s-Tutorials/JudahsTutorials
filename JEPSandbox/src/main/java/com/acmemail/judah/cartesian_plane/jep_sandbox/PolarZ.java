package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.awt.geom.Point2D;

import org.nfunk.jep.type.Complex;

/**
 * Polar representation of a complex number.
 * 
 * @author Jack Straub
 */
public class PolarZ
{
    private final double    radius;
    private final double    theta;
    
    public PolarZ( double radius, double theta )
    {
        this.radius = radius;
        this.theta = theta;
    }

    public Complex toComplex()
    {
        Complex cpx = toComplex( radius, theta );
        return cpx;
    }
    
    public Point2D toPoint()
    {
        Complex cpx     = toComplex();
        Point2D point   = new Point2D.Double( cpx.re(), cpx.im() );
        return point;
    }
    
    public double getRadius()
    {
        return radius;
    }
    
    public double getTheta()
    {
        return theta;
    }
    
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
    public static PolarZ of( Complex zed )
    {
        PolarZ  pzed    = of( zed.re(), zed.im() );
        return pzed;
    }
    
    public static PolarZ of( double real, double imag )
    {
        PolarZ  pzed    = new PolarZ( real, imag );
        return pzed;
    }
    
    public static double radius( double real, double imag )
    {
        double  sum     = real * real + imag * imag;
        double  radius  = Math.sqrt( sum );
        return radius;
    }
    
    public static double theta( double real, double imag )
    {
        double  ratio   = imag / real;
        double  theta   = Math.atan( ratio );
        return theta;
    }
    
    public static Complex toComplex( double radius, double theta )
    {
        double  real    = radius * Math.cos( theta );
        double  imag    = radius * Math.sin( theta );
        Complex zed     = new Complex( real, imag );
        return zed;
    }
}
