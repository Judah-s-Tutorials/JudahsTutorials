package com.acmemail.judah.sandbox;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class listIteratorDemo
{
    public static void main(String[] args)
    {
        List<String>    list    = new ArrayList<>();
        list.add( "every" );
        list.add( "good" );
        list.add( "boy" );
        list.add( "deserves" );
        list.add( "favor" );
        
        ListIterator<String>    iter    = list.listIterator();
        while ( iter.hasNext() )
        {
            String  str = iter.next();
            System.out.println( str );
        }
    }
}
