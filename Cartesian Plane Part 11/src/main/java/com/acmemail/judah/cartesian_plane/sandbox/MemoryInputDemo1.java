package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Application to demonstrate how
 * to designate a memory buffer
 * as in input source.
 * 
 * @author Jack Straub
 */
public class MemoryInputDemo1
{
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
                .collect( Collectors.toList() );
        byte[]          byteBuffer      = getByteArray( lines );
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
            exc.printStackTrace();
            System.exit( 1 );
        }
    }
    
    private static byte[] getByteArray( List<String> lines )
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
    
    private static void readFromInputBuffer( BufferedReader reader )
        throws IOException
    {
        String  line    = null;
        while ( (line = reader.readLine()) != null )
            System.out.println( line );
    }
}
