package com.acmemail.judah.anonymous_classes.streams;

import java.util.stream.DoubleStream;

/**
 * This application demonstrates
 * the use of the <em>Stream.collect()</em>
 * terminal operation
 * in conjunction with the 
 * <em>Collectors.toList()</em> collector.
 * The main method
 * evaluates a cubic function
 * for some range of <em>x</em>,
 * and accumulates all those results
 * for which <em>f(x)</em> 
 * is greater than 0.
 * 
 * @author Jack Straub
 */
public class CollectorsExample2
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        double  averageYco  =
            DoubleStream.iterate( -5, x -> x < 5, x -> x + .1 )
            .map( CollectorsExample2::cubicFunk )
            .average()
            .orElse( Double.NaN );
        
        System.out.println( averageYco );
    }

    /**
     * For a given value of x,
     * evaluates <em>f(x) = x<sup>3</sup> + 2x<sup>2</sup> - 1.</em>
     * The result is stored and returned
     * in a Point2D object.
     * 
     * @param xco   the given value of x
     * 
     * @return 
     *      a Point2D encapsulating
     *      the evaluation 
     *      of the given cubic function
     */
    private static double cubicFunk( double xco )
    {
        double  yco     = Math.pow( xco, 3 ) + 2 * Math.pow( xco, 2 ) - 1;
        return yco;
    }
}
