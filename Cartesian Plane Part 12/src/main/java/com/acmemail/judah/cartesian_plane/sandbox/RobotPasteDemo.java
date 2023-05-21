package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;

/**
 * Program to demonstrate
 * how to use the system clipboard
 * in conjunction with the Robot class
 * to interact with a JFileChooser.
 * 
 * @author Jack Straub
 */
public class RobotPasteDemo
{    
    private static final String endl        = System.lineSeparator();
    private static final String sep         = File.separator;
    private static final String tempDir     = 
        System.getProperty( "java.io.tmpdir" );
    private static final String tempName1  = "CartesianPlaneTestFile1.txt";
    private static final File   tempFile1   =
        new File( tempDir + sep + tempName1 );
    
    private static final JFileChooser chooser = new JFileChooser();

    /** Robot to type paste command. */
    private static Robot    robot;
    /** JFileChooser status, set by runFileChooser() method. */
    private static int      fileChooserStatus   = -1;
    /** JFileChooser selection, set by runFileChooser() method. */
    private static File     fileChooserFile     = null;
    
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
     * Run the demo.
     * 
     * @throws AWTException
     *      if Robot instantiation fails
     * @throws InterruptedException
     *      if a waiting Thread operation is interrupted
     */
    private static void exec()
       throws AWTException, InterruptedException
   {
        robot = new Robot();
        runDemo( true, KeyEvent.VK_ENTER );
        runDemo( true, KeyEvent.VK_ESCAPE );
        runDemo( false, KeyEvent.VK_ENTER );
        runDemo( false, KeyEvent.VK_ESCAPE );
    }
    
    /**
     * Run the demo in a given configuration.
     * If the open argument is true,
     * the JFileChooser is displayed
     * using the showOpenDialog method,
     * otherwise the showSaveDialog method is used.
     * The lastKey argument determines
     * how the dialog is dismissed;
     * it should be either VK_ENTER (approve the operation)
     * or VK_ESCAPE (cancel the operation).
     * 
     * @param open      determines how the JFileChooser is displayed
     * @param lastKey   key used to dismiss the JFileChooser
     * 
     * @throws InterruptedException
     *      if a waiting Thread operation is interrupted
     */
    private static void runDemo( boolean open, int lastKey )
        throws InterruptedException
    {
        Thread  chooserThread   = new Thread( () -> runFileChooser( open ) );
        chooserThread.start();
        Thread.sleep( 1000 );
        
        type( tempFile1.getAbsolutePath(), lastKey );
        chooserThread.join();
        showResult();
    }
    
    /**
     * Prints the result
     * of one execution of the demo.
     * Indicates how the dialog was closed
     * (approved or cancelled)
     * and what file was selected,
     * if any.
     */
    private static void showResult()
    {
        boolean approved    = fileChooserStatus == JFileChooser.APPROVE_OPTION;
        String  status      = null;
        String  selection   = null;
        
        if ( approved )
        {
            status = "Approved";
            selection = fileChooserFile.getAbsolutePath();
        }
        else
        {
            status = "Cancelled";
            selection = "None";
        }
        String  message = 
            "Status: " + status + endl
            + "Selection: " + selection;
        System.out.println( message );
    }
    
    /**
     * Starts the JFileChooser dialog
     * and saves the result
     * The given Boolean value
     * determines how the dialog is started:
     * true to use showOpenDialog(),
     * false to use showSaveDialog().
     * 
     * 
     * @param open  the given Boolean value
     */
    private static void runFileChooser( boolean open )
    {
        if ( open )
            fileChooserStatus = chooser.showOpenDialog( null );
        else
            fileChooserStatus = chooser.showSaveDialog( null );
        fileChooserFile  = chooser.getSelectedFile();
    }
    
    /**
     * Uses a Robot to type a given string.
     * The operation is terminated
     * by typing a given character.
     * Prior to terminating the operation
     * the method sleeps for two seconds
     * so that the operator can examine
     * what was typed.
     * 
     * @param str       the given string
     * @param lastKey   the given character
     * 
     * @throws InterruptedException
     *      if Thread.sleep() is interrupted
     */
    private static void type( String str, int lastKey )
        throws InterruptedException
    {
        String      osName          = 
            System.getProperty( "os.name" ).toLowerCase();
        int         pasteModifier   =
            osName.equals( "mac" ) ? KeyEvent.VK_META : KeyEvent.VK_CONTROL;

        Clipboard   clipboard       = 
            Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection   = new StringSelection( str );
        clipboard.setContents( selection, null );
        
        robot.keyPress( pasteModifier );
        robot.keyPress( KeyEvent.VK_V );
        robot.keyRelease( KeyEvent.VK_V );
        robot.keyRelease( pasteModifier );
        Thread.sleep( 2000 );
        robot.keyPress( lastKey );
        robot.keyRelease( lastKey );
    }

    /**
     * Delete any file that may have been created
     * during execution of this demo.
     */
    private static void cleanUp()
    {
        if ( tempFile1.exists() )
            tempFile1.delete();
    }
}
