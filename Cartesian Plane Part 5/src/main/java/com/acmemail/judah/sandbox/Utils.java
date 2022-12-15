package com.acmemail.judah.sandbox;

/**
 * This is the Utils class used for verifying 
 * Javadoc in a lecture about documentation.
 * It Serves no use beyond this purpose.
 * 
 * @author Jack Straub
 *
 */
public class Utils
{
    /**
     * Pause a thread for a given number of milliseconds.
     * This is a simple method that is designed
     * to relieve the programmer of the burden
     * of catching the InterruptedException
     * that might be thrown by Thread.sleep().
     * Using this method assumes
     * that the user has no need
     * to process the exception.
     * If the exception is thrown it is ignored.
     * 
     * @param millis    the number of milliseconds to sleep
     */
    public static void pause( long millis )
    {
        try
        {
            Thread.sleep( millis );
        }
        catch ( InterruptedException exc )
        {
            // Exception ignored by design.
        }
    }
}
