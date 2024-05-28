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
 * First in a series of demos
 * to use a KeyListener
 * to detect keystrokes
 * in a text field.
 * The GUI has a text field
 * and a Print button.
 * When the Print button is pressed
 * the text of the text field
 * and the length of the text
 * will be printed to stdout.
 * <p>
 * The ultimate goal of these demos
 * is to detect "pi^P"
 * and then substitute the Greek symbol &Pi;.
 * In this first demo 
 * we'll just set up a key listener
 * to detect ^P (control-p).
 * 
 * @author Jack Straub
 * 
 * @see PIListenerDemo2
 * @see PIListenerDemo3
 */
public class PIListenerDemo1
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
        SwingUtilities.invokeLater( () -> new PIListenerDemo1().buildGUI() );
    }
    
    /**
     * Builds the GUI and makes it visible.
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
                String      prev2   = "\"\"";
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
