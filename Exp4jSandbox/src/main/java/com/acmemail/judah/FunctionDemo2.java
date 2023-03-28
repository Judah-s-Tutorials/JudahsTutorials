package com.acmemail.judah;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.NotificationManager;
import com.acmemail.judah.cartesian_plane.PlotColorCommand;
import com.acmemail.judah.cartesian_plane.PlotCommand;
import com.acmemail.judah.cartesian_plane.PlotShapeCommand;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.SquareShape;
import com.acmemail.judah.cartesian_plane.app.FIUtils;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;

public class FunctionDemo2
{
    private static final CartesianPlane plane   = new CartesianPlane();
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        PropertyManager pmgr    = PropertyManager.INSTANCE;
        
        pmgr.setProperty( CPConstants.TIC_MAJOR_LEN_PN, 21 );
        pmgr.setProperty( CPConstants.TIC_MAJOR_WEIGHT_PN, 1 );
        pmgr.setProperty( CPConstants.TIC_MAJOR_MPU_PN, 1 );
        pmgr.setProperty( CPConstants.TIC_MINOR_LEN_PN, 11 );
        pmgr.setProperty( CPConstants.TIC_MINOR_WEIGHT_PN, 1 );
        pmgr.setProperty( CPConstants.TIC_MINOR_MPU_PN, 5 );
        pmgr.setProperty( CPConstants.GRID_UNIT_PN, 50 );
        Root    root    = new Root( plane );
        root.start();
        
        PlotCommand shape   = 
            new PlotShapeCommand( plane, new SquareShape( 5 ) );
        PlotCommand color   = new PlotColorCommand( plane, Color.BLUE );
        plane.setStreamSupplier( () ->
            Stream.concat( Stream.of( shape, color), plotStream() )
        );
        NotificationManager.INSTANCE.propagateNotification( CPConstants.REDRAW_NP );
    }
    
    private static Stream<PlotCommand> plotStream()
    {
        Function    radians = new Function( "toRadians", 1 ) {
            @Override
            public double apply( double... args ) {
                return args[0] * Math.PI / 180;
            }
        };
        
        double  radius  = 2;
        
        Expression xEval    = new ExpressionBuilder( "r * cos(toRadians(t))" )
            .function( radians )
            .variables("t", "r" )
            .build()
            .setVariable( "r", radius );
        
        Expression yEval    = new ExpressionBuilder( "r * sin(toRadians(t))" )
            .function( radians )
            .variables("t", "r" )
            .build()
            .setVariable( "r", radius );
        
        FIUtils.ToPlotPointCommand  toPlotPoint =
            FIUtils.toPlotPointCommand( plane );
        
        Stream<PlotCommand> streamPlot  =
            DoubleStream.of( 0, 90, 180, 270 )
                .peek( d -> xEval.setVariable( "t", d ) )
                .peek( d -> yEval.setVariable( "t", d ) )
                .mapToObj( x -> new Point2D.Double( 
                    xEval.evaluate(), 
                    yEval.evaluate() 
                ))
                .map( toPlotPoint::of );
        return streamPlot;
    }
}
