package com.acmemail.judah.sandbox.sandbox;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class JColorChooserDemo
{
    private static final String STATUS_KEY      =
        "judah.JColorChooserDemo.status";
    private static final int    OK_STATUS       = 1;
    private static final int    CANCEL_STATUS   = 0;
    
    private final JColorChooser colorPane   = new JColorChooser();
    private final JButton       choose      = new JButton( "Choose" );
    private final JTextField    feedback    = new JTextField( 15 );
    private final JDialog       dialog      =
        JColorChooser.createDialog(
            null, 
            "Choose a Color", 
            true, 
            colorPane, 
            e -> setAndClose( OK_STATUS ), 
            e -> setAndClose( CANCEL_STATUS )
        );
    
    public static void main(String[] args)
    {
        JColorChooserDemo   demo    = new JColorChooserDemo();
        SwingUtilities.invokeLater( () -> demo.build() );
    }

    public void build()
    {
        JFrame  frame   = new JFrame( "JColorChooser Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel  pane    = new JPanel();
        pane.add( choose );
        pane.add( feedback );
        choose.addActionListener( e -> showDialog() );
        
        frame.setContentPane( pane );
        frame.pack();
        frame.setVisible( true );
    }
    
    private void showDialog()
    {
        dialog.setVisible( true );
        int status  = (int)colorPane.getClientProperty( STATUS_KEY );
        if ( status == OK_STATUS )
        {
            Color   color   = colorPane.getColor();
            feedback.setBackground( color );
        }
    }
    
    private void setAndClose( int choice )
    {
        colorPane.putClientProperty( STATUS_KEY, choice );
        dialog.setVisible( false );
    }
}
