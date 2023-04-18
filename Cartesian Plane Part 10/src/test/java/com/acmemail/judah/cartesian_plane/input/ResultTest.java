package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ResultTest
{
    @ParameterizedTest
    @ValueSource( booleans= { true, false } )
    void testResultBoolean( boolean testVal )
    {
        Result  result  = new Result( testVal );
        assertEquals( testVal, result.isSuccess() );
    }

    @ParameterizedTest
    @ValueSource( booleans= { true, false } )
    void testResultBooleanListOfString( boolean testVal )
    {
        final List<String>  list    = List.of( "message 1", "message 2" );
        Result  result  = new Result( testVal, list );
        assertEquals( testVal, result.isSuccess() );
        assertEquals( list, result.getMessages() );
        
        result  = new Result( testVal, null );
        assertEquals( testVal, result.isSuccess() );
        assertNotNull( result.getMessages() );
        assertTrue( result.getMessages().isEmpty() );

    }
}
