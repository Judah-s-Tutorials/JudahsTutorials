package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleUnaryOperator;

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
