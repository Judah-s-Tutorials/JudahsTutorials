package com.acmemail.judah.anonymous_classes.basics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * This is a precursor
 * to a simple application to demonstrate
 * the use of an anonymous class
 * as a <em>Comparator.</em>
 * This example uses
 * an instance of
 * an explicitly declared class (<em>ReverseSorter</em>)
 * for sorting a list of integers
 * in reverse order.
 * The goal is to replace
 * the explicit declaration
 * with an anonymous class.
 * This goal is accomplished in
 * {@linkplain SortingExampleInteger2}.
 * 
 * @author Jack Straub
 * 
 * @see SortingExampleInteger2
 */
public class SortingExampleInteger1
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
        
        Comparator<Integer> comp    = new ReverseSorter();
        
        randomList.sort( comp );
        for ( Integer num : randomList )
            System.out.println( num );
    }
    
    /**
     * Encapsulates a Comparator
     * for the purpose 
     * of sorting integers
     * in reverse order.
     * 
     * @author Jack Straub
     */
    private static class ReverseSorter implements Comparator<Integer>
    {
        @Override
        public int compare( Integer num1, Integer num2 )
        {
            int     rcode   = num2 - num1;
            return rcode;
        }
    }
}
