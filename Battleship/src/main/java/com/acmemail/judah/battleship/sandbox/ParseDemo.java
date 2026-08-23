package com.acmemail.judah.battleship.sandbox;

import java.util.Arrays;

public class ParseDemo
{
    private static final String regex  = "[a-z,A-Z]+\\d+";
    public static void main(String[] args)
    {
        parse( "a9" );

    }
    
    private static void parse( String str )
    {
        String[]    args    = str.split( regex );
        System.out.print( args.length + ">" );
        Arrays.stream( args ).forEach( s -> System.out.print( s + " " ) );
        System.out.println( "<" );
    }

}
