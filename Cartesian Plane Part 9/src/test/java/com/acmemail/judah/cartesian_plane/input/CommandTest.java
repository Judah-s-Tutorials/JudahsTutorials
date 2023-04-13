package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

class CommandTest
{
    @Test
    void testGetDescription()
    {
        // I'm just going to make sure that the description
        // for each command is present.
        Arrays.stream( Command.values() )
            .forEach( c -> {
                String  desc    = c.getDescription();
                String  name    = c.name();
                assertNotNull( desc, name );
                assertFalse( desc.isEmpty(), name );
            });
    }

    @Test
    void testToCommand()
    {
        Arrays.stream( Command.values() )
            .forEach( c -> {
                String  text    = c.name();
                assertEquals( c, Command.toCommand( text.toLowerCase() ) );
                assertEquals( c, Command.toCommand( text.toUpperCase() ) );
            });
        
        // verify that an empty string maps to NONE
        assertEquals( Command.NONE, Command.toCommand( "" ) );
        // verify that an unrecognized command maps to INVALID
        assertEquals( Command.INVALID, Command.toCommand( "not-a-command" ) );
    }

    @Test
    void testUsage()
    {
        String  usage   = Command.usage();
        assertNotNull( usage );
        assertFalse( usage.isEmpty() );
    }
}
