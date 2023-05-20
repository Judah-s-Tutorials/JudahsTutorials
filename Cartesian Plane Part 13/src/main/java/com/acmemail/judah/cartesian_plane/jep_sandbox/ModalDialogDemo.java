package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class ModalDialogDemo
{
    public static void main(String[] args)
    {
        ModalDialogDemo demo    = new ModalDialogDemo();
        SwingUtilities.invokeLater( () -> demo.showGUI() );
    }
    
    private void showGUI()
    {
        JFrame  frame   = new JFrame( "Modal Dialog Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel  pane    = new JPanel( new BorderLayout() );
        pane.add( new ButtonPanel(), BorderLayout.CENTER );
        frame.setContentPane( pane );
        frame.pack();
        frame.setVisible( true );
    }
    
    @SuppressWarnings("serial")
    private static class ButtonPanel extends JPanel
    {
        public ButtonPanel()
        {
            super( new GridLayout( 1, 3, 3, 3 ) );
            Border  border  = BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
            setBorder( border );
            
            JButton okButton        = new JButton( "OK" );
            JButton cancelButton    = new JButton( "Cancel" );
            JButton exitButton      = new JButton( "Exit" );
            
            okButton.addActionListener( e -> showMessage( "OK" ) );
            cancelButton.addActionListener( e -> showMessage( "Cancel" ) );
            exitButton.addActionListener( e -> System.exit( 0 ) );
            
            add( okButton );
            add( cancelButton );
            add( exitButton );
        }
        
        private static void showMessage( String comment )
        {
            String  message = "You chose \"" + comment + "\"";
            JOptionPane.showMessageDialog( null, message );
        }
    }
}
