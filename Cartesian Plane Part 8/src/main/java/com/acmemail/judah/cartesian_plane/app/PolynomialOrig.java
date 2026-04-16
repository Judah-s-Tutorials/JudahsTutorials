package com.acmemail.judah.cartesian_plane.app;

import java.util.Arrays;
import java.util.function.DoubleUnaryOperator;

/**
 * This class encapsulates a polynomial 
 * of a fixed degree.
 * The degree of the polynomial
 * is determined by the constructor.
 * It encapsulates the algorithm used in this project
 * prior to adopting Horner's method.
 * 
 * @author Jack Straub
 */
public class PolynomialOrig implements DoubleUnaryOperator
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
    public PolynomialOrig( double... coeff )
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
    public double applyAsDouble( double xval )
    {
        int     degree  = coefficients.length - 1;
        double  yval        = 0;
        for ( double coeff : coefficients )
            yval += coeff * Math.pow( xval, degree-- );
        return yval;
    }
}
