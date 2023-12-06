package com.acmemail.judah.cartesian_plane.sandbox;

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
 * <pre>
 * </pre>
 * 
 * @author Jack Straub
 */
public class ActivityLog
{
    public static final String  STYLE_A     =
        "\"color: black;\"";
    public static final String  STYLE_B     =
        "\"color: black; font-style: bold;\"";
    public static final String  STYLE_C     =
        "\"color: red;\"";
    public static final String  STYLE_D     =
        "\"color: red; font-style: bold;\"";

    private static final String bodyRule    = 
        "body {"
        + "margin-left: 2em;"
        + "font-family: Arial, Helvetica, sans-serif;"
        + " font-size:"
        + " 14;"
        + " min-width: 70em;"
        + " white-space: nowrap;}";
    
    private static final String htmlPrefix      =
        "<html><body><p>";
    private static final String htmlSuffix      =
        "</p></body></html>";
    private final   StringBuilder   html        = 
        new StringBuilder( htmlPrefix ); 
    
    private final   JEditorPane     textPane    = 
        new JEditorPane( "text/html", "" );
    private final   JDialog         dialog;
    
    public ActivityLog()
    {
        this( null, "Activity Log" );
    }
    
    public ActivityLog( Window parent )
    {
        this( parent, "Activity Log" );
    }
    
    public ActivityLog( String title )
    {
        this( null, title );
    }
    
    public ActivityLog( Window parent, String title )
    {
        dialog = new JDialog( parent, title );
        textPane.setText( htmlPrefix + htmlSuffix );
        JScrollPane scrollPane  = new JScrollPane( textPane );
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
    
    public void setVisible( boolean visible )
    {
        dialog.setVisible( visible );
    }
    
    public void setLocation( int xco, int yco )
    {
        dialog.setLocation( xco, yco );
    }
    
    public boolean isVisible()
    {
        return dialog.isVisible();
    }
    
    public void append( String text )
    {
        append( text, STYLE_C );
    }
    
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

}
