package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class HTMLDemo1
{
    private static final String labelText   =
        "<html>"
        + "<span style='color: red;'>Little miss </span>"
        + "<span style='color: green; font-size: 200%;'>"
        +     "Stuffit"
        + "</span><br>"
        + "<span style='color: blue;'>sat on </span>"
        + "<span style='color: #7f00ff; "
        + "font-size: 200%; font-family: arial bold'>"
        +     "a Muppet"
        + "</span>&#x1F911;"
        + "</html>";
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> {
            JFrame  frame   = new JFrame( "HTML Demo 1" );
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            JPanel  panel   = new JPanel();
            JLabel  label   = new JLabel( labelText );
            panel.setPreferredSize( new Dimension( 300, 200 ) );
            panel.add( label );
            frame.setContentPane( panel );
            frame.setLocation( 200, 200 );
            frame.pack();
            frame.setVisible( true );
        });
    }
    
}
