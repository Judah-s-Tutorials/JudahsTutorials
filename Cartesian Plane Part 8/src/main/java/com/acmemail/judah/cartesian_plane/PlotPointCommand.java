package com.acmemail.judah.cartesian_plane;

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
     * Instantiates a command to plot a point in a 
     * {@linkplain CartesianPlane}.
     * 
     * @param plane the plane in which the plot is to be plotted
     * @param xco   the x-coordinate of the point
     * @param yco   the y-coordinate of the point
     */
    public PlotPointCommand( CartesianPlane plane, float xco, float yco )
    {
        this.plane = plane;
        this.xco = xco;
        this.yco = yco;
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
