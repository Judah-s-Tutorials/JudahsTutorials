package com.acmemail.judah.cartesian_plane.sandbox.layout_managers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.sandbox.SandboxUtils;

public class BorderLayoutDemo1
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> buildGUI() );
    }

    private static void buildGUI()
    {
        JFrame  frame   = new JFrame( "FlowLayout Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  panel   = new JPanel( new BorderLayout() );
        
        JPanel  center  = getPanel( "Center", Color.CYAN, 300, 300 );
        panel.add( center, BorderLayout.CENTER );
        
        JPanel  north   = getPanel( "North", Color.ORANGE, 300, 50 );
        panel.add( north, BorderLayout.NORTH );
        
        JPanel  east    = getPanel( "East", Color.MAGENTA, 50, 100 );
        panel.add( east, BorderLayout.EAST );
        
        JPanel  south   = getPanel( "South", Color.GREEN, 300, 50 );
        panel.add( south, BorderLayout.SOUTH );
        
        JPanel  west    = getPanel( "West", Color.YELLOW, 50, 300 );
        panel.add( west, BorderLayout.WEST );
        
        frame.setContentPane( panel );
        frame.pack();
        SandboxUtils.center( frame );
        frame.setVisible( true );
    }
    
    private static 
    JPanel getPanel( String label, Color color, int width, int height )
    {
        Dimension   dim     = new Dimension( width, height );
        JPanel      panel   = new JPanel();
        panel.setPreferredSize( dim );
        panel.setBackground( color );
        panel.add( new JLabel( label ) );
        return panel;
    }
}
