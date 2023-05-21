package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;

/**
 * Program to demonstrate how to use Robot
 * to interact with 
 * a JFileChooser.
 * 
 * @author Jack Straub
 */
public class RobotDemo2
{    
    private static final String sep         = File.separator;
    private static final String tempDir     = 
        System.getProperty( "java.io.tmpdir" );
    private static final String tempName1  = "CartesianPlaneTestFile1.txt";
    private static final File   tempFile1   =
        new File( tempDir + sep + tempName1 );
    
    /** Value set by runFileChooser() from a separate thread. */
    private static int  fileChooserStatus   = -1;
    /** Value set by runFileChooser() from a separate thread. */
    private static File fileChooserFile     = null;
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        cleanUp();        
        try
        {
            exec();
        }
        catch ( InterruptedException | AWTException exc )
        {
            System.out.println( exc.getClass().getName() );
            System.out.println( exc.getMessage() );
        }
        cleanUp();
    }
    
    /**
     * Start a JFileChooser in a separate thread.
     * Use robot to enter a file path
     * and select the Open button.
     * 
     * @throws AWTException
     *      if Robot instantiation fails
     * @throws InterruptedException
     *      if a Thread utility is interrupted
     */
    private static void exec()
       throws AWTException, InterruptedException
   {
        Robot   robot = new Robot();
        Thread  chooserThread   = new Thread( () -> runFileChooser( true ) );
        chooserThread.start();
        Thread.sleep( 1000 );
        
        type( tempFile1.getAbsolutePath(), robot, KeyEvent.VK_ENTER );
        chooserThread.join();
        
        boolean approved    = 
            fileChooserStatus == JFileChooser.APPROVE_OPTION;
        String  selected    = 
            approved ? fileChooserFile.getAbsolutePath() : "none";
        System.out.println( "Approved: " + approved );
        System.out.println( "Selected: " + selected );
   }
    
    /**
     * Start a JFileChooser dialog.
     * If the given Boolean value is true
     * the dialog will be started
     * with the <em>showOpenDialog()</em> method,
     * otherwise the <em>showSaveDialog()</em> method
     * will be used.
     * 
     * <p>
     * Postconditions:
     * <ol>
     * <li>
     *      The fileChooserStatus class variable
     *      will be set to the value
     *      returned by the method
     *      used to start the JFilechooser.
     * </li>
     * <li>
     *      The fileChooserFile class variable
     *      will be set to the selected file,
     *      or null, if the operation is cancelled.
     * </li>
     * </ol>
     * 
     * @param open  the given Boolean value
     */
    private static void runFileChooser( boolean open )
    {
        JFileChooser    chooser = new JFileChooser();
        if ( open )
            fileChooserStatus = chooser.showOpenDialog(chooser);
        else
            fileChooserStatus = chooser.showSaveDialog(chooser);

        if ( fileChooserStatus != JFileChooser.APPROVE_OPTION )
            fileChooserFile = null;
        else
            fileChooserFile  = chooser.getSelectedFile();
    }
    
    /**
     * Uses a given robot to simulate
     * typing a given string.
     * The process is terminated
     * by typing a given key.
     * Not every character can be typed.
     * The following characters 
     * are guaranteed to work.
     * 
     * <ul>
     * <li>Alphanumeric characters</li>
     * <li>Colon (:)</li>
     * <li>Backslash (\)</li>
     * <li>Enter (VK_ENTER)</li>
     * <li>Escape (VK_ESCAPE)</li>
     * </ul>
     * 
     * @param str       the given string
     * @param robot     the given robot
     * @param lastKey   the given key terminating the process
     * 
     * @throws InterruptedException
     *      if a waiting thread method is interrupted
     */
    private static void type( String str, Robot robot, int lastKey )
        throws InterruptedException
    {
        int     shift   = KeyEvent.VK_SHIFT; 
        for ( char ccc : str.toCharArray() )
        {
            boolean isUpper = Character.isUpperCase( ccc );
            char    upper   = Character.toUpperCase( ccc );
            
            if ( upper == ':' )
            {
                upper = ';';
                isUpper = true;
            }
            
            if ( isUpper )
                robot.keyPress( shift );
            robot.keyPress( upper );
            robot.keyRelease( upper );
            if ( isUpper )
                robot.keyRelease( shift );
        }
        Thread.sleep( 2000 );
        robot.keyPress( lastKey );
        robot.keyRelease( lastKey );
    }

    /**
     * If necessary,
     * delete the file created
     * during execution of this program.
     */
    private static void cleanUp()
    {
        if ( tempFile1.exists() )
            tempFile1.delete();
    }
}
