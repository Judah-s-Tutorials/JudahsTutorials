package com.acmemail.judah.cartesian_plane.app;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.components.ColorEditor;

/**
 * Application to display the ColorEditor,
 * by individually positioning 
 * each ColorEditor component.
 * 
 * @author Jack Straub
 */
public class ShowColorEditor
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
        
        ColorEditor     editor  = new ColorEditor();
        JPanel          pane    = new JPanel( new GridLayout( 3, 1 ) );
        pane.add( editor.getColorButton() );
        pane.add( editor.getTextEditor() );
        pane.add( editor.getFeedback() );
        
        frame.setContentPane( pane );
        frame.pack();
        frame.setVisible( true );
    }
}
