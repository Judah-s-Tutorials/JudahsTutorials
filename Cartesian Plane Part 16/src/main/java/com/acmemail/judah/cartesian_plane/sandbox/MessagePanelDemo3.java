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
 * HTML text containing links is read from
 * the SandboxDocs resource folder,
 * then used to instantiate a MessagePanel object.
 * A dialog is then extracted from the object
 * via the 
 * {@linkplain MessagePanel#getDialog(java.awt.Window, String)}
 * method.
 * <p>
 * Links in the text are treated as follows:
 * </p>
 * <ul>
 * <li>
 *      When a link consists of a complete URL
 *      (addresses starting with, for example,
 *      "https://") it is opened
 *      in the system's default browser.
 * </li>
 * <li>
 *      A link to an HTML file
 *      not part of a complete URL
 *      is opened in the dialog
 *      provided by MessagePanel.
 * </li>
 * <li>
 *      A link to any other type of file
 *      is assumed to contain text,
 *      and will be opened in in the dialog
 *      provided by MessagePanel.

 * </li>
 * </ul>
 * 
 * @author Jack Straub
 */
public class MessagePanelDemo3
{
    /** Resource directory for sand box docs. */
    private static final String resDir      = "SandboxDocs/";
    /** Path to HTML file in resource directory. */
    private static final String htmlFile    = resDir + "StartPage.html";
    
    /** Top-level window for this application. */
    private JFrame              frame;
    /** Main dialog for this application. */
    private JDialog             msgDialog;
    
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
        MessagePanelDemo3    app = new MessagePanelDemo3();
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
        String  title       = "Show Message Panel";
        frame = new JFrame( title );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );    
        
        MessagePanel    msgPanel    = 
            MessagePanel.ofResource( htmlFile, null );
        msgDialog = msgPanel.getDialog( frame, title );
        
        JButton     exit        = new JButton( "Exit" );
        JButton     show        = new JButton( "Show Dialog" );
        exit.addActionListener( e -> System.exit( 0 ) );
        show.addActionListener( e -> msgPanel.setTextResource( htmlFile ) );
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
