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
 * Example of using an exp4j expression
 * to plot a quadratic
 * using the <em>CartesianPlane</em> class.
 * 
 * @author Jack Straub
 */
public class Exp4jDemo6PlottingPoints
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
        
        vars.put( "a", 5. );
        vars.put( "b", -1. );
        vars.put( "c", -2. );
        vars.put( "x", 0. );
    
        Expression  expr    = 
            new ExpressionBuilder( "ax^2 + bx + c" )
                .variables( vars.keySet() )
                .build();

        ToPlotPointCommand  toPlotPointCommand =
            FIUtils.toPlotPointCommand( plane );

        expr.setVariables( vars );
        plane.setStreamSupplier( () ->
            DoubleStream.iterate( -1, d -> d <= 1, d -> d + .005 )
                .peek( d -> expr.setVariable( "x", d ) )
                .mapToObj( x -> new Point2D.Double( x, expr.evaluate() ) )
                .map( toPlotPointCommand::of )
        );
        NotificationManager.INSTANCE
            .propagateNotification( CPConstants.REDRAW_NP );
    }
}
