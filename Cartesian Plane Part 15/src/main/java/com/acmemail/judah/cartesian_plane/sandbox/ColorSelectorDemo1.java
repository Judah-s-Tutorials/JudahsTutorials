package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.components.ColorSelector;

/**
 * This is a simple application to demonstrate
 * how to instantiate and interact with
 * our ColorSelector class.
 * One of the most important details
 * is that the ColorSelector constructors
 * build the dialog
 * underlying the selector;
 * that means it interacts with Swing,
 * and must be executed
 * in the event dispatch thread (EDT).
 * <p>
 * INSTANCES OF THIS CLASS
 * MUST BE CONSTRUCTED
 * ON THE EDT,
 * FOR EXAMPLE BY USING
 * SwingUtilities.invokeLater().
 * </p>
 * 
 * @author Jack Straub
 */
public class ColorSelectorDemo1 implements ActionListener
{
    /** The component that provides feedback regarding the selected color. */
    private ColorFeedbackFrame   feedback;
    
    /** Contains the dialog allowing an operator to select a color. */
    private ColorSelector        selector;
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {   
         
        ColorSelectorDemo1  demo    = new ColorSelectorDemo1();
        SwingUtilities.invokeLater( () -> demo.makeGUI( ) );
    }
    
    private void makeGUI()
    {
        feedback = new ColorFeedbackFrame();
        feedback.makeGUI( this );
        selector = new ColorSelector();
    }
    
    /**
     * Posts the ColorSelector dialog and waits for it to be dismissed.
     * If dismissed with the OK action,
     * the selected color 
     * is set in the feedback window.
     * 
     * @param evt   event that caused this method to be activated
     */
    @Override
    public void actionPerformed( ActionEvent evt )
    {
        Color   color   = selector.showDialog();
        
        // Color will be non-null if the operator selects the OK button
        if ( color != null )
            feedback.setColor( color );
    }
}
