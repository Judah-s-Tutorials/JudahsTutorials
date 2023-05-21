package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.awt.geom.Point2D;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.PlotCommand;
import com.acmemail.judah.cartesian_plane.PlotPointCommand;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Demo2Exp4jEllipse
{
    private static final    CartesianPlane  plane   = new CartesianPlane();
    private static final    Root            root    = new Root( plane );
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main( String[] args )
    {
        root.start();
        plane.setStreamSupplier( Demo2Exp4jEllipse::getEllipse );
        plane.repaint();
    }
    
    private static Stream<PlotCommand> getEllipse()
    {
        Expression xExpr    = new ExpressionBuilder( "a cos(t) + h" )
            .variables( "a", "t", "h" )
            .build();
        xExpr.setVariable( "a", 1.5 );
        xExpr.setVariable( "h", -1 );
        
        Expression yExpr    = new ExpressionBuilder( "b sin(t) + k" )
            .variables( "b", "t", "k" )
            .build();
        yExpr.setVariable( "b", .5 );
        yExpr.setVariable( "k", 1 );
        
        Stream<PlotCommand> stream  =
            DoubleStream.iterate( 0, d -> d <= 2 * Math.PI, d -> d + .001 )
                .peek( d -> xExpr.setVariable( "t", d ) )
                .peek( d -> yExpr.setVariable( "t", d ) )
                .mapToObj( d -> new Point2D.Double(
                    xExpr.evaluate(),
                    yExpr.evaluate()
                ))
                .map( p -> PlotPointCommand.of( p, plane ) );
        return stream;
    }
}
