package com.acmemail.judah.cartesian_plane.sandbox.jtable.panels;

/**
 * Unchecked exception to be thrown
 * when an error occurs
 * while traversing an application's 
 * window hierarchy.
 * 
 * @author Jack Straub
 */
public class ComponentException extends RuntimeException
{

    /** Default serial version UID. */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public ComponentException()
    {
    }

    /**
     * Default constructor.
     * 
     * @param message   message associated with this exception
     */
    public ComponentException(String message)
    {
        super(message);
    }

    /**
     * Constructor.
     * @param cause     cause of this exception
     */
    public ComponentException(Throwable cause)
    {
        super(cause);
    }

    /**
     * Constructor.
     * 
     * @param message   message associated with this exception
     * @param cause     cause of this exception
     */
    public ComponentException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
