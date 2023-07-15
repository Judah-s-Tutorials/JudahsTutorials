package com.acmemail.judah.sandbox.sandbox;

import com.acmemail.judah.sandbox.ComponentException;

public class SandboxUtils
{
    public static void pause( long millis )
    {
        try
        {
            Thread.sleep(millis);
        }
        catch ( InterruptedException exc )
        {
            throw new ComponentException( "Sleep: unexpected interrupt" );
        }
    }

    public static void join( Thread thread )
    {
        try
        {
            thread.join();
        }
        catch ( InterruptedException exc )
        {
            throw new ComponentException( "Join: unexpected interrupt" );
        }
    }
}
