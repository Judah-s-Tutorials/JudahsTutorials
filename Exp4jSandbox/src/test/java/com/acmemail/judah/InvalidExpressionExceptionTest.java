package com.acmemail.judah;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import net.objecthunter.exp4j.ValidationResult;

class InvalidExpressionExceptionTest
{
    @Test
    void testInvalidExpressionException()
    {
        InvalidExpressionException  exc =
            new InvalidExpressionException();
        
        ValidationResult    result  = exc.getValidationResult();
        List<String>        list    = result.getErrors();
        assertEquals( 0, list.size() );
    }
    
    @Test
    void testInvalidExpressionExceptionValidationResult()
    {
        String  msg = "message";
        ValidationResult            vResult = 
            new ValidationResult( false, List.of( msg ) );
        InvalidExpressionException  exc =
            new InvalidExpressionException( vResult );
        
        ValidationResult    result  = exc.getValidationResult();
        List<String>        list    = result.getErrors();
        assertEquals( 1, list.size() );
        assertEquals( msg, list.get( 0 ) );
    }
    
    @Test
    void testInvalidExpressionExceptionString()
    {
        String  msg = "message";
        InvalidExpressionException  exc =
            new InvalidExpressionException( msg );
        
        ValidationResult    result  = exc.getValidationResult();
        List<String>        list    = result.getErrors();
        assertEquals( 1, list.size() );
        assertEquals( msg, list.get( 0 ) );
    }
    
    @Test
    void testInvalidExpressionExceptionCause()
    {
        String                      msg     = "message";
        Throwable                   cause   = new Exception( msg );
        InvalidExpressionException  exc     =
            new InvalidExpressionException( cause );
        
        ValidationResult    result  = exc.getValidationResult();
        assertNotNull( result );
        List<String>        list    = result.getErrors();
        assertEquals( 1, list.size() );
            
        Throwable           thr     = exc.getCause();
        assertNotNull( thr );
        assertEquals( msg, thr.getMessage() );
    }
    
    @Test
    void testInvalidExpressionExceptionStringThrowable()
    {
        String      vMsg        = "message";
        String      causeMsg    = "cause message";
        Throwable   cause       = new Exception( causeMsg );
        InvalidExpressionException  exc =
            new InvalidExpressionException( vMsg, cause );
        
        ValidationResult    result  = exc.getValidationResult();
        assertNotNull( result );
        assertFalse( result.isValid() );

        List<String>        list    = result.getErrors();
        assertNotNull( list );
        assertEquals( 1, list.size() );
        assertEquals( vMsg, list.get( 0 ) );
        
        Throwable           thr     = exc.getCause();
        assertNotNull( thr );
        assertEquals( causeMsg, thr.getMessage() );
    }
    
    @Test
    void testInvalidExpressionExceptionValidationResultThrowable()
    {
        String              vMsg        = "message";
        String              causeMsg    = "cause message";
        Throwable           cause       = new Exception( causeMsg );
        ValidationResult    resultIn    = 
            new ValidationResult( false, List.of( vMsg ) );
        InvalidExpressionException  exc =
            new InvalidExpressionException( resultIn, cause );
        
        ValidationResult    resultOut   = exc.getValidationResult();
        assertNotNull( resultOut );
        assertFalse( resultOut.isValid() );

        List<String>        list    = resultOut.getErrors();
        assertNotNull( list );
        assertEquals( 1, list.size() );
        assertEquals( vMsg, list.get( 0 ) );
        
        Throwable           thr     = exc.getCause();
        assertNotNull( thr );
        assertEquals( causeMsg, thr.getMessage() );
    }
}
