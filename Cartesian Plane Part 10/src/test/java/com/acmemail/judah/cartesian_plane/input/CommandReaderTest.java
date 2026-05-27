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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class CommandReaderTest
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
    
    private final PrintStream   saveOut     = System.out;
    
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
    
    @AfterEach
    public void afterEach()
    {
        System.setOut( saveOut );
    }
    
    /**
     * Parse lines of the form "command" with no extraneous whitespace.
     */
    @Test
    public void testSimpleCommandWithoutArg()
    {
        List<String>    input   = 
            Stream.of( Command.END, Command.EXIT, Command.STEP )
                .map( c -> addExpResult( c, "" ) )
                .map( p -> p.getCommandString() )
                .collect( Collectors.toList() );
        ioTest( input, this::testSimpleCommand );
    }
    
    private void testSimpleCommand( BufferedReader reader ) 
        throws IOException
    {
        CommandReader cmdReader = new CommandReader( reader );
        ParsedCommand command   = cmdReader.nextCommand( null );
        while ( command.getCommand() != Command.NONE )
        {
            actResults.add( command );
            command = cmdReader.nextCommand( null );
        }
        assertEquals( expResults, actResults );
    }
    
    /**
     * Parse lines of the form "command arg" with no extraneous whitespace.
     */
    @Test
    public void testSimpleCommandWithArg()
    {
        List<String>    input   = 
            Stream.of( Command.END, Command.EXIT, Command.STEP )
                .map( c -> addExpResult( c, getArg() ) )
                .map( p -> p.getCommandString() + " " + p.getArgString() )
                .collect( Collectors.toList() );
        ioTest( input, this::testSimpleCommand );
    }
    
    /**
     * Parse lines of the form "str arg" 
     * where str is the shortcut for a command.
     */
    @Test
    public void testShortcuts()
    {
        expResults.add( new ParsedCommand( Command.XEQUALS, "x=", "xxx" ) );
        expResults.add( new ParsedCommand( Command.YEQUALS, "y=", "yyy" ) );
        expResults.add( new ParsedCommand( Command.TEQUALS, "t=", "ttt" ) );
        expResults.add( new ParsedCommand( Command.REQUALS, "r=", "rrr" ) );
        expResults.add( new ParsedCommand( Command.XEQUALS, "X=", "XXX" ) );
        expResults.add( new ParsedCommand( Command.YEQUALS, "Y=", "YYY" ) );
        expResults.add( new ParsedCommand( Command.TEQUALS, "T=", "TTT" ) );
        expResults.add( new ParsedCommand( Command.REQUALS, "R=", "RRR" ) );
        List<String>    input   = 
            expResults.stream()
                .map( p -> p.getCommandString() + " " + p.getArgString() )
                .collect( Collectors.toList() );
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
                .map( c -> addExpResult( c, getArg() ) )
                .map( p -> 
                    "   " + p.getCommandString() + 
                    "   " + p.getArgString() +
                    "   "
                )
                .collect( Collectors.toList() );
        ioTest( input, this::testSimpleCommand );
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
                .collect( Collectors.toList() );
        ioTest( input, this::testEmptyLinesAndComments );
    }
    
    private void testEmptyLinesAndComments( BufferedReader reader ) throws IOException
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
    
    private void testEmptyInputStream( BufferedReader reader ) 
        throws IOException
    {
        CommandReader   cmdReader   = new CommandReader( reader );
        ParsedCommand   command     = cmdReader.nextCommand( null );
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
        // representing comments, empty lines and invalid commands.
        List<String>    input   = 
            Stream.of( Command.END, Command.EXIT, Command.STEP )
                .map( c -> addExpResult( c, getArg() ) )
                .map( p -> p.getCommandString() + " " + p.getArgString() )
                .flatMap( s ->
                    Stream.of( s, "", "#", "  #  ", "$BadCommand" )
                )
                .collect( Collectors.toList() );
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

    /**
     * Feed some valid commands to CommandReader
     * and make sure that the stream method
     * processes them all.
     */
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
        List<String>    input   = 
            expResults.stream()
                .map( pc -> "  " + pc.getCommandString() + "   " + pc.getArgString() + "   " )
                .flatMap( s ->
                    Stream.of( s, "", "#", "  #  " )
                )
                .collect( Collectors.toList() );
        ioTest( input, this::testStream );
    }

    /**
     * Feed some valid and some invalid commands to
     * CommandReader, and verify that the stream() method
     * processes them correctly.
     */
    @Test
    public void testStreamWithInvalidCommands()
    {
        expResults =
            List.of( 
                new ParsedCommand( Command.END, "end", "endarg" ),
                new ParsedCommand( Command.INVALID, "eh", "ehArg" ),
                new ParsedCommand( Command.START, "start", "startarg" ),
                new ParsedCommand( Command.INVALID, "mmm", "" ),
                new ParsedCommand( Command.STEP, "step", "steparg" ),
                new ParsedCommand( Command.EQUATION, "equation", "" ),
                new ParsedCommand( Command.INVALID, "uhoh", "a" ),
                new ParsedCommand( Command.YPLOT, "yplot", "" )
            );
        
        // Mix lines representing concrete, valid commands with lines
        // representing comments and empty lines.
        List<String>    input   = 
            expResults.stream()
                .map( pc -> "  " + pc.getCommandString() + "   " + pc.getArgString() + "   " )
                .flatMap( s ->
                    Stream.of( s, "", "#", "  #  " )
                )
                .collect( Collectors.toList() );
        ioTest( input, this::testStream );
    }

    /**
     * Feed the CommandReader an empty list of lines to process,
     * make sure it doesn't break the stream
     */
    @Test
    public void testEmptyStream()
    {
        ioTest( new ArrayList<String>(), this::testStream );
    }
    
    /**
     * Test parser with valid commands
     * that have no intrinsic arguments.
     * 
     * @param str   value-source parameter
     */
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
    
    /**
     * Test parser with valid commands
     * of the form "command argument"
     * 
     * @param str   value-source parameter
     */
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
    
    /**
     * Test parser using commands with liberal whitespace,
     * include whitespace in the argument.
     */
    @Test
    public void testParseCommandStringWithArgWhiteSpace()
    {
        // One more test with: a) liberal whitespace including tabs, and
        // b) an argument containing spaces
        Command         expCommand      = Command.SET;
        String          expCommandStr   = expCommand.toString();
        String          expArg          = "a  b\t\tc";
        String          line            =
            "\t\t " + expCommandStr + "\t\t " + expArg + " \t\t";
        ParsedCommand   parsed      = CommandReader.parseCommand( line );

        assertEquals( expCommand, parsed.getCommand() );
        assertEquals( expCommandStr, parsed.getCommandString() );
        assertEquals( expArg, parsed.getArgString() );
    }
    
    /**
     * Make sure parser returns NONE 
     * for null and empty lines.
     */
    @Test
    public void testParseCommandStringNone()
    {
        // Can't be a parameterized test because @ValueSource
        // won't allow null.
        String[]        vals        = 
        { 
            null,         // null string
            "",           // empty string
            "    ",       // blank string
        };
        for ( String line : vals )
        {
            String          feedback    = "\"" + line + "\"";
            ParsedCommand   parsed      = CommandReader.parseCommand( line );
            assertEquals( Command.NONE, parsed.getCommand(), feedback );
        }
    }
    
    private void testStream( BufferedReader reader ) throws IOException
    {
        CommandReader   cmdReader   = new CommandReader( reader );
        actResults = cmdReader.stream().collect( Collectors.toList() );
        assertEquals( expResults, actResults );
    }
    
    /**
     * Make sure parser returns INVALID 
     * for unrecognized command strings.
     */
    @ParameterizedTest
    @ValueSource( strings = {"bad1", "bad2", "bad3"} )
    public void testParseCommandStringInvalidCommand( String commandStr )
    {
        // With argument
        String          arg         = "argument";
        String          line        = commandStr + "  " + arg;
        ParsedCommand   parsed      = CommandReader.parseCommand( line );
        
        assertEquals( Command.INVALID, parsed.getCommand() );
        assertEquals( commandStr, parsed.getCommandString() );
        assertEquals( arg, parsed.getArgString() );
        
        // Without argument
        parsed = CommandReader.parseCommand( commandStr );
        
        assertEquals( Command.INVALID, parsed.getCommand() );
        assertEquals( commandStr, parsed.getCommandString() );
        assertTrue( parsed.getArgString().isEmpty() );
    }
    
    /**
     * Test nextCommand when it's supposed to print a prompt.
     */
    @Test
    public void testPrompt()
    {
        Command         command     = Command.START;
        String          arg         = "argument";
        List<String>    input       = List.of( command + "  " + arg );
        ioTest( input, this::testPrompt );
        ioTest( input, this::testNoPrompt );
    }
    
    private void testPrompt( BufferedReader reader )
        throws IOException
    {
        ByteArrayOutputStream   outStr  = new ByteArrayOutputStream();
        PrintStream             tempOut = new PrintStream( outStr );
        System.setOut( tempOut );
        
        String          expOut      = "prompt";
        CommandReader   cmdReader   = new CommandReader( reader );
        cmdReader.nextCommand( expOut );
        outStr.close();
        tempOut.close();
        String          actOut      = outStr.toString();
        assertEquals( expOut, actOut );
    }
    
    private void testNoPrompt( BufferedReader reader )
        throws IOException
    {
        ByteArrayOutputStream   outStr  = new ByteArrayOutputStream();
        PrintStream             tempOut = new PrintStream( outStr );
        System.setOut( tempOut );
        
        CommandReader   cmdReader   = new CommandReader( reader );
        cmdReader.nextCommand( null );
        outStr.close();
        tempOut.close();
        String          actOut      = outStr.toString();
        assertTrue( actOut.isEmpty() );
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
     * in the form of a byte array.
     * Transforms a byte array into an input stream
     * in the form of a BufferedReader.
     * The byte buffer is assumed to contain
     * only valid Unicode characters,
     * divided into lines using
     * the appropriate line separator.
     * Uses the Buffered reader
     * to execute a given consumer.
     * 
     * @param list      list of strings to store in byte buffer
     * @param tester    the given consumer
     * 
     * @see #getByteBuffer(List)
     */
    private void ioTest( List<String> list, IOConsumer tester )
    {
        byte[]  buff    = getByteBuffer( list );
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
     * The generated object is added to the expResults list
     * and returned.
     * 
     * @param cmd           the given command
     * @param arg           the given argument
     *                      
     * @return  the generated object
     */
    private ParsedCommand addExpResult( Command cmd, String arg )
    {
        ParsedCommand   pCmd    =
            new ParsedCommand( cmd, cmd.name(), arg.trim() );
        expResults.add( pCmd );
        return pCmd;
    }
}
