package com.acmemail.judah.cartesian_plane.sandbox.app;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.components.ColorEditor;

/**
 * Application to display the ColorEditor
 * using the default component configuration
 * provided by the ColorEditor.
 * 
 * @author Jack Straub
 */
public class ShowColorEditorPanel
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used.
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> showGUI() );
    }

    /**
     * Create and show the GUI.
     */
    private static void showGUI()
    {
        JFrame  frame   = new JFrame( "Show Color Editor" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        frame.setContentPane( new ColorEditor().getPanel() );
        frame.pack();
        frame.setVisible( true );
    }
}
