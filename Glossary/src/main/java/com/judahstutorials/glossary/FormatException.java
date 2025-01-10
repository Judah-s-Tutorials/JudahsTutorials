package com.judahstutorials.glossary;

public class FormatException extends Error
{

    public FormatException()
    {
    }

    public FormatException(String message)
    {
        super(message);
    }

    public FormatException(Throwable cause)
    {
        super(cause);
    }

    public FormatException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public FormatException(
        String message, 
        Throwable cause, 
        boolean enableSuppression, 
        boolean writableStackTrace
    )
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
