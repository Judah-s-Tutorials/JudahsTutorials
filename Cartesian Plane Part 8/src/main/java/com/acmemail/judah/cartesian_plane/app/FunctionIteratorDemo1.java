package com.acmemail.judah.cartesian_plane.app;

import java.awt.geom.Point2D;
import java.util.Iterator;

import com.acmemail.judah.cartesian_plane.sandbox.FunctionIterator;

public class FunctionIteratorDemo1
{
    public static void main(String[] args)
    {
        // Calculate 3.5x**2 + -5x**2 + 0x + 1
        Polynomial              poly        = 
            new Polynomial( 3.5f, -5, 0, 1 );
        // Invoke poly for -1.5 <= x <= 1.5
        Iterator<Point2D>       funkIter    = 
            new FunctionIterator( poly, -1.5f, 1.5f, .5f );
        while ( funkIter.hasNext() )
        {
            Point2D point   = funkIter.next();
            System.out.println( point );
        }
    }
}
