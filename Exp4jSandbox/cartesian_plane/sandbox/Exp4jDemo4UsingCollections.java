package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.HashMap;
import java.util.Map;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Example of using a map
 * to organize variables 
 * when working with exp4j expressions.
 * 
 * @author Jack Straub
 */
public class Exp4jDemo4UsingCollections
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        Map<String,Double>  vars    = new HashMap<>();
        double              result  = 0;
        
        vars.put( "a", 5. );
        vars.put( "b", -1. );
        vars.put( "c", -2. );
        vars.put( "x", 0. );

        Expression  expr    = 
            new ExpressionBuilder( "ax^2 + bx + c" )
                .variables( vars.keySet() )
                .build();
        
        result =
            expr.setVariables( vars )
                .setVariable( "x", .5 )
                .evaluate();
        System.out.println( result );
    }
}
