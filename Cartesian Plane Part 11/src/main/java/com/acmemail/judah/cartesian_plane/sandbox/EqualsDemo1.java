package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.Objects;

public class EqualsDemo1
{
    private final String    alpha;
    private final String    beta;
    
    public EqualsDemo1( String alpha, String beta )
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
        else if ( !(other instanceof EqualsDemo1) )
            result = false;
        else
        {
            EqualsDemo1  that   = (EqualsDemo1)other ;
            if ( this.alpha != null && !this.alpha.equals( that.alpha ) )
                result = false;
            else if ( this.alpha == null  && that.alpha != null )
                result = false;
            else if ( this.beta != null && !this.beta.equals( that.beta ) )
                result = false;
            else if ( this.beta == null && that.beta != null )
                result = false;
            else
                result = true;
        }
        return result;
    }
    
}
