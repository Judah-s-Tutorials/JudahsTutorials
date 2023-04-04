package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.DoubleStream;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Example of using an exp4j expression
 * to generate a stream of (x,y) points
 * in the Cartesian plane.
 * 
 * @author Jack Straub
 */
public class Exp4jDemo5StreamingCoordinates
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        Map<String,Double>  vars    = new HashMap<>();
        
        vars.put( "a", 5. );
        vars.put( "b", -1. );
        vars.put( "c", -2. );
        vars.put( "x", 0. );

        Expression  expr    = 
            new ExpressionBuilder( "ax^2 + bx + c" )
                .variables( vars.keySet() )
                .build();
        
            expr.setVariables( vars );
            DoubleStream.iterate( -1, d -> d <= 1, d -> d + .01 )
                .peek( d -> expr.setVariable( "x", d ) )
                .mapToObj( x -> new Point2D.Double( x, expr.evaluate() ) )
                .forEach( System.out::println );
    }
}
