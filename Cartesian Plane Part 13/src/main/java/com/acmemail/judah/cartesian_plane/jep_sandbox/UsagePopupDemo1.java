package com.acmemail.judah.cartesian_plane.jep_sandbox;

import javax.swing.JOptionPane;

import com.acmemail.judah.cartesian_plane.input.Command;

/**
 * Demonstrates how to display a dialog
 * containing a "usage" message.
 * A "usage" message
 * is a string containing a list of all valid commands
 * and a brief description
 * of each command.
 * 
 * @author Jack Straub
 */
public class UsagePopupDemo1
{
    public static void main(String[] args)
    {
        String  usage   = Command.usage();
        JOptionPane.showMessageDialog( null, usage );
    }
}
