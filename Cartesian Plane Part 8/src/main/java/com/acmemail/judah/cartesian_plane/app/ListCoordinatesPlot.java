package com.acmemail.judah.cartesian_plane.app;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.CircleShape;
import com.acmemail.judah.cartesian_plane.PlotColorCommand;
import com.acmemail.judah.cartesian_plane.PlotCommand;
import com.acmemail.judah.cartesian_plane.PlotPointCommand;
import com.acmemail.judah.cartesian_plane.PlotShape;
import com.acmemail.judah.cartesian_plane.PlotShapeCommand;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

/**
 * This class encapsulates logic to instantiate
 * and display a CartesianPlane.
 * It demonstrates using a list
 * to plot a quadratic.
 * The function is plotted
 * over a range of values,
 * using different colors
 * for points above and below
 * the x-axis.
 * The roots, if any,
 * are highlighted,
 * using a circle
 * drawn in a third color.
 *
 * @author Jack Straub
 */
public class ListCoordinatesPlot
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
        Color               currColor   = posColor;
        double              currSign    = 0;
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
            PlotPointCommand  coords  = 
                new PlotPointCommand( canvas, xco, yco );
            commands.add( coords );
        }
        float[] roots   = getRoots( 1, 0, -3 );
        if ( roots !=  null )
        {
            PlotShape   circle  = new CircleShape( 7 );
            commands.add( new PlotColorCommand( canvas, Color.GREEN ) );
            commands.add( new PlotShapeCommand( canvas, circle ) );
            commands.add( new PlotPointCommand( canvas, roots[0], 0 ) );
            commands.add( new PlotPointCommand( canvas, roots[1], 0 ) );
        }
        canvas.setStreamSupplier( () -> commands.stream() );
    }
    
    /**
     * Calculates the roots, if any,
     * of a quadratic function.
     * If no roots exist
     * in the real number domain
     * null is returned.
     * 
     * @param coefA the coefficient of the second degree term
     * @param coefB the coefficient of the first degree term
     * @param coefC the constant term
     * 
     * @return  
     *      two-dimensional array containing the roots 
     *      of the given function,
     *      or null if none
     */
    private static float[] getRoots( double coefA, double coefB, double coefC )
    {
        float[] roots   = null;
        double  discr   = coefB * coefB - 4 * coefA * coefC;
        if ( discr>= 0 )
        {
            roots = new float[2];
            roots[0] = (float)((-coefB - Math.sqrt( discr )) / (2 * coefA));
            roots[1] = (float)((-coefB + Math.sqrt( discr )) / (2 * coefA));
        }
        return roots;
    }
}
