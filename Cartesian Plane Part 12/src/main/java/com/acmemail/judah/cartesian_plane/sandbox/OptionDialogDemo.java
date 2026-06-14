package com.acmemail.judah.cartesian_plane.sandbox;

import javax.swing.JOptionPane;

/**
 * Demonstrates how to display an "option" dialog,
 * a dialog asking the operator to select
 * one of several choices.
 * 
 * @author Jack Straub
 */
public class OptionDialogDemo
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        String[] options     =
        {
            "Dog", "Cat", "Hamster", 
            "Canary", "Snake", "Cancel"
        };
        int choice = JOptionPane.showOptionDialog(
            null, 
            "What would you like to purchase?", 
            "Tacitus's Pet Shop", 
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            "Hamster"
        );
        if ( choice < 0 )
            System.out.println( "No choice made" );
        else
            System.out.println( "Purchasing: " + options[choice] );    }
}
