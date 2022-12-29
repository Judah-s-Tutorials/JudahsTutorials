package com.acmemail.judah.cartesian_plane.sandbox;

/**
 * Demonstrate the difference (if any) between positive 0 (float)
 * and negative 0 (float).
 * <p>
 * In the context of comparing decimal numbers
 * I was reading the Java spec and started wondering
 * if Java treated -0f and +0f differently.
 * It turns out Java floating point logic
 * distinguishes between +0 and -0;
 * the IEEE754 has different representations
 * for +0 and -0;
 * but Java sees +0 and -0 as equal.
 * </p>
 * 
 * @author Jack Straub
 *
 */
public class Decimal0Demo
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        // perform the test in a helper method to avoid
        // compiler optimizations in evaluating 0/1 and 0/-1.
        test( 1 );
        test( -1 );
    }

    /**
     * Divide 0 by the given divisor and print the detailed results,
     * placing emphasis on the sign of the result.
     * 
     * @param divisor   the given divisor
     */
    private static void test( int divisor )
    {
        float   result  = 0f / divisor;
        System.out.println( "divisor: " + divisor );
        System.out.println( "result: " + result );
        System.out.println( "sign: " + Math.signum( result ) );
        System.out.println( " == 0: " + (result == 0f) );
        System.out.println( "***********************" );
    }
}
