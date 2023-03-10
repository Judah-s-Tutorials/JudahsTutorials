package com.acmemail.judah.anonymous_classes.functional_interfaces;

/**
 * Demonstrates how to implement
 * an <em>IntSequence</em> functional interface.
 * Starting with a given value,
 * generates a sequence of even numbers.
 * 
 * @author Jack Straub
 */
public class EvenIntSequence extends IntSequence
{
    /**
     * Default constructor.
     * Generates a sequence of even numbers
     * beginning with 0.
     */
    public EvenIntSequence()
    {
        this( 0 );
    }
    
    /**
     * Constructor.
     * Generates a sequence of even numbers
     * beginning with given value;
     * the given value must be even.
     * 
     * @param start the given value to start the sequence
     * 
     * @throws  IllegalArgumentException
     *          if the given start value
     *          is not even
     */
    public EvenIntSequence( int start )
    {
        super( start, 2 );
        if ( start % 2 != 0 )
        {
            String  fmt = "start value (%d) illegal; must be an even number";
            String  msg = 
                String.format( fmt, start );
            throw new IllegalArgumentException( msg );
        }
    }
}
