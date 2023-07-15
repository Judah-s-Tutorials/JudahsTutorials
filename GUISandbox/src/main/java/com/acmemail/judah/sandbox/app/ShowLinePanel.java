package com.acmemail.judah.sandbox.app;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.acmemail.judah.sandbox.LinePanel;

public class ShowLinePanel
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> build() );
    }
    
    public static void build()
    {
        JFrame      frame   = new JFrame( "Properties Test" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setContentPane( new LinePanel() );
        frame.pack();
        frame.setVisible( true );
    }
}
