package com.acmemail.judah.cartesian_plane.test_utils;

public class Utils
{
    /**
     * Private constructor to prevent instantiation.
     */
    private Utils()
    {
    }
    
    public static void pause( long millis )
    {
        try
        {
            Thread.sleep( millis );
        }
        catch ( InterruptedException exc )
        {
            // ignore
        }
    }
}
