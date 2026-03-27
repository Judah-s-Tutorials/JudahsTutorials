package com.acmemail.judah.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BattleshipExceptionTest
{

    @Test
    void testBattleshipException()
    {
        new BattleshipException();
    }

    @Test
    void testBattleshipExceptionString()
    {
        String      expMessage  = "Expected message";
        Exception   exc         = new BattleshipException( expMessage );
        String      actMessage  = exc.getMessage();
        assertEquals( expMessage, actMessage );
    }

    @Test
    void testBattleshipExceptionThrowable()
    {
        Throwable   expCause    = new IllegalArgumentException();
        Exception   exc         = new BattleshipException( expCause );
        Throwable   actCause    = exc.getCause();
        assertEquals( expCause, actCause );
    }

    @Test
    void testBattleshipExceptionStringThrowable()
    {
        String      expMessage  = "Expected message";
        Throwable   expCause    = new IllegalArgumentException();
        Exception   exc         = 
            new BattleshipException( expMessage, expCause );
        String      actMessage  = exc.getMessage();
        Throwable   actCause    = exc.getCause();
        assertEquals( expMessage, actMessage );
        assertEquals( expCause, actCause );
    }

    @Test
    void testBattleshipExceptionStringThrowableBooleanBoolean()
    {
        testBattleshipExceptionStringThrowableBooleanBoolean( true, false );
        testBattleshipExceptionStringThrowableBooleanBoolean( false, true );
    }

    private void 
    testBattleshipExceptionStringThrowableBooleanBoolean( 
        boolean enableSuppression,
        boolean writableStackTrace
    )
    {
        String      expMessage  = "Expected message";
        Throwable   expCause    = new IllegalArgumentException();
        Exception   exc         = 
            new BattleshipException( 
                expMessage, 
                expCause, 
                enableSuppression, 
                writableStackTrace
            );
        String      actMessage  = exc.getMessage();
        Throwable   actCause    = exc.getCause();
        assertEquals( expMessage, actMessage );
        assertEquals( expCause, actCause );
    }
}
