package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Application to demonstrate
 * the simplest way
 * to use a JColorChooser.
 * Use <em>JColorChooser.showDialog</em>
 * to display the modal dialog.
 * If the operator clicks OK
 * the selected color is returned;
 * if the operator clicks Cancel
 * null is returned.
 * 
 * @author Jack Straub
 */
public class JColorChooserDemo1
{
    /** The component that provides feedback regarding the selected color. */
    private static JPanel feedback;
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> makeFeedbackWindow() );
        String  title   = "JColorChooser Demo";
        Color   color   = null;
        do
        {
            // Display the dialog
            color = JColorChooser.showDialog( null, title, Color.BLUE );
            
            // Color will be non-null if the operator selects the OK button
            if ( color != null )
                feedback.setBackground( color );
            
            // Color will be null if the operator selects the cancel button
            // (or the close button)
        } while ( color != null );
        System.exit( 0 );
    }
    
    /**
     * Creates a frame containing a feedback window.
     * The background color of the feedback window is updated
     * each time the operator selects a color
     * in the color dialog
     * and pushes the OK button.
     */
    private static void makeFeedbackWindow()
    {
        JFrame  frame   = new JFrame( "JColorChooser Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        feedback = new JPanel();
        feedback.setPreferredSize( new Dimension( 100, 100 ) );
        
        frame.setLocation( 100, 100 );
        frame.setContentPane( feedback );
        frame.pack();
        frame.setVisible( true );
    }
}
