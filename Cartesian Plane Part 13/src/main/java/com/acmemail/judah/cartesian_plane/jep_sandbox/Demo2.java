package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.util.stream.DoubleStream;

import org.nfunk.jep.JEP;

public class Demo2
{
    public static void main(String[] args)
    {
        JEP parser  = new JEP();
        parser.addStandardConstants();
        parser.addStandardFunctions();
        parser.setImplicitMul( true );
        parser.addVariable( "x", 0 );
        parser.parseExpression( "2x" );
        
        DoubleStream.iterate( -3, d -> d <= 3, d -> d = d + .1 )
            .peek( d -> parser.addVariable( "x", d ) )
            .map( d -> parser.getValue() )
            .forEach( System.out::println );
    }
}
