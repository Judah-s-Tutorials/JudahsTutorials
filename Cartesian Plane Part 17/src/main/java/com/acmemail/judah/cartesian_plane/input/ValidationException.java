package com.acmemail.judah.cartesian_plane.input;

/**
 * Describes an expression validation failure
 * in the <em>input</em> module.
 * 
 * @author Jack Straub
 */
public class ValidationException extends RuntimeException
{
    /** Generated serial version UID */
    private static final long serialVersionUID = -3746831103519185859L;

    /**
     * Default constructor.
     */
    public ValidationException()
    {
    }

    /**
     * Constructor.
     * 
     * @param message   message associated with exception
     */
    public ValidationException(String message)
    {
        super(message);
    }

    /**
     * Constructor.
     * 
     * @param cause cause that triggered this exception
     */
    public ValidationException(Throwable cause)
    {
        super(cause);
    }

    /**
     * Constructor.
     * 
     * @param message   message associated with exception
     * @param cause cause that triggered this exception
     */
    public ValidationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
