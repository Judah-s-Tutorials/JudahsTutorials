package com.acmemail.judah.anonymous_classes.basics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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