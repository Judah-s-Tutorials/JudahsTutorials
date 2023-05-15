package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.fail;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileManagerFileChooserTest
{
    private static final String sep         = File.separator;
    private static final String tempDir     = 
        System.getProperty( "java.io.tmpdir" );
    private static final String tempName1  = "CartesianPlaneTestFile1.txt";
    private static final File   tempFile1   =
        new File( tempDir + sep + tempName1 );
    
    private static final Equation   equationOut = new Exp4jEquation();
    private Equation    equationIn  = null;
    
    private Robot   robot               = null;
    private int     fileChooserStatus   = -1;
    private File    fileChooserFile     = null;

    @AfterAll
    static void tearDownAfterClass() throws Exception
    {
        if ( tempFile1.exists() )
            tempFile1.delete();
    }
    
    @BeforeEach
    public void beforeEach()
    {
        try
        {
            robot = new Robot();
        }
        catch ( AWTException exc )
        {
            System.err.println( exc.getMessage() );
            System.exit( 1 );
        }
    }

    @Test
    void testSaveEquation()
    {
        try
        {
            fileChooserTest( true, KeyEvent.VK_ENTER );
        }
        catch ( InterruptedException exc )
        {
            System.out.println( exc.getClass().getName() );
            fail( exc.getMessage() );
        }
    }

    @Test
    void testOpen()
    {
        try
        {
            fileChooserTest( true, KeyEvent.VK_ENTER );
        }
        catch ( InterruptedException exc )
        {
            System.out.println( exc.getClass().getName() );
            fail( exc.getMessage() );
        }
    }
    
    private void fileChooserTest( boolean open, int lastChar )
        throws InterruptedException
    {
         Thread  chooserThread   = new Thread( () -> runFileChooser( open ) );
         chooserThread.start();
         Thread.sleep( 1000 );
         
         type( tempFile1.getAbsolutePath(), lastChar );
         chooserThread.join();
         
         boolean approved    = 
             fileChooserStatus == JFileChooser.APPROVE_OPTION;
         String  selected    = 
             approved ? fileChooserFile.getAbsolutePath() : "none";
         System.out.println( "Approved: " + approved );
         System.out.println( "Selected: " + selected );
    }
    
    private void type( String str, int lastKey )
        throws InterruptedException
    {
        int     shift   = KeyEvent.VK_SHIFT; 
        for ( char ccc : str.toCharArray() )
        {
            Thread.sleep( 10 );
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
        Thread.sleep( 10 );
        robot.keyPress( lastKey );
        robot.keyRelease( lastKey );
    }
    
    private void runFileChooser( boolean open )
    {
        if ( open )
            equationIn = FileManager.open();
        else
            FileManager.save( equationOut );
    }
}
