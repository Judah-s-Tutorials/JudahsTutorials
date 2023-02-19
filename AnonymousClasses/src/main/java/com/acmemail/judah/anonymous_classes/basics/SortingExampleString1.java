package com.acmemail.judah.anonymous_classes.basics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class SortingExampleString1
{
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
    
    private static class InsensitiveSorter implements Comparator<String>
    {
        public int compare( String str1, String str2 )
        {
            return str1.compareToIgnoreCase( str2 );
        }
    }
}
