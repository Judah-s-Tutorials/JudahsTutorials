package com.acmemail.judah.cartesian_plane.sandbox.layout_managers;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.sandbox.SandboxUtils;

public class GridLayoutDemo2
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> makeGUI() );
    }

    private static void makeGUI()
    {
        JFrame  frame       = new JFrame( "GridLayout Demo 2" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  mainPanel   = new JPanel( new GridLayout( 2, 3 ) );
        
        mainPanel.add( new JButton( "ant" ) );
        mainPanel.add( new JButton( "...VERY...WIDE..." ) );
        mainPanel.add( new JButton( "gnu" ) );
        mainPanel.add( new JButton( "ape" ) );
        mainPanel.add( new JButton( "cat" ) );
        mainPanel.add( new JButton( "<html>VERY<BR>VERY<br>TALL</html>" ) );
        
        frame.setContentPane( mainPanel );
        frame.pack();
        SandboxUtils.center( frame );
        frame.setVisible( true );
    }
}
