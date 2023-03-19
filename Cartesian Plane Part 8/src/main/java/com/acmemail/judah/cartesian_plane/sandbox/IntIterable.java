package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.Iterator;

public class IntIterable implements Iterable<Integer>
{
    private final int   incr;
    private final int   end;
    private       int   start;
    public IntIterable( int start, int end, int incr )
    {
        this.start = start;
        this.end = end;
        this.incr = incr;
    }
    @Override
    public Iterator<Integer> iterator()
    {
        return new IntIterator( start, end, incr );
    }
}
