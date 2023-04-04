package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.DoubleStream;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.NotificationManager;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.app.FIUtils;
import com.acmemail.judah.cartesian_plane.app.FIUtils.ToPlotPointCommand;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Example of using an exp4j expression
 * to generate a stream of (x,y) points
 * in the Cartesian plane.
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
        
        new Exp4jDemo6PlottingPoints().execute();
    }
    
    private void execute()
    {
        Map<String,Double>  vars        = new HashMap<>();
        vars.put( "a", 1. );
        vars.put( "b", 1. );
        String              roseXStr    = "a * cos(bt)* cos(t)";
        String              roseYStr    = "a * cos(bt)* sin(t)";
        Expression          xExpr       =
            new ExpressionBuilder( roseXStr )
                .variables( vars.keySet() )
                .variable( "t" )
                .build();
        Expression          yExpr       =
            new ExpressionBuilder( roseYStr )
                .variables( vars.keySet() )
                .variable( "t" )
                .build();
        
        ToPlotPointCommand   toPlotPointCommand =
            FIUtils.toPlotPointCommand( plane );
        xExpr.setVariables( vars );
        yExpr.setVariables( vars );
        plane.setStreamSupplier( () ->
            DoubleStream.iterate( -1, d -> d <= 1, d -> d + .01 )
            .peek( d -> xExpr.setVariable( "t", d ) )
            .peek( d -> yExpr.setVariable( "t", d ) )
            .mapToObj( t -> 
                new Point2D.Double( yExpr.evaluate(), yExpr.evaluate() ) )
            .map( toPlotPointCommand::of )
        );
        NotificationManager.INSTANCE
            .propagateNotification( CPConstants.REDRAW_NP );
    }
}
