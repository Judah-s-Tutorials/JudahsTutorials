package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.StringTokenizer;

public class TestClassPathProperties
{
    public static void main(String[] args)
    {
        String  separator   = System.getProperty( "path.separator" );
        String  classPath   = System.getProperty( "java.class.path" );
        
        StringTokenizer tizer   = new StringTokenizer( classPath, separator );
        while ( tizer.hasMoreTokens() )
            System.out.println( tizer.nextToken() );
    }
}
