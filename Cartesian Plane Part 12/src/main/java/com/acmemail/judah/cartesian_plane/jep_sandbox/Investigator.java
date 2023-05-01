package com.acmemail.judah.cartesian_plane.jep_sandbox;

import org.nfunk.jep.JEP;

public class Investigator
{
    public static void main(String[] args)
    {
        JEP parser = new JEP();
        parser.addStandardConstants();
        parser.addStandardFunctions();
        parser.addComplex();
        parser.setImplicitMul( true );
        
        parser.addVariable( "z", 3, 2 );
        parser.parseExpression( "2 * z" );
        if ( parser.hasError() )
            System.out.println( parser.getErrorInfo() );
        else
            System.out.println( parser.getComplexValue() );
    }
}
