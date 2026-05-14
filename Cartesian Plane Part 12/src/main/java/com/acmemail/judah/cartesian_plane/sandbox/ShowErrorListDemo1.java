package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.List;

import javax.swing.JOptionPane;

public class ShowErrorListDemo1
{
    public static void main(String[] args)
    {
        List<String>    messages    = 
            List.of( "message1", "message2", "message3" );
        String          message     = String.join( "\n", messages );
        JOptionPane.showMessageDialog( 
            null, 
            message, 
            "title", 
            JOptionPane.INFORMATION_MESSAGE 
        );
    }
}
