package com.acmemail.judah.anonymous_classes.functional_interfaces;

import java.util.Random;
import java.util.function.DoubleToIntFunction;

public class DoubleToFunctionDemo
{
    private static final Random randy   = new Random( 1 );
    
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
