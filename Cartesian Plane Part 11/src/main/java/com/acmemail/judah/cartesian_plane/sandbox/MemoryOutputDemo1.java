package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UncheckedIOException;

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
        PrintStream saveOut     = System.out;
        try (
            ByteArrayOutputStream   outStream   = 
                new ByteArrayOutputStream();
            PrintStream             printStream = 
                new PrintStream( outStream );
        )
        {
            System.setOut( printStream );
            
            String  expOutput   = "A very clever message.";
            System.out.println( expOutput );
            
            String  actOutput   = outStream.toString();
            System.out.println( "Output: " + actOutput );
        }
        catch ( IOException exc )
        {
            // Technically, this block should never be reached, but
            // the close methods of ByteArrayOutputStream and
            // PrintStrean declare "throws IOException."
            throw new UncheckedIOException( exc );
        }
        finally
        {
            System.setOut( saveOut );
        }
    }
}
