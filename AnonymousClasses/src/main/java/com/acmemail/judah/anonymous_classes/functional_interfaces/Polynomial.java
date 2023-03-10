package com.acmemail.judah.anonymous_classes.functional_interfaces;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleSupplier;

/**
 * This class is part of a demonstration
 * of the use of the <em>Supplier</em> functional interface.
 * It encapsulates a polynomial 
 * of a fixed degree.
 * The degree of the polynomial
 * is fixed in the constructor.
 * The {@linkplain #plot(int, DoubleSupplier)} method
 * is an example of a method
 * that has a parameter
 * of type <em>Supplier</em>
 * 
 * @author Jack Straub
 * 
 * @see #plot(int, DoubleSupplier)
 */
public class Polynomial
{
    private final double[]  coefficients;
    
    /**
     * Constructor.
     * Defines a polynomial 
     * using the given coefficients.
     * The first coefficient is applied
     * to the highest term.
     * The degree of the polynomial
     * is the number of coefficients - 1.
     * 
     * @param coeff the coefficients of the encapsulated polynomial
     */
    public Polynomial( double... coeff )
    {
        coefficients = Arrays.copyOf( coeff, coeff.length );
    }
    
    /**
     * Evaluates the polynomial 
     * for a given value of <em>x</em>.
     * Returns the calculated value.
     * 
     * @param xval  the given value of <em>x</em>
     * 
     * @return  the calculated value
     */
    public double evaluate( double xval )
    {
        int     degree  = coefficients.length - 1;
        double  yval        = 0;
        for ( double coeff : coefficients )
            yval += coeff * Math.pow( xval, degree-- );
        return yval;
    }
    
    /**
     * Compiles a list of (x,y) pairs
     * by evaluating a sequence of <em>x</em> values
     * according to the encapsulated polynomial.
     * 
     * @param numPoints the number of points to compile
     * @param supplier  source of the sequence of <em>x</em> values
     * 
     * @return  the compiled list
     */
    public List<Point2D> plot( int numPoints, DoubleSupplier supplier )
    {
        List<Point2D>   points  = new ArrayList<>();
        
        for ( int inx = 0  ; inx < numPoints ; ++inx )
        {
            double  xval    = supplier.getAsDouble();
            double  yval    = evaluate( xval );
            points.add( new Point2D.Double( xval, yval ) );
        }
        
        return points;
    }
}
