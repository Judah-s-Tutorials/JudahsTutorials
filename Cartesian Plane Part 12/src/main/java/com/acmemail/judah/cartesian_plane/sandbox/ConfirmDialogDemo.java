package com.acmemail.judah.cartesian_plane.sandbox;

import javax.swing.JOptionPane;

/**
 * Demonstrates how to display a "confirm" dialog,
 * a dialog asking a yes or no question.
 * 
 * @author Jack Straub
 */
public class ConfirmDialogDemo
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        String  message = "Are you sure you want to do that?";
        String  title   = "Confirm your choice";
        int     type    = JOptionPane.YES_NO_OPTION;
        int     choice  = 
            JOptionPane.showConfirmDialog( null, message, title,  type );
        if ( choice == JOptionPane.YES_OPTION )
            System.out.println( "All data will be deleted" );
        else
            System.out.println( "Operation aborted" );
    }
}
