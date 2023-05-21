package com.acmemail.judah.cartesian_plane.sandbox;

import javax.swing.JOptionPane;

/**
 * Simple demonstration of how to display
 * an input dialog
 * using the JOptionPane class.
 * 
 * @author Jack Straub
 */
public class InputDialogDemo1
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        String  name    = 
            JOptionPane.showInputDialog( null, "Enter your name:" );
        if ( name == null )
            System.out.println( "Operation cancelled" );
        else if ( name.isEmpty() )
            System.out.println( "Operation approved, but no name enterd" );
        else
            System.out.println( "Operator name: " + name );
    }
}
