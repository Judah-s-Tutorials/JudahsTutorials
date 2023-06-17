package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class FocusDemo2
{
    private static final Map<String,JFrame> frameMap    = new HashMap<>();
    private static final FocusListener      fListener   = new FocusListener();
    
    private static final int        posDelta    = 50;
    private static final String[]   titles      =
    {
        "Alice",
        "M. Hatter",
        "T. Dum", 
        "T. Dee"
    };
    private static String   nextTitle;
    private static int      nextXco     = 0;
    private static int      nextYco     = 0;
    
    public static void main(String[] args)
    {
        Arrays.stream( titles ).forEach( FocusDemo2::buildFrame );
        int choice  = 0;
        while ( (choice = showDialog()) >= 0 )
        {
            String  title   = titles[choice];
            JFrame  frame   = frameMap.get( title );
            frame.requestFocus();
        }
        System.exit( 0 );
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
        JFrame      frame   = new JFrame( nextTitle );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        Container   pane    = frame.getContentPane();
        pane.setBackground( nextColor() );
        pane.setPreferredSize( new Dimension( 250, 200 ) );
        
        frame.addWindowFocusListener( fListener );
        frame.setLocation( new Point( nextXco, nextYco ) );
        frame.pack();
        frame.setVisible( true );
        
        frameMap.put( nextTitle, frame );
    }
    
    private static int showDialog()
    {
        int choice  = JOptionPane.showOptionDialog(
            null, 
            "Choose a Frame", 
            "Focus Demo",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null, 
            titles, 
            titles[0]
        );
        return choice;
    }
    
    private static Color nextColor()
    {
        float   red     = (float)Math.random();
        float   green   = (float)Math.random();
        float   blue    = (float)Math.random();
        Color   color   = new Color( red, green, blue );
        return color;
    }
    
    private static class FocusListener implements WindowFocusListener
    {
        private static final String showFmt = "%-10s: %s%n";
        
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
            JFrame  frame   = validate( evt );
            if ( frame != null )
            {
                String  title   = frame.getTitle();
                System.out.printf( showFmt, title, msg );
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
