package com.acmemail.judah.battleship.util;

import java.io.PrintStream;
import java.util.List;

/**
 * This class contains methods 
 * that write strings to stdout and stderr.
 * The methods are mutually synchronized.
 * Each method flushes its output
 * prior to returning.
 */
public class StdOutputUtils
{
    /**
     * Default constructor; not used.
     */
    private StdOutputUtils()
    {
        // default constructor; not used
    }
    
    /**
     * Write a string followed by a line-separator to stdout,
     * and flush stdout before returning.
     * 
     * @param line  the string to write
     */
    public static void writeOut( String line )
    {
        writeOut( List.of( line ) );
    }
    
    /**
     * Write each string in a list to stdout.
     * Each string is written on a separate line;
     * stdout is flushed before returning.
     * 
     * @param lines the strings to write
     */
    public static void writeOut( List<String> lines )
    {
        writeLines( System.out, lines );
    }
    
    /**
     * Write each string in a list to stderr.
     * Each string is written on a separate line;
     * stderr is flushed before returning.
     * 
     * @param lines the strings to write
     */
    public static void writeErr( List<String> lines )
    {
        writeLines( System.err, lines );
    }
    
    /**
     * Write a single string to stdout
     * <em>without</em> a line separator.
     * 
     * @param line  the string to write
     */
    public static synchronized void printOut( String line )
    {
        System.out.print( line );
        System.out.flush();
    }
    
    /**
     * Write each string in a list to the given output stream.
     * Each string is written on a separate line;
     * the output stream is flushed before returning.
     * 
     * @param str   the given output stream
     * @param lines the strings to write
     */
    private static synchronized void writeLines( 
        PrintStream str, 
        List<String> lines 
    )
    {
        lines.forEach( str::println );
        str.flush();
    }
}
