package com.acmemail.judah.cartesian_plane.sandbox;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Example of parsing and evaluating an expression
 * with method chaining.
 * 
 * @author Jack Straub
 */
public class Exp4jDemo3WithMethodChaining
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        double              result  = 0;

        Expression  expr    = 
            new ExpressionBuilder( "ax^2 + bx + c" )
                .variables( "a", "b", "c", "x" )
                .build();
        
        result =
            expr.setVariable( "a", 5 )
                .setVariable( "b", -1 )
                .setVariable( "c", -2 )
                .setVariable( "x", .5 )
                .evaluate();
        System.out.println( result );
    }
}
