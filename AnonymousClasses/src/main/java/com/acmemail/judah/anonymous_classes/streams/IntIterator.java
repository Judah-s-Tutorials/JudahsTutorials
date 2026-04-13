package com.acmemail.judah.anonymous_classes.streams;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Simple iterator, to be used in examples
 * of deriving a stream from an iterator.
 * 
 * @see IteratorToStreamDemo
 */
public class IntIterator implements Iterator<Integer>
{
    private final int   upperBound;
    private int         next;
    
    public IntIterator( int lowerBound, int upperBound )
    {
        this.upperBound = upperBound;
        next = lowerBound;
    }

    @Override
    public boolean hasNext()
    {
        boolean hasNext = next < upperBound;
        return hasNext;
    }

    /**
     * Returns the next element in the range.
     * 
     * Bloch Item 74 
     * "Use the Javadoc @throws tag to document each exception
     * that a method can throw, but do not use the throws keyword 
     * on unchecked exceptions."
     * (Bloch, Joshua. Effective Java (p. 304). Pearson Education. Kindle Edition.) 
     *
     * @throws  NoSuchElementException if the bounds
     *          of the iterator are exceeded
     */
    @Override
    public Integer next()
    {
        if ( next >= upperBound )
            throw new NoSuchElementException( "iterator exhausted" );
        int nextInt = next++;
        return nextInt;
    }

}
