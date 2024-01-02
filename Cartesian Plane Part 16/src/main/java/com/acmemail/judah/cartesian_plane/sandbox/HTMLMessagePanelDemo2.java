package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.components.HTMLMessagePanel;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;

/**
 * Application to demonstrate the use
 * of the {@linkplain HTMLMessagePanel} class.
 * CSS and HTML text are read from
 * the SandboxDocs resource folder,
 * then used to instantiate a HTMLMessagePanel object.
 * A dialog is then extracted from the object
 * via the 
 * {@linkplain HTMLMessagePanel#getDialog(java.awt.Window, String)}
 * method.
 * 
 * @author Jack Straub
 */
public class HTMLMessagePanelDemo2
{
    /** System line separator. */
    private static final String newLine     = System.lineSeparator();
    /** Resource directory for sand box docs. */
    private static final String resDir      = "SandboxDocs/";
    /** Path to CSS file in resource directory. */
    private static final String cssFile     = resDir + "Jabberwocky.css";
    /** Path to HTML file in resource directory. */
    private static final String htmlFile    = resDir + "Jabberwocky.html";
    
    /** Style sheet compiled from CSS file */
    private final StyleSheet    styleSheet;
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
        HTMLMessagePanelDemo2    app = new HTMLMessagePanelDemo2();
        SwingUtilities.invokeLater( app::createGUI );
    }
    
    /**
     * Constructor.
     * Extracts the CSS and HTML text
     * from resource files.
     * 
     * @throws ComponentException if an unrecoverable error occurs
     */
    public HTMLMessagePanelDemo2()
    {
        styleSheet = getCSS( cssFile );
        text = getHTMLAsText( htmlFile );
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
        String          title       = "Show HTML Message Panel";
        JFrame          frame       = new JFrame( title );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );        
        HTMLMessagePanel    messagePanel    = 
            new HTMLMessagePanel( text, styleSheet );
        JDialog             messageDialog   = 
            messagePanel.getDialog( frame, title );
        
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
        frame.setVisible( true );
    }
    
    /**
     * Converts a resource file
     * containing CSS 
     * to a StyleSheet.
     * 
     * @param pathName  path to the file containing the CSS
     * 
     * @return  the converted StyleSheet
     * 
     * @throws ComponentException if an unrecoverable error occurs
     */
    private static StyleSheet getCSS( String pathName )
    {
        InputStream      inStream    = getResourceAsStream( pathName );
        HTMLEditorKit    kit         = new HTMLEditorKit();
        StyleSheet       styleSheet  = kit.getStyleSheet();       
        try (
            InputStreamReader reader = new InputStreamReader( inStream );
        )
        {
            styleSheet.loadRules( reader, null );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            throw new ComponentException( exc );
        }
        return styleSheet;
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
    private static String getHTMLAsText( String pathName )
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
                .peek( System.out::println )
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
