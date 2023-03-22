package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator to traverse a range of integers.
 * 
 * @author Jack Straub
 */
public class IntIterator implements Iterator<Integer>
{
    private final int   incr;
    private final int   end;
    private       int   next;
    
    /**
     * Constructor. 
     * Establishes the range of integers
     * over which to iterate.
     * 
     * @param start the first element of the range (inclusive)  
     * @param end   the last element of the range (exclusive)
     * @param incr  the increment to apply in order to determine 
     *              the "next" element of the sequence
     */
    public IntIterator( int start, int end, int incr )
    {
        next = start;
        this.end = end;
        this.incr = incr;
    }

    @Override
    public boolean hasNext()
    {
        return next < end;
    }

    @Override
    public Integer next()
    {
        if ( next >= end )
            throw new NoSuchElementException();
        int result  = next;
        next += incr;
        return result;
    }
}
