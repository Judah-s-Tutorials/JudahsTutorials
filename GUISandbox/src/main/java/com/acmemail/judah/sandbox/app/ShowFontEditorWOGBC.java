package com.acmemail.judah.sandbox.app;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.sandbox.FontEditorWOGBC;

public class ShowFontEditorWOGBC
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> build() );
    }

    private static void build()
    {
        JFrame  frame   = new JFrame( "Font Editor Demo" );    
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel  pane    = new FontEditorWOGBC();
        Border  border  = BorderFactory.createEmptyBorder( 5, 5, 5, 5 );

        pane.setBorder( border );
        frame.setContentPane( pane );
        frame.pack();
        frame.setVisible( true );
    }
}
