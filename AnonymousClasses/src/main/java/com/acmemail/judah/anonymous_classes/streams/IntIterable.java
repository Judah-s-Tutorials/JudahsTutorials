package com.acmemail.judah.anonymous_classes.streams;

import java.util.Iterator;

public class IntIterable implements Iterable<Integer>
{
    private final int   lowerBound;
    private final int   upperBound;
    
    public IntIterable( int lowerBound, int upperBound )
    {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public Iterator<Integer> iterator()
    {
        Iterator<Integer>   iter    = 
            new IntIterator( lowerBound, upperBound );
        return iter;
    }

}
