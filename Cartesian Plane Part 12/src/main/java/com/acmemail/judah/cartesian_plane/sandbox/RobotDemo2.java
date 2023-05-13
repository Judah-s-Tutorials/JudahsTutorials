package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RobotDemo2
{
    private static final Map<Character,Integer> charMap = new HashMap<>();
    static
    {
        charMap.put( ':', KeyEvent.VK_COLON );
        charMap.put( '\\', KeyEvent.VK_BACK_QUOTE );
    }
    
    private static final String sep         = File.separator;
    private static final String tempDir     = 
        System.getProperty( "java.io.tmpdir" );
    private static final String tempName1  = "CartesianPlaneTestFile1.txt";
    private static final String tempName2  = "CartesianPlaneTestFile2.txt";
    private static final File   tempFile1   =
        new File( tempDir + sep + tempName1 );
    private static final File   tempFile2   =
        new File( tempDir + sep + tempName2 );
    
    public static void main(String[] args)
    {
        cleanUp();
        System.out.println( tempFile1.getAbsolutePath() );
        System.out.println( tempFile2.getAbsolutePath() );
//        JFileChooser    chooser = new JFileChooser();
//        chooser.showOpenDialog(chooser);
//        pause( 100 );
        
        Robot   robot = getRobot();
        type( tempFile1.getAbsolutePath(), robot );
        cleanUp();
    }
    
    private static void cleanUp()
    {
        if ( tempFile1.exists() )
            tempFile1.delete();
        if ( tempFile2.exists() )
            tempFile2.delete();
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
        int     shift   = KeyEvent.VK_SHIFT; 
        int     enter   = KeyEvent.VK_ENTER;
        for ( char ccc : str.toCharArray() )
        {
            pause( 10 );
            boolean isUpper = 
                ccc == ':' ? true : Character.isUpperCase( ccc );
            char    upper   = Character.toUpperCase( ccc );
            int     iKey    = charMap.getOrDefault( upper, (int)upper );
            
            if ( isUpper )
                robot.keyPress( shift );
                
            robot.keyPress( iKey );
            robot.keyRelease( iKey );
            if ( isUpper )
                robot.keyRelease( shift );
        }
        pause( 10 );
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
}
