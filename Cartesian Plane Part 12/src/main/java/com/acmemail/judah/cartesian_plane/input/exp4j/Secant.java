package com.acmemail.judah.cartesian_plane.input.exp4j;

import net.objecthunter.exp4j.function.Function;

public class Secant extends Function
{
    public Secant()
    {
        super( "sec", 1 );
    }
    
    @Override
    public double apply( double... args )
    {
        double  secant = 1.0 / Math.cos( args[0] );
        return secant;
    }        
}
