package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ValidationExceptionTest
{
    @Test
    void testValidationException()
    {
        // This does nothing but get coverage on the default constructor.
        new ValidationException();
    }

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
        Exception           cause   = new Exception();
        ValidationException exc     = new ValidationException( cause );
        assertEquals( cause, exc.getCause() );
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
}
