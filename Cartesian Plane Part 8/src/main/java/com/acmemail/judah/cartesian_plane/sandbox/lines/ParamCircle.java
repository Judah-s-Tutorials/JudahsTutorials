package com.acmemail.judah.cartesian_plane.sandbox.lines;

import java.awt.geom.Point2D;
import java.util.function.DoubleFunction;

public class ParamCircle implements DoubleFunction<Point2D>
{
    private final double radius;
    
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
