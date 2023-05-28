package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.util.Arrays;

import org.nfunk.jep.JEP;
import org.nfunk.jep.ParseException;

import com.acmemail.judah.cartesian_plane.input.JEPAbstractFunction;

public class CustomFunctionDemo4AbstractHelper
{

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
    
    private static void print( String strExp, JEP expr )
    {
        expr.parseExpression( strExp );
        System.out.print( strExp + ": " );
        if ( expr.hasError() )
            System.out.println( expr.getErrorInfo() );
        else
            System.out.println( expr.getValue() );

    }
    
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
