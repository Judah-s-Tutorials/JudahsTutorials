package com.acmemail.judah.anonymous_classes.lambdas;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * This is a simple application 
 * that demonstrates four equivalent ways
 * to declare/instantiate an anonymous class.
 * The first provides a "traditional" anonymous class declaration,
 * the second uses a "long form" lambda 
 * and the third uses a lambda 
 * with all syntactical shortcuts applied. 
 * The fourth example uses the same syntax as the third,
 * but instantiates the comparator directly 
 * as an argument to a sort method.
 * 
 * @author Jack Straub
 */
@SuppressWarnings("unused")
public class LambdaExample2
{
    private static final Comparator<String> insensitiveCmp1 =
        new Comparator<String>()
        {
            public int compare( String s1, String s2 )
            {
                return s1.compareToIgnoreCase( s2 );
            }
        };
        
    private static final Comparator<String> insensitiveCmp2 =
        (s1, s2) -> { return s1.compareToIgnoreCase( s2 ); };
            
    private static final Comparator<String> insensitiveCmp3 =
        (s1, s2) -> s1.compareToIgnoreCase( s2 );
        
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main( String[] args )
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
        
        randomList.sort( (s1, s2) -> s1.compareToIgnoreCase( s2 ) );
        
        for ( String str : randomList )
            System.out.println( str );
    }
}
