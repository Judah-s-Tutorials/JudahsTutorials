package com.acmemail.judah.sandbox;

import java.util.ArrayList;
import java.util.List;

public class ListDemo
{
    public static void main( String[] args )
    {
        List<String>    list    = new ArrayList<>();
        list.add( "string 1" );
        list.add( "string 2" );
        String          str     = list.get( 1 );
        System.out.println( str );
    }
}
