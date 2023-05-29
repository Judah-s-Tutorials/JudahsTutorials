package com.acmemail.judah.cartesian_plane.sandbox;

import org.nfunk.jep.JEP;

import com.acmemail.judah.cartesian_plane.input.Exp4jFunctions;
import com.acmemail.judah.cartesian_plane.input.JEPFunctions;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Program to demonstrate
 * how to use the custom functions
 * declared in the 
 * JEPFunctions and Exp4jFunctions classes.
 * 
 * @author Jack Straub
 */
public class FunctionsDemo1
{
    /**
     * Program entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        jepDemo();
        exp4jDemo();
    }
    
    /**
     * Demonstrates use of the JEPFunctions class.
     */
    private static void jepDemo()
    {
        String  exprStr = "csc( pi/4 )";
        JEP     expr    = new JEP();
        expr.addStandardConstants();
        expr.addStandardFunctions();
        expr.setImplicitMul( true );
        expr.addVariable( "a", 1.5 );
        expr.addVariable( "h", -1 );
        expr.addVariable( "t", Math.PI );
        JEPFunctions.addFunctions( expr );
        
        expr.parseExpression( exprStr );
        double  val     = expr.getValue();
        System.out.println( "JEP " + exprStr + ": " + val );
    }
    
    /**
     * Demonstrates use of the Exp4jFunctions class.
     */
    private static void exp4jDemo()
    {
        String  exprStr = "csc( pi/4 )";
        Expression expr = new ExpressionBuilder( exprStr )
            .variables( "a", "h", "t" )
            .functions( Exp4jFunctions.getFunctions() )
            .build();
        double  val     = expr.evaluate();
        System.out.println( "Exp4j " + exprStr + ": " + val );
    }
}
