package com.acmemail.judah.mockito_sandbox;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( () -> new App().buildGUI() );
    }
    
    public int get()
    {
        return 5;
    }
    
    private void buildGUI()
    {
        JFrame  frame       = new JFrame( "Mockito Test ");
        JPanel  pane        = new JPanel();
        JButton exit        = new JButton( "Exit" );
        exit.addActionListener( e -> System.exit( 0 ) );
        pane.add( exit );
        frame.setContentPane( pane );
        
        frame.getRootPane().setDefaultButton( exit );
        
        frame.setLocation( 200, 200 );
        frame.pack();
        frame.setVisible( true );
    }
}
