package com.acmemail.judah.cartesian_plane.graphics_utils;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

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
            throw new RuntimeException( exc );
        }
    }
    
    /*
     * Center a given Window on the screen.
     * The Window is most likely a JFrame or JDialog.
     * 
     * @param window    the given window
     */
    public static void center( Window window )
    {
        Dimension   windowSize  = window.getPreferredSize();
        Dimension   screenSize  = 
            Toolkit.getDefaultToolkit().getScreenSize();
        int         xco         = 
            (screenSize.width - windowSize.width) / 2;
        int         yco         = 
            (screenSize.height - windowSize.height) / 2;
        window.setLocation( xco, yco );
    }
}
