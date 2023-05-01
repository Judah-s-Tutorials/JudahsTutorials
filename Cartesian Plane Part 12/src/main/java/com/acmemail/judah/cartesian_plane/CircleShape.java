package com.acmemail.judah.cartesian_plane;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/**
 * Encapsulates a Shape to use
 * to plot points
 * on the CartesianPlane grid.
 * This Shape is constituted by
 * a circle of a given radius.
 * 
 * @author Jack Straub
 *
 */
public class CircleShape implements PlotShape
{
    /** Shape to use to plot a point. */
    private final Ellipse2D shape   = new Ellipse2D.Float();
    
    /** Radius of the circle. */
    private final float     radius;
    /** 
     * Length of a side of the bounding rectangle
     * that describes the circle.
     */
    private final float     side;
    
    /**
     * Constructor.
     * Sets the radius of the circle
     * to a given value.
     * 
     * @param radius    the given value
     */
    public CircleShape( float radius )
    {
        this.radius = radius;
        side = 2 * radius;
    }
    
    @Override
    public Shape getShape( double xco, double yco )
    {
        double  cornerXco   = xco - radius;
        double  cornerYco   = yco - radius;
        shape.setFrame( cornerXco, cornerYco, side, side );
        return shape;
    }
}
