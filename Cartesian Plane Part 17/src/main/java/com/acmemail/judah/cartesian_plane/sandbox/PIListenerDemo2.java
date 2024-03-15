package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;

/**
 * Second in a series of demos
 * to use a KeyListener
 * to detect keystrokes
 * in a text field.
 * The ultimate goal
 * is to detect "pi^P"
 * and then substitute &Pi;.
 * In this second demo 
 * we start by listening for ^P
 * then find the cursor position
 * in the text field
 * and print the two previous characters.
 * 
 * @author Jack Straub
 * 
 * @see PIListenerDemo1
 * @see PIListenerDemo3
 */
public class PIListenerDemo2
{
    /** The key code for P on the keyboard. */
    private static final int    keyCodeP    = KeyEvent.VK_P;
    /** System-dependent line separator. */
    private static final String endl        = System.lineSeparator();
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new PIListenerDemo2().buildGUI() );
    }
    
    /**
     * Creates the GUI and makes it visible.
     */
    private void buildGUI()
    {
        JFrame      frame   = new JFrame( "PI Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      pane    = new JPanel( new BorderLayout() );
        JTextField  field   = new JTextField( 10 );
        field.addKeyListener( new PIListener() );
        pane.add( field, BorderLayout.NORTH );
        
        JPanel      buttons = new JPanel();
        JButton     exit    = new JButton( "Exit" );
        exit.addActionListener( e -> System.exit( 1 ) );
        buttons.add( exit );
        pane.add( buttons, BorderLayout.SOUTH );
        
        JButton     print   = new JButton( "Print" );
        print.addActionListener( e -> {
            String  text    = field.getText();
            text += " " + text.length();
            System.out.println( text );
        });
        buttons.add( print );
        pane.add( buttons, BorderLayout.SOUTH );

        frame.setContentPane( pane );
        frame.pack();
        GUIUtils.center( frame );
        frame.setVisible( true );
    }
    
    /**
     * KeyListener to listen for PI^P
     * in a text component.
     * 
     * @author Jack Straub
     */
    private class PIListener extends KeyAdapter
    {
        /**
         * Listens for the operator to type the P key
         * while holding down the control key.
         * When this occurs diagnostics are printed to stdout.
         */
        @Override 
        public void keyPressed( KeyEvent evt )
        {
            int     keyCode     = evt.getKeyCode();
            Object  src         = evt.getSource();            
            boolean isCtrl      = evt.isControlDown();
            boolean isText      = src instanceof JTextComponent;
            
            if ( isText && isCtrl && keyCode == keyCodeP )
            {
                JTextField  field   = (JTextField)evt.getSource();
                String      text    = field.getText();
                int         caret   = field.getCaretPosition();
                int         backPos = caret - 2;
                String      prev2   = "\"\"";
                if ( caret > 1 )
                    prev2 = text.substring( backPos, caret );
                StringBuilder   bldr    = new StringBuilder();
                bldr.append( "Text: " ).append( text ).append( endl )
                    .append( "Caret: " ).append( caret ).append( endl )
                    .append( "Prev 2: " ).append( prev2 ).append( endl )
                    .append( "*****************" );
                System.out.println( bldr );
            }
        }
    }
}
