package com.acmemail.judah.sandbox.sandbox;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CheckBoxTest
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> build() );
    }

    private static void build()
    {
        JFrame      frame       = new JFrame( "Check Box Test" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      contentPane = new JPanel( new BorderLayout() );
        JCheckBox   checkBox    = new JCheckBox( "Tester" );
        JButton     button1     = new JButton( "Set Selected" );
        JButton     button2     = new JButton( "Do Click" );
        
        JPanel      buttonPanel = new JPanel();
        buttonPanel.add( button1 );
        buttonPanel.add( button2 );
        frame.setContentPane( contentPane );

        contentPane.add( checkBox, BorderLayout.CENTER );
        contentPane.add( buttonPanel, BorderLayout.SOUTH );
        
        checkBox.addActionListener( e -> 
            System.out.println( checkBox.isSelected() )
        );
        button1.addActionListener( e -> {
            boolean val = !checkBox.isSelected();
            checkBox.setSelected( val );
        });
        
        button2.addActionListener( e -> checkBox.doClick() );
        
        frame.pack();
        frame.setVisible( true );
    }
}
