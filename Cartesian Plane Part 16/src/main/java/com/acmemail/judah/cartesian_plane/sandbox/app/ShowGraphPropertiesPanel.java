package com.acmemail.judah.cartesian_plane.sandbox.app;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.components.GraphPropertiesPanel;

/**
 * Application to demonstrate
 * the LinePropertiesPanel class
 * from the project ...components package.
 * 
 * @author Jack Straub
 */
public class ShowGraphPropertiesPanel
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
     */
    public static void main(String[] args)
    {
        ShowGraphPropertiesPanel demo    = new ShowGraphPropertiesPanel();
        SwingUtilities.invokeLater( () -> demo.buildGUI() );
    }

    /**
     * Create and show the GUI.
     */
    private void buildGUI()
    {
        String  title   = "Show Graph Properties Panel";
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
        
        JDialog dialog  = GraphPropertiesPanel.getDialog( frame );
        button.addActionListener( e -> dialog.setVisible( true ) );
        frame.setVisible( true );
        dialog.setVisible( true );
    }
}