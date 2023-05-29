package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.util.Arrays;

import org.nfunk.jep.JEP;
import org.nfunk.jep.ParseException;

import com.acmemail.judah.cartesian_plane.input.JEPAbstractFunction;

/**
 * Program to demonstrate how to use
 * the JEPAbstractFunction class
 * to implement a custom function
 * in the JEP implementation.
 * 
 * @author Jack Straub
 * 
 * @see JEPAbstractFunction
 */
public class CustomFunctionDemo4AbstractHelper
{
    /**
     * Application entery point.
     * 
     * @param args  command line arguments; not used
     * 
     * @throws ParseException
     *      If an error occurs while parsing an expression.
     */
    public static void main(String[] args)
        throws ParseException
    {
        JEPAbstractFunction[]  functions   =
            { new Cosecant(), new Hypotenuse(), new Average() };
        
        String  strCsc      = "csc( pi / 4 )";
        String  strHypot    = "hypot( 3, 4 )";
        String  strAvg      = "avg( .5, -.5, 1, -1, 10 )";
        JEP     expr        = new JEP();
        expr.addStandardConstants();
        expr.addStandardFunctions();
        expr.setImplicitMul( true );
        Arrays.stream( functions )
            .forEach( f -> expr.addFunction( f.getName(), f ) );
        
        print( strCsc, expr );
        print( strHypot, expr );
        print( strAvg, expr );
    }
    
    /**
     * Prints an expression as a string,
     * its value (if any)
     * and error information (if any).
     * 
     * @param strExp    the string expression
     * @param expr      the JEP object used to evaluate the
     *                  string expression
     */
    private static void print( String strExp, JEP expr )
    {
        expr.parseExpression( strExp );
        System.out.print( strExp + ": " );
        if ( expr.hasError() )
            System.out.println( expr.getErrorInfo() );
        else
            System.out.println( expr.getValue() );

    }
    
    /**
     * Sample custom function.
     * Calculates the cosecant of an angle.
     * 
     * @author Jack Straub
     */
    public static class Cosecant extends JEPAbstractFunction
    {
        public Cosecant()
        {
            super( "csc", 1 );
        }
        
        @Override
        public double evaluate( double... params )
        {
            double  cosecant = 1.0 / Math.sin( params[0] );
            return cosecant;
        }
    }
    
    /**
     * Sample custom function.
     * Calculates the hypotenuse
     * of a right triangle.
     * 
     * @author Jack Straub
     */
    public static class Hypotenuse extends JEPAbstractFunction
    {
        public Hypotenuse()
        {
            super( "hypot", 2 );
        }
        
        @Override
        public double evaluate( double... params )
        {
            double  leg1Sq  = params[0] * params[0];
            double  leg2Sq  = params[1] * params[1];
            double  hypot   = Math.sqrt( leg1Sq + leg2Sq );
            return hypot;
        }
    }

    /**
     * Sample custom function.
     * Calculates the average 
     * of a series of values. 
     * 
     * @author Jack Straub
     */
    public static class Average extends JEPAbstractFunction
    {
        public Average()
        {
            super( "avg", -1 );
        }
        
        @Override
        public double evaluate( double... params )
        {
            double  avg =
                Arrays.stream( params ).average().orElse( 0 );
            return avg;
        }
    }
}
