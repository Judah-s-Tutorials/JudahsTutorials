package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.sandbox.utils.ActivityLog;

public class KeyListenerDemo
{
    /** Activity dialog for displaying feedback. */
    private ActivityLog log;
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        KeyListenerDemo demo    = new KeyListenerDemo();
        SwingUtilities.invokeLater( demo::buildGUI );
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
        field.addKeyListener( new KEListener() );
        pane.add( field, BorderLayout.NORTH );
        
        JPanel      buttons = new JPanel();
        JButton     exit    = new JButton( "Exit" );
        exit.addActionListener( e -> System.exit( 1 ) );
        buttons.add( exit );
        pane.add( buttons, BorderLayout.SOUTH );

        frame.setContentPane( pane );
        frame.setLocation( 200, 100 );
        frame.pack();
        
        log = new ActivityLog( frame );
        Dimension   logSize     = log.getPreferredSize();
        logSize.width *= 2;
        logSize.height *= 2;
        log.setPreferredSize( logSize );
        Dimension   frameSize   = frame.getPreferredSize();
        log.setLocation( 220 + frameSize.width, 100 );
        frame.setVisible( true );
    }
    
    /**
     * KeyListener to listen for PI^P
     * in a text component.
     * 
     * @author Jack Straub
     */
    private class KEListener implements KeyListener
    {
        private static int  allButtons  =
            KeyEvent.BUTTON1_DOWN_MASK |
            KeyEvent.BUTTON2_DOWN_MASK |
            KeyEvent.BUTTON3_DOWN_MASK;
        
        private int     keyCode;
        private char    keyChar;
        private boolean hasCtrl;
        private boolean hasShift;
        private boolean hasAlt;
        private boolean hasButton;
        
        private void decodeEvent( KeyEvent evt )
        {
            int mods    = evt.getModifiersEx();
            keyCode = evt.getKeyCode();
            keyChar = evt.getKeyChar();
            hasCtrl = (mods & KeyEvent.CTRL_DOWN_MASK) != 0;
            hasShift = (mods & KeyEvent.SHIFT_DOWN_MASK) != 0;
            hasAlt = (mods & KeyEvent.ALT_DOWN_MASK) != 0;
            hasButton = (mods & allButtons) != 0;
        }
        
        private void logEvent( String event, String color )
        {
            StringBuilder   bldr    = new StringBuilder();
            bldr.append( "<span style='color: " )
                .append( color ).append( ";'>");
            bldr.append( event ).append( ": " )
                .append( "keyCode=0x" )
                .append( Integer.toHexString( keyCode ) ).append( "," )
                .append( "keyChar='" );
            if ( keyChar >= ' ' )
                bldr.append( keyChar );
            else
                bldr.append( "0x" )
                    .append( String.format( "%02x", (int)keyChar ) );
            bldr.append( "'," )
                .append( "ctrl/shift/alt/button= " ).append( hasCtrl )
                .append( "/" ).append( hasShift).append( "/" )
                .append( hasAlt ).append( "/" ).append( hasButton );
            bldr.append( "</span>" );
            log.append( bldr.toString() );
            if ( event.startsWith( "Released" ) )
                log.append( "********************************" );
            System.out.println( "==" + (int)keyChar );
        }
        
        /**
         * Listens for key-press events
         */
        @Override 
        public void keyPressed( KeyEvent evt )
        {
            decodeEvent( evt );
            logEvent( "Pressed: ", "red " );
        }
        
        /**
         * Listens for key-release events
         */
        @Override 
        public void keyReleased( KeyEvent evt )
        {
            decodeEvent( evt );
            logEvent( "Released: ", "green " );
        }
        
        /**
         * Listens for key-release events
         */
        @Override 
        public void keyTyped( KeyEvent evt )
        {
            decodeEvent( evt );
            logEvent( "Typed: ", "black " );
        }
    }
}
