package com.acmemail.judah.cartesian_plane.app;

import java.util.stream.IntStream;

/**
 * This program demonstrates that a <em>stream</em>
 * can only be traversed <em>once.</em>
 * For the corrected logic
 * see {@link GoodStreamDemo}.
 */
public class BadStreamDemo
{
    /**
     * Default constructor; not used.
     */
    public BadStreamDemo()
    {
        // Not used.
    }

    /**
     * Application entry point.
     * THIS APPLICATION WILL FAIL
     * because it tries to traverse a stream
     * more than once.
     * 
     * @param args  command-line arguments; not used
     */
    public static void main(String[] args)
    {
        IntStream   stream  = streamGetter();
        for ( int inx = 0 ; inx < 2 ; ++inx )
            stream.forEach( System.out::println );
    }

    private static IntStream streamGetter()
    {
        IntStream   stream  = IntStream
            .iterate( 0, i -> i < 10, i -> i + 1 )
            .map( i -> 2 * i );
        return stream;
    }
}
