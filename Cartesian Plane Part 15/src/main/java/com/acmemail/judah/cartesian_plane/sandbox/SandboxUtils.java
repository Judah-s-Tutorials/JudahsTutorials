package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

/**
 * Utility class
 * to provide convenience methods
 * used by other sandbox classes,
 * 
 * @author Jack Straub
 */
public class SandboxUtils
{
    /**
     * Locates a given Window
     * so that it is centered
     * in the display.
     * 
     * @param window    the given window
     */
    public static void center( Window window )
    {
        Dimension   dialogSize  = window.getPreferredSize();
        Dimension   screenSize  = 
            Toolkit.getDefaultToolkit().getScreenSize();
        int     xco     = screenSize.width / 2 - dialogSize.width / 2;
        int     yco     = screenSize.height / 2 - dialogSize.height / 2;
        window.setLocation( xco, yco );
    }
}
