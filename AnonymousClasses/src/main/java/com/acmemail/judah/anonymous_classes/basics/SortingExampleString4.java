package com.acmemail.judah.anonymous_classes.basics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * This is a simple application to demonstrate
 * how to instantiate an anonymous class directly
 * as an argument to a method.
 *
 * @author Jack Straub
 *
 * @see SortingExampleString2
 * @see SortingExampleString3
 */
public class SortingExampleString4
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
        
        randomList.sort( new Comparator<String>(){
            public int compare( String str1, String str2 ){
                return str1.compareToIgnoreCase( str2 );}});
        
        for ( String str : randomList )
            System.out.println( str );
    }
}
