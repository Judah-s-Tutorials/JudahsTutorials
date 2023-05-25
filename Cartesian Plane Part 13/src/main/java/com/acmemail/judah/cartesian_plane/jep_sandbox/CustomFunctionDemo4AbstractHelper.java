package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.util.Arrays;
import java.util.Stack;
import java.util.stream.IntStream;

import org.nfunk.jep.JEP;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

public class CustomFunctionDemo4AbstractHelper
{

    public static void main(String[] args)
        throws ParseException
    {
        AbstractFunction[]  functions   =
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
    
    private static void print( String strExp, JEP expr )
    {
        expr.parseExpression( strExp );
        System.out.print( strExp + ": " );
        if ( expr.hasError() )
            System.out.println( expr.getErrorInfo() );
        else
            System.out.println( expr.getValue() );

    }
    
    public static abstract class AbstractFunction extends PostfixMathCommand
    {
        public abstract double evaluate( double... param );
        private final String    name;
        
        public AbstractFunction( String name, int numParams )
        {
            this.name = name;
            numberOfParameters = numParams;
        }
        
        public String getName()
        {
            return name;
        }
        
        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public void run( Stack inStack )
            throws ParseException, ClassCastException
        {
            // If this number &lt; 0, it means we're evaluating 
            // a varargs function, and we have to use curNumberOfParameters.
            // If this number >= 0, curNumberOfParameters is unpredictable
            int actNumParams    = 
                numberOfParameters < 0 ? 
                curNumberOfParameters : 
                numberOfParameters; 

            checkStack( inStack );
            double[]    params  = new double[actNumParams];
            IntStream.range( 0, actNumParams )
                .forEach( i -> params[i] = (Double)inStack.pop() );
            double      result  = evaluate( params );
            inStack.push( result );
        }
    }
    
    public static class Cosecant extends AbstractFunction
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
    
    public static class Hypotenuse extends AbstractFunction
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

    public static class Average extends AbstractFunction
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
