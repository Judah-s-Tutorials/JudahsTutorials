package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Window;
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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class FocusJDialogDemo
{
    private static final Map<Character,JDialog>  dialogMap    = new HashMap<>();
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
        Arrays.stream( titles ).forEach( FocusJDialogDemo::buildDialog );
    }
    
    private static void buildDialog( String title )
    {
        nextTitle = title;
        try
        {
            SwingUtilities.invokeAndWait( () -> buildDialog() );
        }
        catch ( InterruptedException | InvocationTargetException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }

    private static void buildDialog()
    {
        nextXco += posDelta;
        nextYco += posDelta;
        char        thisKey = nextKey++;
        JDialog     dialog  = 
            new JDialog( (Window)null, nextTitle + " - " + thisKey );
        dialog.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
        JPanel      pane    = getContentPane( thisKey );
        dialog.setContentPane( pane );
        pane.setBackground( nextColor() );
        
        dialog.addWindowFocusListener( fListener );
        dialog.addKeyListener( kMonitor );
        dialog.setLocation( new Point( nextXco, nextYco ) );
        dialog.pack();
        dialog.setVisible( true );
        
        dialogMap.put( thisKey, dialog );
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
            JDialog dialog  = dialogMap.get( key );
            if ( dialog != null )
                dialog.requestFocus();
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
            JDialog dialog  = validate( evt );
            if ( dialog != null )
            {
                String  title   = dialog.getTitle();
                System.out.printf( showFmt, title, msg, time );
            }
        }
        
        private JDialog validate( WindowEvent evt )
        {
            JDialog dialog  = null;
            Object  source  = evt.getSource();
            if ( source instanceof JDialog )
                dialog = (JDialog)source;
            else
            {
                String  fmt = "Exp: JDialog, act: %s%n";
                System.out.printf( fmt, source.getClass().getName() );
            }
            return dialog;
        }
    }
}
