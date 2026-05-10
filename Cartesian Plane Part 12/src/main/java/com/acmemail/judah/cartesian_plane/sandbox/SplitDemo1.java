package com.acmemail.judah.cartesian_plane.sandbox;

public class SplitDemo1
{
    public static void main( String[] args )
    {
        String      regex           = "[\\s=]+";
        String      testString      = "SET x=5";
        String[]    splitString     = testString.split( regex );
        System.out.println( splitString.length );
        for ( String str : splitString )
            System.out.print( ">>" + str + "<< " );
    }
}
