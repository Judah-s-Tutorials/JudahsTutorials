package com.acmemail.judah.cartesian_plane.sandbox;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.acmemail.judah.cartesian_plane.components.MessagePanel;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;

/**
 * Application to demonstrate the use
 * of the {@linkplain MessagePanel} class.
 * A MessagePanel object is instantiated
 * using CSS and HTML text from files
 * in the SandboxDocs resource folder.
 * A dialog is then extracted from the object
 * via the 
 * {@linkplain MessagePanel#getDialog(java.awt.Window, String)}
 * method.
 * 
 * @author Jack Straub
 */
public class MessagePanelDemo2
{
    /** Resource directory for sand box docs. */
    private static final String resDir      = "SandboxDocs/";
    /** Path to CSS file in resource directory. */
    private static final String cssFile     = resDir + "Jabberwocky.css";
    /** Path to HTML file in resource directory. */
    private static final String htmlFile    = resDir + "Jabberwocky.html";
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     * 
     * @throws ComponentException if an unrecoverable error occurs
     *
     */
    public static void main(String[] args)
    {
        MessagePanelDemo2    app = new MessagePanelDemo2();
        SwingUtilities.invokeLater( app::createGUI );
    }
    
    /**
     * Create and show the GUI.
     * The GUI consists of a "Show Dialog" button,
     * which makes the MessagePanel dialog visible,
     * and an "Exit" button,
     * which terminates the application.
     */
    private void createGUI()
    {
        String          title       = "Show HTML Message Panel";
        JFrame          frame       = new JFrame( title );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );        
        MessagePanel    msgPanel    = 
            MessagePanel.ofResource( htmlFile, cssFile );
        JDialog         msgDialog   = msgPanel.getDialog( frame, title );
        
        JButton     exit        = new JButton( "Exit" );
        JButton     show        = new JButton( "Show Dialog" );
        exit.addActionListener( e -> System.exit( 0 ) );
        show.addActionListener( e -> msgDialog.setVisible( true ) );
        
        Border      border      = 
            BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
        JPanel      pane        = new JPanel();
        pane.setBorder( border );
        pane.add( show );
        pane.add( exit );
        frame.setContentPane( pane );
        frame.pack();
        frame.setLocation( 200, 200 );
        msgDialog.setLocation( 200 + frame.getWidth(), 200 );
        frame.setVisible( true );
    }
}
