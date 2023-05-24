package com.acmemail.judah.cartesian_plane.sandbox;

import org.nfunk.jep.JEP;

import com.acmemail.judah.cartesian_plane.input.JEPFunctions;

public class JEPFunctionsDemo1
{
    public static void main(String[] args)
    {
        JEP     xExpr   = new JEP();
        xExpr.addStandardConstants();
        xExpr.addStandardFunctions();
        xExpr.setImplicitMul( true );
        xExpr.addVariable( "a", 1.5 );
        xExpr.addVariable( "h", -1 );
        xExpr.addVariable( "t", Math.PI );
        
        JEPFunctions.getFunctions();
        JEPFunctions.getFunctions()
            .forEach( f -> xExpr.addFunction( f.getName(), f ) );
        
        xExpr.parseExpression( "csc( pi/4 )" );
        System.out.println( xExpr.getValue() );    }
}
