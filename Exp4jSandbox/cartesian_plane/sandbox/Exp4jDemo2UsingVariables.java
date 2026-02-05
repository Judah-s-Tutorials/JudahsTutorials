package com.acmemail.judah.cartesian_plane.sandbox;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Example of parsing an expression
 * containing variables
 * and evaluating it.
 * 
 * @author Jack Straub
 */
public class Exp4jDemo2UsingVariables
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        ExpressionBuilder   bldr    = null;
        Expression          expr    = null;
        double              result  = 0;

        bldr = new ExpressionBuilder( "ax^2 + bx + c" );
        bldr.variables( "a", "b", "c", "x" );
        expr = bldr.build();
        
        expr.setVariable( "a", 5 );
        expr.setVariable( "b", -1 );
        expr.setVariable( "c", -2 );
        expr.setVariable( "x", .5 );
        result = expr.evaluate();
        System.out.println( result );
    }
}
