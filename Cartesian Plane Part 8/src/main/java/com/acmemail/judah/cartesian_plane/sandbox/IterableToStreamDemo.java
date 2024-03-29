package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Simple application that demonstrates
 * how to generate a Stream
 * from an iterable.
 * Compare to {@linkplain IterableToStreamDemoAlt}.
 * 
 * @author Jack Straub
 * 
 * @see IteratorToStreamDemo
 */
public class IterableToStreamDemo
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
        Stream<Integer>         stream      = 
            StreamSupport.stream( splitter, false );
        stream.forEach( System.out::println );
    }
}
