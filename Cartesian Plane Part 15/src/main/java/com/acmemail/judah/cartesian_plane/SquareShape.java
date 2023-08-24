package com.acmemail.judah.cartesian_plane;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

/**
 * Encapsulates a Shape to use
 * to plot points
 * on the CartesianPlane grid.
 * This Shape is constituted by
 * a square of a given side.
 * 
 * @author Jack Straub
 *
 */
public class SquareShape implements PlotShape
{
    /** Shape to use to plot a point. */
    private final Rectangle2D   shape   = new Rectangle2D.Float();
    
    /** Length of a side of the square. */
    private final float     side;
    /** 
     * Offset to position the upper-left corner of the square
     * so that the center of the square
     * is at the coordinates passed to the getShape method.
     */
    private final float     cornerOffset;
    
    /**
     * Constructor.
     * Sets the side of the square
     * to a given value.
     * 
     * @param side    the given value
     */
    public SquareShape( float side )
    {
        this.side = side;
        cornerOffset = side / 2;
    }
    
    @Override
    public Shape getShape( double xco, double yco )
    {
        double  cornerXco   = xco - cornerOffset;
        double  cornerYco   = yco - cornerOffset;
        shape.setFrame( cornerXco, cornerYco, side, side );
        return shape;
    }
}
