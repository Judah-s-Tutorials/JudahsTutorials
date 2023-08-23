package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class JColorChooserDemo3
{
    private static ColorDialog      dialog;    
    private static JPanel           feedback;
    
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> build() );
    }

    public static void build()
    {
        dialog = new ColorDialog();
        
        JFrame  frame   = new JFrame( "Feedback Window" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel  pane    = new JPanel( new BorderLayout() );
        feedback = new JPanel();
        feedback.setPreferredSize( new Dimension( 100, 100 ) );
        pane.add( feedback, BorderLayout.CENTER );
        
        JButton pushMe  = new JButton( "Push Me" );
        pane.add( pushMe, BorderLayout.SOUTH );
        pushMe.addActionListener( e -> {
           Color    color   = dialog.showDialog();
           if ( color != null )
               feedback.setBackground( color );
        });

        frame.setContentPane( pane );
        frame.setLocation( 100, 100 );
        frame.pack();
        frame.setVisible( true );
    }
    
    private static class ColorDialog extends JDialog
    {
        private final JColorChooser colorPane       = new JColorChooser();
        private Color               selectedColor   = null;
        
        public ColorDialog()
        {
            super( (Window)null, "Color Dialog" );
            this.setModal( true );
            JPanel  pane    = new JPanel( new BorderLayout() );
            pane.add( colorPane, BorderLayout.CENTER );
            pane.add( getButtonPanel(), BorderLayout.SOUTH );
            setContentPane( pane );
            pack();
        }
        
        public Color showDialog()
        {
            setVisible( true );
            return selectedColor;
        }
        
        private JPanel getButtonPanel()
        {
            JPanel  panel           = new JPanel();
            JButton okButton        = new JButton( "OK" );
            JButton cancelButton    = new JButton( "Cancel" );
            panel.add( okButton );
            panel.add( cancelButton );
            
            okButton.addActionListener( e -> {
                selectedColor = colorPane.getColor();
                setVisible( false );
            });
            
            cancelButton.addActionListener( e -> {
                selectedColor = null;
                setVisible( false );
            });
            return panel;
        }
    }
}
