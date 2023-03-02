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
 * @see SortingExampleInteger2
 * @see SortingExampleInteger3
 */
public class SortingExampleInteger4
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
        
        randomList.sort(new Comparator<Integer>(){
            public int compare( Integer num1, Integer num2 ){
            int     rcode   = num2 - num1; return rcode;}});
            
        for ( Integer num : randomList )
            System.out.println( num );
    }
}
