package com.acmemail.judah.anonymous_classes.streams;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Simple application to demonstrate the algorithm
 * for converting an Iterable&lt;T> to a Stream&lt;T> .
 */
public class IteratorToStreamDemo
{
    /**
     * Application entry point
     * 
     * @param args  command-line arguments, not used
     */
    public static void main(String[] args)
    {
        Iterator<Integer>       iterator    = new IntIterator( -5, 5 );
        int                     ordered     = Spliterator.ORDERED;
        Spliterator<Integer>    spliterator = 
            Spliterators.spliteratorUnknownSize( iterator, ordered );
        Stream<Integer>         intStream   = 
            StreamSupport.stream( spliterator, false );
        intStream.map( i -> i * 2 ).forEach( System.out::println );
    }
}
