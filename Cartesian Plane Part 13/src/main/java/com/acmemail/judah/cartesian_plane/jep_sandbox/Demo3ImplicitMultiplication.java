package com.acmemail.judah.cartesian_plane.jep_sandbox;

import org.nfunk.jep.JEP;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Demonstrates how Exp4j and JEP
 * interpret implicit multiplication
 * slightly differently.
 * Specifically,
 * if "a" and "b" are distinct variables,
 * "3ab" produces <em>3 * a * b</em>
 * in Exp4j,
 * but fails in JEP.
 * <p>
 * It's tempting to explore
 * what would happen in Exp4j 
 * if "a", "b" and "ab"
 * were all distinct variables.
 * Since the behavior is not documented
 * I choose to assume
 * that it is undefined.
 * 
 * @author Jack Straub
 */
public class Demo3ImplicitMultiplication
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        example1();
        System.out.println();
        example2();
    }
    
    /**
     * Evaluates the expression "3ab"
     * where a=2 and b=3.
     * Expect Exp4j to compute 3 * a * b = 18;
     * expect JEP to compute 3 * ab", 
     * where "ab" is an undefined variable,
     * producing a result of NaN.
     */
    private static void example1()
    {
        System.out.println( "evaluating 3ab" );
        
        JEP     jep     = new JEP();
        jep.addStandardConstants();
        jep.addStandardFunctions();
        jep.setImplicitMul( true );
        jep.addVariable( "a", 2 );
        jep.addVariable( "b", 3 );
        jep.parseExpression( "3ab" );
        System.out.println( "JEP: " + jep.getValue() );
        
        Expression expj4    = new ExpressionBuilder( "3ab" )
            .variables( "a", "b" )
            .build();
        expj4.setVariable( "a", 2 );
        expj4.setVariable( "b", 3 );
        System.out.println( "Exp4j: " + expj4.evaluate() );
    }
    
    /**
     * Evaluates the expression "3a b"
     * where a=2 and b=3.
     * Expect Exp4j and JEP both
     * to compute 3 * a * b = 18,
     * For the record, 
     * "3 a b" also produced 18.
     */
    private static void example2()
    {
        System.out.println( "evaluating 3a b" );
        JEP     jep     = new JEP();
        jep.addStandardConstants();
        jep.addStandardFunctions();
        jep.setImplicitMul( true );
        jep.addVariable( "a", 2 );
        jep.addVariable( "b", 3 );
        jep.parseExpression( "3a b" );
        System.out.println( "JEP: " + jep.getValue() );
        
        Expression expj4    = new ExpressionBuilder( "3a b" )
            .variables( "a", "b" )
            .build();
        expj4.setVariable( "a", 2 );
        expj4.setVariable( "b", 3 );
        System.out.println( "Exp4j: " + expj4.evaluate() );
    }
}
