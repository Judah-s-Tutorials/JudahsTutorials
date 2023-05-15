package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.AWTException;
import java.awt.Font;
import java.awt.Robot;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class KeyMapper
{
    private static final char   upArrow     = '\u2191';
    private static final String endl        = System.lineSeparator();
    
    private static final Map<Character,KeySequence>    mapper  = new HashMap<>();
    
    private final JFrame        outputFrame = new JFrame( "Output Frame" );  
    private final JFrame        inputFrame  = new JFrame( "Input Frame" );  
    private final JTextArea     output      = new JTextArea( 24, 50 );
    private final JTextField    input       = new JTextField( 40 );
    
    public static void main(String[] args)
        throws AWTException
    {
        KeyMapper   tester  = new KeyMapper();
        SwingUtilities.invokeLater( () -> tester.buildGUI() );
        Robot   robot   = new Robot();
        pause( 1000 );
        robot.setAutoDelay( 10 );
        System.out.println( robot.getAutoDelay() );
        mapUnshifted( robot );
        mapShifted( robot );
    }
    
    private static void mapUnshifted( Robot robot )
    {
        for ( int inx = 1 ; inx <= 0xff ; ++inx )
        {
            try
            {
                robot.keyPress( inx );
                robot.keyRelease( inx );
            }
            catch( IllegalArgumentException exc )
            {
            }
        }
    }
    
    private static void mapShifted( Robot robot )
    {
        Collection<KeySequence>    values  = new HashSet<>();
        values.addAll( mapper.values() );
        for ( KeySequence seq : values )
        {
            try
            {
                robot.keyPress( KeyEvent.VK_SHIFT );
                robot.keyPress( seq.keyCode );
                robot.keyRelease( seq.keyCode );
                robot.keyRelease( KeyEvent.VK_SHIFT );
            }
            catch( IllegalArgumentException exc )
            {
            }
        }
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
        JScrollPane  scrollPane  = new JScrollPane();
        scrollPane.setViewportView( output );
        output.setEditable( false );
        outputFrame.getContentPane().add( scrollPane );
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
                String  keyText     = KeyEvent.getKeyText( keyCode );
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
                    .append( '"' ).append( keyText ).append( '"' ).append( ' ' )
                    .append( endl );
                output.append( bldr.toString() );
            }
            
            if ( keyCode != 0xffffff )
                mapper.put( keyChar, new KeySequence( event ) );
        }
    }
    
    private static void pause( long millis )
    {
        try
        {
            Thread.sleep( millis );
        }
        catch ( InterruptedException exc )
        {
            // ignore
        }
    }
    
    private static class KeySequence
        implements Comparable<KeySequence>
    {
        public final boolean    hasShift;
        public final boolean    hasAlt;
        public final boolean    hasCtrl;
        public final int        keyCode;
        public final char       unicode;
        
        public KeySequence( KeyEvent evt )
        {
            hasShift = evt.isShiftDown();
            hasAlt = evt.isAltDown();
            hasCtrl = evt.isControlDown();
            keyCode = evt.getKeyCode();
            unicode = evt.getKeyChar();
        }
        
        public void type( Robot robot )
        {
            configModifiers( robot::keyPress );
            robot.keyPress( keyCode );
            robot.keyRelease( keyCode );
            configModifiers( robot::keyRelease );
        }
        
        public String toString()
        {
            String  keyCodeStr  = String.format( "(int)%04X", keyCode );
            String  unicodeStr  = String.format( "(\\u%04X)", (int)unicode );
            String  keyText     = KeyEvent.getKeyText( keyCode );
            char    shift       = hasShift ? upArrow : ' ';
            char    ctrl        = hasCtrl ? upArrow : ' ';
            char    alt         = hasAlt ? upArrow : ' ';
    
            StringBuilder   bldr    = new StringBuilder( "    " );
            bldr.append( unicode ).append( ' ' )
                .append( unicodeStr ).append( ' ' )
                .append( keyCodeStr ).append( ' ' )
                .append( "Shift:" ).append( shift ).append( ' ' )
                .append( "Cntrl:" ).append( ctrl ).append( ' ' )
                .append( "Alt:" ).append( alt ).append( ' ' )
                .append( '"' ).append( keyText ).append( '"' ).append( ' ' )
                .append( endl );
            
            return bldr.toString();
        }
        
        private void configModifiers( Consumer<Integer> consumer )
        {
            if ( hasShift )
                consumer.accept( KeyEvent.VK_SHIFT );
            if ( hasCtrl )
                consumer.accept( KeyEvent.VK_CONTROL );
            if ( hasAlt )
                consumer.accept( KeyEvent.VK_ALT );
        }

        @Override
        public int compareTo(KeySequence o)
        {
            return unicode - o.unicode;
        }
    }
}
