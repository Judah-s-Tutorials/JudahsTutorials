package com.acmemail.judah.cartesian_plane.sandbox.layout_managers;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.sandbox.SandboxUtils;

public class GridLayoutDemo3
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> makeGUI() );
    }

    private static void makeGUI()
    {
        JFrame  frame       = new JFrame( "GridLayout Demo 2" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  mainPanel   = new JPanel( new GridLayout( 3, 4, 20, 5 ) );
        
        mainPanel.add( new JButton( "ant" ) );
        mainPanel.add( new JButton( "elephant" ) );
        mainPanel.add( new JButton( "gnu" ) );
        mainPanel.add( new JButton( "ape" ) );
        mainPanel.add( new JButton( "cat" ) );
        mainPanel.add( new JButton( "tiger" ) );
        mainPanel.add( new JButton( "dodo" ) );
        mainPanel.add( new JButton( "dog" ) );
        mainPanel.add( new JButton( "wolf" ) );
        mainPanel.add( new JButton( "lion" ) );
        mainPanel.add( new JButton( "zebra" ) );
        mainPanel.add( new JButton( "fox" ) );

        frame.setContentPane( mainPanel );
        frame.pack();
        SandboxUtils.center( frame );
        frame.setVisible( true );
    }
}
