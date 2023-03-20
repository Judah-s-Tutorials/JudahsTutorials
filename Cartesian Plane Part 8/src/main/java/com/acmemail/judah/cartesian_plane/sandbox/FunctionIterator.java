package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.DoubleUnaryOperator;

public class FunctionIterator implements Iterator<Point2D>
{
    private final DoubleUnaryOperator   funk;
    private final double                last;
    private final double                incr;
    private double                      next;
    
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
