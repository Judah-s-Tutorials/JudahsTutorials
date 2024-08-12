package com.acmemail.judah.cartesian_plane.graphics_utils;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.junit.platform.commons.util.ExceptionUtils;

public class GUIUtils
{
    /**
     * Calls SwingUtilities.invokeAndWait
     * to schedule a task to run,
     * and wait for it to complete.
     * If an InterruptedException
     * or InvocationTargetException is thrown
     * it is treated as a fatal error;
     * a stack trace is printed to stderr
     * and a RuntimeException 
     * encapsulating the original exception
     * is thrown.
     * 
     * @param runner    the task to schedule
     * @throws RuntimeException if invokeAndWait throws an exception
     */
    public static void schedEDTAndWait( Runnable runner )
    {
        try
        {
            SwingUtilities.invokeAndWait( runner );
        }
        catch ( InterruptedException | InvocationTargetException exc )
        {
            exc.printStackTrace();
            Throwable   cause   = exc.getCause();
            if ( cause != null )
                ExceptionUtils.throwAsUncheckedException( cause );
            else
                throw new RuntimeException( exc );
        }
    }
}
