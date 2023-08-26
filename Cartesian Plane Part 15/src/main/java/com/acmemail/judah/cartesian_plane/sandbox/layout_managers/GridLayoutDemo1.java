package com.acmemail.judah.cartesian_plane.sandbox.layout_managers;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.sandbox.SandboxUtils;

public class GridLayoutDemo1
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> makeGUI() );
    }

    private static void makeGUI()
    {
        JFrame  frame       = new JFrame( "GridLayout Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  mainPanel   = new JPanel( new GridLayout( 3, 3 ) );
        
        mainPanel.add( getButton( "" ) );
        mainPanel.add( getButton( "X" ) );
        mainPanel.add( getButton( "O" ) );
        mainPanel.add( getButton( "X" ) );
        mainPanel.add( getButton( "O" ) );
        mainPanel.add( getButton( "" ) );
        mainPanel.add( getButton( "" ) );
        mainPanel.add( getButton( "X" ) );
        mainPanel.add( getButton( "O" ) );
        
        frame.setContentPane( mainPanel );
        frame.pack();
        SandboxUtils.center( frame );
        frame.setVisible( true );
    }
    
    private static JComponent getButton( String text )
    {
        JButton button  = new JButton( text );
        Font    font    = button.getFont().deriveFont( 20f );
        button.setFont( font );
        return button;
    }
}
