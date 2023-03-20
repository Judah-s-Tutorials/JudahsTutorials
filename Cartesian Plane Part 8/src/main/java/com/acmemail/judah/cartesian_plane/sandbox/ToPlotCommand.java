package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.geom.Point2D;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.PlotPointCommand;

/**
 * Utility class to derive PlotCommands
 * from miscellaneous related objects.
 * 
 * @author Jack Straub
 */
public class ToPlotCommand
{
    /** The CartesianPlane to which commands are to be applied. */
    private final CartesianPlane    plane;
    
    /**
     * Constructor.
     * Establishes the CartesianPlane
     * to which commands
     * are to be applied.
     * 
     * @param plane the CartesianPlane to which commands are to be applied
     */
    public ToPlotCommand( CartesianPlane plane )
    {
        this.plane = plane;
    }
    
    /**
     * Helper method to make a PlotPointCommand object
     * from a given Point2D.
     * 
     * @param point the given Point2D
     * 
     * @return  the derived PlotPointCommand
     */
    public PlotPointCommand toPlotPointCommand( Point2D point )
    {
        float   xco = (float)point.getX();
        float   yco = (float)point.getY();
        PlotPointCommand    cmd = new PlotPointCommand( plane, xco, yco );
        return cmd;
    }
}
