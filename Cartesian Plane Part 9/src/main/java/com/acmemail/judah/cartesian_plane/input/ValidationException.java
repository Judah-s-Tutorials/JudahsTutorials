package com.acmemail.judah.cartesian_plane.input;

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

    /**
     * Constructor.
     * 
     * @param message               message associated with exception
     * @param cause                 cause that triggered this exception
     * @param enableSuppression     see documentation for RuntimeException
     * @param writableStackTrace    see documentation for RuntimeException
     */
    public ValidationException(
        String message, 
        Throwable cause, 
        boolean enableSuppression, 
        boolean writableStackTrace
    )
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
