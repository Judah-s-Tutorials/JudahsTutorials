package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.Random;
import java.util.stream.IntStream;

/**
 * This is a class encapsulates a throw-away application
 * to demonstrate the validity of an algorithm 
 * to generate a floating point value
 * within a given range.
 * 
 * @author Jack Straub
 *
 */
public class NextFloatDemo
{
    private static final Random randy   = new Random( 5 );
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        String  fmt     = "min=%.1f, max=%.1f, next=%.1f ";
        float   start   = 101.25F;
        IntStream.range( 0, 26 ).forEach( inx -> 
        {
            float   min     = inx * start;
            float   max     = 2 * min + 1;
            IntStream.range( 0, 10 ).forEach( jnx ->
            {
                float   next    = nextFloat( min, max );
                System.out.printf( fmt, min, max, next );
                if ( next  < min )
                    System.out.print( "MIN " );
                if ( next >= max )
                    System.out.print( "MAX" );
                System.out.println();
            }
            );
        }
        );
    }

    private static float nextFloat( float min, float max )
    {
        float   diff    = max - min;
        float   next    = randy.nextFloat();
        next = next * diff + min;
        return next;
    }
}
