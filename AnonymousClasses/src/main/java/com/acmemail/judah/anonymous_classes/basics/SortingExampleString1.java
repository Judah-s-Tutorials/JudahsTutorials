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
 * an explicitly declared class (<em>InsensitiveSorter</em>)
 * for sorting a list of strings
 * without distinguishing
 * between upper- and lower case.
 * The goal is to replace
 * the explicit declaration
 * with an anonymous class.
 * This goal is accomplished in
 * {@linkplain SortingExampleString2}.
 * 
 * @author Jack Straub
 * 
 * @see SortingExampleString2
 */
public class SortingExampleString1
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        List<String>    randomList  = new ArrayList<>();
        Random          randy       = new Random( 0 );
        for ( int inx = 0 ; inx < 100 ; ++inx )
        {
            int     rInt    = randy.nextInt( 100 );
            String  str     = rInt % 2 == 0 ? "ITEM" : "item";
            String  item    = String.format( "%s: %03d", str, rInt );
            randomList.add( item );
        }
        
        Comparator<String> comp = new InsensitiveSorter();
        
        randomList.sort( comp );
        for ( String str : randomList )
            System.out.println( str );
    }
    
    /**
     * Encapsulates a Comparator
     * for the purpose of performing
     * a case-insensitive sort
     * of a list of strings.
     * 
     * @author Jack Straub
     */
    private static class InsensitiveSorter implements Comparator<String>
    {
        @Override
        public int compare( String str1, String str2 )
        {
            return str1.compareToIgnoreCase( str2 );
        }
    }
}
