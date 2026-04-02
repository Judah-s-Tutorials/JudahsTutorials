package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.File;

public class TargetClassesLister
{
    private static String   classesDir  = "target/classes";
    public static void main(String[] args)
    {
        File    classes = new File( classesDir );
        if ( !classes.exists() )
            System.out.println( classesDir + " not found" );
        else if ( !classes.isDirectory() )
            System.out.println( classesDir + " not a directory" );
        else
        {
            File[]  files   = classes.listFiles();
            for ( File file : files )
                System.out.println( file );
        }
    }
}
