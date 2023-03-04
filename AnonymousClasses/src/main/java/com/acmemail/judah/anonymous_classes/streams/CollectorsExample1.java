package com.acmemail.judah.anonymous_classes.streams;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.stream.Collectors;
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
public class CollectorsExample1
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        List<Point2D>   aboveXAxis  =
            DoubleStream.iterate( -5, x -> x < 5, x -> x + .1 )
                .mapToObj( CollectorsExample1::cubicFunk )
                .filter( p -> p.getY() >= 0 )
                .collect( Collectors.toList() );
        
        aboveXAxis.stream()
            .map( CollectorsExample1::toString )
            .forEach( System.out::println );
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
    private static Point2D cubicFunk( double xco )
    {
        double  yco     = Math.pow( xco, 3 ) + 2 * Math.pow( xco, 2 ) - 1;
        Point2D point   = new Point2D.Double( xco, yco );
        return point;
    }
    
    /**
     * Creates a string
     * encapsulating a Point2D object
     * in the form <em>(xco,yco).</em>
     * 
     * @param point the Point2D to format
     * 
     * @return  a formatted string encapsulating a Point2D object
     */
    private static String toString( Point2D point )
    {
        String  fmt     = "(%5.2f,%5.2f)";
        String  output  = String.format( fmt, point.getX(), point.getY() );
        return output;
    }
}
