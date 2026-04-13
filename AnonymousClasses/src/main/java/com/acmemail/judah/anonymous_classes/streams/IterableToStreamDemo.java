package com.acmemail.judah.anonymous_classes.streams;

import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Simple application to demonstrate the algorithm
 * for converting an Iterable&lt;T> to a Stream&lt;T> .
 */
public class IterableToStreamDemo
{
    /**
     * Application entry point
     * 
     * @param args  command-line arguments, not used
     */
    public static void main(String[] args)
    {
        Iterable<Integer>       iterable    = new IntIterable( -5, 5 );
        Spliterator<Integer>    spliterator = iterable.spliterator();
        Stream<Integer>         intStream   = 
            StreamSupport.stream( spliterator, false );
        intStream.map( i -> i * 2 ).forEach( System.out::println );
    }
}
