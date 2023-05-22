package com.acmemail.judah.cartesian_plane.jep_sandbox;

import org.nfunk.jep.JEP;

/**
 * Presents a very simple
 * operation using JEP.
 * Compare to {@linkplain Demo1Exp4jBasics}.
 * 
 * @author Jack Straub
 * 
 * @see Demo1Exp4jBasics
 */
public class Demo1JEPBasics
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        String  str     = "a * cos(t) + h";
        JEP     xExpr   = new JEP();
        xExpr.addStandardConstants();
        xExpr.addStandardFunctions();
        xExpr.setImplicitMul( true );
        xExpr.addVariable( "a", 1.5 );
        xExpr.addVariable( "h", -1 );
        xExpr.addVariable( "t", Math.PI );
        xExpr.parseExpression( str );
        System.out.println( xExpr.getValue() );
    }
}
