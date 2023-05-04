package com.acmemail.judah.cartesian_plane.input.exp4j;

import net.objecthunter.exp4j.function.Function;

public class ToDegrees extends Function
{
    public ToDegrees()
    {
        super( "toDegrees", 1 );
    }
    
    @Override
    public double apply( double... args )
    {
        double  degrees = args[0] * 180 / Math.PI;
        return degrees;
    }        
}
