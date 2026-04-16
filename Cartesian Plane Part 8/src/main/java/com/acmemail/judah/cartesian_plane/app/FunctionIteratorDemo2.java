package com.acmemail.judah.cartesian_plane.app;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.acmemail.judah.cartesian_plane.sandbox.FunctionIterator;

public class FunctionIteratorDemo2
{

    public static void main(String[] args)
    {
        // Calculate 3.5x**2 + -5x**2 + 0x + 1
        Polynomial              poly        = 
            new Polynomial( 3.5f, -5, 0, 1 );
        Iterator<Point2D>       funkIter    = 
            new FunctionIterator( poly, -2, 3, .5f );
        int                     props       = Spliterator.ORDERED;
        Spliterator<Point2D>    splitter    = 
            Spliterators.spliteratorUnknownSize( funkIter, props );
        Stream<Point2D>     stream      = 
            StreamSupport.stream( splitter, false );
        stream.forEach( System.out::println );
    }

}
