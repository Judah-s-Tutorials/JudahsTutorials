package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;

public class RobotPasteDemo
{    
    private static final String endl        = System.lineSeparator();
    private static final String sep         = File.separator;
    private static final String tempDir     = 
        System.getProperty( "java.io.tmpdir" );
    private static final String tempName1  = "CartesianPlaneTestFile1.txt";
    private static final String tempName2  = "CartesianPlaneTestFile2.txt";
    private static final File   tempFile1   =
        new File( tempDir + sep + tempName1 );
    private static final File   tempFile2   =
        new File( tempDir + sep + tempName2 );
    
    private static final JFileChooser chooser = new JFileChooser();

    private static Robot    robot;
    private static int      fileChooserStatus   = -1;
    private static File     fileChooserFile     = null;
    
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
    
    private static void exec()
       throws AWTException, InterruptedException
   {
        robot = new Robot();
        runDemo( true, KeyEvent.VK_ENTER );
        runDemo( true, KeyEvent.VK_ESCAPE );
        runDemo( false, KeyEvent.VK_ENTER );
        runDemo( false, KeyEvent.VK_ESCAPE );
    }
    
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
    
    private static void runFileChooser( boolean open )
    {
        if ( open )
            fileChooserStatus = chooser.showOpenDialog( null );
        else
            fileChooserStatus = chooser.showSaveDialog( null );
        fileChooserFile  = chooser.getSelectedFile();
    }
    
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

    private static void cleanUp()
    {
        if ( tempFile1.exists() )
            tempFile1.delete();
        if ( tempFile2.exists() )
            tempFile2.delete();
    }
}
