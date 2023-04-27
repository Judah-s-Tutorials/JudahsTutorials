package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Application to demonstrate how to redirect stdout
 * to a memory buffer.
 * 
 * @author Jack Straub
 */
public class MemoryOutputDemo1
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     * 
     * @throws IOException  if an I/O error occurs
     */
    public static void main(String[] args)
    {
        try (
            ByteArrayOutputStream outStream   = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream( outStream );
        )
        {
            PrintStream             saveOut     = System.out;
            System.setOut( printStream );
            
            String                  expOutput   = "A very clever message.";
            System.out.println( expOutput );
            System.setOut( saveOut );
            
            String                  actOutput   = outStream.toString();
            System.out.println( "Output: " + actOutput );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }
}
