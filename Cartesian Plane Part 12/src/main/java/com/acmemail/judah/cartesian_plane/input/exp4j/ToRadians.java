package com.acmemail.judah.cartesian_plane.input.exp4j;

import net.objecthunter.exp4j.function.Function;

public class ToRadians extends Function
{
    public ToRadians()
    {
        super( "toRadians", 1 );
    }
    
    @Override
    public double apply( double... args )
    {
        double  radians = args[0] * Math.PI / 180.;
        return radians;
    }        
}
