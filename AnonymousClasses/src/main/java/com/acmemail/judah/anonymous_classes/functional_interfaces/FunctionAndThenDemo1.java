package com.acmemail.judah.anonymous_classes.functional_interfaces;

import java.util.function.BiFunction;
import java.util.function.DoubleBinaryOperator;
import java.util.function.Function;

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
        Double                              side1       = 3.;
        Double                              side2       = 4.;
        BiFunction<Double,Double,Double>    sumSquare   =
            (s1,s2) -> s1 * s1 + s2 * s2;
        double  hypot   = 
            sumSquare.andThen( x -> Math.sqrt( x ) ).apply( side1, side2 );
    }
    
    private static double 
    execute( double arg1, double arg2, DoubleBinaryOperator funk )
    {
        double  result  = funk.applyAsDouble( arg1, arg2 );
        return result;
    }
    
    /**
     * Stub to simulate sending a given message 
     * to a designated recipient.
     * The message and recipient are simply
     * written to stdout.
     * 
     * @param message   the given message
     * @param recipient the designatedsendTo(message, "Home Office") recipient
     */
    private static void sendTo( String message, String recipient )
    {
        System.out.println( "sending to: " + recipient );
        System.out.println( message );
        System.out.println( "************* end message **************" );
    }
}
