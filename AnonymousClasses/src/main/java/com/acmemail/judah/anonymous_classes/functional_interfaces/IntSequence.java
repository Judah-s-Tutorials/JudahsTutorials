package com.acmemail.judah.anonymous_classes.functional_interfaces;

import java.util.function.IntSupplier;

/**
 * Demonstrates how to implement
 * an <em>IntSequence</em> functional interface.
 * Starting with a given value,
 * generates a series of integers
 * by applying an increment
 * to the previous integer in the series.
 * 
 * @author Jack Straub
 */
public class IntSequence implements IntSupplier
{
    private final int   increment;
    private final int   start;
    private int         next;
    
    /**
     * Default constructor.
     * Generates a sequence of integers
     * with a start value of one,
     * and an increment of one.
     */
    public IntSequence()
    {
        this( 1, 1 );
    }
    
    /**
     * Constructor.
     * Generates a sequence of integers
     * with a given start value,
     * and an increment of one.
     * 
     * @param   start   the given start value
     */
    public IntSequence( int start )
    {
        this( start, 1 );
    }
    
    /**
     * Constructor.
     * Generates a sequence of integers
     * with a given start value,
     * and a given increment
     * 
     * @param   start       the given start value
     * @param   increment   the given increment
     */
    public IntSequence( int start, int increment )
    {
        this.increment = increment;
        this.start = start;
        this.next = start;
    }
    
    /**
     * Resets the sequence generation
     * it its original start value.
     */
    public void reset()
    {
        next = start;
    }
    
    @Override
    public int getAsInt()
    {
        int result  = next;
        next += increment;
        return result;
    }
}
