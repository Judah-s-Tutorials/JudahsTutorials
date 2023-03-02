package com.acmemail.judah.anonymous_classes.lambdas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This is a simple application
 * that demonstrates the use
 * of a method reference.
 * 
 * @author Jack Straub
 */
public class MethodReferenceExample1
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
        randomList.sort( (s1,s2) -> s1.compareToIgnoreCase( s2 ) );
        
        // The following two statements are equivalent.
        // The second statement uses a method reference.
        randomList.forEach( s -> System.out.println( s ) );
        randomList.forEach( System.out::println );
    }

}
