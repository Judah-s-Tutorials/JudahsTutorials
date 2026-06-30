package com.acmemail.judah.cartesian_plane.app;

import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * This program demonstrates that,
 * to apply the logic of a stream multiple times,
 * the stream must be regenerated each time.
 */
public class GoodStreamDemo
{
    /**
     * Default constructor; not used.
     */
    public GoodStreamDemo()
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
        Supplier<IntStream> supplier    = () -> streamGetter();
        for ( int inx = 0 ; inx < 2 ; ++inx )
            supplier.get().forEach( System.out::println );
    }

    private static IntStream streamGetter()
    {
        IntStream   stream  = IntStream
            .iterate( 0, i -> i < 10, i -> i + 1 )
            .map( i -> 2 * i );
        return stream;
    }
}
