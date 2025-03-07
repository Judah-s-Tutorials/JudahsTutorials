package com.acmemail.judah.cartesian_plane.sandbox;

import javax.swing.JOptionPane;

/**
 * This program is intended to be
 * the child in a simple demonstration
 * of how to spawn a child process.
 * @author Jack Straub
 */
public class IPCDemo1Child
{
    /**
     * Application entry point.
     * This program displays its first
     * command-line argument, 
     * or "No arg" if there are no arguments.
     *
     * @param args command line arguments
     *
    */
    public static void main(String[] args)
    {
        String  arg     = args.length == 0 ? "No arg" : args[0];
        JOptionPane.showMessageDialog( null, arg );
        System.exit( 12 );
    }
}
