package com.acmemail.judah.battleship;

/**
 * This is an unchecked exception used for miscellaneous purposes
 * in the Battleship project.
 */
@SuppressWarnings("serial")
public class BattleshipException extends RuntimeException
{
    /**
     * Constructor.
     */
    public BattleshipException()
    {
    }

    /**
     * Constructor.
     * 
     * @param message   the message associated with this exception
     */
    public BattleshipException(String message)
    {
        super(message);
    }

    /**
     * Constructor. 
     * 
     * @param cause the cause associated with this exception
     */
    public BattleshipException(Throwable cause)
    {
        super(cause);
    }

    /**
     * Constructor.
     * @param message   the message associated with this exception
     * @param cause     the cause associated with this exception
     */
    public BattleshipException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Constructor.
     * @param message   
     *      the message associated with this exception
     * @param cause     
     *      the cause associated with this exception
     * @param enableSuppression     
     *      true to enable suppression of this exception
     * @param writableStackTrace
     *      true to enable a writable stack trace of this exception
     */
    public BattleshipException(
        String message, 
        Throwable cause, 
        boolean enableSuppression, 
        boolean writableStackTrace
    )
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
