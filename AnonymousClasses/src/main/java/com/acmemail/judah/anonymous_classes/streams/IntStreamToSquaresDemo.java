package com.acmemail.judah.anonymous_classes.streams;

import java.util.stream.IntStream;

/**
 * Application to demonstrate 
 * the <em>Stream.forEach(Consumer&lt;T&gt;)</em> method.
 * 
 * @author Jack Straub
 */
public class IntStreamToSquaresDemo
{
    /**
     * Application entry point.
     * Produces a sequential list of integers
     * and their squares.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        IntStream.range( 1, 11 )
            .forEach( i -> System.out.println( i + " -> " + i * i ) );

    }
}
