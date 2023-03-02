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
 * {@linkplain SortingExampleString1}.
 * In this example,
 * the explicit class declaration in the precursor,
 * {@linkplain SortingExampleString1},
 * is replaced with an anonymous class.
 * 
 * @author Jack Straub
 * 
 * @see SortingExampleString1
 */
public class SortingExampleString2
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
        
        Comparator<String> comp = new Comparator<>()
        {
            public int compare( String str1, String str2 )
            {
                return str1.compareToIgnoreCase( str2 );
            }
        };
        
        randomList.sort( comp );
        for ( String str : randomList )
            System.out.println( str );
    }
}
