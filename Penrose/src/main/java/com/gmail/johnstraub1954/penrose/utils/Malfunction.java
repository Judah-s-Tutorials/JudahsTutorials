package com.gmail.johnstraub1954.penrose.utils;

/**
 * Unchecked exception to be used
 * when an unexpected error,
 * likely related to incorrect coding,
 * is encountered.
 */
public class Malfunction extends RuntimeException
{
    /**
     * Default serial version UID
     */
    private static final long serialVersionUID = 1L;

    public Malfunction()
    {
    }

    public Malfunction(String message)
    {
        super(message);
    }

    public Malfunction(Throwable cause)
    {
        super(cause);
    }

    public Malfunction(String message, Throwable cause)
    {
        super(message, cause);
    }

    public Malfunction(
        String message, 
        Throwable cause, 
        boolean enableSuppression, 
        boolean writableStackTrace
    )
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
