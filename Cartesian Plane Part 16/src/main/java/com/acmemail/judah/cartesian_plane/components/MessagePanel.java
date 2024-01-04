package com.acmemail.judah.cartesian_plane.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Window;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;

/**
 * This class consists of a set of components
 * that can be configured for use in a window.
 * The components can be obtained individually, or
 * configured as a JDialog.
 * The components are:
 * <ul>
 * <li>
 * The Editor Pane<br>
 * This is a JEditorPane that is configured 
 * to display HTML text.
 * If you obtain this component
 * (by calling {@linkplain #getEditorPane()})
 * the component will not be 
 * a part of any other container.
 * </li>
 * <li>
 * The Scroll Pane<br>
 * This is a JScrollPane that is configured 
 * with the editor pane as a view.
 * If you obtain this component
 * (by calling {@linkplain #getScrollPane()})
 * the component will not be 
 * a part of any other container.
 * </li>
 * <li>
 * The Message Dialog<br>
 * This is a JDialog that is configured 
 * with a JPanel that contains the scroll pane,
 * which will in turn contain the editor pane as a view.
 * </li>
 * </ul>
 * <p>
 * The goal of this class
 * is to be flexible,
 * but not infinitely so.
 * If you get the dialog component,
 * be aware that it will contain the scroll pane,
 * and you must not make the scroll pane
 * a child of another container.
 * 
 * @author Jack Straub
 */
public class MessagePanel
{
    /** Content type of an HTML document. */
    public static final String  HTML_TYPE   = "text/html";
    /** Content type of a plain text document. */
    public static final String  PLAIN_TYPE  = "text/plain";
    
    /** System line separator. */
    private static final String newLine     = System.lineSeparator();
    /** HTML/CSS-aware component for displaying text. */
    private final JEditorPane   editorPane;
    /** Editor kit associated with contained JEditorPane. */
    private final HTMLEditorKit editorKit   = new HTMLEditorKit();
    /** Scroll pane; usually with view set to textPane. */
    private final JScrollPane   scrollPane;
    /** Dialog containing this object's message panel. */
    private JDialog             dialog;

    /**
     * Constructor.
     * Creates all components except the dialog.
     * Configures the editor pane
     * with the given text and style sheet; 
     * if styleSheet is null, 
     * a default will be supplied.
     * Does not configure GUI hierarchy;
     * e.g., after construction
     * the scroll pane will have no view,
     * and the main panel will have no child components.
     * 
     * @param text          the given text
     * @param styleSheet    the given style sheet
     * 
     * @see #getScrollPane()
     * @see #getDialog(Window, String)
     */
    private MessagePanel( String text, String type, StyleSheet styleSheet )
    {
        editorKit.setStyleSheet( styleSheet );
        
        editorPane = new JEditorPane( type, text );
        editorPane.setFocusable( false );
        editorPane.setEditable( false );
        editorPane.addHyperlinkListener( this::hyperlinkUpdate );
        
        scrollPane = new JScrollPane();
        Dimension   dim = new Dimension( 500, 300 );
        scrollPane.setPreferredSize( dim );
        editorPane.setCaretPosition( 0 );
    }
    
    /**
     * Creates a MessagePanel
     * using the given text and StyleSheet.
     * The content-type is assumed to be text/html.
     * 
     * @param text          the given text
     * @param styleSheet    the given StyleSheet; may be null
     * 
     * @return  the created MessagePanel
     */
    public static MessagePanel of( String text, StyleSheet styleSheet )
    {
        MessagePanel    panel   = 
            new MessagePanel( text, HTML_TYPE, styleSheet );
        return panel;
    }
    
    /**
     * Creates a MessagePanel
     * using the given text and CSS.
     * The content-type is assumed to be text/html.
     * 
     * @param text  the given text
     * @param css   the given StyleSheet
     * <p>
     * Precondition: 
     * the given string be valid CSS syntax,
     * with the restrictions specified by
     * class javax.swing.text.html.CSS.
     * 
     * @return  the created MessagePanel
     */
    public static MessagePanel of( String text, String css )
    {
        StyleSheet      style   = getStyleSheetFromCSS( css );
        MessagePanel    panel   = 
            new MessagePanel( text, HTML_TYPE, style );
        return panel;
    }
    
    
    /**
     * Creates a MessagePanel
     * using the given text.
     * The content-type is assumed to be text/plain.
     * 
     * @param text          the given text
     * 
     * @return  the created MessagePanel
     */
    public static MessagePanel of( String text )
    {
        MessagePanel    panel   = 
            new MessagePanel( text, PLAIN_TYPE, null );
        return panel;
    }
    
    /**
     * Creates a MessagePanel
     * using the given text
     * from the given named resource.
     * The content-type is assumed to be text/html,
     * if the given name ends in ".HTML",
     * otherwise it is assumed to be text/plain.
     * 
     * @param resource  the given resource
     * 
     * @return  the created MessagePanel
     */
    public static MessagePanel ofResource( String resource )
    {
        String          testName    = resource.toUpperCase().strip();
        String          type        =
            testName.endsWith( ".HTML" ) ? HTML_TYPE : PLAIN_TYPE;
        InputStream     inStream    = getResourceAsStream( resource );
        String          text        = getText( inStream );
        MessagePanel    panel       = new MessagePanel( text, type, null );
        return panel;
    }
    
    /**
     * Creates a MessagePanel
     * using two resources;
     * the first resource is used as the source of the text,
     * and the second is either null
     * or the source of CSS data
     * that will be compiled into
     * a StyleSheet.
     * The content-type is assumed to be text/html.
     * <p>
     * Precondition: 
     * if non-null,
     * the resource be valid CSS syntax,
     * with the restrictions specified by
     * class javax.swing.text.html.CSS.
     * 
     * @param textRes   the given text resource
     * @param cssRes    the given CSS resource; may be null
     * 
     * @return  the created MessagePanel
     */
    public static MessagePanel ofResource( String textRes, String cssRes )
    {
        InputStream     inStream    = getResourceAsStream( textRes );
        StyleSheet      styleSheet  = null;
        String          text        = getText( inStream );
        if ( cssRes != null )
            styleSheet  = getStyleSheetFromResource( cssRes );
        MessagePanel    panel       = 
            new MessagePanel( text, HTML_TYPE, styleSheet );
        return panel;
    }
    
    /**
     * Incorporates the given StyleSheet
     * 
     * @param sheet the given StyleSheet
     */
    public void setStyleSheet( StyleSheet sheet )
    {
        editorKit.setStyleSheet( sheet );
        editorPane.setText( editorPane.getText() );
        
        if ( dialog != null )
            dialog.pack();
    }
    
    /**
     * Compiles and incorporates a StyleSheet
     * using CSS data from the given string.
     * <p>
     * Precondition: 
     * the given string must be valid CSS syntax,
     * with the restrictions specified by
     * class javax.swing.text.html.CSS.
     * 
     * @param css   the given string
     */
    public void setStyleSheet( String css )
    {
        StyleSheet  sheet   = getStyleSheetFromCSS( css );
        setStyleSheet( sheet );
    }
    
    /**
     * Compiles and incorporates a StyleSheet
     * using CSS data from the given resource.
     * <p>
     * Precondition: 
     * the given resource must be valid CSS syntax,
     * with the restrictions specified by
     * class javax.swing.text.html.CSS.
     * 
     * @param resource  the given resource
     * 
     * @throws ComponentException if an error occurs
     */
    public void setStyleSheetFromResource( String resource )
    {
        StyleSheet  sheet   = getStyleSheetFromResource( resource );
        setStyleSheet( sheet );
    }
    
    /**
     * Gets the StyleSheet currently incorporated
     * in this MessagePanel.
     * 
     * @return  
     *      the StyleSheet currently incorporated
     *      in this MessagePanel
     */
    public StyleSheet getStyleSheet()
    {
        StyleSheet  sheet   = editorKit.getStyleSheet();
        return sheet;
    }
    
    /**
     * Sets the text of this object's editor pane
     * to the given String.
     * 
     * @param text  the given String
     */
    public void setText( String text )
    {
        editorPane.setText( text );
        editorPane.setCaretPosition( 0 );

        if ( dialog != null )
            dialog.pack();
    }
    
    /**
     * Sets the text of this object's editor pane
     * to from the given resource.
     * 
     * @param resource  the given resource
     * 
     * @throws ComponentException if an error occurs
     */
    public void setTextResource( String resource )
    {
        activateLink( resource );
    }
    
    /**
     * Gets the text of this object's editor pane.
     * 
     * @return  the text of this object's editor pane
     */
    public String getText()
    {
        return editorPane.getText();
    }
    
    /**
     * Sets the content-type of the encapsulated JEditorPane
     * to the given string.
     * 
     * @param type  the given string
     */
    public void setContentType( String type )
    {
        editorPane.setContentType( type );
        if ( dialog != null )
            dialog.pack();
    }
    
    /**
     * Gets the content-type of the encapsulated JEditorPane.
     * 
     * @return  the content type of this object's editor pane
     */
    public String getContentType()
    {
        return editorPane.getText();
    }
    
    /**
     * Adds a given HyperLinkListener to the JEditorPane
     * encapsulated in this panel.
     * 
     * @param listener  the given HyperLinkListener
     */
    public void addHyperlinkListener( HyperlinkListener listener )
    {
        editorPane.addHyperlinkListener( listener );
    }
    
    /**
     * Gets the JEditorPane component.
     * 
     * @return  the JEditorPane component of this object
     */
    public JEditorPane getEditorPane()
    {
        return editorPane;
    }
    
    /**
     * Gets the JScrollPane component.
     * 
     * @return  the JScrollPane component of this object
     */
    public JScrollPane getScrollPane()
    {
        Component   comp    = scrollPane.getViewport().getView();
        if ( comp == null )
            scrollPane.setViewportView( editorPane );
        return scrollPane;
    }
    
    /**
     * Gets the dialog created for this object.
     * If necessary, the object will be created
     * with the given parent and title.
     * <p>
     * It is safe to call this method multiple times,
     * however after the dialog is initially created
     * the given parent and title will be ignored.
     * 
     * @param parent    the given parent
     * @param title     the given title
     * 
     * @return  the dialog created for this object
     */
    public JDialog getDialog( Window parent, String title )
    {
        if ( dialog == null )
            createDialog( parent, title );
        return dialog;
    }
    
    /**
     * Compiles a StyleSheet from a given string.
     * <p>
     * Precondition: 
     * the string must be valid CSS syntax,
     * with the restrictions specified by
     * class javax.swing.text.html.CSS.
     * 
     * @param css   the given string
     * 
     * @return  the compiled StyleSheet
     * 
     * @throws ComponentException if an error occurs
     */
    private static StyleSheet getStyleSheetFromCSS( String css )
    {
        StyleSheet              styleSheet  = null;
        try ( ByteArrayInputStream inStream    = 
            new ByteArrayInputStream( css.getBytes() )
        )
        {
            styleSheet = getStyleSheet( inStream );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            throw new ComponentException( exc );
        }
        return styleSheet;
    }
    
    /**
     * Reads a string from a given resource
     * and compiles it into a StyleSheet.
     * <p>
     * Precondition: 
     * data from the stream must be valid CSS syntax,
     * with the restrictions specified by
     * class javax.swing.text.html.CSS.
     * 
     * @param resource  the given resource
     * 
     * @return  the compiled StyleSheet
     * 
     * @throws ComponentException if an error occurs
     */
    private static StyleSheet getStyleSheetFromResource( String resource )
    {
        StyleSheet              styleSheet  = null;
        try ( InputStream inStream  = getResourceAsStream( resource ) )
        {
            styleSheet = getStyleSheet( inStream );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            throw new ComponentException( exc );
        }
        return styleSheet;
    }
    
    /**
     * Reads a given input stream,
     * compiling the data into a StyleSheet.
     * <p>
     * Precondition: 
     * data from the stream must be valid CSS syntax,
     * with the restrictions specified by
     * class javax.swing.text.html.CSS.
     * 
     * @param inStream  the given input stream
     * 
     * @return  the compiles StyleSheet
     * 
     * @throws ComponentException if an error occurs
     */
    private static StyleSheet getStyleSheet( InputStream inStream )
    {
        StyleSheet  styleSheet  =  new StyleSheet();       
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
            JOptionPane.showMessageDialog(
                null, 
                exc.getMessage(),
                "Link Error",
                JOptionPane.ERROR_MESSAGE,
                null
            );
        }
    }
    
    /**
     * Attempts to display the text
     * of a given resource file in a new dialog.
     * If the name of the file
     * ends in ".html"
     * the content-type is assumed to be text/html,
     * otherwise the content-type is assumed to be text/plain.
     * 
     * @param resource  the given resource file name
     */
    private void activateLink( String resource )
    {
        try
        {
            String      contentType = null;
            InputStream inStream    = getResourceAsStream( resource );
            String      text        = getText( inStream );
            String      lcFileName  = resource.toLowerCase().trim();
            if ( lcFileName.endsWith( ".html" ) )
                contentType = HTML_TYPE;
            else
                contentType = PLAIN_TYPE;
            editorPane.setContentType( contentType );
            editorPane.setText( text );
            editorPane.setCaretPosition( 0 );
            if ( dialog != null )
                dialog.pack();
        } 
        catch ( ComponentException exc )
        {
            exc.printStackTrace();
            JOptionPane.showMessageDialog(
                null, 
                exc.getMessage(),
                "Link Error",
                JOptionPane.ERROR_MESSAGE,
                null
            );
        }
    }

    /**
     * Creates the dialog for this object,
     * using the given parent and title.
     * 
     * @param parent    the given parent
     * @param title     the given title
     */
    private void createDialog( Window parent, String title )
    {
        dialog = new JDialog( parent, title );
        
        JPanel  cPane   = new JPanel( new BorderLayout() );
        cPane.add( getScrollPane(), BorderLayout.CENTER );
        
        JPanel  controls    = new JPanel();
        JButton close       = new JButton( "Close" );
        close.addActionListener( e -> dialog.setVisible( false ) );
        
        controls.add( close );
        cPane.add( controls, BorderLayout.SOUTH );
        dialog.setModal( true );
        dialog.setContentPane( cPane );
        dialog.getRootPane().setDefaultButton( close );
        dialog.pack();
    }

    /**
     * Reads text from a given input stream.
     * The contents of the file
     * are returned as a single string,
     * with newlines inserted between individual lines
     * from the stream. 
     * 
     * @param inStream  the given input stream
     * 
     * @return  the extracted text
     * 
     * @throws ComponentException if an unrecoverable error occurs
     */
    private static String getText( InputStream inStream )
    {
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
