package com.acmemail.judah.anonymous_classes.functional_interfaces;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.function.DoubleSupplier;

public class DoubleSupplierDemo
{
    public static void main(String[] args)
    {
        Polynomial      poly        = new Polynomial( 2, 3, 1 );
        List<Point2D>   points  = poly.plot( 10, new XGenerator( -2, .5 ) );
        points.forEach( p -> 
            System.out.println( p.getX() + "->" + p.getY() )
        );
    }
    
    private static class XGenerator implements DoubleSupplier
    {
        private final double    incr;
        private double          next;
        
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
