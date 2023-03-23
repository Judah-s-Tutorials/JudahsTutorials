package com.acmemail.judah;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.NotificationManager;
import com.acmemail.judah.cartesian_plane.PlotCommand;
import com.acmemail.judah.cartesian_plane.PlotPointCommand;
import com.acmemail.judah.cartesian_plane.app.FIUtils;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Main4
{
    private static final    CartesianPlane    plane   = new CartesianPlane();
    
    public static void main(String[] args)
    {
        Root    root    = new Root( plane );
        root.start();
        
        FIUtils.ToPlotPointCommand  toPlotPoint =
            FIUtils.toPlotPointCommand( plane );
        Expression e = new ExpressionBuilder( "4 * x^2 + 3 * x - 2" )
            .variables("x")
            .build();
        
        Stream<PlotCommand> stream  =
            DoubleStream.iterate( -1, x -> x <= .5, x -> x + .005 )
                .peek( d -> e.setVariable( "x", d ) )
                .mapToObj( x -> new Point2D.Double( x, e.evaluate() ) )
                .map( toPlotPoint::of );
        plane.setStreamSupplier( () -> stream );
        NotificationManager.INSTANCE.propagateNotification( CPConstants.REDRAW_NP );
    }
}
