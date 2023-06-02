package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.AWTException;
import java.awt.event.KeyEvent;

import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.test_utils.RobotAssistant;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

class ItemSelectionDialogTest
{
    private final String[]  names   =
    { "Sally", "Manny", "Jane", "Moe", "Anapurna", 
      "Jack", "Alice", "Tweedledee", "Elizabeth", "Tweedledum",
    };
    
    private final ItemSelectionDialog   dialog  =
        new ItemSelectionDialog( "JUnit Test", names );
    
    /** This variable set by show() method. */
    private int selection   = 0;
    
    @Test
    void testShowOK() 
        throws AWTException, InterruptedException
    {
        Thread          thread  = startDialog();
        RobotAssistant  robot   = new RobotAssistant();
        robot.downArrow();
        robot.downArrow();
        robot.type( "", KeyEvent.VK_ENTER );
        thread.join();
        
        assertEquals( 2, selection );
    }

    @Test
    void testShowCancel() 
        throws AWTException, InterruptedException
    {
        Thread          thread  = startDialog();
        RobotAssistant  robot   = new RobotAssistant();
        robot.downArrow();
        robot.type( "", KeyEvent.VK_ESCAPE );
        thread.join();
        
        assertTrue( selection < 0 );
    }
    
    private Thread startDialog()
    {
        Thread  thread  = new Thread( () -> show() );
        thread.start();
        Utils.pause( 500 );
        return thread;
    }
    
    private void show()
    {
        selection = dialog.show();
    }
}
