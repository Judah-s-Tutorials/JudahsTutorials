package com.acmemail.judah;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommandStringReaderTest
{
    /**
     * Defines a functional interface
     * that accepts a command of type BufferedReader
     * and returns nothing.
     * This is essentially a Consumer<BufferedReader>
     * except that the abstract method, accept
     * may throw an IOException.
     * 
     * @author Jack Straub
     */
    @FunctionalInterface
    private interface IOConsumer
    {
        /**
         * Executes a test
         * using a BufferedReader for input.
         * 
         * @param reader    the BufferedReader representing the intpu
         * 
         * @throws IOException  if an I/O error occurs
         */
        public abstract void accept( BufferedReader reader ) 
            throws IOException;
    }
    
    private final String        randStr     =
        "abc def g h ijk l mnopqr st uvw xyz " +
        "ABC DEF G H IJK L MNOPQR ST UVW XYZ";
    private final int           randStrLen  = randStr.length();
    private final Random        randy       = new Random( 0 );
    
    private List<ParsedCommand> expResults;
    private List<ParsedCommand> actResults;
    
    @BeforeEach
    public void beforeEach()
    {
        expResults = new ArrayList<>();
        actResults = new ArrayList<>();
    }
    
    /**
     * Parse lines of the form "command arg" with no extraneous whitespace.
     */
    @Test
    void testSimpleCommandWithoutArg()
    {
        List<String>    input   = 
            Stream.of( Command.END, Command.EXIT, Command.STEP )
                .map( c -> getExpResult( c, "", true ) )
                .map( p -> p.getCommandString() + " " + p.getArgString() )
                .toList();
        ioTest( input, this::testSimpleCommandWithoutArg );
    }
    
    private void testSimpleCommandWithoutArg( BufferedReader reader ) throws IOException
    {
        CommandStringReader cmdReader   = new CommandStringReader( reader );
        ParsedCommand       command     = cmdReader.nextCommand();
        while ( command.getCommand() != Command.NONE )
        {
            actResults.add( command );
            command = cmdReader.nextCommand();
        }
        assertEquals( expResults, actResults );
    }
    
    /**
     * Parse lines of the form "command" with no extraneous whitespace.
     */
    @Test
    public void testSimpleCommandWithArg()
    {
        List<String>    input   = 
            Stream.of( Command.END, Command.EXIT, Command.STEP )
                .map( c -> getExpResult( c, getArg(), true ) )
                .map( p -> p.getCommandString() + " " + p.getArgString() )
                .toList();
        ioTest( input, this::testSimpleCommandWithArg );
    }
    
    private void testSimpleCommandWithArg( BufferedReader reader ) throws IOException
    {
        CommandStringReader cmdReader   = new CommandStringReader( reader );
        ParsedCommand       command     = cmdReader.nextCommand();
        while ( command.getCommand() != Command.NONE )
        {
            actResults.add( command );
            command = cmdReader.nextCommand();
        }
        assertEquals( expResults, actResults );
    }
    
    /**
     * Parse lines of the form " command  arg " 
     * with leading and trailing, and with extra whitespace
     * between "command" and "arg". 
     */
    @Test
    public void testLeadingTrailingSpaces()
    {
        List<String>    input   = 
            Stream.of( Command.END, Command.EXIT, Command.STEP )
                .map( c -> getExpResult( c, getArg(), true ) )
                .map( p -> 
                    "   " + p.getCommandString() + 
                    "   " + p.getArgString() +
                    "   "
                )
                .toList();
        ioTest( input, this::testLeadingTrailingSpaces );
    }
    
    private void testLeadingTrailingSpaces( BufferedReader reader ) throws IOException
    {
        CommandStringReader cmdReader   = new CommandStringReader( reader );
        ParsedCommand       command     = cmdReader.nextCommand();
        while ( command.getCommand() != Command.NONE )
        {
            actResults.add( command );
            command = cmdReader.nextCommand();
        }
        assertEquals( expResults, actResults );
    }
    
    /**
     * Test input consisting of only empty lines and comments.
     */
    @Test
    public void testEmptyLinesAndComments()
    {
        // Test input consisting of only empty lines and comments. 
        List<String>    input   = 
            Stream.of( 
                "",
                "     ",
                "#",
                "     #     "
                )
                .toList();
        ioTest( input, this::testEmptyLinesAndComments );
    }
    
    private void testEmptyLinesAndComments( BufferedReader reader ) throws IOException
    {
        CommandStringReader cmdReader   = new CommandStringReader( reader );
        ParsedCommand       command     = cmdReader.nextCommand();
        assert( command.getCommand() == Command.NONE );
        assertTrue( actResults.isEmpty() );
    }
    
    /**
     * Test against an empty input stream.
     */
    @Test
    public void testEmptyInputStream()
    {
        ioTest( new ArrayList<String>(), this::testEmptyInputStream );
    }
    
    private void testEmptyInputStream( BufferedReader reader ) throws IOException
    {
        CommandStringReader cmdReader   = new CommandStringReader( reader );
        ParsedCommand       command     = cmdReader.nextCommand();
        assertEquals( Command.NONE, command.getCommand() );
    }
    
    /**
     * Test a mixture lines 
     * representing concrete, valid commands 
     * with lines containing empty strings,
     * comments and invalid commands.
     */
    @Test
    public void testMixAndMatch()
    {
        // Mix lines representing concrete, valid commands with lines
        // representing comments, empty lines and in valid arguments.
        List<String>    input   = 
            Stream.of( Command.END, Command.EXIT, Command.STEP )
                .map( c -> getExpResult( c, getArg(), true ) )
                .map( p -> p.getCommandString() + " " + p.getArgString() )
                .flatMap( s ->
                    Stream.of( s, "", "#", "  #  ", "$BadCommand" )
                )
                .toList();
        ioTest( input, this::testMixAndMatch );
    }
    
    private void testMixAndMatch( BufferedReader reader ) throws IOException
    {
        CommandStringReader cmdReader   = new CommandStringReader( reader );
        ParsedCommand       command     = cmdReader.nextCommand();
        while ( command.getCommand() != Command.NONE )
        {
            if ( command.getCommand() != Command.INVALID )
                actResults.add( command );
            command = cmdReader.nextCommand();
        }
        assertEquals( expResults, actResults );
    }
    
    /**
     * Writes each string in a list into a byte buffer.
     * Each string is terminated with a line separator.
     * The effect is as if individual strings in a list
     * were written to the console with println,
     * except the output is redirected to a buffer.
     * The buffer, 
     * suitable for use in instantiating 
     * a ByteArrayInputStream,
     * is returned to the caller.
     *  
     * @param list  the list to write
     * 
     * @return  the byte array containing the output from the operation
     * 
     * @see #ioTest(byte[], IOConsumer)
     * @see #ioTest(List, IOConsumer)
     */
    private byte[] getByteBuffer( List<String> list )
    {
        byte[]  bytes   = null;
        try (
            ByteArrayOutputStream baoStream = new ByteArrayOutputStream();
            PrintWriter writer = new PrintWriter( baoStream );
        )
        {
            list.forEach( writer::println );
            writer.flush();
            bytes = baoStream.toByteArray();
        }
        catch ( IOException exc )
        {
            fail( "Unexpected I/O error", exc );
        }
        return bytes; 
    }
    
    /**
     * Generates sample input from a list
     * in the form of a BufferedReader.
     * Uses the Buffered reader
     * to execute a given consumer.
     * 
     * @param list      the list to convert to input
     * @param tester    the given consumer
     * 
     * @see #getByteBuffer(List)
     * @see #ioTest(byte[], IOConsumer)
     */
    private void ioTest( List<String> list, IOConsumer tester )
    {
        byte[]  bytes   = getByteBuffer( list );
        ioTest( bytes, tester );
    }
    
    
    /**
     * Transforms a byte buffer into an input stream
     * in the form of a BufferedReader.
     * The byte buffer is assumed to contain
     * only valid Unicode characters,
     * divided into lines using
     * the appropriate line separator.
     * Uses the Buffered reader
     * to execute a given consumer.
     * 
     * @param buff      the source byte buffer
     * @param tester    the given consumer
     * 
     * @see #getByteBuffer(List)
     * @see #ioTest(List, IOConsumer)
     */
    private void ioTest( byte[] buff, IOConsumer tester )
    {
        try (
            ByteArrayInputStream baiStream = new ByteArrayInputStream( buff );
            InputStreamReader strReader = new InputStreamReader( baiStream );
            BufferedReader bufReader = new BufferedReader( strReader );
        )
        {
            tester.accept( bufReader );
        }
        catch ( IOException exc )
        {
            fail( "Unexpected I/O error", exc );
        }
    }
    
    /**
     * Generates a string of random alphanumeric characters.
     * The string is guaranteed to be non-empty.
     * 
     * @return  string of random alphanumeric characters
     */
    private String getArg()
    {
        int     start   = randy.nextInt( randStrLen - 5 );
        int     end     = randy.nextInt( randStrLen - start ) + start;
        String  arg     = randStr.substring( start, end );
        return arg;
    }
    
    /**
     * Given a command and an argument,
     * create a ParsedCommand object
     * reflecting the given data.
     * The generated object is returned,
     * and optionally added
     * to the "expected results" list.
     * 
     * @param cmd           the given command
     * @param arg           the given argument
     * @param addToExpList  true to add the generated object
     *                      to the "expected results" list
     *                      
     * @return  the generated object
     */
    private ParsedCommand 
    getExpResult( Command cmd, String arg, boolean addToExpList )
    {
        ParsedCommand   pCmd    =
            new ParsedCommand( cmd, cmd.name(), arg.trim() );
        if ( addToExpList )
            expResults.add( pCmd );
        return pCmd;
    }
}
