package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class FocusDemo
{
    private static final Map<Character,JFrame>  frameMap    = new HashMap<>();
    private static final FocusListener  fListener   = new FocusListener();
    private static final KeyMonitor     kMonitor    = new KeyMonitor();
    private static final String         choiceStr;
    
    private static final int            posDelta    = 50;
    private static final Color[]        colors      =
    {
        Color.YELLOW,
        Color.CYAN,
        Color.PINK,
        Color.ORANGE
    };
    private static final String[]       titles      =
    {
        "Alice",
        "M. Hatter",
        "T. Dum", 
        "T. Dee"
    };
    private static String   nextTitle;
    private static char     nextKey     = 'A';
    private static int      nextXco     = 0;
    private static int      nextYco     = 0;
    private static int      nextColor   = 0;
    
    static
    {
        StringBuilder   bldr    = new StringBuilder( "Type ");
        bldr.append( 'A' );
        IntStream.range( 1, titles.length ).forEach( i -> {
            bldr.append( ", " );
            bldr.append( (char)('A' + i) );
        });
        choiceStr = bldr.toString();
    }
    
    public static void main(String[] args)
    {
        Arrays.stream( titles ).forEach( FocusDemo::buildFrame );
    }
    
    private static void buildFrame( String title )
    {
        nextTitle = title;
        try
        {
            SwingUtilities.invokeAndWait( () -> buildFrame() );
        }
        catch ( InterruptedException | InvocationTargetException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }

    private static void buildFrame()
    {
        nextXco += posDelta;
        nextYco += posDelta;
        char        thisKey = nextKey++;
        JFrame      frame   = new JFrame( nextTitle + " - " + thisKey );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JPanel      pane    = getContentPane( thisKey );
        frame.setContentPane( pane );
        pane.setBackground( nextColor() );
        
        frame.addWindowFocusListener( fListener );
        frame.addKeyListener( kMonitor );
        frame.setLocation( new Point( nextXco, nextYco ) );
        frame.pack();
        frame.setVisible( true );
        
        frameMap.put( thisKey, frame );
    }
    
    private static JPanel getContentPane( int thisID )
    {
        JPanel  panel   = new JPanel( new GridLayout( 4, 1 ) );
        
        Font    oldFont = panel.getFont();
        int     oldSize = oldFont.getSize();
        float   newSize = oldSize * 1.5f;
        Font    newFont = oldFont.deriveFont( newSize );
        
        int     vert    = 25;
        int     hori    = 75;
        Border  border  = 
            BorderFactory.createEmptyBorder(0, hori, vert, hori );
        panel.setBorder( border );
        
        JLabel  ident   = new JLabel( "" + (char)thisID );
        ident.setFont( newFont );
        ident.setHorizontalAlignment( SwingConstants.RIGHT );
        panel.add( ident );
        
        JLabel  spacer   = new JLabel( "   " );
        spacer.setFont( newFont );
        spacer.setHorizontalAlignment( SwingConstants.CENTER );
        panel.add( spacer );
        
        JLabel  legend  = new JLabel( choiceStr );
        legend.setFont( newFont );
        ident.setHorizontalAlignment( SwingConstants.CENTER );
        panel.add( legend );
        
        JLabel  exit    = new JLabel( "X to Exit");
        exit.setFont( newFont );
        exit.setHorizontalAlignment( SwingConstants.CENTER );
        panel.add( exit );
        
        return panel;
    }
    
    private static Color nextColor()
    {
        int     len     = colors.length;
        Color   color   = colors[nextColor++ % len];
        return color;
    }
    
    private static class KeyMonitor extends KeyAdapter
    {
        @Override
        public void keyPressed( KeyEvent evt )
        {
            char    key = evt.getKeyChar();
            key = Character.toUpperCase( key );
            if ( key == 'X' )
                System.exit( 0 );
            JFrame  frame   = frameMap.get( key );
            if ( frame != null )
                frame.requestFocus();
        }
    }
    
    private static class FocusListener implements WindowFocusListener
    {
        private static final String showFmt = "%-13s %-14s %d%n";
        
        @Override
        public void windowGainedFocus( WindowEvent evt )
        {
            feedback( evt, "Focus Gained" );
        }

        @Override
        public void windowLostFocus( WindowEvent evt )
        {
            feedback( evt, "Focus Lost" );
        }
        
        private void feedback( WindowEvent evt, String msg )
        {
            long    time    = System.currentTimeMillis();
            JFrame  frame   = validate( evt );
            if ( frame != null )
            {
                String  title   = frame.getTitle();
                System.out.printf( showFmt, title, msg, time );
            }
        }
        
        private JFrame validate( WindowEvent evt )
        {
            JFrame  frame   = null;
            Object  source  = evt.getSource();
            if ( source instanceof JFrame )
                frame = (JFrame)source;
            else
            {
                String  fmt = "Exp: JFrame, act: %s%n";
                System.out.printf( fmt, source.getClass().getName() );
            }
            return frame;
        }
    }
}
