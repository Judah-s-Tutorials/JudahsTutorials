package com.acmemail.judah.anonymous_classes.streams;

import java.util.stream.IntStream;

/**
 * This is a simple application demonstrating 
 * how to generate a <em>Stream<Integer></em>
 * using the <em>IntStream.range</em> method.
 * 
 * @author Jack Straub
 */
public class StreamGenerationDemo1
{
    /**
     * Application entry point.
     * Produces a sequential stream of integers
     * in the range [10, 15).
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        // Produces a stream of integers in the range 10 (inclusive)
        // to 15 (exclusive).
        IntStream.range( 10, 15 ).forEach( System.out::println );
        
        
    }
}