package com.acmemail.judah.sandbox;

import java.awt.geom.Point2D;
import java.util.stream.DoubleStream;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.NotificationManager;
import com.acmemail.judah.cartesian_plane.app.FIUtils;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Exp4jDemo3
{
    private static final    CartesianPlane    plane   = new CartesianPlane();
    
    public static void main(String[] args)
    {
        Root    root    = new Root( plane );
        root.start();
        
        Expression expr = new ExpressionBuilder( "4x^2 + 3 * x - 2" )
            .variables("x")
            .build();
        
        FIUtils.ToPlotPointCommand  toPlotPoint =
            FIUtils.toPlotPointCommand( plane );
        
        plane.setStreamSupplier( () -> 
            DoubleStream.iterate( -1, x -> x <= .5, x -> x + .005 )
                .peek( x -> expr.setVariable( "x", x ) )
                .mapToObj( x -> 
                    new Point2D.Double( x, expr.evaluate() )
                )
                .map( toPlotPoint::of )
        );
        NotificationManager.INSTANCE.propagateNotification( CPConstants.REDRAW_NP );
    }
}
