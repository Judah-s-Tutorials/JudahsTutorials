package com.acmemail.judah.cartesian_plane.sandbox;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ActivityLogDemo
{
    private ActivityLog dialog;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        ActivityLogDemo demo    = new ActivityLogDemo();
        SwingUtilities.invokeLater( () -> demo.buildGUI() );
    }
    
    private void buildGUI()
    {
        JFrame      frame   = new JFrame( "Activity Log Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        dialog = new ActivityLog( frame );
        
        JButton showButton  = new JButton( "Show Dialog" );
        JButton exitButton  = new JButton( "Exit" );
        showButton.addActionListener( e -> dialog.setVisible( true ) );
        exitButton.addActionListener( e -> System.exit( 0 ) );
        
        JPanel      pane    = new JPanel();
        pane.add( showButton );
        pane.add( exitButton );
        frame.setContentPane( pane );
        frame.pack();
        
        frame.setLocation( 100, 100 );
        dialog.setLocation( 100 + frame.getWidth(), 100 );
        frame.setVisible( true );
        
        demo();
    }
    
    private void demo()
    {
        dialog.append( "<em>The sun was shining on the sea,</em>" );
        dialog.append( "<strong>Shining with all his might,</strong>" );
        dialog.append( 
            "He did his very best",
            "color: blue;"
        );
        dialog.append( 
            "To make the billows smooth and bright,",
            "color: green; font-size: 150%"
        );
    }
}
