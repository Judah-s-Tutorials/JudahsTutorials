package com.acmemail.judah.cartesian_plane.app;

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

public class ShowAboutDialog_orig
{
    /** System line separator. */
    private static final String newLine     = System.lineSeparator();
    /** Resource directory for sand box docs. */
    private static final String resDir      = "MenuBarDocs/";
//    /** Path to CSS file in resource directory. */
//    private static final String cssFile     = resDir + "About.css";
    /** Path to HTML file in resource directory. */
    private static final String htmlFile    = resDir + "About.html";
    
    /** Text compiled from HTML file. */
    private final String        text;
    /** Dialog encapsulating HTMLMessagePanel. */
    private JDialog             htmlDialog;

    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        ShowAboutDialog_orig app = new ShowAboutDialog_orig();
        SwingUtilities.invokeLater( app::createGUI );
    }

    public ShowAboutDialog_orig()
    {
        text = getHTMLAsText( htmlFile );   
    }
    
    public void createGUI()
    {
        String          title       = "Show About Dialog";
        JFrame          frame       = new JFrame( title );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );        
        HTMLMessagePanel    messagePanel    = 
            new HTMLMessagePanel( text, null );
        htmlDialog  = messagePanel.getDialog( frame, title );
        messagePanel.addHyperlinkListener( this::hyperlinkUpdate );
        
        JButton     exit        = new JButton( "Exit" );
        JButton     show        = new JButton( "Show Dialog" );
        exit.addActionListener( e -> System.exit( 0 ) );
        show.addActionListener( e -> htmlDialog.setVisible( true ) );
        
        Border      border      = 
            BorderFactory.createEmptyBorder( 10, 10, 10, 10 );
        JPanel      pane        = new JPanel();
        pane.setBorder( border );
        pane.add( show );
        pane.add( exit );
        frame.setContentPane( pane );
        frame.pack();
        frame.setLocation( 200, 200 );
        htmlDialog.setLocation( 200 + frame.getWidth(), 200 );
        frame.setVisible( true );
    }
    
    private void hyperlinkUpdate( HyperlinkEvent evt )
    {
        String  source      = evt.getSource().getClass().getSimpleName();
        String  evtType     = getHyperLinkEventType( evt.getEventType() );
        URL     url         = evt.getURL();
        String  desc        = evt.getDescription();
        
        StringBuilder   bldr    = new StringBuilder()
            .append( "Source: " ).append( source ).append( newLine )
            .append( "Event Type: " ).append( evtType ).append( newLine )
            .append( "Description: " ).append( evt.getDescription() ).append( newLine )
            .append( "URL: " ).append( url ).append( newLine );
        System.out.println( bldr );
        
        if ( evtType.equals( "ACTIVATED" ) )
        {
            if ( url != null )
                activateLink( url );
            else if ( desc != null )
                activateLink( desc );
            else
                ;
        }
    }
    
    private String 
    getHyperLinkEventType( HyperlinkEvent.EventType type )
    {
        String  evtType = null;
        
        if ( type == HyperlinkEvent.EventType.ACTIVATED )
            evtType = "ACTIVATED";
        else if ( type == HyperlinkEvent.EventType.ENTERED )
            evtType = "ENTERED";
        else if ( type == HyperlinkEvent.EventType.EXITED )
            evtType = "EXITED";
        else
            evtType = "UNKNOWN";
        
        return evtType;
    }
    
    private void activateLink(URL url)
    {
        Desktop desktop = Desktop.getDesktop();
        try
        {
            desktop.browse( url.toURI() );
        } 
        catch (IOException | URISyntaxException exc)
        {
            exc.printStackTrace();
            htmlDialog.setVisible( false );
            JOptionPane.showMessageDialog(
                htmlDialog, 
                exc.getMessage(),
                "Link Error",
                JOptionPane.ERROR_MESSAGE,
                null
            );
        }
    }
    
    private void activateLink( String fileName )
    {
        try
        {
            htmlDialog.setVisible( false );
            String  path    = resDir + "/" + fileName;
            String  text    = getHTMLAsText( path );
            String  lcFileName  = fileName.toLowerCase();
            if ( !lcFileName.endsWith( ".html" ) )
                showSimpleText( text );
            else
                showHTMLDialog( text );
        } 
        catch ( ComponentException exc )
        {
            exc.printStackTrace();
            JOptionPane.showMessageDialog(
                htmlDialog, 
                exc.getMessage(),
                "Link Error",
                JOptionPane.ERROR_MESSAGE,
                null
            );
        }
    }
    
    private void showSimpleText( String text )
    {
        JDialog dialog  = new SimpleMessageDialog( htmlDialog, text );
        dialog.setVisible( true );
        dialog.dispose();
    }
    
    private void showHTMLDialog( String fileName )
    {
        HTMLMessagePanel    messagePanel    = 
            new HTMLMessagePanel( text, null );
        
        JDialog dialog  = 
            messagePanel.getDialog( htmlDialog, fileName );
        dialog.setVisible( true );
        dialog.dispose();
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
