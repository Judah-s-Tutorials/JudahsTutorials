package com.acmemail.judah.cartesian_plane;

import java.awt.Color;

/**
 * Command to temporarily change the color
 * used to plot points in the Cartesian plane.
 * 
 * @author Jack Straub
 *
 */
public class ChangePlotColorCommand implements PlotCommand
{
    private final CartesianPlane    plane;
    private final Color             color;
    
    /**
     * Constructor.
     * Determines the plane in which the plot color 
     * is to be set and the color to set it to.
     * 
     * @param plane the plane in which the plot color is to be set
     * @param color the color to set the plot color to.
     */
    public ChangePlotColorCommand( CartesianPlane plane, Color color )
    {
        this.plane = plane;
        this.color = color;
    }
    
    @Override
    public void execute()
    {
        plane.setPlotColor( color );
    }
}
