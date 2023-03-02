package com.acmemail.judah.anonymous_classes.basics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * This is a simple application to demonstrate
 * the use of an anonymous class
 * as a <em>Comparator.</em>
 * This example builds on sample application
 * {@linkplain SortingExampleInteger1}.
 * In this example,
 * the explicit class declaration in the precursor,
 * {@linkplain SortingExampleInteger1},
 * is replaced with an anonymous class.
 * 
 * @author Jack Straub
 * 
 * @see SortingExampleInteger1
 */
public class SortingExampleInteger2
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        List<Integer>   randomList  = new ArrayList<>();
        Random          randy       = new Random( 0 );
        for ( int inx = 0 ; inx < 100 ; ++inx )
            randomList.add( randy.nextInt( 100 ) );
        
        // Declare/instantiate an anonymous class for use as
        // a Comparator. It sorts integers in reverse order
        Comparator<Integer> comp    = 
            new Comparator<Integer>()
            {
                @Override
                public int compare( Integer num1, Integer num2 )
                {
                    int     rcode   = num2 - num1;
                    return rcode;
                }
            };
        
        randomList.sort( comp );
        for ( Integer num : randomList )
            System.out.println( num );
    }
}
