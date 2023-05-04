package com.acmemail.judah.cartesian_plane.input.exp4j;

import net.objecthunter.exp4j.function.Function;

public class Cotangent extends Function
{
    public Cotangent()
    {
        super( "cot", 1 );
    }
    
    @Override
    public double apply( double... args )
    {
        double  cotan = 1.0 / Math.tan( args[0] );
        return cotan;
    }        
}
