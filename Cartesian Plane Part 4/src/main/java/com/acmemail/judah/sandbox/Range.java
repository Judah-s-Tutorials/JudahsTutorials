package com.acmemail.judah.sandbox;

import java.util.Iterator;

public class Range implements Iterable<Integer>
{
    private final int   lowerBound;
    private final int   upperBound;
    
    // Iterable over num,
    // where lowerBound <= num < upperBound
    public Range( int lowerBound, int upperBound )
    {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }
    
    public Iterator<Integer> iterator()
    {
        IntIterator iter    = new IntIterator( upperBound, lowerBound );
        return iter;
    }
}
