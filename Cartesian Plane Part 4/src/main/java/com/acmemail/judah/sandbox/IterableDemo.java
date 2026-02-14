package com.acmemail.judah.sandbox;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IterableDemo
{
    public static void main(String[] args)
    {
        List<String>    list    = new ArrayList<>();
        list.add( "every" );
        list.add( "good" );
        list.add( "boy" );
        list.add( "deserves" );
        list.add( "favor" );
        
        Iterator<String>    iter    = list.iterator();
        while ( iter.hasNext() )
        {
            String  str = iter.next();
            System.out.println( str );
        }
    }
}
