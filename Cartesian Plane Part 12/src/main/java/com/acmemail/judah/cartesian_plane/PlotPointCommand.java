package com.acmemail.judah.cartesian_plane;

import java.awt.geom.Point2D;

/**
 * Command to plot a point in the Cartesian plane.
 * 
 * @author Jack Straub
 *
 */
public class PlotPointCommand implements PlotCommand
{
    private static final String     format  = "PlotCommand: (%4.2f,%4.2f)";
    
    private final CartesianPlane    plane;
    private final float             xco;
    private final float             yco;
    
    /**
     * Constructor.
     * Determines the plane in which 
     * a point is to be plotted,
     * and the its coordinates in the plane.
     * 
     * @param plane the plane in which the point is to be plotted
     * @param xco   the x-coordinate of the point
     * @param yco   the y-coordinate of the point
     */
    public PlotPointCommand( CartesianPlane plane, float xco, float yco )
    {
        this.plane = plane;
        this.xco = xco;
        this.yco = yco;
    }
    
    /**
     * Generates a PlotPointCommand from a given Point2D object
     * and CartesianPlane.
     * 
     * @param point the given Point2D object
     * @param plane the given CartesianPlane
     * 
     * @return  the generated PlotPointCommand
     */
    public static PlotPointCommand of( Point2D point, CartesianPlane plane )
    {
        float   xco = (float)point.getX();
        float   yco = (float)point.getY();
        PlotPointCommand    cmd = new PlotPointCommand( plane, xco, yco );
        return cmd;
    }

    @Override
    public String toString()
    {
        String  str = String.format( format, xco, yco );
        return str;
    }
    
    @Override
    public void execute()
    {
        plane.plotPoint( xco, yco );
    }
}
