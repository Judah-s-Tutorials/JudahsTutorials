package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.awt.geom.Point2D;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import org.nfunk.jep.JEP;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.PlotCommand;
import com.acmemail.judah.cartesian_plane.PlotPointCommand;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

public class Demo2JEPEllipse
{
    private static final    CartesianPlane  plane   = new CartesianPlane();
    private static final    Root            root    = new Root( plane );
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        root.start();
        plane.setStreamSupplier( Demo2JEPEllipse::getEllipse );
        plane.repaint();
    }
    private static Stream<PlotCommand> getEllipse()
    {
        JEP xExpr  = new JEP();
        xExpr.addStandardConstants();
        xExpr.addStandardFunctions();
        xExpr.setImplicitMul( true );
        xExpr.addVariable( "a", 1.5 );
        xExpr.addVariable( "h", -1 );
        xExpr.addVariable( "t", 0 );
        xExpr.parseExpression( "a cos(t) + h" );
        
        JEP yExpr  = new JEP();
        yExpr.addStandardConstants();
        yExpr.addStandardFunctions();
        yExpr.setImplicitMul( true );
        yExpr.addVariable( "b", .5 );
        yExpr.addVariable( "k", 1 );
        yExpr.addVariable( "t", 0 );
        yExpr.parseExpression( "b sin(t) + k" );
        
        Stream<PlotCommand> stream  =
            DoubleStream.iterate( 0, d -> d <= 2 * Math.PI, d -> d + .001 )
                .peek( d -> xExpr.addVariable( "t", d ) )
                .peek( d -> yExpr.addVariable( "t", d ) )
                .mapToObj( d -> new Point2D.Double(
                    xExpr.getValue(),
                    yExpr.getValue()
                ))
                .peek( System.out::println )
                .map( p -> PlotPointCommand.of( p, plane ) );
        
        return stream;
    }
}
