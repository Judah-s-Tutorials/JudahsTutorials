package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class KeyTester
{
    private static final char   upArrow     = '\u2191';
    private static final String endl        = System.lineSeparator();
    
    private final JFrame        outputFrame = new JFrame( "Output Frame" );  
    private final JFrame        inputFrame  = new JFrame( "Input Frame" );  
    private final JTextArea     output      = new JTextArea( 24, 40 );
    private final JTextField    input       = new JTextField( 40 );
    
    public static void main(String[] args)
    {
        KeyTester   tester  = new KeyTester();
        SwingUtilities.invokeLater( () -> tester.buildGUI() );
    }
    
    private void buildGUI()
    {
        makeOutputFrame();
        makeInputFrame();
    }

    private void makeInputFrame()
    {
        inputFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        inputFrame.getContentPane().add( input );
        inputFrame.setLocation( 100, 100 );
        inputFrame.pack();
        inputFrame.setVisible( true );
        
        input.addKeyListener( new KeyDetector() );
    }

    private void makeOutputFrame()
    {
        Font font = new Font("Monospaced", Font.PLAIN, 12);
        outputFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        output.setEditable( false );
        outputFrame.getContentPane().add( output );
        outputFrame.pack();
        outputFrame.setVisible( true );
        output.setFont( font );
        
        String          fmt     = "%-15s %04x";
        StringBuilder   bldr    = new StringBuilder()
            .append( String.format( fmt, "VK_COLON:", KeyEvent.VK_COLON ) )
            .append( endl )
            .append( String.format( fmt, "VK_SEMICOLON:", KeyEvent.VK_SEMICOLON ) )
            .append( endl )
            .append( String.format( fmt, "VK_BACK_SLASH:", KeyEvent.VK_BACK_SLASH ) )
            .append( endl )
            .append( String.format( fmt, "VK_SLASH:", KeyEvent.VK_SLASH ) )
            .append( endl );
        output.append( bldr.toString() );
    }
    
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
            
            // Don't process key events for shift, control or alt 
            if ( keyCode != KeyEvent.VK_SHIFT 
                && keyCode != KeyEvent.VK_CONTROL 
                && keyCode != KeyEvent.VK_ALT
            )
            {
                String  keyCodeStr  = String.format( "(int)%04X", keyCode );
                String  unicodeStr  = String.format( "(\\u%04X)", (int)keyChar );
                char    shift       = hasShift ? upArrow : ' ';
                char    ctrl        = hasCtrl ? upArrow : ' ';
                char    alt         = hasAlt ? upArrow : ' ';
        
                StringBuilder   bldr    = new StringBuilder( "    " );
                bldr.append( keyChar ).append( ' ' )
                    .append( unicodeStr ).append( ' ' )
                    .append( keyCodeStr ).append( ' ' )
                    .append( "Shift:" ).append( shift ).append( ' ' )
                    .append( "Cntrl:" ).append( ctrl ).append( ' ' )
                    .append( "Alt:" ).append( alt ).append( ' ' )
                    .append( endl );
                output.append( bldr.toString() );
            }
        }
    }
}
