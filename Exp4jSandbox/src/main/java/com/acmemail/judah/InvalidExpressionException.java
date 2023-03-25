package com.acmemail.judah;

import net.objecthunter.exp4j.ValidationResult;

@SuppressWarnings("serial")
public class InvalidExpressionException extends RuntimeException
{
    private final ValidationResult  validationResult;
    
    public InvalidExpressionException( ValidationResult validationResult )
    {
        this.validationResult = validationResult;
    }
    
    public ValidationResult getValidationResult()
    {
        return validationResult;
    }
}
