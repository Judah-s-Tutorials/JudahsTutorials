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
    /** PropertyManager singleton; declared here for convenience. */
    private static final PropertyManager    PMGR            = 
        PropertyManager.INSTANCE;
    private static final String             GRID_UNIT       =
        CPConstants.GRID_UNIT_PN;
    private static final String             TIC_MINOR_MPU   =
        CPConstants.TIC_MINOR_MPU_PN;
    
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
     * Default constructor.
     * Instantiate a circle with radius
     * equal to the spacing between minor ticks.
     */
    public CircleShape()
    {
        this( getSpacing() );
    }
    
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
