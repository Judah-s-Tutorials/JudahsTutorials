package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class CommandTest
{
    @Test
    public void testGetDescription()
    {
        // I'm just going to make sure that the description
        // for each command is present.
        for ( Command cmd : Command.values() )
        {
            String  desc    = cmd.getDescription();
            String  name    = cmd.name();
            assertNotNull( desc, name );
            assertFalse( desc.isEmpty(), name );
        }
    }

    @Test
    public void testToCommand()
    {
        for ( Command c : Command.values() )
        {
            String  text    = c.name();
            String  lower   = text.toLowerCase();
            String  upper   = text.toUpperCase();
            String  mixed   = getMixedCaseName( c );
            assertEquals( c, Command.toCommand( lower ), lower );
            assertEquals( c, Command.toCommand( upper ), upper );
            assertEquals( c, Command.toCommand( mixed ), mixed );
        }
    
        // verify that an empty string maps to NONE; "isEmpty()" path
        assertEquals( Command.NONE, Command.toCommand( "" ) );
        // verify that an blank string maps to NONE; "isEmpty()" path
        assertEquals( Command.NONE, Command.toCommand( " " ) );
        // verify that an unrecognized command maps to INVALID
        assertEquals( Command.INVALID, Command.toCommand( "not-a-command" ) );
        // verify that passing null elicits exception
        Class<NullPointerException> clazz   = 
            NullPointerException.class;
        assertThrows( clazz, () -> Command.toCommand( null ) );
    }

    /**
     * Make a weak attempt to validate the usage message.
     * We'll try to verify that the message is sane,
     * that it seems to include all the commands
     * except NONE and INVALID.
     */
    @Test
    public void testUsage()
    {
        String  usage   = Command.usage();
        assertNotNull( usage );
        assertFalse( usage.isEmpty() );
        
        assertTrue( usage.startsWith( "Valid commands:" ) );
        assertFalse( usage.contains( Command.NONE.name() ) );
        assertFalse( usage.contains( Command.INVALID.name() ) );
        Arrays.stream( Command.values() )
            .filter( c -> c != Command.NONE )
            .filter( c -> c != Command.INVALID )
            .map( c -> c.toString() )
            .forEach( s -> assertTrue( usage.contains( s ), s ) );
    }
    
    private static String getMixedCaseName( Command command )
    {
        String          name    = command.toString();
        char[]          chars   = name.toCharArray();
        int             len     = chars.length;
        StringBuilder   bldr    = new StringBuilder();
        for ( int inx = 0 ; inx < len ; ++inx )
        {
            char    ccc = chars[inx];
            if ( inx % 2 == 0)
                bldr.append( ccc );
            else
                bldr.append( Character.toLowerCase( ccc ) );
        }
        String  result  = bldr.toString();
        return result;
    }
}
