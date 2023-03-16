package com.acmemail.judah.cartesian_plane;

/**
 * Command to describe the coordinates
 * of a point plotted in the Cartesian plane.
 * 
 * @author Jack Straub
 *
 */
public class ChangePlotShapeCommand implements PlotCommand
{
    private final CartesianPlane    plane;
    private final PlotShape         shape;
    
    /**
     * Constructor.
     * Determines the shape to be used
     * when plotting a point
     * in the Cartesian plane.
     * 
     * @param plane the Cartesian plane in which the shape is to be used
     * @param shape the shape to use
     */
    public ChangePlotShapeCommand( CartesianPlane plane, PlotShape shape )
    {
        this.plane = plane;
        this.shape = shape;
    }
    
    @Override
    public void execute()
    {
        plane.setPlotShape( shape );

    }
}
