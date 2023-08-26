package com.acmemail.judah.cartesian_plane.sandbox.layout_managers;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.sandbox.SandboxUtils;

public class BoxLayoutDemo2
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> makeGUI() );
    }

    private static void makeGUI()
    {
        JFrame  frame       = new JFrame( "BoxLayout Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  mainPanel   = new JPanel();
        mainPanel.add( getHorizontalPanel() );
        mainPanel.add( getVerticalPanel() );
        
        frame.setContentPane( mainPanel );
        frame.pack();
        SandboxUtils.center( frame );
        frame.setVisible( true );
    }
    
    private static JPanel getHorizontalPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        Dimension   dim     = new Dimension( 10, 0 );
        Color       color   = Color.ORANGE;
        
        panel.setLayout( layout );
        panel.add( getLabel( "This is a -", color ) );
        panel.add( Box.createRigidArea( dim ) );
        panel.add( getLabel( "Horizontal -", color ) );
        panel.add( Box.createRigidArea( dim ) );
        panel.add( getLabel( "Layout", color ) );
        return panel;
    }
    
    private static JPanel getVerticalPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        Dimension   dim     = new Dimension( 0, 10 );
        Color       color   = Color.YELLOW;

        panel.setLayout( layout );
        panel.add( getLabel( "- This is a -", color ) );
        panel.add( Box.createRigidArea( dim ) );
        panel.add( getLabel( "- Vertical -", color ) );
        panel.add( Box.createRigidArea( dim ) );
        panel.add( getLabel( "- Layout -", color ) );
        return panel;
    }
    
    private static JLabel getLabel( String text, Color color )
    {
        JLabel  label   = new JLabel( text );
        label.setOpaque( true );
        label.setBackground( color );
        return label;
    }
}
