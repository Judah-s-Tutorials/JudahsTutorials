package com.acmemail.judah.sandbox;

import java.util.ArrayList;
import java.util.List;

public class ForEachDemo
{
    public static void main( String[] args )
    {
        List<String>    list    = new ArrayList<>();
        list.add( "every" );
        list.add( "good" );
        list.add( "boy" );
        list.add( "deserves" );
        list.add( "favor" );
        
        for ( String str : list )
            System.out.println( str );
    }
}
