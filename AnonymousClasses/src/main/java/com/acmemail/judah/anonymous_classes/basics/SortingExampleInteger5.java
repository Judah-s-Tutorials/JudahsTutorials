package com.acmemail.judah.anonymous_classes.basics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class SortingExampleInteger5
{
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
    
    private static class PreferEvenSorter implements Comparator<Integer>
    {
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
