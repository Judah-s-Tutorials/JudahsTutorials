package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.HyperlinkEvent;

import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.components.HTMLMessagePanel;
import com.acmemail.judah.cartesian_plane.components.SimpleMessageDialog;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;

/**
 * Application to demonstrate the use
 * of the {@linkplain HTMLMessagePanel} class.
 * HTML text containing links is read from
 * the SandboxDocs resource folder,
 * then used to instantiate a HTMLMessagePanel object.
 * A dialog is then extracted from the object
 * via the 
 * {@linkplain HTMLMessagePanel#getDialog(java.awt.Window, String)}
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
 *      is opened in a dialog
 *      provided by HTMLMessagePanel.
 *      Note that links in the new HTMLMessagePanel
 *      are NOT followed.
 * </li>
 * <li>
 *      A link to any other type of file
 *      is assumed to contain text,
 *      and will be opened in a SimpleMessageDialog.
 * </li>
 * </ul>
 * 
 * @author Jack Straub
 */
public class HTMLMessagePanelDemo3
{
    /** System line separator. */
    private static final String newLine     = System.lineSeparator();
    /** Resource directory for sand box docs. */
    private static final String resDir      = "SandboxDocs/";
    /** Path to HTML file in resource directory. */
    private static final String htmlFile    = resDir + "Colorful.html";
    
    /** Top-level window for this application. */
    private JFrame              frame;
    /** Main dialog for this application. */
    private JDialog             messageDialog;
    /** For following links to local HTML files. */
    private HTMLMessagePanel    miscHTMLPanel;
    /** For following links to local text files. */
    private SimpleMessageDialog miscTextDialog;
    
    /** Text compiled from HTML file. */
    private final String        text;
    
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
        HTMLMessagePanelDemo3    app = new HTMLMessagePanelDemo3();
        SwingUtilities.invokeLater( app::createGUI );
    }
    
    /**
     * Constructor.
     * Extracts the CSS and HTML text
     * from resource files.
     * 
     * @throws ComponentException if an unrecoverable error occurs
     */
    public HTMLMessagePanelDemo3()
    {
        text = getText( htmlFile );
    }
    
    /**
     * Create and show the GUI.
     * The GUI consists of a "Show Dialog" button,
     * which makes the HTMLMessagePanel dialog visible,
     * and an "Exit" button,
     * which terminates the application.
     */
    private void createGUI()
    {
        String  title       = "Show HTML Message Panel";
        frame = new JFrame( title );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );    
        
        HTMLMessagePanel    messagePanel    = 
            new HTMLMessagePanel( text, null );
        messageDialog = messagePanel.getDialog( frame, title );
        messagePanel.addHyperlinkListener( this::hyperlinkUpdate );
        
        JButton     exit        = new JButton( "Exit" );
        JButton     show        = new JButton( "Show Dialog" );
        exit.addActionListener( e -> System.exit( 0 ) );
        show.addActionListener( e -> messageDialog.setVisible( true ) );
        
        Border      border      = 
            BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
        JPanel      pane        = new JPanel();
        pane.setBorder( border );
        pane.add( show );
        pane.add( exit );
        frame.setContentPane( pane );
        frame.pack();
        frame.setLocation( 200, 200 );
        messageDialog.setLocation( 200 + frame.getWidth(), 200 );
        
        miscHTMLPanel = new HTMLMessagePanel( "", null );
        miscTextDialog = new SimpleMessageDialog( frame, "" );
        frame.setVisible( true );
    }
    
    /**
     * Processes a hyperlink event.
     * A hyperlink event may be issued 
     * for types ENTER, EXIT or ACTIVATED.
     * Only ACTIVATED event types are processed;
     * other types are ignored.
     * 
     * @param evt   the hyperlink event object
     */
    private void hyperlinkUpdate( HyperlinkEvent evt )
    {
        HyperlinkEvent.EventType  type    = evt.getEventType();
        if ( type == HyperlinkEvent.EventType.ACTIVATED )
        {
            URL url = evt.getURL();
            if ( url != null )
                activateLink( url );
            else
                activateLink( evt.getDescription() );
        }
    }
    
    /**
     * Attempts to open a given URL
     * in the systems default web browser.
     * 
     * @param url   the given URL
     */
    private void activateLink( URL url )
    {
        Desktop desktop = Desktop.getDesktop();
        try
        {
            desktop.browse( url.toURI() );
        } 
        catch ( IOException | URISyntaxException exc )
        {
            exc.printStackTrace();
            messageDialog.setVisible( false );
            JOptionPane.showMessageDialog(
                frame, 
                exc.getMessage(),
                "Link Error",
                JOptionPane.ERROR_MESSAGE,
                null
            );
        }
    }
    
    /**
     * Attempts to display the text
     * of a given file in a new dialog.
     * If the name of the file
     * ends in ".html"
     * the text is displayed 
     * in a a dialog provided by 
     * {@link HTMLMessagePanel}, otherwise
     * the text is displayed in a 
     * in a new SimpleMessageDiaog.
     * The given file is assumed to reside
     * in the default resource directory.
     * If the file is successfully read
     * the ABOUT dialog is made invisible;
     * otherwise the ABOUT dialog is left visible
     * and an error message is displayed.
     * 
     * @param fileName  the given file name
     * 
     * @see #showHTMLDialog(String)
     * @see #showSimpleText(String)
     */
    private void activateLink( String fileName )
    {
        try
        {
            String  path    = resDir + "/" + fileName;
            String  text    = getText( path );
            String  lcFileName  = fileName.toLowerCase();
            messageDialog.setVisible( false );
            if ( lcFileName.endsWith( ".html" ) )
                showHTMLDialog( text );
            else
                showSimpleText( text );
        } 
        catch ( ComponentException exc )
        {
            exc.printStackTrace();
            JOptionPane.showMessageDialog(
                frame, 
                exc.getMessage(),
                "Link Error",
                JOptionPane.ERROR_MESSAGE,
                null
            );
        }
    }
    

    /**
     * Creates and displays a new SimpleMessageDialog
     * containing the given text.
     * The new dialog is disposed
     * immediately upon being closed.
     * 
     * @param text  the given text
     */
    private void showSimpleText( String text )
    {
        miscTextDialog.getTextArea().setText( text );
        miscTextDialog.pack();
        miscTextDialog.setVisible( true );
    }
    
    /**
     * Displays the given text
     * in a new dialog
     * provided by {@link HTMLMessageDialog}.
     * Hyperlinks in the dialog text
     * will not be followed.
     * The new dialog is disposed
     * immediately upon being closed.

     * @param text  the given text
     */
    private void showHTMLDialog( String text )
    {
        miscHTMLPanel.setText( text );
        JDialog dialog  = 
            miscHTMLPanel.getDialog( frame, text );
        dialog.setVisible( true );
    }

    /**
     * Reads text from a resource file.
     * The contents of the file
     * are returned as a single string,
     * with newlines inserted between individual lines
     * from the file. 
     * 
     * @param pathName  path to file containing HTML text
     * 
     * @return  the extracted text
     * 
     * @throws ComponentException if an unrecoverable error occurs
     */
    private static String getText( String pathName )
    {
        InputStream  inStream    = getResourceAsStream( pathName );
        String       text        = null;
        try (
            InputStreamReader reader = new InputStreamReader( inStream );
            BufferedReader bufReader = new BufferedReader( reader );
        )
        {
            StringBuilder    bldr    = new StringBuilder();
            bufReader.lines()
                .map( bldr::append )
//                .peek( System.out::println )
                .forEach( b -> b.append( newLine ) );
            text = bldr.toString();
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            throw new ComponentException( exc );
        }
        return text;
    }
    
    /**
     * Obtains an InputStream
     * for a resource file.
     * 
     * @param resource  path to resource file
     * 
     * @return  an InputStream linked to the given resource file
     * 
     * @throws ComponentException if an unrecoverable error occurs
     */
    private static InputStream getResourceAsStream( String resource )
    {
        ClassLoader loader      = PropertyManager.class.getClassLoader();
        InputStream inStream    = loader.getResourceAsStream( resource );
        if ( inStream == null )
        {
            String  msg = "Resource file \"" 
                + resource + "\" not found";
            System.err.println( msg );
            throw new ComponentException( msg );
        }
        return inStream;
    }
}
