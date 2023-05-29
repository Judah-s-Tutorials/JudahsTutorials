package com.acmemail.judah.cartesian_plane.jep_sandbox;

import org.nfunk.jep.JEP;
import org.nfunk.jep.type.Complex;

/**
 * A program that evaluates
 * trigonometric functions
 * using complex values
 * as arguments.
 * 
 * @author Jack Straub
 */
public class InvestigatorTrig1
{
    private static final    JEP parser = new JEP();
    
    /**
     * Program entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        parser.addStandardConstants();
        parser.addStandardFunctions();
        parser.addComplex();
        parser.setImplicitMul( true );
        
        parser.addVariable( "z", 3, 2 );
        print( "2 * 3" );
        print( "2z" );
        print( "2(3 + 2i)" );
        print( "cos(z)" );
        
        Complex cpx = new Complex( 3, 2 );
        System.out.println( cpx.cos() );
    }
    
    private static void print( String expr )
    {
        parser.parseExpression( expr );
        if ( parser.hasError() )
            System.out.println( parser.getErrorInfo() );
        else
            System.out.println( parser.getComplexValue() );
    }
}
;