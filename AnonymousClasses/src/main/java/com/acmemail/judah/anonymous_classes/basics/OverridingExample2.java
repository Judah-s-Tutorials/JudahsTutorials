package com.acmemail.judah.anonymous_classes.basics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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