package com.acmemail.judah.cartesian_plane;

import java.awt.Shape;
import java.awt.geom.Path2D;

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
public class DiamondShape implements PlotShape
{    
    /** 
     * PropertyManager instance.
     * Declared here for convenience.
     */
    private static final PropertyManager    pmgr    = 
        PropertyManager.INSTANCE;
    
    /** Shape to use to plot a point. */
    private final Path2D    shape           = new Path2D.Float();
    
    /** Width of bounding rectangle. */
    private final float     rectWidth;
    
    /** Height of bounding rectangle. */
    private final float     rectHeight;
    
    
    /**
     * Default constructor.
     * Set the width of the bounding rectangle
     * to 2 * minor tic spacing, 
     * and height to major tic length.
     */
    public DiamondShape()
    {
        this(
            pmgr.asFloat( CPConstants.TIC_MINOR_MPU_PN ) * 2,
            pmgr.asFloat( CPConstants.TIC_MAJOR_LEN_PN )
        );
    }
    
    /**
     * Constructor.
     * Sets the bounding rectangle
     * for this Shape
     * to the given width and height.
     * 
     * @param rectWidth     the given width
     * @param rectHeight    the given height
     */
    public DiamondShape( float rectWidth, float rectHeight )
    {
        float   rWidth  = rectWidth;
        float   rHeight = rectHeight;
        
        this.rectWidth = rWidth;
        this.rectHeight = rHeight;
    }
    
    @Override
    public Shape getShape( double xco, double yco )
    {
        double  leftXco     = xco - rectWidth / 2f;
        double  rightXco    = leftXco + rectWidth;
        double  topYco      = yco - rectHeight / 2;
        double  bottomYco   = topYco + rectHeight;
        
        shape.reset();
        shape.moveTo( leftXco, yco );
        shape.lineTo( xco, topYco );
        shape.lineTo( rightXco, yco );
        shape.lineTo( xco, bottomYco );
        shape.closePath();
        return shape;
    }
}
