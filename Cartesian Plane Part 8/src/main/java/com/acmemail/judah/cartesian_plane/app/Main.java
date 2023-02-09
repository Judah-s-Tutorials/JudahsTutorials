package com.acmemail.judah.cartesian_plane.app;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.ChangePlotColorCommand;
import com.acmemail.judah.cartesian_plane.PlotCommand;
import com.acmemail.judah.cartesian_plane.PlotCoordinatesCommand;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

/**
 * This class encapsulates logic to instantiate
 * and display a CartesianPlane.
 *
 * @author Jack Straub
 */
public class Main
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
        
        Color               posColor    = Color.RED;
        Color               negColor    = Color.BLUE;
        double              currSign    = 0;
        List<PlotCommand>   commands  = new ArrayList<>();
        for ( float xco = -10 ; xco <= 10 ; xco += .005f )
        {
            float   yco     = xco * xco - 3;
            float   thisSign    = (int)Math.signum( yco );
            if ( thisSign != currSign )
            {
                currSign = thisSign; 
                Color   color   = thisSign < 0 ? negColor : posColor;
                commands.add( new ChangePlotColorCommand( canvas, color ) );
            }
            PlotCoordinatesCommand  coords  = 
                new PlotCoordinatesCommand( canvas, xco, yco );
            commands.add( coords );
        }
        canvas.setUserCommands( commands );
        canvas.repaint();
    }
}
