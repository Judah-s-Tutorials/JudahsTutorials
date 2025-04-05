package com.acmemail.judah.anonymous_classes.functional_interfaces;

import java.util.function.BiFunction;

/**
 * This is a simple demonstration of how 
 * the Function <em>andThen</em> default method
 * can be used to chain several Function.apply() invocations
 * as a single lambda.
 * 
 * @author Jack Straub
 */
public class FunctionAndThenDemo1
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main( String[] args )
    {
        BiFunction<Double,Double,Double>    sumSquare   =
            (x,y) -> x * x + y * y;
        BiFunction<Double,Double,Double>    funk        = 
            sumSquare.andThen(z -> Math.sqrt( z )).andThen(z -> 3*z);
        plot( funk );
    }
    
    private static void plot( BiFunction<Double,Double,Double> calc )
    {
        for ( double xco = 0 ; xco < 10 ; xco += .2 )
            for ( double yco = 0 ; yco < 10 ; yco += .2 )
            {
                double  zco = calc.apply( xco, yco );
                plot( xco, yco, zco );
            }
    }
    
    private static void plot( double xco, double yco, double zco )
    {
        String  fmt = "f(%.2f, %.2f) = %.2f%n";
        System.out.printf( fmt, xco, yco, zco );
    }
}
