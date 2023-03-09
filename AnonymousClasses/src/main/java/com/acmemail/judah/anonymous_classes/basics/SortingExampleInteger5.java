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
 * an explicitly declared class (<em>PreferEvenSorter</em>)
 * for sorting a list of integers
 * in such a way
 * that all even numbers
 * are ordered after all odd numbers.
 * The goal is to replace
 * the explicit declaration
 * with an anonymous class.
 * This goal is accomplished in
 * {@linkplain SortingExampleInteger6}.
 * 
 * @author Jack Straub
 * 
 * @see SortingExampleInteger6
 */
public class SortingExampleInteger5
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
        
        Comparator<Integer> comp    = new PreferEvenSorter();
        
        randomList.sort( comp );
        for ( Integer num : randomList )
            System.out.println( num );
    }
    
    /**
     * This class
     * is an implementation of Comparator<Integer>
     * that sorts a list of integers
     * so that 
     * all even numbers
     * come later than all odd numbers.
     * 
     * @author Jack Straub
     */
    private static class PreferEvenSorter implements Comparator<Integer>
    {
        @Override
        public int compare( Integer num1, Integer num2 )
        {
            boolean isEven1 = num1 % 2 == 0;
            boolean isEven2 = num2 % 2 == 0;
            int     rcode   = 0;
            
            if ( isEven1 == isEven2 )
                rcode = num1 - num2;
            else if ( isEven1 )
                rcode = 1;
            else
                rcode = -1;
            return rcode;
        }
    }
}
