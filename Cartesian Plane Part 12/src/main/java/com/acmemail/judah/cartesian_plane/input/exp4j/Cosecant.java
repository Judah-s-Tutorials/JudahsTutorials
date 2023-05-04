package com.acmemail.judah.cartesian_plane.input.exp4j;

import net.objecthunter.exp4j.function.Function;

public class Cosecant extends Function
{
    public Cosecant()
    {
        super( "csc", 1 );
    }
    
    @Override
    public double apply( double... args )
    {
        double  cosecant = 1.0 / Math.sin( args[0] );
        return cosecant;
    }        
}
