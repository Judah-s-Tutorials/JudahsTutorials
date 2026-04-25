package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.Objects;

public class EqualsObjectDemo
{
    private final String    alpha;
    private final String    beta;
    
    public EqualsObjectDemo( String alpha, String beta )
    {
        this.alpha = alpha;
        this.beta = beta;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(alpha, beta);
    }

    @Override
    public boolean equals( Object object )
    {
        if ( this == object )
            return true;
        else
            return false;
    }    
}
