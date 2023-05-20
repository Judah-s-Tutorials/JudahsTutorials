package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;

public class RobotDemo2
{    
    private static final String sep         = File.separator;
    private static final String tempDir     = 
        System.getProperty( "java.io.tmpdir" );
    private static final String tempName1  = "CartesianPlaneTestFile1.txt";
    private static final String tempName2  = "CartesianPlaneTestFile2.txt";
    private static final File   tempFile1   =
        new File( tempDir + sep + tempName1 );
    private static final File   tempFile2   =
        new File( tempDir + sep + tempName2 );
    
    private static int  fileChooserStatus   = -1;
    private static File fileChooserFile     = null;
    
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

    private static void cleanUp()
    {
        if ( tempFile1.exists() )
            tempFile1.delete();
        if ( tempFile2.exists() )
            tempFile2.delete();
    }
}
