package com.acmemail.judah.cartesian_plane;

import java.awt.Shape;
import java.awt.geom.Line2D;

/**
 * Encapsulates a Shape to use
 * to plot points
 * on the CartesianPlane grid.
 * This Shape is constituted by
 * a single pixel.
 * <p>
 * A single pixel is represented by a line
 * that is one pixel long.
 * </p>
 * 
 * @author Jack Straub
 *
 */
public class PointShape implements PlotShape
{
    /** Shape to use to plot a point. */
    private final Line2D    shape   = new Line2D.Float();
    
    @Override
    public Shape getShape( double xco, double yco )
    {
        shape.setLine( xco, yco, xco, yco );
        return shape;
    }

}
