package com.acmemail.judah.anonymous_classes.lambdas;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * This sample application is a revision 
 * to the previous sample,
 * {@linkplain LambdaFramePart4}.
 * It replaces the explicit 
 * PropertyChangeListener in the previous sample
 * with a lambda.
 * 
 * @author Jack Straub
 *
 *@see LambdaFramePart4
 */
public class LambdaFramePart5
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
        frame.addPropertyChangeListener( e -> 
            System.out.println( 
                e.getPropertyName() + 
                " changed from " + e.getOldValue() +
                " to " + e.getNewValue()
            )
        );
        frame.setContentPane( new Canvas() );
        frame.pack();
        frame.setVisible( true );
    }
}
