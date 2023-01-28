package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class TargetProcessA
{
    public static void main(String[] args)
    {
        try
        {
            Thread.sleep( 1000 );
        }
        catch ( InterruptedException exc )
        {
            
        }
        Map<String,String>  envMap  = System.getenv();
        Set<String>         keySet  = envMap.keySet();
        
        try ( Scanner scanner = new Scanner( System.in ); )
        {
            String  fromParent  = scanner.nextLine();
            System.out.println( fromParent );
            for ( String key : keySet )
                System.out.println( key + "-> " + envMap.get( key ) );
        }
        
        System.err.println( "error message" );
        System.exit( 42 );
    }
}
