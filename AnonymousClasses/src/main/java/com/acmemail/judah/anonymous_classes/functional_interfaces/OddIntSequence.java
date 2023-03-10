package com.acmemail.judah.anonymous_classes.functional_interfaces;

/**
 * Demonstrates how to implement
 * an <em>IntSequence</em> functional interface.
 * Starting with a given value,
 * generates a sequence of odd numbers.
 * 
 * @author Jack Straub
 */
public class OddIntSequence extends IntSequence
{
    /**
     * Default constructor.
     * Generates a sequence of odd numbers
     * beginning with one.
     */
    public OddIntSequence()
    {
        this( 1 );
    }
    
    /**
     * Constructor.
     * Generates a sequence of odd numbers
     * beginning with a given value.
     * 
     * @param   start   the given start valiue   
     */
    public OddIntSequence( int start )
    {
        super( start, 2 );
        if ( start % 2 == 0 )
        {
            String  fmt = "start value (%d) illegal; must be an odd number";
            String  msg = 
                String.format( fmt, start );
            throw new IllegalArgumentException( msg );
        }
    }
}
