package com.acmemail.judah.cartesian_plane.test_utils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

public class RobotAssistant
{
    private static final String     osName          = 
        System.getProperty( "os.name" ).toLowerCase();
    private static final int        pasteModifier   =
        osName.equals( "mac" ) ? KeyEvent.VK_META : KeyEvent.VK_CONTROL;

    private static final int        pasteKey        = KeyEvent.VK_V;
    private static final Clipboard  clipboard       = 
        Toolkit.getDefaultToolkit().getSystemClipboard();
    
    private final Robot robot;
    
    public RobotAssistant() throws AWTException
    {
        robot = new Robot();
        robot.setAutoDelay( 10 );
    }
    
    public void type( String chars, int lastKey )
    {
        StringSelection selection    = new StringSelection( chars );
        clipboard.setContents( selection, null );
        
        robot.keyPress( pasteModifier );
        robot.keyPress( pasteKey );
        robot.keyRelease( pasteKey );
        robot.keyRelease( pasteModifier );
        robot.keyPress( lastKey );
        robot.keyRelease( lastKey );
    }
}
