package com.acmemail.judah.cartesian_plane.app;

import java.awt.geom.Point2D;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.PlotPointCommand;

/**
 * Utility class for bundling utilities
 * associated with functional interface tasks.
 * 
 * @author Jack Straub
 */
public class FIUtils
{
    /**
     * Default constructor declaration
     * to prevent utilities class
     * from being instantiated.
     */
    private FIUtils()
    {
    }
    
    public static ToPlotPointCommand 
    toPlotPointCommand( CartesianPlane plane )
    {
        ToPlotPointCommand   cmd = new ToPlotPointCommand( plane );
        return cmd;
    }

    /**
     * Utility class to derive PlotCommands
     * from miscellaneous related objects.
     * 
     * @author Jack Straub
     */
    public static class ToPlotPointCommand
    {
        /** The CartesianPlane to which commands are to be applied. */
        private final CartesianPlane    plane;
        
        /**
         * Constructor.
         * Establishes the CartesianPlane
         * to which commands
         * are to be applied.
         * This constructor is deliberately
         * declared private to prevent
         * direct instantiation.
         * To get an instance of this class
         * use {@linkplain FIUtils#toPlotPointCommand(CartesianPlane)}.
         * 
         * @param plane the CartesianPlane to which commands are to be applied
         * @see #of(Point2D)
         */
        private ToPlotPointCommand( CartesianPlane plane )
        {
            this.plane = plane;
        }
        
        /**
         * Instantiates a PlotPointCommand object
         * from a given Point2D.
         * 
         * @param point the given Point2D
         * 
         * @return  the derived PlotPointCommand
         */
        public PlotPointCommand of( Point2D point )
        {
            float   xco = (float)point.getX();
            float   yco = (float)point.getY();
            PlotPointCommand    cmd = new PlotPointCommand( plane, xco, yco );
            return cmd;
        }
    }
}
