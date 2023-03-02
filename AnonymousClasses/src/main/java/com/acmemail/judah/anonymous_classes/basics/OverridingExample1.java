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
 * It overrides the <em>add</em> and <em>remove</em> methods
 * for the purpose of logging 
 * each occurrence of their invocation.
 * 
 * @author Jack Straub
 */
public class OverridingExample1
{
    @SuppressWarnings("serial")
    private static final List<String>  processQueue    = new ArrayList<>()
    {
        @Override
        public boolean add( String str )
        {
            System.err.println( "added: " + str );
            return super.add( str );
        }
        
        @Override
        public String remove( int index )
        {
            String  str = super.remove( index );
            System.err.println( "removed: " + str );
            return str;
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