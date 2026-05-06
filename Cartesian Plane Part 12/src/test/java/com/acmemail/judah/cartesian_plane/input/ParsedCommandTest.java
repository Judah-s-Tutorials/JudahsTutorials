package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ParsedCommandTest
{
    @Test
    public void testCtorNonNullArgs()
    {
        Command command = Command.END;
        String  cmdStr  = "command";
        String  argStr  = "this is an argument";
        ParsedCommand   parsedCommand   = 
            new ParsedCommand( command, cmdStr, argStr );
        assertEquals( command, parsedCommand.getCommand() );
        assertEquals( cmdStr, parsedCommand.getCommandString() );
        assertEquals( argStr, parsedCommand.getArgString() );
    }
    
    @Test
    public void testCtorNullArgs()
    {
        // Null strings must be converted to empty strings
        Command command = Command.END;
        String  cmdStr  = "command";
        String  argStr  = "this is an argument";
        ParsedCommand   parsedCommand   = 
            new ParsedCommand( command, null, null );
        assertNotNull( parsedCommand.getCommandString() );
        assertTrue( parsedCommand.getCommandString().isEmpty() );
        assertNotNull( parsedCommand.getArgString() );
        assertTrue( parsedCommand.getArgString().isEmpty() );
        
        parsedCommand   = new ParsedCommand( command, cmdStr, null );
        assertEquals( cmdStr, parsedCommand.getCommandString() );
        assertNotNull( parsedCommand.getArgString() );
        assertTrue( parsedCommand.getArgString().isEmpty() );
        
        parsedCommand   = new ParsedCommand( command, null, argStr );
        assertNotNull( parsedCommand.getCommandString() );
        assertTrue( parsedCommand.getCommandString().isEmpty() );
        assertEquals( argStr, parsedCommand.getArgString() );
    }
    
    @Test
    public void testCtorInvalidNullCommand()
    {
        // Null not permitted for command argument
        assertThrows( NullPointerException.class, () -> 
            new ParsedCommand( null, "", "" )
        );
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
    
    /**
     * This method asserts that, given the test data,
     * unequal objects have unequal hashcodes.
     * THIS IS NOT REQUIRED BY THE JAVA SPECIFICATION.
     * The justification for testing this is that,
     * given a small data set,
     * if unequal objects generate the same hashcodes
     * there's probably something wrong 
     * with the hashcode algorithm,
     * and that could have significant negative effects
     * on operations that rely on hashing.
     * (In the context of hash tables, 
     * for instance,
     * it would generate a lot of collisions.)
     */
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
        assertEquals( pCmdB, pCmdA );
        assertEquals( pCmdA.hashCode(), pCmdB.hashCode() );
        
        pCmdB = new ParsedCommand( cmd2, cmdStr1, argStr1 );
        assertNotEquals( pCmdA, pCmdB );
        assertNotEquals( pCmdB, pCmdA );
        assertHashcodeInequality( pCmdA, pCmdB );
        
        pCmdB = new ParsedCommand( cmd1, cmdStr2, argStr1 );
        assertNotEquals( pCmdA, pCmdB );
        assertNotEquals( pCmdB, pCmdA );
        assertHashcodeInequality( pCmdA, pCmdB );
        
        pCmdB = new ParsedCommand( cmd1, cmdStr1, argStr2 );
        assertNotEquals( pCmdA, pCmdB );
        assertNotEquals( pCmdB, pCmdA );
        assertHashcodeInequality( pCmdA, pCmdB );
        
        // Make sure that objects created from NULL strings and
        // objects created from empty strings test equal
        ParsedCommand   withNulls   = new ParsedCommand( cmd1, null, null );
        ParsedCommand   withEmpties = new ParsedCommand( cmd1, "", "" );
        assertEquals( withNulls, withEmpties );
        assertEquals( withNulls.hashCode(), withEmpties.hashCode() );
    }
    
    private static void assertHashcodeInequality( Object objA, Object objB )
    {
        final String    equalsMessage   =
            "Input objects are required to be unequal";
        final String    hashMessage     =
            "Unequal objects have equal hashcodes; "
            + "hash algorithm should be investigated.";
        assertNotEquals( objA, objB, equalsMessage );
        assertNotEquals( objA.hashCode(), objB.hashCode(), hashMessage );
    }
}
