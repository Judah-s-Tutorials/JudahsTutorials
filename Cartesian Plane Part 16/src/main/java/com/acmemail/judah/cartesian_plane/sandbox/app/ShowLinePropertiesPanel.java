package com.acmemail.judah.cartesian_plane.sandbox.app;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.components.LinePropertiesPanel;

public class ShowLinePropertiesPanel
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
     */
    public static void main(String[] args)
    {
        ShowLinePropertiesPanel demo    = new ShowLinePropertiesPanel();
        SwingUtilities.invokeLater( () -> demo.buildGUI() );
    }

    private void buildGUI()
    {
        String  title   = "Show Line Properties Panel";
        JFrame  frame   = new JFrame( title );
        frame.setDefaultCloseOperation( JDialog.EXIT_ON_CLOSE );
        
        JPanel  panel   = new JPanel();
        panel.setBorder(
            BorderFactory.createEmptyBorder( 100, 100, 100, 100 )
        );
        JButton button  = new JButton( "Open Dialog" );
        panel.add( button );
        frame.setContentPane( panel );
        frame.setLocation( 200, 200 );
        frame.pack();
        
        JDialog dialog  = showDialog( frame );
        button.addActionListener( e -> dialog.setVisible( true ) );
        frame.setVisible( true );
    }
    
    private JDialog showDialog( JFrame frame )
    {
        JDialog dialog  = new JDialog( frame );
        dialog.setTitle( "Line Properties Dialog" );
        dialog.setLocation( 300, 300 );
        dialog.setContentPane( new LinePropertiesPanel() );
        dialog.pack();
        return dialog;
    }
}