package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.AWTException;
import java.awt.Font;
import java.awt.Robot;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Program to discover the key code/shift mappings 
 * for the first 256 key codes.
 * The assumption is that the first 256 key codes
 * will map roughly to the most common Unicode values
 * (at least in US English).
 * On that assumption, 
 * we can discover what combinations of key code and shift
 * can be used to generate the most common characters.
 * 
 * @author Jack Straub
 */
public class KeyMapper
{
    private static final char   downArrow   = '\u2193';
    private static final String endl        = System.lineSeparator();
    private static Robot        robot;
    
    /** Maps a Unicode value to a key code/shift combination. */
    private static final Map<Character,KeySequence>    mapper  = new HashMap<>();
    
    private final JFrame        outputFrame = new JFrame( "Output Frame" );  
    private final JFrame        inputFrame  = new JFrame( "Input Frame" );  
    private final JTextArea     output      = new JTextArea( 24, 50 );
    private final JTextField    input       = new JTextField( 40 );
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments
     * 
     * @throws AWTException if instantiation of Robot fails
     */
    public static void main(String[] args)
        throws AWTException
    {
        KeyMapper   tester  = new KeyMapper();
        SwingUtilities.invokeLater( () -> tester.buildGUI() );
        
        robot   = new Robot();
        pause( 1000 );
        robot.setAutoDelay( 10 );
        System.out.println( robot.getAutoDelay() );
        mapUnshifted();
        mapShifted();
        
        tester.testMapping();
    }
    
    private void testMapping()
    {
        output.grabFocus();
        output.setEditable( true );
        String[]   testLines   =
        {
           "(A poem, by L. Carrol)",
           "The sun was shining on the sea,",
           "Shining with all his might;",
           "He did his very best to make",
           "The billows smooth and bright;",
           "And this was odd, because it was",
           "The middle of the night!"
        };
        for ( String line : testLines )
            typeLine( line );
    }

    private static void typeLine( String line )
    {
        for ( char ccc : line.toCharArray() )
        {
            KeySequence keySeq  = mapper.get( ccc );
            if ( keySeq == null )
                System.err.println( "No mapping for '" + ccc + "'" );
            else
                keySeq.type( robot );
        }
        robot.keyPress( KeyEvent.VK_ENTER );
        robot.keyRelease( KeyEvent.VK_ENTER );
    }

    
    private static void mapUnshifted()
    {
        for ( int inx = 0 ; inx <= 0xFF ; ++inx )
        {
            if ( inx != KeyEvent.VK_WINDOWS )
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
    
    private static void mapShifted()
    {
        // sort on key code to make output easier to examine
        List<KeySequence>   values  = new ArrayList<>();
        values.addAll( mapper.values() );
        values.sort( (ks1,ks2) -> ks1.keyCode - ks2.keyCode );
        
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
                char    shift       = hasShift ? downArrow : ' ';
                char    ctrl        = hasCtrl ? downArrow : ' ';
                char    alt         = hasAlt ? downArrow : ' ';
        
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
            
            
            if ( keyChar != 0xffffff )
            {
                mapper.putIfAbsent( keyChar, new KeySequence( event ) );
            }
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
            char    shift       = hasShift ? downArrow : ' ';
            char    ctrl        = hasCtrl ? downArrow : ' ';
            char    alt         = hasAlt ? downArrow : ' ';
    
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
            int rcode   = unicode - o.unicode;
            if ( rcode == 0 )
                rcode = hasShift ? -1 : 1;
            return rcode;
        }
    }
}
