package com.acmemail.judah.anonymous_classes.lambdas;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * This is a simple application
 * that demonstrates how to replace
 * the explicit class declaration
 * in the previous example,
 * {@linkplain LambdaFramePart1},
 * with an anonymous class
 * declared/instantiated using a lambda.
 * 
 * @author Jack Straub
 * 
 * @see LambdaFramePart1
 * @see LambdaFramePart3
 */
public class LambdaFramePart2
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () ->
            {
                JFrame  frame   = new JFrame();
                frame.setContentPane( new Canvas() );
                frame.pack();
                frame.setVisible( true );
            }
        );
    }
}
