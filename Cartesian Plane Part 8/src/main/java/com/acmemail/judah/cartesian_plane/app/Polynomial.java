package com.acmemail.judah.cartesian_plane.app;

import java.util.Arrays;
import java.util.function.DoubleUnaryOperator;

/**
 * This class encapsulates a polynomial 
 * of a fixed degree.
 * The degree of the polynomial
 * is determined by the constructor.
 * Polynomial evaluation is according to Horner's method.
 * 
 * @author Jack Straub
 */
public class Polynomial implements DoubleUnaryOperator
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
     * Using Horner's method,
     * evaluates the polynomial 
     * for a given value of <em>x</em>.
     * 
     * @param xval  the given value of <em>x</em>
     * 
     * @return  the calculated value
     */
    public double applyAsDouble( double xval )
    {
        int     degree  = coefficients.length - 1;
        double  yval    = coefficients[degree];
        for ( int inx = degree - 1 ; inx >= 0 ; --inx )
            yval = coefficients[inx] + xval * yval;
        return yval;
    }
}
