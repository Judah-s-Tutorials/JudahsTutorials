package com.acmemail.judah.anonymous_classes.functional_interfaces;
import java.awt.geom.Point2D;
import java.util.function.Function;

public class FunctionStaticMethodDemo
{
    public static void main(String[] args)
    {
        // 2x*x + 3x + 1
        Function<Double,Double> degree2Term = x -> 2 * x * x;
        Function<Double,Double> degree1Term = x -> 3 * x;
        Function<Double,Double> degree0Term = x -> 1d;
        Function<Double,Double> quadratic   =
            degree2Term.andThen( degree1Term );
        System.out.println( getPoint( 3, quadratic ) );
    }
    
    private static Point2D 
    getPoint( double xco, Function<Double,Double> funk )
    {
        Point2D point   = new Point2D.Double( xco, funk.apply( xco ) );
        return point;
    }
}
