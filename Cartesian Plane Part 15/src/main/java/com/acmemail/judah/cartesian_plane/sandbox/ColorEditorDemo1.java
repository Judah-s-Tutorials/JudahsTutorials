package com.acmemail.judah.cartesian_plane.sandbox;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.components.ColorEditor;

/**
 * Illustrates one way
 * in which the ColorEditor
 * can be used.
 * The ColorEditor's default panel
 * is ignored,
 * and its constituent components
 * are displayed
 * in a custom arrangement.
 * 
 * @author Jack Straub
 */
public class ColorEditorDemo1
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments, not used
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(  () -> {
            ColorEditor editor  = new ColorEditor();
            JFrame      frame   = new JFrame( "Color Editor Demo1" );
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            
            JPanel      panel   = new JPanel();
            BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
            panel.setLayout( layout );
            panel.add( editor.getColorButton() );
            panel.add( editor.getTextEditor() );
            panel.add( editor.getFeedback() );
            frame.setContentPane( panel );
            frame.setLocation( 300, 300 );
            frame.pack();
            frame.setVisible( true );
        });
    }
}
