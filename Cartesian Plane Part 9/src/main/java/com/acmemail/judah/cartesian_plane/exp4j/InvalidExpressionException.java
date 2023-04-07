package com.acmemail.judah.cartesian_plane.exp4j;

import java.util.ArrayList;
import java.util.List;

import net.objecthunter.exp4j.ValidationResult;

/**
 * An object of this class encapsulates an exception
 * associated with an expression operation.
 * In addition to a message,
 * contains a ValidationResult object
 * that, presumably, describes the exception.
 * 
 * @author Jack Straub
 */
@SuppressWarnings("serial")
public class InvalidExpressionException extends RuntimeException
{
    /** Encapsulates messages associated with this exception. */
    private final ValidationResult  validationResult;

    /**
     * Default constructor.
     * Creates an InvalidExpressionException object
     * with a non-null, empty ValidationResult
     * with is-valid set to false;
     */
    public InvalidExpressionException()
    {
        validationResult = new ValidationResult( false, new ArrayList<>() );
    }

    /**
     * Constructor.
     * Creates an object with the given cause and message.
     * Creates a <em>false</em> ValidationResult 
     * that contains the given message.
     * 
     * @param message   the given message
     * @param cause     the given cause
     */
    public InvalidExpressionException( String message, Throwable cause )
    {
        super(message, cause);
        validationResult = new ValidationResult( false, List.of( message ) );
    }

    /**
     * Constructor.
     * Creates an object with the given message.
     * Creates a <em>false</em> ValidationResult 
     * that contains the given message.
     * 
     * @param message   the given message
     */
    public InvalidExpressionException( String message )
    {
        super(message);
        validationResult = new ValidationResult( false, List.of( message ) );
    }

    /**
     * Constructor. 
     * Creates an InvalidExpressionException object
     * with the given cause
     * and a non-null, empty ValidationResult
     * with  <em>is-valid</em> set to false.
     * @param cause
     */
    public InvalidExpressionException( Throwable cause )
    {
        super(cause);
        validationResult = new ValidationResult( false, List.of( cause.getMessage() ) );
    }
    
    /**
     * Constructor. 
     * Creates an InvalidExpressionException object
     * with the given ValidationResult.
     *
     * @param validationResult the given ValidationResult
     */
    public InvalidExpressionException( ValidationResult validationResult )
    {
        super();
        this.validationResult = validationResult;
    }
    
    /**
     * Constructor. 
     * Creates an InvalidExpressionException object
     * with the given cause and ValidationResult.
     *
     * @param cause             the given cause
     * @param validationResult  the given ValidationResult
     */
    public InvalidExpressionException( ValidationResult validationResult, Throwable cause )
    {
        super( cause );
        this.validationResult = validationResult;
    }
    
    /**
     * Returns the encapsulated ValidationResult.
     * 
     * @return  the encapsulated ValidationResult
     */
    public ValidationResult getValidationResult()
    {
        return validationResult;
    }
}
