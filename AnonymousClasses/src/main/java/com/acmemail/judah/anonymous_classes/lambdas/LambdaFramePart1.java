package com.acmemail.judah.anonymous_classes.lambdas;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * This is a simple application
 * that displays a window
 * in a JFrame.
 * It uses an explicitly declared class
 * to implement the Runnable
 * required by SwingUtilities.invokeLater.
 * It is a precursor to an example,
 * {@linkplain LambdaFramePart2},
 * that replaces the explicitly declared class
 * with an anonymous class
 * declared/instantiated using a lambda.
 * 
 * @author Jack Straub
 * 
 * @see LambdaFramePart2
 */
public class LambdaFramePart1
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( new Root() );
    }
    
    /**
     * Instantiates and displays
     * the window used in this application.
     * 
     * @author Jack Straub
     */
    private static class Root implements Runnable
    {
        @Override
        public void run()
        {
            JFrame  frame   = new JFrame();
            frame.setContentPane( new Canvas() );
            frame.pack();
            frame.setVisible( true );
        }
    }
}

