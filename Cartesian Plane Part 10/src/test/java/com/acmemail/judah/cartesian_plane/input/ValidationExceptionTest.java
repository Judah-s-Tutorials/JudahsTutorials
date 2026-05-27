package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class ValidationExceptionTest
{
    @Test
    public void testValidationException()
    {
        Exception   exc = new ValidationException();
        assertNull( exc.getMessage() );
        assertNull( exc.getCause() );
    }

    @Test
    public void testValidationExceptionString()
    {
        String              str = "this is a message";
        ValidationException exc = new ValidationException( str );
        assertEquals( str, exc.getMessage() );
        assertNull( exc.getCause() );
    }

    @Test
    public void testValidationExceptionThrowable()
    {
        Exception           cause   = new IOException( "Error message" );
        ValidationException exc     = new ValidationException( cause );
        assertEquals( cause, exc.getCause() );
        assertEquals( cause.toString(), exc.getMessage() );
    }

    @Test
    public void testValidationExceptionStringThrowable()
    {
        String              str     = "this is a message";
        Exception           cause   = new Exception();
        ValidationException exc     = new ValidationException( str, cause );
        assertEquals( cause, exc.getCause() );
        assertEquals( str, exc.getMessage() );
    }
    
    @Test
    public void testIsRuntimeException()
    {
        // Verify this exception is unchecked.
        Exception   exc     = new ValidationException();
        assertInstanceOf( RuntimeException.class, exc );
    }
}
