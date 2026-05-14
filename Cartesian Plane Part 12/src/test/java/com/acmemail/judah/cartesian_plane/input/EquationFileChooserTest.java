package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.fail;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EquationFileChooserTest
{

    @BeforeEach
    public void setUp() throws Exception
    {
    }

    @AfterEach
    public void tearDown() throws Exception
    {
    }

    @Test
    public void testEquationFileChooser()
    {
        invokeAndWait( () -> {
           new EquationFileChooser(); 
        });
    }

    @Test
    public void testEquationFileChooserComponent()
    {
        invokeAndWait( () -> {
            new EquationFileChooser( null ); 
         });
         invokeAndWait( () -> {
             JFrame  frame   = new JFrame();
             new EquationFileChooser( frame ); 
             frame.dispose();
          });
    }

    @Test
    public void testOpenDialog()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testSaveDialog()
    {
        fail("Not yet implemented");
    }

    /**
     * Ensures that an operation executes in the context
     * of the event dispatch thread.
     * 
     * @param runner
     */
    private static void invokeAndWait( Runnable runner )
    {
        if ( EventQueue.isDispatchThread() )
            runner.run();
        else
        {
            try
            {
                EventQueue.invokeAndWait( runner );
            }
            catch ( InterruptedException | InvocationTargetException exc )
            {
                fail( exc );
            }
        }
    }
}
