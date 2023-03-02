package com.acmemail.judah.anonymous_classes.basics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This is a simple application to demonstrate
 * overloading of a method in an anonymous class.
 * In this example,
 * <em>processQueue</em> is an instance
 * of an anonymous class
 * which is a subclass of <em>ArrayList</em>. 
 * It overrides the <em>remove</em> method
 * for the purpose of preventing the use
 * of the <em>remove</em> method.
 * 
 * @author Jack Straub
 */
public class OverridingExample2
{
    @SuppressWarnings("serial")
    private static final List<String>  processQueue    = 
        new ArrayList<>()
        {
            @Override
            public String remove( int index )
            {
                throw new UnsupportedOperationException();
            }
        };
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        Random  randy   = new Random( 0 );
        int     itemNum = 0;
        while ( itemNum < 10 )
        {
            String  item    = String.format( "item: %03d", itemNum++ );
            processQueue.add( item );
            item = String.format( "item: %03d", itemNum++ );
            processQueue.add( item );
            
            int index   = randy.nextInt( processQueue.size() );
            processQueue.remove( index );
        }
    }
}