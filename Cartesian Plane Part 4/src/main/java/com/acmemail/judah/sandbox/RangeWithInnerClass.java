package com.acmemail.judah.sandbox;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RangeWithInnerClass implements Iterable<Integer>
{
    private final int   lowerBound;
    private final int   upperBound;
    
    // Iterable over num,
    // where lowerBound <= num < upperBound
    public RangeWithInnerClass( int lowerBound, int upperBound )
    {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }
    
    public Iterator<Integer> iterator()
    {
        InnerIntIterator    iter    = new InnerIntIterator();
        return iter;
    }
    
    private class InnerIntIterator implements Iterator<Integer>
    {
        private int         next    = lowerBound;
        
        @Override
        public boolean hasNext()
        {
            boolean hasNext = next < upperBound;
            return hasNext;
        }

        @Override
        public Integer next()
        {
            if ( next >= upperBound )
                throw new NoSuchElementException( "iterator exhausted" );
            int nextInt = next++;
            return nextInt;
        }
        
    }
}
