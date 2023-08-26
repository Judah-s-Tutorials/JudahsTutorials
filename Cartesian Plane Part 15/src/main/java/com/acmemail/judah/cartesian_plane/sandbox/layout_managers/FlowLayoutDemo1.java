package com.acmemail.judah.cartesian_plane.sandbox.layout_managers;

import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.sandbox.SandboxUtils;

public class FlowLayoutDemo1
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> buildGUI() );
    }

    private static void buildGUI()
    {
        JFrame  frame   = new JFrame( "FlowLayout Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  panel   = new JPanel(); // flow-layout added by default
        Stream.of( "Alice", "Frank", "Jane", "Joe", "Nancy" )
            .map( JButton::new )
            .forEach( panel::add );
        frame.setContentPane( panel );
        frame.pack();
        SandboxUtils.center( frame );
        frame.setVisible( true );
    }
}
