package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;
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
public class JColorChooserDemo1 implements ActionListener
{
    /** The component that provides feedback regarding the selected color. */
    private static ColorFeedbackFrame    feedback;
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {   
        feedback = new ColorFeedbackFrame();  
        ActionListener  listener    = new JColorChooserDemo1();
        SwingUtilities.invokeLater( () -> feedback.makeGUI( listener ) );
    }
    
    /**
     * Posts a JColorChooser dialog and waits for it to be dismissed.
     * If dismissed with the OK action,
     * the selected color 
     * is set in the feedback window.
     * 
     * @param evt   event that caused this method to be activated
     */
    @Override
    public void actionPerformed( ActionEvent evt )
    {
        String  title   = "JColorChooser Demo";
        // Display the dialog
        Color   color   = JColorChooser.showDialog( null, title, Color.BLUE );
        
        // Color will be non-null if the operator selects the OK button
        if ( color != null )
            feedback.setColor( color );
    }
}
