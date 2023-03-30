package com.acmemail.judah;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

class CommandTest
{
    @Test
    void testGetAbbreviation()
    {
        Arrays.stream( Command.values() )
            .forEach( cmd -> {
                String  name    = cmd.name().toLowerCase();
                String  abbr    = cmd.getAbbreviation().toLowerCase();
                assertTrue( name.startsWith( abbr ) );
            });
    }

    @Test
    void testUniqueAbbreviation()
    {
        // verify that abbreviations for commands are unique
        List<String> abbreviations  =
            Arrays.stream( Command.values() )
                .map( Command::getAbbreviation )
                .map( s -> s.toLowerCase() )
                .map( String::toLowerCase )
                .collect( Collectors.toList());
        while ( !abbreviations.isEmpty() )
        {
            String  str = abbreviations.remove( abbreviations.size() - 1 );
            assertFalse( abbreviations.contains( str ) );
        }
    }

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
                String  abbr    = c.getAbbreviation();
                assertEquals( c, Command.toCommand( abbr.toLowerCase() ) );
                assertEquals( c, Command.toCommand( abbr.toUpperCase() ) );
            });
        assertEquals( Command.NONE, Command.toCommand( "" ) );
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
