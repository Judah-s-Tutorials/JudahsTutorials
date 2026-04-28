package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CommandReaderTest
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
         * @param reader    the BufferedReader representing the input
         * 
         * @throws IOException  if an I/O error occurs
         */
        public abstract void accept( BufferedReader reader ) 
            throws IOException;
    }
    
    private static final String randStr     =
        "abc def g h ijk l mnopqr st uvw xyz " +
        "ABC DEF G H IJK L MNOPQR ST UVW XYZ";
    private static final int    randStrLen  = randStr.length();
    private static Random       randy;
    
    private List<ParsedCommand> expResults;
    private List<ParsedCommand> actResults;
    
    @BeforeEach
    public void beforeEach()
    {
        expResults = new ArrayList<>();
        actResults = new ArrayList<>();
        randy = new Random( 0 );
    }
    
    /**
     * Parse lines of the form "command" with no extraneous whitespace.
     */
    @Test
    public void testSimpleCommandWithoutArg()
    {
        List<String>    input   = 
            Stream.of( Command.END, Command.EXIT, Command.STEP )
                .map( c -> getExpResult( c, "", true ) )
                .map( p -> p.getCommandString() )
                .toList();
        ioTest( input, this::testSimpleCommand );
    }
    
    /**
     * Parse lines of the form "command arg" with no extraneous whitespace.
     */
    @Test
    public void testSimpleCommandWithArg()
    {
        List<String>    input   = 
            Stream.of( Command.END, Command.EXIT, Command.STEP )
                .map( c -> getExpResult( c, getArg(), true ) )
                .map( p -> p.getCommandString() + " " + p.getArgString() )
                .toList();
        ioTest( input, this::testSimpleCommand );
    }
    
    private void
    testSimpleCommand( BufferedReader reader ) throws IOException
    {
        CommandReader   cmdReader   = new CommandReader( reader );
        ParsedCommand   command     = cmdReader.nextCommand( null );
        while ( command.getCommand() != Command.NONE )
        {
            actResults.add( command );
            command = cmdReader.nextCommand( null );
        }
        assertEquals( expResults, actResults );
    }

    /**
     * Parse lines of the form "str arg"
     * where str is the shortcut for a command.
     * Each input line is paired with the ParsedCommand the reader
     * should produce; whitespace variants live on the input side.
     */
    @Test
    public void testShortcuts()
    {
        List<String>        input       = List.of(
            "x= xxx",
            "y= yyy",
            "X= XXX",
            "Y= YYY",
            "Y=  YYY",          // extra space between shortcut and arg
            "  Y=   YYY   ",    // leading/trailing whitespace on the line
            "x=",               // shortcut with empty arg
            "z= YYY"            // z= is not a shortcut
        );
        List<ParsedCommand> expected    = List.of(
            new ParsedCommand( Command.XEQUALS, "x=", "xxx" ),
            new ParsedCommand( Command.YEQUALS, "y=", "yyy" ),
            new ParsedCommand( Command.XEQUALS, "X=", "XXX" ),
            new ParsedCommand( Command.YEQUALS, "Y=", "YYY" ),
            new ParsedCommand( Command.YEQUALS, "Y=", "YYY" ),
            new ParsedCommand( Command.YEQUALS, "Y=", "YYY" ),
            new ParsedCommand( Command.XEQUALS, "x=", "" ),
            new ParsedCommand( Command.INVALID, "z=", "YYY" )
        );
        expResults.addAll( expected );
        ioTest( input, this::testSimpleCommand );
    }
    
    @Test
    public void testShortcutNoSpace()
    {
        List<String>    input           = List.of( "x=xxx" );
        ParsedCommand   parsedCommand   = 
            new ParsedCommand( Command.XEQUALS, "x=", "xxx" );
        expResults.add( parsedCommand );
        ioTest( input, this::testSimpleCommand );
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
        ioTest( input, this::testSimpleCommand );
    }
    
    /**
     * Test input consisting of only empty lines and comments.
     */
    @Test
    public void testEmptyLinesAndComments()
    {
        List<String>    input   = 
            Stream.of( 
                "",
                "     ",
                "#",
                "     #     ",
                " # this is a comment"
                )
            .toList();
        ioTest( input, this::testEmptyLinesAndComments );
    }
    
    private void testEmptyLinesAndComments( BufferedReader reader ) 
        throws IOException
    {
        CommandReader   cmdReader   = new CommandReader( reader );
        ParsedCommand   command     = cmdReader.nextCommand( null );
        assertEquals( Command.NONE, command.getCommand() );
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
        CommandReader   cmdReader   = new CommandReader( reader );
        ParsedCommand   command     = cmdReader.nextCommand( null );
        assertEquals( Command.NONE, command.getCommand() );
    }
    
    /**
     * Test a mixture of lines 
     * representing concrete, valid commands 
     * with lines containing empty strings,
     * comments and invalid commands.
     */
    @Test
    public void testMixAndMatch()
    {
        // Mix lines representing concrete, valid commands with lines
        // representing comments, empty lines and invalid arguments.
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
        CommandReader   cmdReader   = new CommandReader( reader );
        ParsedCommand   command     = cmdReader.nextCommand( null );
        while ( command.getCommand() != Command.NONE )
        {
            if ( command.getCommand() != Command.INVALID )
                actResults.add( command );
            command = cmdReader.nextCommand( null );
        }
        assertEquals( expResults, actResults );
    }

    @Test
    public void testStream()
    {
        expResults =
            List.of( 
                new ParsedCommand( Command.END, "end", "endarg" ),
                new ParsedCommand( Command.START, "start", "startarg" ),
                new ParsedCommand( Command.STEP, "step", "steparg" ),
                new ParsedCommand( Command.EQUATION, "equation", "" ),
                new ParsedCommand( Command.YPLOT, "yplot", "" )
            );
        
        // Mix lines representing concrete, valid commands with lines
        // representing comments and empty lines.
        // Don't add NONE command to input; this is produced automatically
        // by CommandReader.stream().
        List<String>    input   = 
            expResults.stream()
                .map( pc -> 
                    "  " + pc.getCommandString() 
                    + "   " + pc.getArgString() 
                    + "   "
                )
                .flatMap( s ->
                    Stream.of( s, "", "#", "  #  " )
                )
                .toList();
        ioTest( input, this::readStream );
    }

    @Test
    public void testEmptyStream()
    {
        ioTest( new ArrayList<String>(), this::readStream );
    }

    /**
     * Verify that nextCommand writes the given prompt to stdout
     * each time it reads a line from its source.
     */
    @Test
    public void testNextCommandWithPrompt()
    {
        List<String>    input   = List.of( "", "# comment", "end" );
        ioTest( input, this::readWithPrompt );
    }

    private void readWithPrompt( BufferedReader reader ) throws IOException
    {
        final String            prompt      = "cmd> ";
        PrintStream             saveOut     = System.out;
        ByteArrayOutputStream   newOut      = new ByteArrayOutputStream();
        System.setOut( new PrintStream( newOut ) );
        try
        {
            CommandReader   cmdReader   = new CommandReader( reader );
            ParsedCommand   command     = cmdReader.nextCommand( prompt );
            assertEquals( Command.END, command.getCommand() );
        }
        finally
        {
            System.setOut( saveOut );
        }
        // Prompt is written once per readLine attempt, so the two
        // skipped lines (blank, comment) plus the "end" line produce
        // three prompts.
        assertEquals( prompt + prompt + prompt, newOut.toString() );
    }

    @ParameterizedTest
    @ValueSource( strings = {"END", "SET", "START"} )
    public void testParseCommandStringWithArg( String str )
    {
        Command         command     = Command.valueOf( str );
        String          commandStr  = str.toLowerCase();
        String          arg         = "argument";
        String          line        = commandStr + "   " + arg;
        ParsedCommand   parsed      = CommandReader.parseCommand( line );
        
        assertEquals( command, parsed.getCommand() );
        assertEquals( commandStr, parsed.getCommandString() );
        assertEquals( arg, parsed.getArgString() );
    }
    
    @ParameterizedTest
    @ValueSource( strings = {"END", "SET", "START"} )
    public void testParseCommandStringWithoutArg( String str )
    {
        Command         command     = Command.valueOf( str );
        String          commandStr  = str.toLowerCase();
        ParsedCommand   parsed      = CommandReader.parseCommand( commandStr );
        
        assertEquals( command, parsed.getCommand() );
        assertEquals( commandStr, parsed.getCommandString() );
        assertTrue( parsed.getArgString().isEmpty() );
    }
    
    @ParameterizedTest
    @ValueSource( strings = {"bad1", "bad2", "bad3"} )
    public void 
    testParseCommandStringInvalidCommandWithArg( String commandStr )
    {
        String          arg         = "argument";
        String          line        = commandStr + "  " + arg;
        ParsedCommand   parsed      = CommandReader.parseCommand( line );
        
        assertEquals( Command.INVALID, parsed.getCommand() );
        assertEquals( commandStr, parsed.getCommandString() );
        assertEquals( arg, parsed.getArgString() );
    }
    
    @ParameterizedTest
    @ValueSource( strings = {"bad1", "bad2", "bad3"} )
    public void 
    testParseCommandStringInvalidCommandWithoutArg( String commandStr )
    {
        ParsedCommand   parsed = CommandReader.parseCommand( commandStr );
        
        assertEquals( Command.INVALID, parsed.getCommand() );
        assertEquals( commandStr, parsed.getCommandString() );
        assertTrue( parsed.getArgString().isEmpty() );
    }
    
    private void readStream( BufferedReader reader ) throws IOException
    {
        CommandReader   cmdReader   = new CommandReader( reader );
        actResults = cmdReader.stream().toList();
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
    private static byte[] getByteBuffer( List<String> list )
    {
        byte[]  bytes   = null;
        try (
            ByteArrayOutputStream   baoStream   = new ByteArrayOutputStream();
            PrintWriter             writer      = new PrintWriter( baoStream );
        )
        {
            list.forEach( writer::println );
            writer.flush();
            bytes = baoStream.toByteArray();
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
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
    private static void ioTest( List<String> list, IOConsumer tester )
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
    private static void ioTest( byte[] buff, IOConsumer tester )
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
    private static String getArg()
    {
        int     start   = randy.nextInt( randStrLen - 5 );
        int     end     = randy.nextInt( randStrLen - start ) + start + 1;
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
