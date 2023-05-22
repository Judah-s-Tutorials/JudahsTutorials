package com.acmemail.judah.cartesian_plane.jep_sandbox;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Presents a very simple
 * operation using Exp4j.
 * Compare to {@linkplain Demo1JEPBasics}.
 * 
 * @author Jack Straub
 * 
 * @see Demo1JEPBasics
 */
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
