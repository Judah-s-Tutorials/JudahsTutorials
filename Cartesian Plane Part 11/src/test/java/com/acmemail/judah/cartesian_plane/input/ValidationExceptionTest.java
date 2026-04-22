package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class ValidationExceptionTest
{
    @Test
    void testValidationException()
    {
        Exception   exc = new ValidationException();
        assertNull( exc.getMessage() );
        assertNull( exc.getCause() );    }

    @Test
    void testValidationExceptionString()
    {
        String              str = "this is a message";
        ValidationException exc = new ValidationException( str );
        assertEquals( str, exc.getMessage() );
    }

    @Test
    void testValidationExceptionThrowable()
    {
        String              message = "Error message";
        Exception           cause   = new IOException( message );
        ValidationException exc     = new ValidationException( cause );
        assertEquals( cause, exc.getCause() );
        assertEquals( message, cause.getMessage() );
        assertThrows(ValidationException.class, () -> { throw new
            ValidationException("x"); });
    }

    @Test
    void testValidationExceptionStringThrowable()
    {
        String              str     = "this is a message";
        Exception           cause   = new Exception();
        ValidationException exc     = new ValidationException( str, cause );
        assertEquals( cause, exc.getCause() );
        assertEquals( str, exc.getMessage() );
    }
    
    @Test
    void misc()
    {
        // Verify this exception is unchecked.
        Exception   exc     = new ValidationException();
        assertTrue( exc instanceof RuntimeException );
    }
}
