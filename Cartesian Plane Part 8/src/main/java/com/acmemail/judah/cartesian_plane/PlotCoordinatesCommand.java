package com.acmemail.judah.cartesian_plane;

/**
 * Command to plot a point in the Cartesian plane.
 * 
 * @author Jack Straub
 *
 */
public class PlotCoordinatesCommand implements PlotCommand
{
    private static final String     format  = "PlotCommand: (%4.2f,%4.2f)";
    
    private final CartesianPlane    plane;
    private final float             xco;
    private final float             yco;
    
    /**
     * Constructor.
     * Determines the plane in which the plot color 
     * is to be set and the color to set it to.
     * 
     * @param plane the plane in which the plot color is to be set
     * @param color the color to set the plot color to.
     */
    public PlotCoordinatesCommand( CartesianPlane plane, float xco, float yco )
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
