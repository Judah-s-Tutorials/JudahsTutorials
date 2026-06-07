package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.Objects;

/**
 * Second pass at demonstrating how to write an Equals method.
 * Updated by employing Objects.equals(Object, Object), 
 * pattern-matching instanceof.
 * 
 * @see EqualsDemo1
 */
public class EqualsDemo2
{
    /** Demonstration field. */
    private final String    alpha;
    /** Demonstration field. */
    private final String    beta;
    
    /**
     * Constructor.
     * Initializes a new instance of this class.
     * 
     * @param alpha initializer for the final alpha field
     * @param beta  initializer for the final beta field
     */
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
        else if ( other instanceof EqualsDemo2 that )
        {
            if ( !Objects.equals( this.alpha, that.alpha ) )
                result = false;
            else if ( !Objects.equals( this.beta, that.beta ) )
                result = false;
            else
                result = true;
        }
        return result;
    }

    @Override
    public String toString()
    {
        return alpha + ", " + beta;
    }
}
