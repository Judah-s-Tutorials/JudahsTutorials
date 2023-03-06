package com.acmemail.judah.anonymous_classes.streams;

import java.awt.geom.Point2D;
import java.util.Optional;
import java.util.stream.DoubleStream;

/**
 * Simple application to demonstrate 
 * the use of the <em>Stream.reduce(BinaryOperator<T> accumulator)</em>
 * method.
 * 
 * @author Jack Straub
 */
public class ReduceDemo
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        // Given a stream of points, (x, f(x)) find the point
        // where f(x) is closest to 0.
        Optional<Point2D>   closestTo0  =
            DoubleStream.iterate( -5, x -> x < 5, x -> x + .1 )
                .mapToObj( ReduceDemo::cubicFunk )
                .reduce( (p1,p2) -> 
                    Math.abs( p1.getY() ) < Math.abs( p2.getY() ) ? p1 : p2 );
        
        String  str = "none";
        if ( closestTo0.isPresent() )
            str = toString( closestTo0.get() );
        System.out.println( "closest to 0: " + str );
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
