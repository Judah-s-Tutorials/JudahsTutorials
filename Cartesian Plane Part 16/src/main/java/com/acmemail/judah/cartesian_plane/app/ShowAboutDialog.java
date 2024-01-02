package com.acmemail.judah.cartesian_plane.app;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.components.AboutDialog;

public class ShowAboutDialog
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> createGUI() );
    }
    
    public static void createGUI()
    {
        String      title       = "Show About Dialog";
        JFrame      frame       = new JFrame( title );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );  
        
        JDialog     dialog      = new AboutDialog().getDialog();
        
        JButton     exit        = new JButton( "Exit" );
        JButton     show        = new JButton( "Show Dialog" );
        exit.addActionListener( e -> System.exit( 0 ) );
        show.addActionListener( e -> dialog.setVisible( true ) );
        
        Border      border      = 
            BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
        JPanel      pane        = new JPanel();
        pane.setBorder( border );
        pane.add( show );
        pane.add( exit );
        frame.setContentPane( pane );
        frame.pack();
        frame.setLocation( 200, 200 );
        dialog.setLocation( 200 + frame.getWidth(), 200 );
        frame.setVisible( true );
    }

}
