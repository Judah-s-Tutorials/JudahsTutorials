package com.acmemail.judah.glass_panes.util;

import java.awt.BorderLayout;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Display
{
    private static final String newLine     = System.lineSeparator();
    private static Display      instance    = null;

    private final JFrame        frame       = new JFrame( "Logger" );
    private final JTextArea     textArea    = new JTextArea( 40, 40 );
    
    private Display()
    {
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        JScrollPane scrollPane  = new JScrollPane( textArea );
        JPanel      contentPane = new JPanel( new BorderLayout() );
        contentPane.add( scrollPane );
        frame.setContentPane( contentPane );
        frame.setLocation( 300, 10 );
        frame.pack();
        frame.setVisible( true );
    }

    public static Display getDisplay()
    {
        if ( SwingUtilities.isEventDispatchThread() )
            instance = new Display();
        else
            try
            {
                SwingUtilities.invokeAndWait( 
                    () -> instance = new Display()
                );
            }
            catch ( InterruptedException | InvocationTargetException exc )
            {
                exc.printStackTrace();
                System.exit( 1 );
            }
        return instance;
    }
    
    public void print( Object obj )
    {
        textArea.append( obj.toString() );
    }
    
    public void println( Object obj )
    {
        textArea.append( obj.toString() + newLine );
    }
}
