package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.awt.geom.Point2D;
import java.util.stream.DoubleStream;

import org.nfunk.jep.JEP;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.NotificationManager;
import com.acmemail.judah.cartesian_plane.PlotPointCommand;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

public class Demo5YPlot
{
    public static void main(String[] args)
    {
        CartesianPlane  plane   = new CartesianPlane();
        Root            root    = new Root( plane );
        root.start();
        
        JEP parser  = new JEP();
        parser.addStandardConstants();
        parser.addStandardFunctions();
        parser.setImplicitMul( true );
        parser.addVariable( "x", 0 );
        parser.parseExpression( "2x" );
        
        plane.setStreamSupplier(  () ->
            DoubleStream.iterate( -2, d -> d <= 2, d -> d + .1 )
            .peek( d -> parser.addVariable( "x", d ) )
            .mapToObj( d -> new Point2D.Double( d, parser.getValue() ) )
            .map( p -> PlotPointCommand.of( p, plane ) )
        );
        NotificationManager.INSTANCE
        .propagateNotification( CPConstants.REDRAW_NP );
    }
}
