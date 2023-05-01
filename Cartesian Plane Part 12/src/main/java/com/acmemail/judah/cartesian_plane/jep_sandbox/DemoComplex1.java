package com.acmemail.judah.cartesian_plane.jep_sandbox;

import org.nfunk.jep.JEP;

public class DemoComplex1
{
    public static void main(String[] args)
    {
        JEP parser  = new JEP();
        parser.addStandardConstants();
        parser.addStandardFunctions();
        parser.addComplex();
        parser.setImplicitMul( true );
        parser.addVariable( "x", 0, 1 );
        parser.parseExpression( "e^(i pi) + 1" );
        
        if ( !parser.hasError() )
            System.out.println( parser.getComplexValue() );
        else
            System.out.println( parser.getErrorInfo() );
    }
}
