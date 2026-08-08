package com.acmemail.judah.color_primer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.color_primer.util.ComponentFinder;

/**
 * Behavioral tests for SpectrumDial's mouse- and keyboard-driven
 * interaction: clicking or dragging the bar, and nudging it with
 * the arrow keys. barAngle has no public accessor (it is purely
 * driven by user interaction), so these tests read it back via
 * reflection rather than adding test-only production API.
 *
 * @author claude.ai
 *
 * @see SpectrumDialInteractionTest
 */
class SpectrumDialMouseKeyTest
{
    private SpectrumDial    dial;

    @BeforeEach
    public void setUp() throws InvocationTargetException, InterruptedException
    {
        dial = new SpectrumDial( 300 );
        DialFrame   frame   = new DialFrame( dial );
        frame.start();
        invokeAndWait( () -> {} );

        // Force a real paint pass so refRect (used to hit-test clicks)
        // and colorGlobe's position are established.
        invokeAndWait( () -> dial.paintImmediately( dial.getBounds() ) );
    }
    
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
    public void clickInsideDialSetsBarAngleToClickedAngle()
        throws Exception
    {
        // Directly right of center: angle 0 degrees.
        Point   rightOfCenter   = offsetFromCenter( 1, 0 );
        click( rightOfCenter );
        assertAngleCloseTo( 0, dial.getBarAngle() );

        // Directly above center: angle 90 degrees (Java y grows
        // downward, so "up" is a negative y offset).
        Point   aboveCenter     = offsetFromCenter( 0, -1 );
        click( aboveCenter );
        assertAngleCloseTo( 90, dial.getBarAngle() );
    }

    @Test
    public void clickOutsideDialDoesNotChangeBarAngle() throws Exception
    {
        int     before  = dial.getBarAngle();
        click( new Point( 1, 1 ) );
        assertEquals( before, dial.getBarAngle() );
    }

    @Test
    public void upArrowIncrementsBarAngleByOneDegree() throws Exception
    {
        click( offsetFromCenter( 1, 0 ) );
        int     before  = dial.getBarAngle();
        pressKey( KeyEvent.VK_UP );
        assertEquals( before + 1, dial.getBarAngle() );
    }

    @Test
    public void downArrowFromZeroWrapsToThreeFiveNine() throws Exception
    {
        assertEquals( 0, dial.getBarAngle() );
        pressKey( KeyEvent.VK_DOWN );
        assertEquals( 359, dial.getBarAngle() );
    }

    @Test
    public void unmappedKeyDoesNotChangeBarAngle() throws Exception
    {
        int     before  = dial.getBarAngle();
        pressKey( KeyEvent.VK_ENTER );
        assertEquals( before, dial.getBarAngle() );
    }

    @Test
    public void draggingWithoutFirstGrabbingGlobeDoesNotChangeBarAngle()
        throws Exception
    {
        int     before  = dial.getBarAngle();
        drag( offsetFromCenter( 0, -1 ) );
        assertEquals( before, dial.getBarAngle() );
    }

    @Test
    public void draggingGlobeAfterPressingItChangesBarAngle() throws Exception
    {
        Point   globeCenter = getGlobeCenter();
        press( globeCenter );
        drag( offsetFromCenter( 0, -1 ) );
        assertAngleCloseTo( 90, dial.getBarAngle() );
    }

    /**
     * The exact angle computed from a clicked/dragged point is
     * subject to +.5 rounding in the production code, so compare
     * with a small tolerance rather than exact equality.
     */
    private static void assertAngleCloseTo( int expected, int actual )
    {
        int     diff    = Math.abs( expected - actual );
        assertTrue(
            diff <= 1,
            "expected angle near " + expected + " but was " + actual
        );
    }

    /**
     * @param xUnit sign/magnitude of the horizontal offset from
     *              center, as a fraction of the dial's half-width
     * @param yUnit sign/magnitude of the vertical offset from
     *              center, as a fraction of the dial's half-height
     *
     * @return  a point safely inside the dial's circular area,
     *          offset from its center in the given direction
     */
    private Point offsetFromCenter( int xUnit, int yUnit )
    {
        int     cx      = dial.getWidth() / 2;
        int     cy      = dial.getHeight() / 2;
        int     offset  = dial.getWidth() / 4;
        return new Point( cx + xUnit * offset, cy + yUnit * offset );
    }

    private void click( Point point ) throws Exception
    {
        dispatch( MouseEvent.MOUSE_CLICKED, point );
    }

    private void press( Point point ) throws Exception
    {
        dispatch( MouseEvent.MOUSE_PRESSED, point );
    }

    private void drag( Point point ) throws Exception
    {
        dispatch( MouseEvent.MOUSE_DRAGGED, point );
    }

    private void dispatch( int eventID, Point point ) throws Exception
    {
        MouseEvent  evt = new MouseEvent(
            dial,
            eventID,
            System.currentTimeMillis(),
            0,
            point.x,
            point.y,
            1,
            false,
            MouseEvent.BUTTON1
        );
        invokeAndWait( () -> dial.dispatchEvent( evt ) );
    }

    private void pressKey( int keyCode ) throws Exception
    {
        KeyEvent    evt = new KeyEvent(
            dial,
            KeyEvent.KEY_PRESSED,
            System.currentTimeMillis(),
            0,
            keyCode,
            KeyEvent.CHAR_UNDEFINED
        );
        invokeAndWait( () -> dial.dispatchEvent( evt ) );
    }

    private Point getGlobeCenter() throws Exception
    {
        Field   globeField = SpectrumDial.class.getDeclaredField( "colorGlobe" );
        globeField.setAccessible( true );
        Object  colorGlobe  = globeField.get( dial );

        Field   circleField =
            colorGlobe.getClass().getDeclaredField( "gCircle" );
        circleField.setAccessible( true );
        Ellipse2D   gCircle = (Ellipse2D)circleField.get( colorGlobe );

        return new Point(
            (int)gCircle.getCenterX(), (int)gCircle.getCenterY() );
    }

    private static void invokeAndWait( Runnable runner )
        throws InvocationTargetException, InterruptedException
    {
        if ( SwingUtilities.isEventDispatchThread() )
            runner.run();
        else
            SwingUtilities.invokeAndWait( runner );
    }
}
