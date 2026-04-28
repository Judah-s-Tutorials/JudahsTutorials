package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Application to demonstrate how
 * to designate a memory buffer
 * as an input source.
 * 
 * @author Jack Straub
 */
public class MemoryInputDemo1
{
    /**
     * Default constructor, not used.
     */
    private MemoryInputDemo1()
    {
        // not used
    }
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        List<String>    lines   = 
            IntStream.range( 1, 11 )
                .mapToObj( i -> "Input line #" + i )
                .toList();
        byte[]          byteBuffer      = getByteBuffer( lines );
        try(
            ByteArrayInputStream baiStream = 
                new ByteArrayInputStream( byteBuffer );
            InputStreamReader strReader = new InputStreamReader( baiStream );
            BufferedReader bufReader = new BufferedReader( strReader );
        )
        {
            readFromInputBuffer( bufReader );
        }
        catch ( IOException exc )
        {
            throw new UncheckedIOException( exc );
        }
    }
    
    /**
     * Write a list of strings to a byte array,
     * treating each list element as a line of text.
     * 
     * @param lines the list to convert
     * 
     * @return  the converted byte array
     */
    private static byte[] getByteBuffer( List<String> lines )
    {
        ByteArrayOutputStream   baoStream   = new ByteArrayOutputStream();
        try ( PrintWriter writer = new PrintWriter( baoStream ); )
        {
            lines.forEach( writer::println );
            writer.flush();
        }
        byte[]  bytes = baoStream.toByteArray();
        return bytes; 
    }
    
    /**
     * Read all the lines in a given BufferedReader,
     * and write them to stdout.
     * 
     * @param reader    the given BufferedReader
     * 
     * @throws IOException  if an I/O error occurs
     */
    private static void readFromInputBuffer( BufferedReader reader )
        throws IOException
    {
        String  line    = null;
        while ( (line = reader.readLine()) != null )
            System.out.println( line );
    }
}
