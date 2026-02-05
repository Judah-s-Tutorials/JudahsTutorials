package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.DoubleStream;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.NotificationManager;
import com.acmemail.judah.cartesian_plane.app.FIUtils;
import com.acmemail.judah.cartesian_plane.app.FIUtils.ToPlotPointCommand;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Example of using two exp4j expressions
 * to plot a parametric equation.
 * 
 * @author Jack Straub
 */
public class Exp4jDemo7ParametricEquation
{
    private static final CartesianPlane plane   = new CartesianPlane();
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        Root    root    = new Root( plane );
        root.start();
        
        Map<String,Double>  vars    = new HashMap<>();
        
        vars.put( "a", 3. );
        vars.put( "n", 4. );
        vars.put( "t", 0. );
    
        Expression  roseExprX   =
            new ExpressionBuilder( "a sin(nt)cos(t)" )
                .variables( vars.keySet() )
                .build();
        
        Expression  roseExprY   =
            new ExpressionBuilder( "a sin(nt)sin(t)" )
                .variables( vars.keySet() )
                .build();

        ToPlotPointCommand  toPlotPointCommand =
            FIUtils.toPlotPointCommand( plane );

        roseExprX.setVariables( vars );
        roseExprY.setVariables( vars );
        plane.setStreamSupplier( () ->
            DoubleStream.iterate( 0, t -> t < 2 * Math.PI, t -> t + .001 )
                .peek( t -> roseExprX.setVariable( "t", t ) )
                .peek( t -> roseExprY.setVariable( "t", t ) )
                .mapToObj( t -> 
                    new Point2D.Double( 
                        roseExprX.evaluate(), 
                        roseExprY.evaluate() 
                 ))
                .map( toPlotPointCommand::of )
        );
        NotificationManager.INSTANCE
            .propagateNotification( CPConstants.REDRAW_NP );
    }
}
