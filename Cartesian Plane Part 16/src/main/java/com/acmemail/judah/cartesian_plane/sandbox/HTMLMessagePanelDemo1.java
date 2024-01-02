package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import com.acmemail.judah.cartesian_plane.components.HTMLMessagePanel;

/**
 * Application to illustrate the use
 * of the {@linkplain HTMLMessagePanel} class.
 * Several hard-coded CSS rules 
 * are added to the default style sheet.
 * The style sheet and hard-coded HTML text
 * are then used to instantiate the HTMLMessagePanel.
 * A dialog is then extracted from the object
 * via the 
 * {@linkplain HTMLMessagePanel#getDialog(java.awt.Window, String)}
 * method.
 * 
 * @author Jack Straub
 */
public class HTMLMessagePanelDemo1
{
    /** CSS rule for configuring the HTML body element. */
    private static final String bodyRule    =
        "body"
        + "{"
        + " background-color: #9FE2BF"
        + " margin: 2em 2em 2em 2em; "
        + " font-family: fantasy;"
        + "}";
    
    /** CSS rule for configuring a class "s" HTML paragraph element. */
    private static final String psRule      =
        "p.s"
        + "{"
        + " color: #ffd700;"
        + " background-color: #b22222;"
        + " font-size: 200%;"
        + " font-weight: bold;"
        + "}";
    
    /** HTML text to display in the sample HTMLMessagePanel. */
    private static final String htmlText    =
        "<html> "
        + "<body> "
        + "<p class='s'>"
        + "The sun was shining on the sea,<br>"
        + "Shining with all his might;<br>"
        + "He did his very best to make<br>"
        + "The billows smooth and bright.<br>"
        + "And this was odd because it was<br>"
        + "The middle of the night!"
        + "</p>"
        + "<p>"
        + "</p>"
        + "&copy; copyright 1850, Jabberwocky"
        + "</body>"
        + "</html>";
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        HTMLMessagePanelDemo1    app = new HTMLMessagePanelDemo1();
        SwingUtilities.invokeLater( app::createGUI );
    }
    
    
    /**
     * Fully configures and shows the GUI for this application.
     * The dialog containing the sample HTMLMessagePanel
     * is initially not visible.
     * To make if visible, 
     * press the "Show Dialog" button.
     */
    private void createGUI()
    {
        HTMLEditorKit   kit         = new HTMLEditorKit();
        StyleSheet      styleSheet  = kit.getStyleSheet();
        Stream.of( bodyRule, psRule )
            .peek( System.out::println )
            .forEach( styleSheet::addRule);
        
        String          title       = "Show HTML Message Panel";
        JFrame          frame       = new JFrame( title );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );        
        HTMLMessagePanel    messagePanel    = 
            new HTMLMessagePanel( htmlText, styleSheet );
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
}
