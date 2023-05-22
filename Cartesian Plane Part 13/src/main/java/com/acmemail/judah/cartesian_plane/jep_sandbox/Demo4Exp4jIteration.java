package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.util.stream.DoubleStream;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Uses Exp4j
 * to iteratively evaluate an expression
 * for a range of values.
 * Compare to {@linkplain Demo4JEPIteration}.
 * 
 * @author Jack Straub
 * 
 * @see Demo4JEPIteration
 */
public class Demo4Exp4jIteration
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments, not used
     */
    public static void main(String[] args)
    {
        Expression parser   = new ExpressionBuilder( "2x" )
            .variables( "x" )
            .build();
        
        DoubleStream.iterate( -3, d -> d <= 3, d -> d = d + .1 )
            .peek( d -> parser.setVariable( "x", d ) )
            .map( d -> parser.evaluate() )
            .forEach( System.out::println );
    }
}
