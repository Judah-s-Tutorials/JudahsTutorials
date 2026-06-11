package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.AWTException;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.test_utils.RobotAssistant;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

class ItemSelectionDialogTest
{
    /** The maximum time to wait for the dialog to become visible. */
    private static final long   MAX_FOCUS_WAIT  = 2000; // milliseconds
    /** The time to wait between checks for dialog visibility. */
    private static final long   FOCUS_WAIT      = 100;
    /** The maximum time to wait for the dialog thread to complete. */
    private static final long   MAX_DIALOG_WAIT = 2000;

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
        joinWithTimeout( thread );

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
        joinWithTimeout( thread );

        assertTrue( selection < 0 );
    }
    
    private Thread startDialog()
    {
        Thread  thread  = new Thread( () -> show() );
        thread.start();
        waitForDialog();
        return thread;
    }

    private void show()
    {
        selection = dialog.show();
    }

    /**
     * Wait for the dialog to become visible
     * and for one of its components to acquire focus.
     * If this doesn't happen within {@linkplain #MAX_FOCUS_WAIT}
     * milliseconds a failure is asserted.
     */
    private static void waitForDialog()
    {
        boolean focused     = false;
        long    startTime   = System.currentTimeMillis();
        while ( !focused
            && System.currentTimeMillis() - startTime < MAX_FOCUS_WAIT
        )
        {
            focused = Arrays.stream( Window.getWindows() )
                .filter( Window::isShowing )
                .map( Window::getFocusOwner )
                .anyMatch( Objects::nonNull );
            if ( !focused )
                Utils.pause( FOCUS_WAIT );
        }
        if ( !focused )
            fail( "dialog failed to become visible and acquire focus" );
    }

    /**
     * Join the given thread, waiting a maximum of
     * {@linkplain #MAX_DIALOG_WAIT} milliseconds.
     * If the thread does not complete in that time
     * a failure is asserted.
     *
     * @param thread    the given thread
     *
     * @throws InterruptedException if the join is interrupted
     */
    private static void joinWithTimeout( Thread thread )
        throws InterruptedException
    {
        thread.join( MAX_DIALOG_WAIT );
        if ( thread.isAlive() )
        {
            thread.interrupt();
            fail( "timeout waiting for dialog operation to complete" );
        }
    }
}
