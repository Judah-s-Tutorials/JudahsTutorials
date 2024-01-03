package com.acmemail.judah.cartesian_plane.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;

/**
 * This class consists of a set of components
 * that can be configured for use in a window.
 * The components can be obtained individually,
 * configured as a JPanel or
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
 * (by calling {@linkplain #scrollPane()})
 * the component will not be 
 * a part of any other container.
 * </li>
 * <li>
 * The Message Panel<br>
 * This is a JPanel that is configured 
 * with the scroll pane as a child;
 * note that the scroll pane
 * will likely contain the editor pane as a view.
 * If you obtain this component
 * (by calling {@linkplain #getMessagePanel()})
 * the component will not be 
 * a part of any other container.
 * </li>
 * <li>
 * The Message Dialog<br>
 * This is a JDialog that is configured 
 * with the message panel as a content pane.
 * Note that the message panel
 * will contain the scroll pane,
 * which will in turn contain the editor pane as a view.
 * </li>
 * </ul>
 * <p>
 * The goal of this class
 * is to be flexible,
 * but not infinitely so.
 * If you get the dialog component,
 * be aware that it will contain the message panel,
 * and you must not make the message panel
 * a child of another container.
 * Likewise, if you get the message panel
 * and add it to a container,
 * getting the dialog would make the message panel
 * a child of two different containers,
 * which is not allowed.
 * @author Jack Straub
 */
public class MessagePanel extends JPanel
{
    public static final String  HTML_TYPE   = "text/html";
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
    
    /** Default CSS for configuring body element. */
//    private static final String bodyRule    = 
//        "body {"
//        + "margin-left: 2em;"
//        + "font-family: Arial, Helvetica, sans-serif;"
//        + " font-size:"
//        + " 14;"
//        + " min-width: 70em;"
//        + " white-space: nowrap;}";

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
     * @see #getMessagePanel()
     * @see #getScrollPane()
     * @see #getDialog(Window, String)
     */
    private MessagePanel( String text, String type, StyleSheet styleSheet )
    {
        editorKit.setStyleSheet( styleSheet );
        
        editorPane = new JEditorPane( type, text );
        editorPane.setFocusable( false );
        scrollPane = new JScrollPane( editorPane );
        editorPane.setEditable( false );
        editorPane.setCaretPosition( 0 );
        Dimension   dim         = new Dimension( 500, 300 );
        scrollPane.setPreferredSize( dim );
    }
    
    public static MessagePanel of( String text, StyleSheet styleSheet )
    {
        MessagePanel    panel   = 
            new MessagePanel( text, HTML_TYPE, styleSheet );
        return panel;
    }
    
    public static MessagePanel of( String text, String css )
    {
        StyleSheet      style   = getStyleSheetFromCSS( css );
        MessagePanel    panel   = 
            new MessagePanel( text, HTML_TYPE, style );
        return panel;
    }
    
    public static MessagePanel of( String text )
    {
        MessagePanel    panel   = 
            new MessagePanel( text, PLAIN_TYPE, null );
        return panel;
    }
    
    public static MessagePanel ofResource( String resource )
    {
        String          testName    = resource.toUpperCase().strip();
        String          type        =
            testName.endsWith( "HTML" ) ? HTML_TYPE : PLAIN_TYPE;
        InputStream     inStream    = getResourceAsStream( resource );
        String          text        = getText( inStream );
        MessagePanel    panel       = new MessagePanel( text, type, null );
        return panel;
    }
    
    public static MessagePanel ofResource( String textRes, String cssRes )
    {
        InputStream     inStream    = getResourceAsStream( textRes );
        StyleSheet      styleSheet  = getStyleSheetFromResource( cssRes );
        String          text        = getText( inStream );
        MessagePanel    panel       = 
            new MessagePanel( text, HTML_TYPE, styleSheet );
        return panel;
    }
    
    public void setStyleSheet( String css )
    {
        StyleSheet  sheet   = getStyleSheetFromCSS( css );
        editorKit.setStyleSheet( sheet );
    }
    
    public void setStyleSheet( StyleSheet sheet )
    {
        editorKit.setStyleSheet( sheet );
    }
    
    public void setStyleSheetFromResource( String resource )
    {
        StyleSheet  sheet   = getStyleSheetFromResource( resource );
        editorKit.setStyleSheet( sheet );
    }
    
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
     * Sets the text of this object's editor pane
     * to the given String.
     * 
     * @param text  the given String
     */
    public void setText( String text )
    {
        editorPane.setText( text );
        if ( dialog != null )
            dialog.pack();
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
    
    public void addHyperlinkListener( HyperlinkListener listener )
    {
        editorPane.addHyperlinkListener( listener );
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
        
        JPanel  controls    = new JPanel();
        JButton close       = new JButton( "Close" );
        close.addActionListener( e -> dialog.setVisible( false ) );
        
        controls.add( close );
        add( controls, BorderLayout.SOUTH );
        dialog.setModal( true );
        dialog.setContentPane( this );
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
