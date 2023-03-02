package com.acmemail.judah.anonymous_classes.lambdas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This is a simple application 
 * that demonstrates how to declare a comparator
 * using a lambda.
 * The comparator is declared
 * directly as an argument
 * to a sort method.
 * 
 * @author Jack Straub
 */
public class LambdaExample3
{        
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main( String[] args )
    {
        List<Integer>   randomList  = new ArrayList<>();
        Random          randy       = new Random( 0 );
        for ( int inx = 0 ; inx < 100 ; ++inx )
            randomList.add( randy.nextInt( 100 ) );
        
        // Perform a reverse sort on the list of integers.
        randomList.sort( (num1, num2) -> num2 - num1 );
        for ( Integer num : randomList )
            System.out.println( num );
    }
}
