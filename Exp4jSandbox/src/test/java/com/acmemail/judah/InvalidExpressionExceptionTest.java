package com.acmemail.judah;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.Test;

import net.objecthunter.exp4j.ValidationResult;

class InvalidExpressionExceptionTest
{
    @Test
    void testInvalidExpressionException()
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
}
