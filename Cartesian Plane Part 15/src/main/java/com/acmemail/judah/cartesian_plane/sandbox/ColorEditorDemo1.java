package com.acmemail.judah.cartesian_plane.sandbox;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.components.ColorEditor;

public class ColorEditorDemo1
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(  () -> {
            ColorEditor editor  = new ColorEditor();
            JFrame      frame   = new JFrame( "Color Editor Demo1" );
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            
            JPanel      panel   = new JPanel();
            BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
            panel.setLayout( layout );
            panel.add( editor.getColorButton() );
            panel.add( editor.getTextEditor() );
            panel.add( editor.getFeedback() );
            frame.setContentPane( panel );
            frame.setLocation( 300, 300 );
            frame.pack();
            frame.setVisible( true );
        });
    }
}
