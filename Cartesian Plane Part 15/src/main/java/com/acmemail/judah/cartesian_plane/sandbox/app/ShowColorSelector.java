package com.acmemail.judah.cartesian_plane.sandbox.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.components.ColorSelector;

/**
 * Application to display the ColorEditor dialog.
 * 
 * @author Jack Straub
 */
public class ShowColorSelector
{
    /** Dialog to display. */
    private static ColorSelector    dialog;
    /** Component to display the color selected from the dialog. */
    private static JPanel           feedback;
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used.
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> build() );
    }

    /**
     * Create and show the GUI.
     */
    private static void build()
    {
        dialog = new ColorSelector();
        
        JFrame  frame   = new JFrame( "Feedback Window" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel  pane    = new JPanel( new BorderLayout() );
        feedback = new JPanel();
        feedback.setPreferredSize( new Dimension( 100, 100 ) );
        pane.add( feedback, BorderLayout.CENTER );
        
        JButton pushMe  = new JButton( "Push Me" );
        pane.add( pushMe, BorderLayout.SOUTH );
        pushMe.addActionListener( e -> {
           Color    color   = dialog.showDialog();
           if ( color != null )
               feedback.setBackground( color );
        });

        frame.setContentPane( pane );
        frame.setLocation( 100, 100 );
        frame.pack();
        frame.setVisible( true );
    }

}
