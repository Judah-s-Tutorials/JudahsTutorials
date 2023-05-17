package com.acmemail.judah.cartesian_plane.sandbox;

import javax.swing.JOptionPane;

public class InputDialogDemo1
{
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
