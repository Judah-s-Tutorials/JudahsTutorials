package com.acmemail.judah.cartesian_plane.jep_sandbox;

import org.nfunk.jep.JEP;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Demo3ImplicitMultiplication
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        JEP     jep     = new JEP();
        jep.addStandardConstants();
        jep.addStandardFunctions();
        jep.setImplicitMul( true );
        jep.addVariable( "a", 2 );
        jep.addVariable( "b", 3 );
        jep.parseExpression( "3a b" );
        System.out.println( jep.getValue() );
        
        Expression expj4    = new ExpressionBuilder( "3ab" )
            .variables( "a", "b" )
            .build();
        expj4.setVariable( "a", 2 );
        expj4.setVariable( "b", 3 );
        System.out.println( expj4.evaluate() );

    }
}
