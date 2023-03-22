package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Simple application that demonstrates
 * how to use generate a Stream
 * from an Iterable.
 * Compare to {@linkplain IterableToStreamDemo}.
 * 
 * @author Jack Straub
 * 
 * @see IteratorToStreamDemo
 */
public class IterableToStreamDemoAlt
{
    /**
     * Application entry point. 
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        Iterable<Integer>       iter        = new IntIterable( 5, 10, 2 );
        Spliterator<Integer>    splitter    = iter.spliterator();
        StreamSupport.stream( splitter, false )
            .forEach( System.out::println );
    }
}
