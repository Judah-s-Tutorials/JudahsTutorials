package com.acmemail.judah.battleship.artwork.awt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.Test;

import com.acmemail.judah.battleship.BattleshipException;
import com.acmemail.judah.battleship.artwork.awt.utils.TestUtils;

/**
 * 
 */
class GridFrameTest
{
    private Container   client;
    private GridFrame   gridFrame;
    
    @Test
    void testGetClient()
    {
        gridFrame = GridFrame.getFrame( () -> new Tester() );
        client = gridFrame.getClient();
        assertTrue( client instanceof Tester );
    }

    @Test
    void testGetFrameNoEDT()
    {
        gridFrame = GridFrame.getFrame( () -> {
            assertTrue( SwingUtilities.isEventDispatchThread() );
            client = new JPanel();
            return client;
        });
        assertEquals( client, gridFrame.getClient() );
        JFrame  jFrame  = getParentFrame( client );
        assertTrue( jFrame.isVisible() );
    }

    @Test
    void testGetFrameNoEDTGoWrong() 
        throws InvocationTargetException
    {
        Supplier<Container> supplier    = () -> {
            new TargetInvocationExceptionTester();
            return new JPanel();
        };
        assertThrows( 
            BattleshipException.class, () -> GridFrame.getFrame( supplier ) 
        );
        assertThrows(
            NullPointerException.class, () -> GridFrame.getFrame( null )
        );
    }

    @Test
    void testGetFrameEDTGoWrong()
    {
        Supplier<Container> supplier    = () -> {
            throw new IllegalStateException( "testing" );
        };
        TestUtils.invokeAndWait( () ->
            assertThrows(
                BattleshipException.class, () -> GridFrame.getFrame( supplier )
            )
        );
    }

    @Test
    void testGetFrameEDT()
    {
        try
        {
            SwingUtilities.invokeAndWait( () -> {
                client = new JPanel();
                gridFrame = GridFrame.getFrame( () -> client );
            });
        }
        catch ( InterruptedException | InvocationTargetException exc )
        {
            fail( "Unexpected exception", exc );
        }
        
        assertEquals( client, gridFrame.getClient() );
        JFrame  jFrame  = getParentFrame( client );
        assertTrue( jFrame.isVisible() );
    }

    /**
     * Get the JFrame in a Component's Component hierarchy.
     * Raises an assertion if none found.
     * 
     * @param comp  the component to examine
     * 
     * @return  the JFrame in comp's Component hierarchy
     */
    private static JFrame getParentFrame( Component comp )
    {
        assertNotNull( comp );
        Component   result  = comp;
        if ( !(result instanceof JFrame) )
            result = getParentFrame( comp.getParent() );
        return (JFrame)result;
    }
    
    /**
     * Unique subclass of a JPanel 
     * that assists in validating
     * the client instantiated by GridFrame.getFrame().
     */
    @SuppressWarnings("serial")
    private static class Tester extends JPanel {};

    /**
     * Used to generate InvocationTargetException in go-wrong tests.
     */
    @SuppressWarnings("serial")
    private static class TargetInvocationExceptionTester extends JPanel
    {
        public TargetInvocationExceptionTester()
        {
            throw new IllegalArgumentException( "testing" );
        }
    };
}
