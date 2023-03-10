package com.acmemail.judah.anonymous_classes.functional_interfaces;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.function.DoubleSupplier;

/**
 * Simple application to demonstrate
 * how to implement
 * a <em>DoubleSupplier</em> functional interface.
 * 
 * @author Jack Straub
 */
public class DoubleSupplierDemo
{
    /**
     * Application entry point.
     * Employs a <em>DoubleSupplier</em>
     * to generate a series of x-coordinates
     * which are then mapped to y-coordinates
     * as indicated by a polynomial.
     * Results are printed to stdout.
     * 
     * @param args  command line arguments; not used
     * 
     * @see Polynomial
     */
    public static void main(String[] args)
    {
        Polynomial      poly        = new Polynomial( 2, 3, 1 );
        List<Point2D>   points  = poly.plot( 10, new XGenerator( -2, .5 ) );
        points.forEach( p -> 
            System.out.println( p.getX() + "->" + p.getY() )
        );
    }
    
    /**
     * Implementation of the <em>DoubleSupplier</em>
     * functional interface.
     * Generates an effectively infinite series of values
     * from a given start point,
     * and controlled by a given increment.
     *  
     * @author Jack Straub
     */
    private static class XGenerator implements DoubleSupplier
    {
        private final double    incr;
        private double          next;
        
        /**
         * Constructor.
         * Establishes the given start point
         * and increment of the
         * encapsulated series.
         * 
         * @param start the given start point
         * @param incr  the given increment
         */
        public XGenerator( double start, double incr )
        {
            this.next = start;
            this.incr = incr;
        }

        @Override
        public double getAsDouble()
        {
            double  val = next;
            next += incr;
            
            return val;
        }
    }
}
