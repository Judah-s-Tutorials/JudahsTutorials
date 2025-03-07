package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.Map;
import java.util.Set;

/**
 * This simple program writes everything in it environment
 * to <em>stdout.</em>
 * The intent is to demonstrate
 * how one Java program can start another
 * and be able to read this process's output.
 * 
 * @author Jack Straub
 * 
 * @see IPCParentSimpleDemo2
 */
public class IPCDemo2Child
{
    /**
     * Application entry point.
     * Write all key/value pairs
     * from this process's environment
     * to <em>stdout</em>
     * and then exit.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        Map<String,String>  envMap  = System.getenv();
        Set<String>         keySet  = envMap.keySet();
        
        for ( String key : keySet )
            System.out.println( key + " -> " + envMap.get( key ) );
        System.out.println( "done" );
    }
}
