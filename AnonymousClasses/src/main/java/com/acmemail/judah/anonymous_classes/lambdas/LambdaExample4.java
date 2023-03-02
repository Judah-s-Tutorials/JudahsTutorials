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
public class LambdaExample4
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
        
        // Sort the list of integers in such a way that all
        // even numbers appear greater than all odd numbers.
        randomList.sort( 
            (num1, num2) ->
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
        );
        for ( Integer num : randomList )
            System.out.println( num );
    }
}
