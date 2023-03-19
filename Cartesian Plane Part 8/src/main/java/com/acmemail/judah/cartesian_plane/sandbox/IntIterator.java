package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IntIterator implements Iterator<Integer>
{
    private final int   incr;
    private final int   end;
    private       int   next;
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
