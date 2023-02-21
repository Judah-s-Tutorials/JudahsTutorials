package com.acmemail.judah.anonymous_classes.basics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class SortingExampleInteger6
{
    public static void main(String[] args)
    {
        List<Integer>   randomList  = new ArrayList<>();
        Random          randy       = new Random( 0 );
        for ( int inx = 0 ; inx < 100 ; ++inx )
            randomList.add( randy.nextInt( 100 ) );
        
        randomList.sort( 
            new Comparator<>()
            { 
                public int compare( Integer num1, Integer num2 )
                {
                    int     iNum1   = num1;
                    int     iNum2   = num2;
                    boolean isEven1 = iNum1 % 2 == 0;
                    boolean isEven2 = iNum2 % 2 == 0;
                    int     rcode   = 0;
                    
                    if ( isEven1 == isEven2 )
                        rcode = iNum1 - iNum2;
                    else if ( isEven1 )
                        rcode = 1;
                    else
                        rcode = -1;
                    return rcode;
                }
            });
        for ( Integer num : randomList )
            System.out.println( num );
    }
}
