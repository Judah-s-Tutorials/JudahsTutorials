package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.Objects;

public class EqualsDemo2
{
    private final String    alpha;
    private final String    beta;
    
    public EqualsDemo2( String alpha, String beta )
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
    public boolean equals( Object other )
    {
        boolean result  = false;
        if ( this == other  )
            result = true;
        else if (other == null)
            result = false;
        else if ( !(other instanceof EqualsDemo2) )
            result = false;
        else
        {
            EqualsDemo2  that    = (EqualsDemo2)other ;
            if ( !Objects.equals( this.alpha, that.alpha ) )
                result = false;
            else if ( !Objects.equals( this.beta, that.beta ) )
                result = false;
            else
                result = true;
        }
        return result;
    }
    
}
