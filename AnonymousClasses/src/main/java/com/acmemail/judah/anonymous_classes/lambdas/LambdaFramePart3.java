package com.acmemail.judah.anonymous_classes.lambdas;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * This is a simple program
 * that displays a window.
 * It is equivalent to the previous sample program,
 * {@linkplain LambdaFramePart2},
 * but transfers some of the logic
 * of the previous program
 * from the lambda expression
 * (SwingUtilities.invokeLater( () -&gt; ... ))
 * to a helper method (buildGUI)
 * @author Jack Straub
 */
public class LambdaFramePart3
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
     * Helper method to instantiate the GUI
     * displayed by this application.
     */
    private static void buildGUI()
    {
        JFrame  frame   = new JFrame();
        frame.setContentPane( new Canvas() );
        frame.pack();
        frame.setVisible( true );
    }
}
