package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ResultTest
{
    @ParameterizedTest
    @ValueSource( booleans= { true, false } )
    public void testResultBoolean( boolean testVal )
    {
        Result  result  = new Result( testVal );
        assertEquals( testVal, result.success() );
        assertTrue( result.messages().isEmpty() );
    }

    @ParameterizedTest
    @ValueSource( booleans= { true, false } )
    public void testResultBooleanListOfStringNonNull( boolean testVal )
    {
        final List<String>  list    = List.of( "message 1", "message 2" );
        Result  result  = new Result( testVal, list );
        assertEquals( testVal, result.success() );
        assertEquals( list, result.messages() );
    }

    @ParameterizedTest
    @ValueSource( booleans= { true, false } )
    public void testResultBooleanListOfStringNull( boolean testVal )
    {
        Result          result  = new Result( testVal, null );
        List<String>    list    = result.messages();
        assertEquals( testVal, result.success() );
        assertNotNull( list );
        assertTrue( list.isEmpty() );
        
        List<String>    withNull    = Arrays.asList( "a", null );
        Class<NullPointerException> clazz   = NullPointerException.class;
        assertThrows( clazz, () -> new Result( testVal, withNull ) );
    }

    @Test
    public void testResultBooleanListOfString_defensiveCopy()
    {
        List<String>    source      = new ArrayList<>( List.of( "a", "b" ) );
        Result          result      = new Result( true, source );
        source.add( "c" );
        List<String>    messages    = result.messages();
        assertEquals( 2, messages.size() );
        assertFalse( messages.contains( "c" ) );
    }

    @Test
    public void testMessages_unmodifiable()
    {
        Result  result  = new Result( true, List.of( "a" ) );
        assertThrows(
            UnsupportedOperationException.class,
            () -> result.messages().add( "x" )
        );
    }
    
    /**
     * We're not trying to get a thorough validation of toString.
     * We have a test with minimal verification
     * that mainly gets code coverage on toString.
     */
    @Test
    public void testToString()
    {
        String          message = "A non-trivial message";
        boolean         success = true;
        List<String>    listA   = List.of( message );
        Result          result  = new Result( success, listA );
        String          str     = result.toString();
        assertTrue( str.contains( Boolean.toString( success ) ) );
        assertTrue( str.contains( message ) );
    }
    
    @Test
    public void testEqualsHash()
    {
        List<String>    listA   = List.of( "a" );
        List<String>    listB   = List.of( "b" );
        Result          result1 = new Result( true, listA );
        Result          result2 = new Result( true, listA );
        
        assertEquals( result1, result1 );
        assertNotEquals( result1, null );
        assertNotEquals( result1, new Object() );
        assertEquals( result1, result2 );
        assertEquals( result2, result1 );
        assertEquals( result1.hashCode(), result2.hashCode() );
        
        result2 = new Result( false, listA );
        assertNotEquals( result1, result2 );
        assertNotEquals( result2, result1 );
        
        result2 = new Result( true, listB );
        assertNotEquals( result1, result2 );
        assertNotEquals( result2, result1 );
        
        result2 = new Result( false, listB );
        assertNotEquals( result1, result2 );
        assertNotEquals( result2, result1 );
        
        Result emptyA = new Result( true );
        Result emptyB = new Result( true, null );
        Result emptyC = new Result( true, List.of() );
        assertEquals( emptyA, emptyB );
        assertEquals( emptyA, emptyC );
        assertEquals( emptyA.hashCode(), emptyB.hashCode() );
        assertEquals( emptyA.hashCode(), emptyC.hashCode() );
    }
}
