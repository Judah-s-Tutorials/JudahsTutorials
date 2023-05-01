package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.awt.geom.Point2D;
import java.util.stream.DoubleStream;

import org.nfunk.jep.JEP;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.NotificationManager;
import com.acmemail.judah.cartesian_plane.PlotPointCommand;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

public class Demo6XYPlot
{
    public static void main(String[] args)
    {
        CartesianPlane  plane   = new CartesianPlane();
        Root            root    = new Root( plane );
        root.start();
        
        JEP roseExpX    = new JEP();
        roseExpX.addStandardConstants();
        roseExpX.addStandardFunctions();
        roseExpX.setImplicitMul( true );
        roseExpX.addVariable( "a", 3 );
        roseExpX.addVariable( "n", 4 );
        roseExpX.addVariable( "t", 0 );
        roseExpX.parseExpression( "a sin(n t) cos(t)" );
        
        JEP roseExpY    = new JEP();
        roseExpY.addStandardConstants();
        roseExpY.addStandardFunctions();
        roseExpY.setImplicitMul( true );
        roseExpY.addVariable( "a", 3 );
        roseExpY.addVariable( "n", 4 );
        roseExpY.addVariable( "t", 0 );
        roseExpY.parseExpression( "a sin(n t) sin(t)" );
        System.out.println( "x: " + roseExpX.hasError() );
        System.out.println( "x: " + roseExpX.getErrorInfo() );
        System.out.println( "y: " + roseExpY.hasError() );
        System.out.println( "y: " + roseExpY.getErrorInfo() );
        
        plane.setStreamSupplier(  () ->
            DoubleStream.iterate( 0, t -> t < 2 * Math.PI, t -> t + .001 )
                .peek( t -> roseExpX.addVariable( "t", t ) )
                .peek( t -> roseExpY.addVariable( "t", t ) )
                .mapToObj( t -> 
                    new Point2D.Double( 
                        roseExpX.getValue(),
                        roseExpY.getValue()
                    )
                )
                .map( p -> PlotPointCommand.of( p, plane ) )
        );
        NotificationManager.INSTANCE
            .propagateNotification( CPConstants.REDRAW_NP );
    }
}
