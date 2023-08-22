package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class JColorChooserDemo2b
{
    private static final int        OK_CHOICE       = 0;
    private static final int        CANCEL_CHOICE   = 1;
    private static JColorChooser    colorPane;
    private static JDialog          dialog;    
    private static JPanel           feedback;
    
    private static int  choice  = -1;
    
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> build() );
    }

    public static void build()
    {
        colorPane = new JColorChooser();
        dialog      =
            JColorChooser.createDialog(
                null, 
                "Choose a Color", 
                true, 
                colorPane, 
                e -> choice = OK_CHOICE, 
                e -> choice = CANCEL_CHOICE
            );

        JFrame  frame   = new JFrame( "Feedback Window" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        Container   pane    = frame.getContentPane();
        pane.setLayout( new BorderLayout() );
        feedback = new JPanel();
        feedback.setPreferredSize( new Dimension( 100, 100 ) );
        pane.add( feedback, BorderLayout.CENTER );
        
        JButton pushMe  = new JButton( "Push Me" );
        pushMe.addActionListener( e -> {
            dialog.setVisible( true );
            if ( choice == OK_CHOICE )
                feedback.setBackground( colorPane.getColor() );
        });
        pane.add( pushMe, BorderLayout.SOUTH );

        feedback.setLocation( 100, 100 );
        frame.pack();
        frame.setVisible( true );
    }
}
