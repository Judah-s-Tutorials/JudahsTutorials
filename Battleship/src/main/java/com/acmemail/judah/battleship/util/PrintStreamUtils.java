package com.acmemail.judah.battleship.util;

import java.io.PrintStream;
import java.util.List;

/**
 * This class contains methods 
 * that write strings to a PrintStream.
 * Each method flushes its output
 * prior to returning.
 */
public class PrintStreamUtils
{
    /**
     * Default constructor; not used.
     */
    private PrintStreamUtils()
    {
        // default constructor; not used
    }
    
    /**
     * Write a string 
     * to stdout without a line-separator,
     * and flush the stream before returning.
     * 
     * @param line      the given String
     */
    public static void printOut( String line )
    {
        printOut( System.out, line );
    }
    
    /**
     * Write a string 
     * to a given PrintStream without a line-separator,
     * and flush the stream before returning.
     * 
     * @param stream    the given PrintStream
     * @param line      the given String
     */
    public static void printOut( PrintStream stream, String line )
    {
        stream.print( line );
        stream.flush();
    }
    
    /**
     * Write a string followed by a line-separator to stdout,
     * and flush stdout before returning.
     * 
     * @param line  the string to write
     */
    public static void writeOut( String line )
    {
        writeOut( System.out, line );
    }
    
    /**
     * Write a string followed by a line-separator
     * to a given PrintStream,
     * and flush the stream before returning.
     * 
     * @param stream    the given PrintStream
     * @param line      the given String
     */
    public static void writeOut( PrintStream stream, String line )
    {
        stream.println( line );
        stream.flush();
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
        writeOut( System.out, lines );
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
        writeOut( System.err, lines );
    }
    
    /**
     * Write each string in a list to the given output stream.
     * Each string is written on a separate line;
     * the output stream is flushed before returning.
     * 
     * @param str   the given output stream
     * @param lines the strings to write
     */
    public static void writeOut( 
        PrintStream str, 
        List<String> lines 
    )
    {
        lines.forEach( str::println );
        str.flush();
    }
}
