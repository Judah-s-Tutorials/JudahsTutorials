package com.acmemail.judah.cartesian_plane.sandbox;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;

/**
 * Demonstrates how to write and use a custom function
 * using the exp4j API.
 * The custom function converts degrees to radians.
 * This example uses an explicit class declaration
 * to encapsulate the function.
 * 
 * @author Jack Straub
 * 
 * @see CustomFunctionDemo2DegreesRadians
 */
public class CustomFunctionDemo1ToRadians
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        Function    radians = new ToRadians();
        
        Expression  expr    = new ExpressionBuilder( "toRadians( d )" )
            .variables("d")
            .function( radians )
            .build();
        
        expr.setVariable( "d", 180 );
        System.out.println( expr.evaluate() );
    }
    
    /**
     * Exp4j function to convert degrees to radians.
     * 
     * @author Jack Straub
     */
    private static class ToRadians extends Function
    {
        /**
         * Default constructor.
         * Establishes the name of the function
         * and the number of arguments
         * required by the function.
         */
        public ToRadians()
        {
            super( "toRadians", 1 );
        }
        
        @Override
        public double apply( double... args )
        {
            double  radians = args[0] * Math.PI / 180.;
            return radians;
        }        
    }
}
