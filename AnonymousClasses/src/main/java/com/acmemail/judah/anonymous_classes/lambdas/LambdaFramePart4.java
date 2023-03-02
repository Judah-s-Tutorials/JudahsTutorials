package com.acmemail.judah.anonymous_classes.lambdas;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * This application demonstrates the implementation
 * of a <em>PropertyChangeListener.</em>
 * In this example 
 * the listener is implemented as
 * an explicitly declared class.
 * The next example,
 * {@linkplain LambdaFramePart5}
 * replaces the explicit declaration
 * with a lambda.
 * 
 * @author Jack Straub
 * 
 * @see LambdaFramePart5 
 */
public class LambdaFramePart4
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> buildGUI() );
    }
    
    /**
     * Instantiates and configures
     * the sample window
     * displayed by this application.
     */
    private static void buildGUI()
    {
        JFrame  frame   = new JFrame();
        frame.addPropertyChangeListener( new PropertyMonitor() );
        frame.setContentPane( new Canvas() );
        frame.pack();
        frame.setVisible( true );
    }
    
    /**
     * Used to monitor changes to properties.
     * 
     * @author Jack Straub
     */
    private static class PropertyMonitor implements PropertyChangeListener
    {
        @Override
        public void propertyChange(PropertyChangeEvent evt)
        {
            System.out.println( 
                evt.getPropertyName() + 
                " changed from " + evt.getOldValue() +
                " to " + evt.getNewValue()
            );
            
        }
    }
}
