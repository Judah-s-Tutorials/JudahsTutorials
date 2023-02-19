package com.acmemail.judah.anonymous_classes.functional_interfaces;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleSupplier;

public class Polynomial
{
    private final double[]  coefficients;
    
    public Polynomial( double... coeff )
    {
        coefficients = Arrays.copyOf( coeff, coeff.length );
    }
    
    public double evaluate( double xval )
    {
        int     degree  = coefficients.length - 1;
        double  yval        = 0;
        for ( double coeff : coefficients )
            yval += coeff * Math.pow( xval, degree-- );
        return yval;
    }
    
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
