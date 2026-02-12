package com.acmemail.judah.sanbox;

/**
 * This class simply demonstrates how
 * a final variable can be
 * initialized at runtime.
 */
public class FinalVarInitializedAtRuntime
{
    public static final long    SESSION_KEY = makeSessionKey();
    
    private FinalVarInitializedAtRuntime()
    {
    }

    public static void main(String[] args)
    {
        String  hexKey  = String.format( "0x%x", SESSION_KEY );
        System.out.println( hexKey );
    }

    private static long makeSessionKey()
    {
        long    mask    = 0xFF00FF00FF00FF00L;
        long    millis  = System.currentTimeMillis();
        long    key      = millis ^ mask;
        return key;
    }
}
