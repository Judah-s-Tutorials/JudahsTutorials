package com.acmemail.judah.cartesian_plane.exp4j;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.input.Command;
import com.acmemail.judah.cartesian_plane.input.ParsedCommand;

class ParsedCommandTest
{
    @Test
    void test()
    {
        Command command = Command.END;
        String  cmdStr  = "command";
        String  argStr  = "this is an argument";
        ParsedCommand   parsedCommand   = 
            new ParsedCommand( command, cmdStr, argStr );
        assertEquals( command, parsedCommand.getCommand() );
        assertEquals( cmdStr, parsedCommand.getCommandString() );
        assertEquals( argStr, parsedCommand.getArgString() );
        
        // make sure null values are correctly converted to empty strings
        parsedCommand   = new ParsedCommand( command, null, null );
        assertNotNull( parsedCommand.getCommandString() );
        assertTrue( parsedCommand.getCommandString().isEmpty() );
        assertNotNull( parsedCommand.getArgString() );
        assertTrue( parsedCommand.getArgString().isEmpty() );
    }
    
    @Test
    public void testToString()
    {
        Command command = Command.END;
        String  cmdStr  = "command";
        String  argStr  = "this is an argument";
        ParsedCommand   parsedCommand   = 
            new ParsedCommand( command, cmdStr, argStr );
        String  str     = parsedCommand.toString();
        assertTrue( str.contains( command.name() ) );
        assertTrue( str.contains( cmdStr ) );
        assertTrue( str.contains( argStr ) );
    }
    
    @Test
    public void testEqualsHash()
    {
        Command cmd1    = Command.END;
        Command cmd2    = Command.EXIT;
        String  cmdStr1 = "this command string";
        String  cmdStr2 = "other command string";
        String  argStr1 = "this arg string";
        String  argStr2 = "other arg string";
        
        ParsedCommand   pCmdA   = new ParsedCommand( cmd1, cmdStr1, argStr1 );
        ParsedCommand   pCmdB   = new ParsedCommand( cmd1, cmdStr1, argStr1 );
        
        assertEquals( pCmdA, pCmdA );
        assertNotEquals( pCmdA, new Object() );
        
        assertNotEquals( pCmdA, null );
        assertEquals( pCmdA, pCmdB );
        assertEquals( pCmdA.hashCode(), pCmdB.hashCode() );
        
        pCmdB = new ParsedCommand( cmd2, cmdStr1, argStr1 );
        assertNotEquals( pCmdA, pCmdB );
        assertNotEquals( pCmdB, pCmdA );
        
        pCmdB = new ParsedCommand( cmd1, cmdStr2, argStr1 );
        assertNotEquals( pCmdA, pCmdB );
        assertNotEquals( pCmdB, pCmdA );
        
        pCmdB = new ParsedCommand( cmd1, cmdStr1, argStr2 );
        assertNotEquals( pCmdA, pCmdB );
        assertNotEquals( pCmdB, pCmdA );
    }
}
