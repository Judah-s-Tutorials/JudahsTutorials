package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.IOException;

/**
 * Program to demonstrate how to execute a non-Java program
 * as a child process.
 * This has only been tested on Windows.
 * 
 * @author Jack Straub
 */
public class NonJavaChildProcessDemo
{
     /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args) 
        throws IOException, InterruptedException
    {
        String  osName  = System.getProperty( "os.name" );
        if ( osName == null )
        {
            System.err.println( "cannot find os name, terminating" );
            System.exit( 1 );
        }
        String[]    cmd     = new String[2];
        if ( osName.startsWith( "Windows" ) )
        {
            cmd[0] = "notepad";
            cmd[1] = "sample.txt";
        }
        else
        {
            cmd[0] = "echo";
            cmd[1] = "Hello World";
        }

        ProcessBuilder  bldr    = new ProcessBuilder( cmd );
        Process         process = bldr.start();
        int             status  = process.waitFor();
        System.out.println( "child exit status: " + status );
    }
    
}
