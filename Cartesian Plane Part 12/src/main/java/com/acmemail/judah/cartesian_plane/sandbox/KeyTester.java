package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Program that allows users
 * to determine their keyboard mappings.
 * The program displays a single line text box
 * and a multiline text box.
 * When the operator presses a keyboard key
 * in the single line text box
 * a description of the event
 * appears in the multiline text box;
 * modifier key presses
 * (shift/control/alt/meta)
 * are ignored.
 * The event description includes:
 * <ul>
 * <li>
 *     The character produced by the key press
 *     (assuming the character is printable);
 * </li>
 * <li>
 *     The Unicode value of the character 
 *     associated with the key press
 *     (assuming the key is associated
 *     with a Unicode character;
 *     non-Unicode keys display as '\uFFFFFF');
 * </li>
 * <li>
 *     The key code of the key pressed;
 * </li>
 * <li>
 *     An indication of which modifier keys
 *     where held down 
 *     at the time of the key press.
 * </li>
 * <li>
 *     An indication of which modifier keys
 *     where held down 
 *     at the time of the key press.
 * </li>
 * <li>
 *     The text that appears on the pressed key
 *     (e.g. "Home", "F12").
 * </li>
 * </ul>
 * 
 * @author Jack Straub
 */
public class KeyTester
{
    private static final char   downArrow   = '\u2193';
    private static final String endl        = System.lineSeparator();
    
    private final JFrame        outputFrame = new JFrame( "Output Frame" );  
    private final JFrame        inputFrame  = new JFrame( "Input Frame" );  
    private final JTextArea     output      = new JTextArea( 24, 60 );
    private final JTextField    input       = new JTextField( 40 );
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments (not used)
     */
    public static void main(String[] args)
    {
        KeyTester   tester  = new KeyTester();
        SwingUtilities.invokeLater( () -> tester.buildGUI() );
    }
    
    /**
     * Creates the input and output windows.
     */
    private void buildGUI()
    {
        makeOutputFrame();
        makeInputFrame();
    }

    /**
     * Creates the input window.
     */
    private void makeInputFrame()
    {
        inputFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        inputFrame.getContentPane().add( input );
        inputFrame.setLocation( 100, 100 );
        inputFrame.pack();
        inputFrame.setVisible( true );
        
        input.addKeyListener( new KeyDetector() );
    }

    /**
     * Creates the output window.
     */
    private void makeOutputFrame()
    {
        Font font = new Font("Monospaced", Font.PLAIN, 12);
        outputFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        output.setEditable( false );
        outputFrame.getContentPane().add( output );
        outputFrame.pack();
        outputFrame.setVisible( true );
        output.setFont( font );
    }
    
    /**
     * Class to listen for keyboard events.
     * Only key-press events are processed.
     * Modifier key presses are ignored.
     * 
     * @author Jack Straub
     */
    private class KeyDetector extends KeyAdapter
    {
        @Override
        public void keyPressed( KeyEvent event )
        {
            int     keyCode     = event.getKeyCode();
            char    keyChar     = event.getKeyChar();
            boolean hasShift    = event.isShiftDown();
            boolean hasCtrl     = event.isControlDown();
            boolean hasAlt      = event.isAltDown();
            boolean hasMeta     = event.isMetaDown();
            
            // Don't process key events for shift, control or alt 
            if ( keyCode != KeyEvent.VK_SHIFT 
                && keyCode != KeyEvent.VK_CONTROL 
                && keyCode != KeyEvent.VK_ALT
                && keyCode != KeyEvent.VK_META
            )
            {
                String  keyCodeStr  = String.format( "(int)0x%04X", keyCode );
                String  unicodeStr  = String.format( "(\\u%04X)", (int)keyChar );
                char    shift       = hasShift ? downArrow : ' ';
                char    ctrl        = hasCtrl ? downArrow : ' ';
                char    alt         = hasAlt ? downArrow : ' ';
                char    meta        = hasMeta ? downArrow : ' ';
        
                StringBuilder   bldr    = new StringBuilder( "    " );
                bldr.append( keyChar ).append( ' ' )
                    .append( unicodeStr ).append( ' ' )
                    .append( keyCodeStr ).append( ' ' )
                    .append( "Shift:" ).append( shift ).append( ' ' )
                    .append( "Cntrl:" ).append( ctrl ).append( ' ' )
                    .append( "Alt:" ).append( alt ).append( ' ' )
                    .append( "Meta:" ).append( meta ).append( ' ' )
                    .append( KeyEvent.getKeyText( keyCode ) )
                    .append( endl );
                output.append( bldr.toString() );
            }
        }
    }
}
