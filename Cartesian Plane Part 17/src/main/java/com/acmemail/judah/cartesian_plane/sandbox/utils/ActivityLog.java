package com.acmemail.judah.cartesian_plane.sandbox.utils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

/**
 * Implements a non-modal dialog
 * which can be used
 * for logging events
 * and other messages.
 * Text can be "styled"
 * using HTML and CSS, for example:
 * <pre>    append( "&lt;em>The sun was shining on the sea,&lt;/em>" );
 *     append( "&lt;strong>Shining with all his might,&lt;/strong>" );
 *     append(
 *         "He did his very best to make",
 *         "color: blue;"
 *     );
 *     append( 
 *         "The billows smooth and bright,",
 *         "color: green; font-size: 150%"
 *       );
 * </pre>
 * 
 * @author Jack Straub
 * 
 * @see ActivityLogDemo
 */
public class ActivityLog
{
    /** Pre-configured style string to make text black. */
    public static final String  STYLE_A     =
        "\"color: black;\"";
    /** Pre-configured style string to make text black and bold. */
    public static final String  STYLE_B     =
        "\"color: black; font-style: bold;\"";
    /** Pre-configured style string to make text red. */
    public static final String  STYLE_C     =
        "\"color: red;\"";
    /** Pre-configured style string to make text red and bold. */
    public static final String  STYLE_D     =
        "\"color: red; font-style: bold;\"";

    /** CSS for configuring body element. */
    private static final String bodyRule    = 
        "body {"
        + "margin-left: 2em;"
        + "font-family: Arial, Helvetica, sans-serif;"
        + " font-size:"
        + " 14;"
        + " min-width: 70em;"
        + " white-space: nowrap;}";
    
    /** Start of HTML. */
    private static final String htmlPrefix      =
        "<html><body><p>";
    /** Tail of HTML. */
    private static final String htmlSuffix      =
        "</p></body></html>";
    /** 
     * Contains most of the text for this page. It is updated
     * every time the append method is called.
     */
    private final   StringBuilder   html        = 
        new StringBuilder( htmlPrefix ); 
    
    /** HTML/CSS-aware component for displaying text. */
    private final   JEditorPane     textPane    = 
        new JEditorPane( "text/html", "" );
    /** Scroll pane containing text component. */
    JScrollPane scrollPane  = new JScrollPane( textPane );
    /** Dialog containing GUI. */
    private final   JDialog         dialog;
    
    /**
     * Default constructor.
     * Configures a dialog
     * with no parent, 
     * and a default title.
     */
    public ActivityLog()
    {
        this( null, "Activity Log" );
    }
    
    /**
     * Constructor.
     * Configures a dialog
     * with the given parent
     * and a default title.
     * 
     * @param parent    the given parent
     */
    public ActivityLog( Window parent )
    {
        this( parent, "Activity Log" );
    }
    
    /**
     * Constructor.
     * Configures a dialog
     * with no parent, 
     * and the given title.
     * 
     * @param title the given title
     */
    public ActivityLog( String title )
    {
        this( null, title );
    }
    
    /**
     * Constructor.
     * Configures a dialog
     * with the given parent, 
     * and the given title.
     * 
     * @param parent    the given parent
     * @param title     the given title
     */
    public ActivityLog( Window parent, String title )
    {
        dialog = new JDialog( parent, title );
        textPane.setText( htmlPrefix + htmlSuffix );
        Dimension   dim         = new Dimension( 300, 150 );
        scrollPane.setPreferredSize( dim );

        JPanel  contentPane = new JPanel( new BorderLayout() );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        dialog.setContentPane( contentPane );
        dialog.pack();
        dialog.setLocation( 200, 200 );
        dialog.setVisible( true );
        
        HTMLEditorKit   kit         = new HTMLEditorKit();
        textPane.setEditorKit( kit );
        StyleSheet      styleSheet  = kit.getStyleSheet();
        styleSheet.addRule( bodyRule );
    }
    
    /**
     * Changes the visibility
     * of the encapsulated dialog.
     * 
     * @param visible   true to make the dialog visible
     */
    public void setVisible( boolean visible )
    {
        dialog.setVisible( visible );
    }
    
    /**
     * Sets the location 
     * of the encapsulated dialog.
     * 
     * @param xco   desired x-coordinate of location
     * @param yco   desired y-coordinate of location
     */
    public void setLocation( int xco, int yco )
    {
        dialog.setLocation( xco, yco );
    }
    
    /**
     * Returns true
     * if the encapsulated dialog
     * is visible.
     * 
     * @return  true if this dialog is visible
     */
    public boolean isVisible()
    {
        return dialog.isVisible();
    }
    
    /**
     * Appends the given text
     * to this dialog.
     * May contain HTML tags
     * and style information
     * that would normally
     * be permissible
     * in the body
     * of an HTML document.
     * For example:
     * <ul>
     *      <li>"&lt;strong>Display this text in a bold font.&lt;/strong>"</li>
     *      <li>"&lt;span style='color: green;'>Display text in green.&lt;span>"</li>
     * </ul>
     * 
     * @param text  the given text
     */
    public void append( String text )
    {
        append( text, STYLE_C );
    }
    
    /**
     * Append the given text
     * and style information 
     * to this page.
     * If the given style
     * null or empty 
     * it is ignored.
     * Otherwise it,
     * and the given text,
     * are embedded in a span element
     * that includes the string
     * as the style attribute
     * of the span element.
     * For example, if:
     * <ul>
     * <li>text = "He did his very best to make"</li>
     * <li>style = "color: blue;"</li>
     * </ul>
     * The string appended
     * to this page would be:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&lt;span style="color: blue;">He did his very best to make&lt;/span>
     * 
     * @param text      the given text
     * @param style     the given style information
     */
    public void append( String text, String style )
    {
        boolean hasStyle    = style != null && !style.isEmpty();
        
        if ( hasStyle )
            html.append( "<span style=\"" ).append( style ).append( "\">" );
        html.append( text );
        if ( hasStyle )
            html.append( "</span>" );
        html.append( "<br>" );
        textPane.setText( html.toString() + htmlSuffix );
        int     len     = textPane.getDocument().getLength();
        textPane.setCaretPosition( len );
    }

    /**
     * 
     * Gets the preferred size of the dialog.
     *     
     * return  the preferred size of the dialog
     */
    public Dimension getPreferredSize()
    {
        Dimension   size    = dialog.getPreferredSize();
        return size;
    }

    /**
     * Sets the preferred size of the dialog.
     *     
     * @param size  the preferred size
     */
    public void setPreferredSize( Dimension size )
    {
        dialog.setPreferredSize( size );
        dialog.pack();
    }
}
