package com.acmemail.judah.battleship;

public class BattleshipException extends RuntimeException
{
    public BattleshipException()
    {
    }

    public BattleshipException(String message)
    {
        super(message);
    }

    public BattleshipException(Throwable cause)
    {
        super(cause);
    }

    public BattleshipException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public BattleshipException(
        String message, 
        Throwable cause, 
        boolean enableSuppression, 
        boolean 
        writableStackTrace
    )
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
