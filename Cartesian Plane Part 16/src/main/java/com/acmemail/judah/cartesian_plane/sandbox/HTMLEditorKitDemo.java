package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;

public class HTMLEditorKitDemo
{
    private static final String orange  = "#FFAC1C";
    private static final String yellow  = "#FFFF00";
    private static final String cyan    = "#00FFFF";
    
    private static final String rule1   = 
        "body.a{ background-color:" + orange + ";}";
    private static final String rule2   = 
        "body.a{ background-color:" + yellow + ";}";
    private static final String rule3   = 
        "body.a{ background-color:" + cyan + ";}";
    private static final String html    =
        "<html><body class='a'>"
        + "<p>A wop bop a loo bop a lop bam boom</p>"
        + "</body></html>";
    
    private JEditorPane pane1   = null;
    private JEditorPane pane2   = null;
    private JEditorPane pane3   = null;
    private JEditorPane pane4   = null;
    
    private JLabel      label1  = null;
    private JLabel      label2  = null;
    private JLabel      label3  = null;
    private JLabel      label4  = null;
    
    private JFrame      frame   = null;
    private int         step    = 0;
    
    public static void main( String[] args )
    {
        HTMLEditorKitDemo   demo    = new HTMLEditorKitDemo();
        SwingUtilities.invokeLater( () -> demo.buildGUI() );
    }
    
    private void buildGUI()
    {
        frame = new JFrame( "HTMLEditorKit Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel  contentPane = new JPanel( new BorderLayout() );
        contentPane.add( getMainPanel(), BorderLayout.CENTER );
        contentPane.add( getControlPanel(), BorderLayout.SOUTH );
        frame.setContentPane( contentPane );
        refresh();
        frame.pack();
        frame.setLocation( 200, 200 );
        frame.setVisible( true );
    }
    
    private JPanel getMainPanel()
    {
        final String contentType    = "text/html";
        JPanel  panel   = new JPanel( new GridLayout( 4, 2 ) );
        pane1 = new JEditorPane( contentType, "" );
        pane2 = new JEditorPane( contentType, "" );
        pane3 = new JEditorPane( contentType, "" );
        pane4 = new JEditorPane( contentType, "" );
        
        pane1.setEditorKit( new HTMLEditorKit() );
        pane2.setEditorKit( new HTMLEditorKit() );
        pane3.setEditorKit( new HTMLEditorKit() );
        pane4.setEditorKit( new HTMLEditorKit() );
        
        label1 = new JLabel( "", JLabel.RIGHT );
        label2 = new JLabel( "", JLabel.RIGHT );
        label3 = new JLabel( "", JLabel.RIGHT );
        label4 = new JLabel( "", JLabel.RIGHT );
        
        panel.add( label1 );
        panel.add( pane1 );
        panel.add( label2 );
        panel.add( pane2 );
        panel.add( label3 );
        panel.add( pane3 );
        panel.add( label4 );
        panel.add( pane4 );
        
        pane1.setText( html );
        pane2.setText( html );
        
        return panel;
    }
    
    private JPanel getControlPanel()
    {
        JPanel  panel   = new JPanel();
        JButton next    = new JButton( "Next" );
        JButton exit    = new JButton( "Exit" );
        panel.add( next );
        panel.add( exit );
        next.addActionListener( e -> next() );
        exit.addActionListener( e -> System.exit( 0 ) );
        return panel;
    }
    
    private void next()
    {
        StyleSheet  styleSheet  = null;
        
        switch ( step++ )
        {
        case 0:
            styleSheet = getStyleSheet( pane2 );
            styleSheet.addRule( rule1 );
            break;
        case 1:
            styleSheet = new StyleSheet();
            styleSheet.addRule( rule2 );
            HTMLEditorKit   kit1    = getEditorKit( pane3 );
            kit1.setStyleSheet( styleSheet );
            pane3.setEditorKit( kit1 );
            pane3.setText( html );
            break;
        case 2:
            HTMLEditorKit   kit2    = getEditorKit( pane4 );
            pane4.setEditorKit( kit2 );
            pane4.setText( html );
            break;
        case 3:
            styleSheet = getStyleSheet( pane4 );
            styleSheet.addRule( rule3 );
            break;
        default:
            throw new ComponentException( "hard fail" );
        }
        refresh();
    }
    
    private void refresh()
    {
        refresh( pane1, label1 );
        refresh( pane2, label2 );
        refresh( pane3, label3 );
        refresh( pane4, label4 );
    }
    
    private void refresh( JEditorPane pane, JLabel label )
    {
        String      text    = pane.getText();
        StyleSheet  sheet   = getStyleSheet( pane );
        String      ident   = 
            String.format( "0x%06x", sheet.hashCode() );
        label.setText( ident );
        pane.setText( text );
    }
    
    private StyleSheet getStyleSheet( JEditorPane pane )
    {
        HTMLEditorKit   kit     = getEditorKit( pane );
        StyleSheet      sheet   = ((HTMLEditorKit)kit).getStyleSheet();
        return sheet;
    }
    
    private HTMLEditorKit getEditorKit( JEditorPane pane )
    {
        EditorKit   kit     = pane.getEditorKitForContentType( "text/html" );
        if ( !(kit instanceof HTMLEditorKit ) )
            throw new ComponentException( "hard fail" );
        return (HTMLEditorKit)kit;
    }
}
