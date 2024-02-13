package com.acmemail.judah.cartesian_plane.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;

/**
 * Application to demonstrate how a StyleSheet can be created,
 * and updated one rule at a time.
 * When first started there will be a JEditorPane
 * with some simple text.
 * It will have a button you can use to add a rule.
 * Each time a rule is added
 * the JEditorPane will be updated
 * with the new rule.
 * 
 * @author Jack Straub
 */
public class StyleSheetDemo1
{
    /** System line separator. */
    private static final String newLine     = System.lineSeparator();

    /** CSS rules to be added to style sheet one at a time. */
    private static final String[]   allRules    =
    {
        "body { background-color: #FFAE42; }",
        "p.blue { background-color: #0000ff; color: #ffffff; }",
        "p.pink { background-color: #f8bfd4; color: #16745b; }",
        "p.italic { font: italic 150% monospaced; }",
    };
    
    /** Sample text to display in JEditorPane. */
    private static final String sampleHTML      =
        "<html>"
        + "<body>"
        + "<p class='blue'}>This will be a blue paragraph.</p>"
        + "<p class='pink'}>This will be a pinkish paragraph.</p>"
        + "<p class='italic'}>"
        +       "This will be a big, italic and monospaced paragraph."
        + "</p>"
        + "</body>"
        + "</html>";
    /** Text area the current set of rules will be displayed. */
    private final JTextArea     rulesArea       = new JTextArea( 8, 30 );
    /** The JEditorPane used in this demo. */
    private final JEditorPane   editorPane      = 
        new JEditorPane( "text/html", sampleHTML );
    /** The style sheet used in this demo. */
    private final StyleSheet    styleSheet      = new StyleSheet();
    /** Index for traversing the alRules array. */
    private int                 nextRule        = 0;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new StyleSheetDemo1() );
    }
    
    /**
     * Constructor.
     * Initializes and displays the application GUI.
     */
    public StyleSheetDemo1()
    {
        rulesArea.setRows( allRules.length  + 1 );
        rulesArea.setColumns( 40 );
        
        HTMLEditorKit   kit = new HTMLEditorKit();
        kit.setStyleSheet( styleSheet );
        editorPane.setEditorKit( kit );
        editorPane.setText( sampleHTML );
              
        JFrame      frame       = new JFrame( "Style Sheet Demo 1" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      contentPane = new JPanel( new BorderLayout() );
        contentPane.add( getMainPanel(), BorderLayout.CENTER );
        contentPane.add( getControlPanel(), BorderLayout.SOUTH );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setLocation( 200, 200 );
        frame.setVisible( true );
        
        refreshRulesDisplay();
    }
    
    /**
     * Gets the main panel for this application's GUI.
     * This panel contains:
     * <ul>
     * <li>A scroll pane with the JEditorPane as a view; and</li>
     * <li>A scroll pane with the rules display as a view.
     * </ul>
     * <p>
     * The panes are arranged vertically.
     * 
     * @return  the application GUI's main panel
     */
    private JPanel getMainPanel()
    {
        Border  lineBorder  = 
            BorderFactory.createLineBorder( Color.BLACK, 3 );
        
        JPanel      htmlPanel       = new JPanel();
        JScrollPane htmlScrollPane  = new JScrollPane( htmlPanel );
        Border  htmlBorder          = 
            BorderFactory.createTitledBorder( lineBorder, "HTML" );
        htmlPanel.setBorder( htmlBorder );
        htmlPanel.add( editorPane );
        
        JPanel      rulesPanel      = new JPanel();
        JScrollPane rulesScrollPane = new JScrollPane( rulesPanel );
        Border      rulesBorder     = 
            BorderFactory.createTitledBorder( lineBorder, "Rules" );
        rulesPanel.setBorder( rulesBorder );
        rulesPanel.add( rulesArea );
        
        JPanel  mainPanel   = new JPanel( new GridLayout( 2, 1 ) );
        mainPanel.add( htmlScrollPane );
        mainPanel.add( rulesScrollPane );
        return mainPanel;
    }
    
    /**
     * Gets the control panel for this application's GUI.
     * It consists of two buttons arranged horizontally.
     * 
     * @return  the control panel for this application's GUI
     */
    private JPanel getControlPanel()
    {
        JButton ruleButton  = new JButton( "Next Rule" );
        ruleButton.addActionListener( this::addRule );
        JButton exitButton  = new JButton( "Exit" );
        exitButton.addActionListener( e -> System.exit( 0 ) );
        
        JPanel  panel   = new JPanel();
        panel.add( ruleButton );
        panel.add( exitButton );
        return panel;
    }
    
    /**
     * Adds the next rule from the list
     * to the style sheet.
     */
    private void addRule( ActionEvent evt )
    {
        styleSheet.addRule( allRules[nextRule++] );
        editorPane.setText( sampleHTML );
        editorPane.setCaretPosition( 0 );
        refreshRulesDisplay();
        if ( nextRule == allRules.length )
        {
            Object  source  = evt.getSource();
            if ( !(source instanceof JComponent) )
                throw new ComponentException( "Unkown component" );
            ((JComponent)source).setEnabled( false );
        }
    }
    
    /**
     * Displays all the rules currently present
     * in the style sheet.
     */
    private void refreshRulesDisplay()
    {
        StringBuilder   bldr    = new StringBuilder();
        Enumeration<?> rules    = styleSheet.getStyleNames();
        Collections.list( rules ).stream()
            .map( r -> (String)r )
            .map( styleSheet::getStyle )
            .map( s -> s.toString() )
            .peek( bldr::append )
            .map( s -> newLine )
            .forEach( bldr::append );
        rulesArea.setText( bldr.toString() );
        rulesArea.setCaretPosition( 0 );
    }
}
