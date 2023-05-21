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

public class Demo1Exp4jBasics
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main( String[] args )
    {
        String     str   = "a cos(t) + h";
        Expression xExpr = new ExpressionBuilder( str )
            .variables( "a", "t", "h" )
            .build();
        xExpr.setVariable( "a", 1.5 );
        xExpr.setVariable( "h", -1 );
        xExpr.setVariable( "t", Math.PI );
        System.out.println( xExpr.evaluate() );
    }
}
