package com.acmemail.judah.cartesian_plane.app;

import java.util.ArrayList;
import java.util.List;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.PlotCommand;
import com.acmemail.judah.cartesian_plane.PlotCoordinatesCommand;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

/**
 * This class encapsulates logic to instantiate
 * and display a CartesianPlane.
 * It create a list of PlotCommands,
 * and then obtains a stream by invoking
 * the <em>stream</em> method of the <em>List</em> interface.
 *
 * @author Jack Straub
 */
public class SimpleCoordinatesPlot
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        CartesianPlane  canvas  = new CartesianPlane();
        Root            root    = new Root( canvas );
        root.start();
        
        List<PlotCommand>   commands  = new ArrayList<>();
        for ( float xco = -10 ; xco <= 10 ; xco += .005f )
        {
            float   yco     = 
                (float)(Math.pow( xco, 3 ) + 2 * Math.pow( xco, 2 ) - 1);
            PlotCoordinatesCommand  coords  = 
                new PlotCoordinatesCommand( canvas, xco, yco );
            commands.add( coords );
        }
        canvas.setStreamSupplier( () -> commands.stream() );
    }
}
