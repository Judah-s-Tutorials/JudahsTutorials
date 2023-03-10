package com.acmemail.judah.anonymous_classes.functional_interfaces;

import java.util.Random;
import java.util.function.DoubleToIntFunction;

/**
 * Simple application to demonstrate
 * how to use the
 * <em>DoubleToInt</em> functional interface.
 * 
 * @author Jack Straub
 */
public class DoubleToIntFunctionDemo
{
    private static final Random randy   = new Random( 1 );
    
    /**
     * Application entry point.
     * Calculates an integer average
     * of a series of double values.
     * Conversion from double to int
     * is demonstrated using four different 
     * rounding algorithms:
     * ceiling, floor, half-up and half-down.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        for ( int inx = 0 ; inx < 10 ; ++inx )
        {
            System.out.print( "ceiling:   ");
            average( 20, d -> (int)Math.ceil( d ) );
            System.out.print( "floor:     ");
            average( 20, d -> (int)Math.floor( d ) );
            System.out.print( "half-up:   ");
            average( 20, d -> (int)(d + .5 ) );
            System.out.print( "half-down: ");
            average( 20, d -> (int)Math.ceil( d - .5 ) );
            System.out.println( "***********************" );
        }
    }

    /**
     * Computes the average of a random series of double values,
     * and rounds to an integer
     * using the rounding algorithm provided
     * in the given functional interface.
     * 
     * @param numInputs the number of values to generate/average
     * @param funk      the given functional interface
     */
    private static void average( int numInputs, DoubleToIntFunction funk )
    {
        double  sum     = 0;
        for ( int inx = 0 ; inx < numInputs ; ++inx )
            sum += randy.nextDouble() * 100;;
        double  avg     = sum / numInputs;
        int     iResult = funk.applyAsInt( avg );
        
        System.out.printf( "%f / %d -> %f (%d)%n",  sum, numInputs, avg, iResult );
    }
}
