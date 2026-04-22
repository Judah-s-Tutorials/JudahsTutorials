package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ResultTest
{
    @ParameterizedTest
    @ValueSource( booleans = { true, false } )
    void testResultBoolean( boolean testVal )
    {
        Result  result  = new Result( testVal );
        assertEquals( testVal, result.isSuccess() );
        assertTrue( result.getMessages().isEmpty() );
    }

    @ParameterizedTest
    @ValueSource( booleans = { true, false } )
    void testResultBooleanListOfString( boolean testVal )
    {
        final List<String>  list    = List.of( "message 1", "message 2" );
        Result  result  = new Result( testVal, list );
        assertEquals( testVal, result.isSuccess() );
        assertEquals( list, result.getMessages() );
    }

    @ParameterizedTest
    @ValueSource( booleans = { true, false } )
    void testResultBooleanListOfString_nullMessages( boolean testVal )
    {
        Result  result  = new Result( testVal, null );
        assertEquals( testVal, result.isSuccess() );
        assertTrue( result.getMessages().isEmpty() );
    }

    @Test
    void testResultBooleanListOfString_defensiveCopy()
    {
        List<String>    source      = new ArrayList<>( List.of( "a", "b" ) );
        Result          result      = new Result( true, source );
        source.add( "c" );
        List<String>    messages    = result.getMessages();
        assertEquals( 2, messages.size() );
        assertFalse( messages.contains( "c" ) );
    }

    @Test
    void testGetMessages_unmodifiable()
    {
        Result  result  = new Result( true, List.of( "a" ) );
        assertThrows(
            UnsupportedOperationException.class,
            () -> result.getMessages().add( "x" )
        );
    }
}
