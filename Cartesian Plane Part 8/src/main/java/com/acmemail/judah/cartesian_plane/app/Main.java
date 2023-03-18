package com.acmemail.judah.cartesian_plane.app;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.PlotColorCommand;
import com.acmemail.judah.cartesian_plane.PlotShapeCommand;
import com.acmemail.judah.cartesian_plane.DiamondShape;
import com.acmemail.judah.cartesian_plane.PlotCommand;
import com.acmemail.judah.cartesian_plane.PlotCoordinatesCommand;
import com.acmemail.judah.cartesian_plane.PointShape;
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
        
        DiamondShape        diaShape    = new DiamondShape();
        PointShape          pointShape  = new PointShape();
        Color               shapeColor  = Color.GREEN;
        Color               posColor    = Color.RED;
        Color               negColor    = Color.BLUE;
        Color               currColor   = posColor;
        double              currSign    = 0;
        boolean             isDiamond   = false;
        List<PlotCommand>   commands  = new ArrayList<>();
        for ( float xco = -10 ; xco <= 10 ; xco += .005f )
        {
            float   yco     = xco * xco - 3;
            float   thisSign    = (int)Math.signum( yco );
            if ( thisSign != currSign )
            {
                currSign = thisSign; 
                currColor = thisSign < 0 ? negColor : posColor;
                commands.add( new PlotColorCommand( canvas, currColor ) );
            }
            if ( isZero( yco ) )
            {
                commands.add( new PlotShapeCommand( canvas, diaShape ) );
                commands.add( new PlotColorCommand( canvas, shapeColor) );
                isDiamond = true;
            }
            PlotCoordinatesCommand  coords  = 
                new PlotCoordinatesCommand( canvas, xco, yco );
            commands.add( coords );
            if ( isDiamond )
            {
                commands.add( new PlotShapeCommand( canvas, pointShape ) );
                commands.add( new PlotColorCommand( canvas, currColor) );
            }
        }
        canvas.setUserCommands( commands );
        canvas.repaint();
    }
    
    private static boolean isZero( float num )
    {
        float   diff    = Math.abs( num );
        boolean result  = diff < .008;
        return result;
    }
}
