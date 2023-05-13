package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class RobotDemo1
{
    private static final int    shift   = KeyEvent.VK_SHIFT; 
    private static final int    enter   = KeyEvent.VK_ENTER;

    private static final String[]   testLines   =
    {
         "The sun was shining on the sea,",
         "Shining with all his might;",
         "He did his very best to make",
         "The billows smooth and bright;",
         "And this was odd, because it was",
         "The middle of the night."
    };
    
    public static void main(String[] args)
    {
        try
        {
            SwingUtilities.invokeAndWait( () -> showGUI() );
        }
        catch ( InterruptedException | InvocationTargetException exc )
        {
            System.err.println( "show GUI failure" );
            System.err.println( exc.getClass().getName() );
            System.err.println( exc.getMessage() );
            System.exit( 1 );
        }
        pause( 100 );
        
        Robot   robot = getRobot();
        Arrays.stream( testLines ).forEach( l -> type( l, robot ) );
    }
    
    private static Robot getRobot() 
    {
        try
        {
            Robot   robot   = new Robot();
            return robot;
        }
        catch ( AWTException exc )
        {
            System.err.println( "Robot exception" );
            System.err.println( exc.getMessage() );
            System.exit( 1 );
        }
        return null;
    }
    
    private static void type( String str, Robot robot )
    {
        for ( char ccc : str.toCharArray() )
        {
            pause( 10 );
            boolean isUpper = Character.isUpperCase( ccc );
            char    upper   = Character.toUpperCase( ccc );
            if ( isUpper )
                robot.keyPress( shift );
            robot.keyPress( upper );
            robot.keyRelease( upper );
            if ( isUpper )
                robot.keyRelease( shift );
        }
        robot.keyPress( enter );
        robot.keyRelease( enter );
    }

    private static void pause( long millis )
    {
        try
        {
            Thread.sleep( millis );
        }
        catch ( InterruptedException exc )
        {
       }
    }
    
    private static void showGUI()
    {
        JFrame  frame   = new JFrame( "Robot Test" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        JPanel  panel   = new JPanel();
        frame.setContentPane( panel );
        
        JTextArea   textBox = new JTextArea( 10, 25 );
        panel.add( textBox );
        
        frame.pack();
        frame.setVisible( true );
    }
}
