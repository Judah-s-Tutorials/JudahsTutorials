package com.acmemail.judah.cartesian_plane.jep_sandbox;

import org.nfunk.jep.JEP;

public class Demo1
{
    public static void main(String[] args)
    {
        JEP parser  = new JEP();
        parser.addStandardConstants();
        parser.addStandardFunctions();
        parser.setImplicitMul( true );
        parser.addVariable( "x", 2 );
        parser.parseExpression( "2x" );
        if ( !parser.hasError() )
            System.out.println( parser.getValue() );
        else
            System.out.println( parser.getErrorInfo() );
        
        parser.parseExpression( "2m" );
        if ( !parser.hasError() )
            System.out.println( parser.getValue() );
        else
            System.out.println( parser.getErrorInfo() );
    }
}
