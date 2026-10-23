package com.acmemail.judah.battleship.util;

import static com.acmemail.judah.battleship.StatusMessages.ARE_YOU_SURE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import com.acmemail.judah.battleship.BattleshipException;

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

    
    /**
     * Prompt the operator with a yes/no "are you sure" message.
     * Read and interpret the operator's response:
     * return true if the string entered by the operator
     * starts with 'Y' or 'y,'
     * return false for anything else.
     * 
     * @param prompt    message to print before "are you sure?"
     * @param reader    source of input
     * @param outStream output destination
     * 
     * @return  true, if the operator replies "yes"     * 
     * 
     * @throws BattleshipException if an I/O error occurs
     */
    public static boolean areYouSure( 
        String prompt,
        BufferedReader reader,
        PrintStream outStream
    )
    {
        String  fullPrompt  = prompt + "; " + ARE_YOU_SURE;
        printOut( outStream, fullPrompt );
        String  reply       = readLine( reader ).trim().toUpperCase();
        boolean result      = reply.charAt( 0 ) == 'Y';
        String  status      = result ? "yes" : "no";
        writeOut( outStream, status );
        return result;
    }
    
    /**
     * Reads and returns a line of text
     * from the given input source.
     * I/O exceptions are caught
     * and recast as BattleshipExceptions.
     * 
     * @param reader    the given input source
     * 
     * @return  a line of text from the given input source
     * 
     * @throws BattleshipException if an I/O error occurs
     */
    public static String readLine( BufferedReader reader )
    {
        String  line;
        try
        {
            line = reader.readLine().trim();
        }
        catch ( IOException exc )
        {
            String  msg = "Unexpected error reading from console";
            throw new BattleshipException( msg, exc);
        }
        return line;
    }
}
