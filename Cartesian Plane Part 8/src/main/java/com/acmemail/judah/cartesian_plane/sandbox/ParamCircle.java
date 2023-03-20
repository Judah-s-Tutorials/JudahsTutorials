package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.geom.Point2D;
import java.util.function.DoubleFunction;

/**
 * This is the implementation
 * of a functional interface
 * to compute the coordinates of a circle
 * using a parametric equation.
 * The invoker provides the angle, in radians,
 * corresponding to the point to compute.
 * 
 * @author Jack Straub
 */
public class ParamCircle implements DoubleFunction<Point2D>
{
    /** Radius of the target circle. */
    private final double radius;
    
    /**
     * Constructor.
     *
     * @param radius    radius of the target circle
     */
    public ParamCircle( double radius )
    {
        this.radius = radius;
    }

    @Override
    public Point2D apply( double radians )
    {
        double  xco     = radius * Math.cos( radians );
        double  yco     = radius * Math.sin( radians );
        Point2D point   = new Point2D.Double( xco, yco );
        return point;
    }
}
