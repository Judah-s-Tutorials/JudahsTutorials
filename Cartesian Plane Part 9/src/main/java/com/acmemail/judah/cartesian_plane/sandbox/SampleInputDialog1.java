package com.acmemail.judah.cartesian_plane.sandbox;

import javax.swing.JOptionPane;

/**
 * This dialog does nothing
 * except draw a picture
 * of what an input dialog
 * for entering an expression
 * would look like.
 * 
 * @author Jack Straub
 */
public class SampleInputDialog1
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        // showInputDialog return value deliberately ignored;
        // purpose is only to demonstrate dialog's appearance
        JOptionPane.showInputDialog( 
            null,
            "Enter a command:",
            "Expression Parser",
            JOptionPane.QUESTION_MESSAGE
        );
    }
}
