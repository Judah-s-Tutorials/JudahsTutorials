package com.acmemail.judah.cartesian_plane.components;

import java.awt.Desktop;
import java.awt.Window;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.HyperlinkEvent;

import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;

/**
 * Encapsulates a dialog containing the text
 * in the About.html file
 * from the menu bar resource directory.
 * The text is displayed in an HTML-aware JTextPane,
 * and the dialog is provided by the 
 * {@linkplain HTMLMessagePanel} class.
 * <p>
 * Links from the About.html file
 * (and the About.html file <em>only</em>; see below)
 * are followed.
 * URLs (addresses, for example,
 * beginning with "https:")
 * are displayed in the system's default
 * web browser.
 * Non-URL addresses are assumed to be 
 * the names of files in the menu bar resource directory.
 * <p>
 * Linked files from the menu bar resource directory
 * are always displayed in new modal dialogs,
 * which are disposed as soon as they are closed.
 * Text from a file with a name ending in ".html"
 * will be displayed in an HTML-aware JEditorPane
 * BUT LINKS WILL NOT BE FOLLOWED.
 * Text from all other files
 * will be displayed in a SimpleMessageDialog.
 * 
 * @author Jack Straub
 */
public class AboutDialog
{
    /** System line separator. */
    private static final String newLine     = System.lineSeparator();
    /** Title for the ABOUT dialog. */
    private static final String aboutTitle  = "About This Application";
    /** Default resource directory for files. */
    private static final String defResDir   = "MenuBarDocs/";
    /** Default resource directory for files. */
    private static final String aboutFile   = "About.html/";

    /** Default resource directory for files. */
    private final String        resDir;
    /** Dialog parent; may be null. */
    private final Window        parent;
    /** This object's dialog. */
    private final JDialog       htmlDialog;
    
    /**
     * Constructor.
     * Creates dialog with null parent
     * and default resource directory.
     */
    public AboutDialog()
    {
        this( null,defResDir );
    }
    
    /**
     * Constructor.
     * Creates dialog with given parent
     * and default resource directory.
     */
    public AboutDialog( Window topWindow )
    {
        this( topWindow,defResDir );
    }

    /**
     * Constructor.
     * Creates dialog using given resource directory.
     * 
     * @param resDir    given resource directory
     * 
     * @throws ComponentException if dialog cannot be created
     */
    public AboutDialog( Window parent, String resDir )
    {
        this.parent = parent;
        this.resDir = resDir;
        
        String  text    = getText( aboutFile );
        HTMLMessagePanel    messagePanel    = 
            new HTMLMessagePanel( text, null );
        htmlDialog  = messagePanel.getDialog( parent, aboutTitle );
        messagePanel.addHyperlinkListener( this::hyperlinkUpdate );
    }
    
    /**
     * Returns the dialog produced by
     * an instance of this class.
     * 
     * @return  the dialog produced by this object
     */
    public JDialog getDialog()
    {
        return htmlDialog;
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
            String  text    = getText( fileName );
            String  lcFileName  = fileName.toLowerCase();
            htmlDialog.setVisible( false );
            if ( lcFileName.endsWith( ".html" ) )
                showHTMLDialog( text );
            else
                showSimpleText( text );
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
        JDialog dialog  = new SimpleMessageDialog( parent, text );
        dialog.setVisible( true );
        dialog.dispose();
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
        HTMLMessagePanel    messagePanel    = 
            new HTMLMessagePanel( text, null );
        
        JDialog dialog  = 
            messagePanel.getDialog( parent, text );
        dialog.setVisible( true );
        dialog.dispose();
    }
    
    /**
     * Returns the complete text from a file
     * with the given name.
     * Line separators are inserted
     * between lines from the file.
     * The file is assumed to be located
     * in the MenuBar resource directory.
     * 
     * @param fileName  the given file name
     * 
     * @return  
     *     all lines from the given file
     *     concatenated into a single String
     *     
     * @throws
     *      ComponentException if an error occurs
     *      while opening and reading the file
     */
    private String getText( String fileName )
    {
        String      pathName    = resDir + "/" + fileName;
        InputStream inStream    = getResourceAsStream( pathName );
        String      text        = null;
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
