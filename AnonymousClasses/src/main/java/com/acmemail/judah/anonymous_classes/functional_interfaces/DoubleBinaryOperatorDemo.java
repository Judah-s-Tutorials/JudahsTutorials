package com.acmemail.judah.anonymous_classes.functional_interfaces;

import java.util.Random;
import java.util.function.DoubleBinaryOperator;

/**
 * Simple application to demonstrate
 * the <em>DoubleBinaryOperator</em> functional interface.
 * 
 * @author Jack Straub
 */
public class DoubleBinaryOperatorDemo
{
    private static final Random randy1  = new Random( 1 );
    private static final Random randy2  = new Random( 2 );
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        combine(  10, (d1,d2) -> d1 * d2 );
    }

    /**
     * Demonstrates the invocation
     * of a DoubleBinaryOperator functional interface.
     * Runs a loop generating two random numbers,
     * and combining them as indicated
     * by the given functional interface.
     * The result of the operation 
     * is printed to stdout.
     * 
     * @param numOps    the number of times to execute the loop
     * @param funk      the given functional interface
     */
    private static void combine( int numOps, DoubleBinaryOperator funk )
    {
        for ( int inx = 0 ; inx < numOps ; ++inx )
        {
            double  arg1    = randy1.nextDouble() * 100;
            double  arg2    = randy2.nextDouble() * 100;
            double  result  = funk.applyAsDouble( arg1, arg2 );
            System.out.printf( "%f, %f -> %f%n", arg1, arg2, result );
        }
    }
}
