package com.acmemail.judah.cartesian_plane.app;

import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.components.CPFrame;

/**
 * Application to read operator input
 * from the console
 * and produce a plot.
 * 
 * @author Jack Straub
 */
public class ShowCartesianPlane
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new CPFrame() );
    }
}
