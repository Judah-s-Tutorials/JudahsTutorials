package com.acmemail.judah.cartesian_plane.sandbox;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Simple examples of parsing an expression
 * and evaluating it.
 * Variables are not used
 * in this demo.
 * 
 * @author Jack Straub
 */
public class Exp4jDemo1SimpleExamples
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

        bldr = new ExpressionBuilder( "4" );
        expr = bldr.build();
        result = expr.evaluate();
        System.out.println( result );
        
        bldr = new ExpressionBuilder( "2^5 + 7" );
        expr = bldr.build();
        result = expr.evaluate();
        System.out.println( result );
        
        bldr = new ExpressionBuilder( "(2 + 5)(3 - 7)" );
        expr = bldr.build();
        result = expr.evaluate();
        System.out.println( result );
    }
}
