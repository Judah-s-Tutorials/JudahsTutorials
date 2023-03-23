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
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Main3
{
    private static final    CartesianPlane    plane   = new CartesianPlane();
    
    public static void main(String[] args)
    {
        Root    root    = new Root( plane );
        root.start();
        
        Expression e = new ExpressionBuilder("4  x^2 + 3 * x - 2" )
            .variables("x")
            .build();
        
        Stream<PlotCommand> stream  =
            DoubleStream.iterate( -1, d -> d <= .5, d -> d + .005 )
                .peek( d -> e.setVariable( "x", d ) )
                .mapToObj( d -> calc( e, d ) )
                .map( Main3::toPlotPoint );
        plane.setStreamSupplier( () -> stream );
        NotificationManager.INSTANCE.propagateNotification( CPConstants.REDRAW_NP );
    }
    
    private static Point2D calc( Expression expr, double xco )
    { 
        double  yco = expr.evaluate();
        Point2D point   = new Point2D.Double( xco, yco );
        return point;
    }
    
    private static PlotCommand toPlotPoint( Point2D point )
    {
        PlotCommand cmd = new PlotPointCommand(
            plane,
            (float)point.getX(),
            (float)point.getY()
        );
        return cmd;
    }
}
