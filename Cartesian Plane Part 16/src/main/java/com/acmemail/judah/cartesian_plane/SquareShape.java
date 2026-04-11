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
    /** PropertyManager singleton; declared here for convenience. */
    private static final PropertyManager    PMGR            = 
        PropertyManager.INSTANCE;
    /** Grid unit property name; declared here for convenience. */
    private static final String             GRID_UNIT       =
        CPConstants.GRID_UNIT_PN;
    /** Minor tick/LPU property name; declared here for convenience. */
    private static final String             TIC_MINOR_MPU   =
        CPConstants.TIC_MINOR_MPU_PN;

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
     * Default constructor.
     * Sets the side of the square
     * to a given value.
     * 
     * @param radius    the given value
     */
    public SquareShape()
    {
        this( getSpacing() * 2 );
    }
    
    /**
     * Constructor.
     * Sets the side of the square
     * to a given value.
     * 
     * @param radius    the given value
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
    
    /**
     * Calculate the spacing between minor ticks
     * based on the current grid unit and 
     * tick minor MPU.
     * 
     * @return  the current spacing between minor ticks
     */
    private static float getSpacing()
    {
        float   gridUnit    = PMGR.asFloat( GRID_UNIT );
        float   mpu         = PMGR.asFloat( TIC_MINOR_MPU );
        float   spacing     = gridUnit / mpu;
        return spacing;
    }
}
