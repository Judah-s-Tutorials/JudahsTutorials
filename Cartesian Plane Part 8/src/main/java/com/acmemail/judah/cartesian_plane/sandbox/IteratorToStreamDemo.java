package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

/**
 * Simple application that demonstrates
 * how to use generate a Stream
 * from an Iterator.
 * 
 * @author Jack Straub
 */
public class IteratorToStreamDemo
{
    /**
     * Application entry point. 
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        Iterator<Integer>       iter        = new IntIterator( 5, 10, 2 );
        Spliterator<Integer>    splitter    = 
            Spliterators.spliteratorUnknownSize( iter, 0 );
        StreamSupport.stream( splitter, false )
            .forEach( System.out::println );
    }
}
