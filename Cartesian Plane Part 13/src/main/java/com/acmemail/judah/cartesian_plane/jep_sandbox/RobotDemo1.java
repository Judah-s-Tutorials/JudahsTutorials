package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * Program that uses Robot 
 * to translate strings
 * into key-press/key-release events.
 * Only spaces and alphanumeric characters are recognized;
 * all other characters
 * result in printed error messages.
 * 
 * @author Jack Straub
 */
public class RobotDemo1
{
    private static final String[]   testLines   =
    {
         "The sun was shining on the sea,",
         "Shining with all his might;",
         "He did his very best to make",
         "The billows smooth and bright;",
         "And this was odd, because it was",
         "The middle of the night."
    };
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments, ignored
     */
    public static void main(String[] args)
    {
        try
        {
            // Start GUI using invokeAndWait; this method won't
            // return until GUI is configured and made visible.
            SwingUtilities.invokeAndWait( () -> showGUI() );
        }
        catch ( InterruptedException | InvocationTargetException exc )
        {
            System.err.println( "show GUI failure" );
            System.err.println( exc.getClass().getName() );
            System.err.println( exc.getMessage() );
            System.exit( 1 );
        }
        
        try
        {
            // Instantiate and configure Robot.
            Robot   robot = new Robot();
            robot.setAutoDelay( 10 );
            
            // Print test lines one at a time; the type method
            // will take care of line separators.
            Arrays.stream( testLines ).forEach( l -> type( l, robot ) );
        }
        catch ( AWTException exc )
        {
            System.err.println( exc.getMessage() );
            System.exit( 1 );
        }
    }
    
    /**
     * Translate the characters in a given string
     * into key-press/key-release events
     * issued by a given Robot.
     * Characters that cannot
     * be reliably translated 
     * are ignored.
     * The translation of a string
     * is followed by a line separator, 
     * generated via VK_ENTER
     * press/release events.
     * 
     * @param str       the given string
     * @param robot     the given Robot
     */
    private static void type( String str, Robot robot )
    {
        for ( char ccc : str.toCharArray() )
        {
            if ( canTranslate( ccc ) )
            {
                boolean isUpper = Character.isUpperCase( ccc );
                char    upper   = Character.toUpperCase( ccc );
                if ( isUpper )
                    robot.keyPress( KeyEvent.VK_SHIFT );
                robot.keyPress( upper );
                robot.keyRelease( upper );
                if ( isUpper )
                    robot.keyRelease( KeyEvent.VK_SHIFT );
            }
        }
        robot.keyPress( KeyEvent.VK_ENTER );
        robot.keyRelease( KeyEvent.VK_ENTER );
    }
    
    /**
     * Determines if a character
     * can be reliably translated
     * into key-press/key-release events.
     * This will be true
     * if the character is alphanumeric
     * or a space.
     * If the character cannot be translated
     * an error message is printed to stderr.
     * 
     * @param ccc
     * @return
     */
    private static boolean canTranslate( char ccc )
    {
        boolean result  = 
            Character.isAlphabetic( ccc ) || 
            Character.isDigit( ccc )      ||
            ccc == ' ';
        
        if ( !result )
        {
            String  message =
                "Can't translate '" + ccc + "' into robot";
            System.err.println( message );
        }
        return result;
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
