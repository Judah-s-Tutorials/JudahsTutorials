package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.DoubleUnaryOperator;

/**
 * This is iterator to traverse a range of doubles
 * producing a double result
 * as determined by a given 
 * <em>DoubleUnaryOperator</em> functional interface.
 * 
 * @author Jack Straub
 */
public class FunctionIterator implements Iterator<Point2D>
{
    private final DoubleUnaryOperator   funk;
    private final double                last;
    private final double                incr;
    private double                      next;
    
    /**
     * Constructor.
     * Establishes the range for the iterator
     * and the given DoubleUnaryOperator functional interface.
     * 
     * @param funk      the given functional interface
     * @param first     the first value of the range 
     *                  over which to iterate (inclusive)
     * @param last      the last value of the range 
     *                  over which to iterate (inclusive)
     * @param incr      the incremental value that, given element <em>n</em>,
     *                  is applied to generate element <em>n + 1</em>
     *                  of the sequence.
     */
    public FunctionIterator( 
        DoubleUnaryOperator funk, 
        double first, 
        double last, 
        double incr
    )
    {
        this.funk = funk;
        this.last = last;
        this.incr = incr;
        next = first;
    }

    @Override
    public boolean hasNext()
    {
        boolean result  = next <= last;
        return result;
    }

    @Override
    public Point2D next()
    {
        if ( next > last )
            throw new NoSuchElementException();
        Point2D point   = 
            new Point2D.Double( next, funk.applyAsDouble( next ) );
        next += incr;
        return point;
    }
}
