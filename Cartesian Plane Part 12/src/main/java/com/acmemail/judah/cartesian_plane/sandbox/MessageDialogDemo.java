package com.acmemail.judah.cartesian_plane.sandbox;

import javax.swing.JOptionPane;

/**
 * Demonstrates how to display a "message" dialog,
 * a dialog containing a message.
 * 
 * @author Jack Straub
 */
public class MessageDialogDemo
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        String  title   = "Messag Dialog Demo";
        String  message = "The system will shutdown in 5 minutes";
        int     type    = JOptionPane.INFORMATION_MESSAGE;
        
        System.out.println( "waiting for dialog" );
        JOptionPane.showMessageDialog( null, message, title, type );
        System.out.println( "dialog dismissed" );
    }
}
