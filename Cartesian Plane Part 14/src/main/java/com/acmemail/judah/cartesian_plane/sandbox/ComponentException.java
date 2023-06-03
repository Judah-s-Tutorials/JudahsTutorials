package com.acmemail.judah.cartesian_plane.sandbox;

public class ComponentException extends RuntimeException
{

    /** Default serial version UID. */
    private static final long serialVersionUID = 1L;

    public ComponentException()
    {
    }

    public ComponentException(String message)
    {
        super(message);
    }

    public ComponentException(Throwable cause)
    {
        super(cause);
    }

    public ComponentException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
