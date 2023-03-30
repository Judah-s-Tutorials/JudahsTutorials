package com.acmemail.judah;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    
    @Test
    void testSimpleCommandWithoutArg()
    {
        List<String>    input   = 
            Stream.of( Command.END, Command.EXIT, Command.INCREMENT )
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
    
    @Test
    void testSimpleCommandWithArg()
    {
        List<String>    input   = 
            Stream.of( Command.END, Command.EXIT, Command.INCREMENT )
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
    
    @Test
    void testLeadingTrailingSpaces()
    {
        List<String>    input   = 
            Stream.of( Command.END, Command.EXIT, Command.INCREMENT )
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
    
    private void ioTest( List<String> list, IOConsumer tester )
    {
        byte[]  bytes   = getByteBuffer( list );
        ioTest( bytes, tester );
    }
    
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
    
    private String getArg()
    {
        int     start   = randy.nextInt( randStrLen - 5 );
        int     end     = randy.nextInt( randStrLen - start ) + start;
        String  arg     = randStr.substring( start, end );
        return arg;
    }
    
    private ParsedCommand 
    getExpResult( Command cmd, String arg, boolean addToExpList )
    {
        ParsedCommand   pCmd    =
            new ParsedCommand( cmd, cmd.name(), arg.trim() );
        if ( addToExpList )
            expResults.add( pCmd );
        return pCmd;
    }
    
    @FunctionalInterface
    private interface IOConsumer
    {
        public abstract void accept( BufferedReader reader ) 
            throws IOException;
    }
}
