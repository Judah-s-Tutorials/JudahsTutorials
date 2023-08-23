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
            color = JColorChooser.showDialog( null, title, Color.BLUE );
            if ( color != null )
                feedback.setBackground( color );
        } while ( color != null );
        System.exit( 0 );
    }
    
    private static void makeFeedbackWindow()
    {
        JFrame  frame   = new JFrame( "JColorChooser Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        feedback = new JPanel();
        feedback.setPreferredSize( new Dimension( 100, 100 ) );
        frame.setContentPane( feedback );
        frame.pack();
        frame.setVisible( true );
    }
}
