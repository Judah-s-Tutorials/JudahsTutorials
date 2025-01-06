package com.acmemail.judah.anonymous_classes.streams;

import java.util.Arrays;

/**
 * This application is one of a pair
 * illustrating the difference between
 * the map and flatMap methods
 * found in the Stream class.
 * In this application we will stream a two-dimensonal array
 * and use the map method
 * producing a sequence of one-dimensional arrays
 * which can then be streamed.
 * 
 * @author Jack Straub
 */
public class MapVsFlatmapDemo2
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        int[][] values  =
        {
            { 10, 20, 30, 40, 50 },
            { 100, 200, 300 },
            { -5, -10, -15, -20, -25, -30, -35 }
        };
        Arrays.stream( values )
            .flatMapToInt( a -> Arrays.stream( a ) )
            .forEach( System.out:: println );
    }
    
}
