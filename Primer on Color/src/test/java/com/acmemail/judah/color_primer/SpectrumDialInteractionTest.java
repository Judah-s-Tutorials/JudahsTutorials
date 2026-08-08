package com.acmemail.judah.color_primer;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.color_primer.util.ComponentFinder;

/**
 * Regression test for a bug in which SpectrumDial's MouseMonitor and
 * KeyMonitor inner classes called repaint()/requestFocusInWindow() on
 * the class's static singleton instance instead of the enclosing
 * SpectrumDial instance. This meant that any independently-created
 * SpectrumDial (as opposed to the one static instance used by main())
 * never visually updated in response to mouse or keyboard input.
 * 
 * @author claude.ai
 */
class SpectrumDialInteractionTest
{
    /**
     * Make sure that all Windows created during this test
     * are disposed before JUnit starts the next test.
     */
    @AfterAll
    public static void afterAll()
    {
        ComponentFinder.disposeAll();
    }
    
    @Test
    void mouseClickShouldRepaintTheClickedDial()
        throws InvocationTargetException, InterruptedException
    {
        SpectrumDial    dial    = new SpectrumDial( 300 );
        DialFrame       frame   = new DialFrame( dial );
        frame.start();
        invokeAndWait( () -> {} );

        // Force an immediate, synchronous paint so that the dial's
        // internal notion of its own bounds (used to hit-test mouse
        // clicks) is established before we click on it.
        invokeAndWait( () -> dial.paintImmediately( dial.getBounds() ) );

        RecordingRepaintManager recorder    = new RecordingRepaintManager();
        RepaintManager          saved       = RepaintManager.currentManager( dial );
        invokeAndWait( () -> RepaintManager.setCurrentManager( recorder ) );

        try
        {
            int         xco = dial.getWidth() / 2;
            int         yco = dial.getHeight() / 2;
            MouseEvent  evt = new MouseEvent(
                dial,
                MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(),
                0,
                xco,
                yco,
                1,
                false,
                MouseEvent.BUTTON1
            );
            invokeAndWait( () -> dial.dispatchEvent( evt ) );

            assertTrue(
                recorder.getRepainted().contains( dial ),
                "Clicking the dial should repaint the clicked "
                    + "SpectrumDial instance, not some other instance"
            );
        }
        finally
        {
            invokeAndWait( () -> RepaintManager.setCurrentManager( saved ) );
        }
    }

    private static void invokeAndWait( Runnable runner )
        throws InvocationTargetException, InterruptedException
    {
        if ( SwingUtilities.isEventDispatchThread() )
            runner.run();
        else
            SwingUtilities.invokeAndWait( runner );
    }

    /** Records every JComponent passed to addDirtyRegion (i.e. repaint()). */
    private static class RecordingRepaintManager extends RepaintManager
    {
        private final Set<JComponent>  repainted   = new HashSet<>();

        @Override
        public void addDirtyRegion(
            JComponent comp, int x, int y, int width, int height
        )
        {
            repainted.add( comp );
            super.addDirtyRegion( comp, x, y, width, height );
        }

        public Set<JComponent> getRepainted()
        {
            return repainted;
        }
    }
}
