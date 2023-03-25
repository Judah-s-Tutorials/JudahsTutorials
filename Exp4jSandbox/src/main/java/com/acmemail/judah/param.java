package com.acmemail.judah;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.CircleShape;
import com.acmemail.judah.cartesian_plane.NotificationManager;
import com.acmemail.judah.cartesian_plane.PlotColorCommand;
import com.acmemail.judah.cartesian_plane.PlotCommand;
import com.acmemail.judah.cartesian_plane.PlotPointCommand;
import com.acmemail.judah.cartesian_plane.PlotShape;
import com.acmemail.judah.cartesian_plane.PlotShapeCommand;
import com.acmemail.judah.cartesian_plane.app.FIUtils;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class param
{
    private static final    CartesianPlane    plane   = new CartesianPlane();
    
    public static void main(String[] args)
    {
        Root    root    = new Root( plane );
        root.start();
        
        FIUtils.ToPlotPointCommand  toPlotPoint =
            FIUtils.toPlotPointCommand( plane );
        
        float   ellipseXco  = 1;
        float   ellipseYco  = 1.5f;
        double  minorAxis   = 1;
        double  majorAxis   = 2;
        double  minorRadius = minorAxis / 2;
        double  majorRadius = majorAxis / 2;
        
        Expression xEval    = new ExpressionBuilder( "a * cos(t) + h" )
            .variables("t", "a", "h" )
            .build()
            .setVariable( "a", majorRadius )
            .setVariable( "h", ellipseXco );
        
        Expression yEval    = new ExpressionBuilder( "b * sin(t) + k" )
            .variables("t", "b", "k" )
            .build()
            .setVariable( "b", minorRadius )
            .setVariable( "k", ellipseYco );
        
        Stream<PlotCommand> streamPeri  =
            DoubleStream.iterate( 0, t -> t < 2 * Math.PI, x -> x + .005 )
                .peek( d -> xEval.setVariable( "t", d ) )
                .peek( d -> yEval.setVariable( "t", d ) )
                .mapToObj( x -> new Point2D.Double( 
                    xEval.evaluate(), 
                    yEval.evaluate() 
                ))
                .map( toPlotPoint::of );
        PlotShape           centerShape     = new CircleShape( 3 );
        Stream<PlotCommand> streamCenter    =
            Stream.of(
                new PlotColorCommand( plane, Color.RED ),
                new PlotShapeCommand( plane, centerShape ),
                new PlotPointCommand( plane, ellipseXco, ellipseYco )
            );
        plane.setStreamSupplier( () -> Stream.concat( streamPeri, streamCenter ) );
        NotificationManager.INSTANCE.propagateNotification( CPConstants.REDRAW_NP );
    }
}
