package com.acmemail.judah.cartesian_plane.sandbox.layout_managers;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.sandbox.SandboxUtils;

public class BoxLayoutDemo3c
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> makeGUI() );
    }

    private static void makeGUI()
    {
        JFrame  frame       = new JFrame( "BoxLayout Demo 3" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  mainPanel   = getVerticalPanel();
        
        frame.setContentPane( mainPanel );
        frame.pack();
        SandboxUtils.center( frame );
        frame.setVisible( true );
    }
    
    private static JPanel getVerticalPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );

        panel.setLayout( layout );
        JLabel      shortLabel  = new JLabel( "Short" );
        JLabel      longLabel   = new JLabel( "Really, Really long Label" );
        JLabel      medLabel    = new JLabel( "Mid-size label" );
        JCheckBox   shortCBox   = new JCheckBox( "Once" );
        JCheckBox   longCBox    = new JCheckBox( "Many, many, many times" );
        JCheckBox   medCBox     = new JCheckBox( "Occasionally" );
        
        float       boxHPos     = JComponent.CENTER_ALIGNMENT;
        shortLabel.setAlignmentX( boxHPos );
        longLabel.setAlignmentX( boxHPos );
        medLabel.setAlignmentX( boxHPos );
        shortCBox.setAlignmentX( boxHPos );
        longCBox.setAlignmentX( boxHPos );
        medCBox.setAlignmentX( boxHPos );
        
        panel.add( shortLabel );
        panel.add( medLabel );
        panel.add( longLabel );
        panel.add( shortCBox );
        panel.add( longCBox );
        panel.add( medCBox );
        return panel;
    }
}
