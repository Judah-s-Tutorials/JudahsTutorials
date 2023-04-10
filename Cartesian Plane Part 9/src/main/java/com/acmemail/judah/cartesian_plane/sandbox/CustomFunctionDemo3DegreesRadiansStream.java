package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;

/**
 * Demonstrates how to write and a custom function
 * using the exp4j API.
 * The custom function converts radians to degrees.
 * This example uses an anonymous class
 * to encapsulate the function.
 * It employs the function in a loop
 * to generate multiple values.
 * 
 * @author Jack Straub
 * 
 * @see CustomFunctionDemo1ToRadians
 * @see CustomFunctionDemo2DegreesRadians
 */
public class CustomFunctionDemo3DegreesRadiansStream
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        Function    degrees = new Function( "toDegrees", 1 ) {
            @Override
            public double apply( double... args ) {
                return args[0] * 180 / Math.PI;
            }
        };
        
        Expression  dToRExpr    = new ExpressionBuilder( "toDegrees( r )" )
            .variables("r")
            .function( degrees )
            .build();
        
        Stream.of( 0., Math.PI / 2, Math.PI, 3 * Math.PI / 2, 2 * Math.PI )
            .peek( r -> dToRExpr.setVariable( "r", r ) )
            .map( r -> dToRExpr.evaluate() )
            .forEach( System.out::println );
    }
}
