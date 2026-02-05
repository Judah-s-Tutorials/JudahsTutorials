package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.ArrayList;
import java.util.List;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;

/**
 * Demonstrates how to write and use custom functions
 * using the exp4j API.
 * There are two custom function that convert degrees to radians
 * and radians to degrees.
 * This example uses an anonymous class declarations
 * to encapsulate the functions.
 * 
 * @author Jack Straub
 * 
 * @see CustomFunctionDemo1ToRadians
 * @see CustomFunctionDemo3DegreesRadiansStream
 */
public class CustomFunctionDemo2DegreesRadians
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        Function    radians = new Function( "toRadians", 1 ) {
            @Override
            public double apply( double... args ) {
                return args[0] * Math.PI / 180;
            }
        };
        
        Function    degrees = new Function( "toDegrees", 1 ) {
            @Override
            public double apply( double... args ) {
                return args[0] * 180 / Math.PI;
            }
        };
        
        List<Function>  funkList    = new ArrayList<>();
        funkList.add( degrees );
        funkList.add( radians );
        
        Expression  dToRExpr    = new ExpressionBuilder( "toRadians( d )" )
            .variables("d")
            .functions( funkList )
            .build();
        
        dToRExpr.setVariable( "d", 180 );
        System.out.println( dToRExpr.evaluate() );
        
        Expression  rToDExpr    = new ExpressionBuilder( "toDegrees( r )" )
            .variables("r")
            .functions( funkList )
            .build();
        
        rToDExpr.setVariable( "r", Math.PI );
        System.out.println( rToDExpr.evaluate() );
    }
}
