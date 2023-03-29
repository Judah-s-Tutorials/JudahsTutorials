package com.acmemail.judah.sandbox;

import java.awt.geom.Point2D;
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

public class RationalPolynomialDemo
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
        
        ToPlotPointCommand      toPlotPoint = 
            FIUtils.toPlotPointCommand( plane );
        
        String  numerator   = "(x^3 - 2x)";
        String  denominator = "(2(x^2 - 5))";
        String  strExpr     = numerator + "/" + denominator;
        Expression expr = new ExpressionBuilder( strExpr )
            .variables("x")
            .build();
        
        plane.setStreamSupplier( () -> 
            DoubleStream.iterate( -10, x -> x <= 10, x -> x + .005 )
            .peek( d -> expr.setVariable( "x", d ) )
            .mapToObj( x -> new Point2D.Double( x, expr.evaluate() ) )
            .map( toPlotPoint::of ));
        NotificationManager.INSTANCE.propagateNotification( CPConstants.REDRAW_NP );
    }
}
