package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.Iterator;

/**
 * Implementation of <em>Iterable&lt;Integer&gt;</em>.
 * Iterates sequentially over a range of integers
 * using a given increment.
 * 
 * @author Jack Straub
 */
public class IntIterable implements Iterable<Integer>
{
    private final int   incr;
    private final int   end;
    private       int   start;
    
    /**
     * Constructor.
     * 
     * @param start the start of the range to iterate over (inclusive)
     * @param end   the end of the range to iterate over (exclusive)
     * @param incr  the increment to apply to an element
     *              to determine the "next" element
     */
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
