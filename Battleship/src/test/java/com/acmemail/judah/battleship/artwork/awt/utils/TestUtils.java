package com.acmemail.judah.battleship.artwork.awt.utils;

import java.awt.Component;
import java.awt.Window;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.function.Supplier;

import javax.swing.SwingUtilities;

import com.acmemail.judah.battleship.BattleshipException;

/**
 * This is a utility class
 * with support for the test classes
 * in the artwork.awt package.
 */
public class TestUtils
{
    /**
     * Default constructor, not used.
     */
    private TestUtils()
    {
        // not used
    }
    
    /**
     * Execute a task in the context of the EDT.
     * Asserts failure if the operation fails.
     * 
     * @param runner    the given task
     * 
     * @throws  NullPointerException if runner is null
     */
    public static void invokeAndWait( Runnable runner )
    {
        Objects.requireNonNull( runner, "runner" );
        if ( SwingUtilities.isEventDispatchThread() )
            runner.run();
        else
        {
            try
            {
                SwingUtilities.invokeAndWait( () -> runner.run() );
            }
            catch ( InterruptedException | InvocationTargetException exc )
            {
                exc.printStackTrace();
                throw new BattleshipException( "unexpected exception", exc );
            }
        }
    }
    
    /**
     * Execute the given supplier
     * in the context of the EDT,
     * and return the supplied value.
     * 
     * @param <T>       the type of the Supplier
     * @param supplier  the given supplier
     * 
     * @return  the value obtained from the supplier
     * 
     * @throws NullPointerException if supplier is null
     */
    public static <T> T invokeAndWaitGet( Supplier<T> supplier )
    {
        Objects.requireNonNull( supplier, "supplier" );
        Object[]    result  = new Object[1];
        invokeAndWait( () -> result[0] = supplier.get() );
        @SuppressWarnings("unchecked")
        T           rVal    = (T)result[0];
        return rVal;
    }

    /**
     * Gets the root of the window hierarchy
     * for the given component. 
     * Returns null if not found.
     * 
     * @param component the given component
     * 
     * @return  
     *      the root of the window hierarchy for component,
     *      or null if not found.
     *      
     * @throws NullPointerException if component is null
     */
    public static Window getAppRoot( Component component )
    {
        Objects.requireNonNull( component, "component" );
        Supplier<Window>    supplier    = () -> {
            Component parent    = component;
            while ( parent != null && !(parent instanceof Window ))
                parent = parent.getParent();
            return (Window)parent;
        };
        
        Window[]    result  = new Window[1];
        if ( SwingUtilities.isEventDispatchThread() )
            result[0] = supplier.get();
        else
            TestUtils.invokeAndWait( () -> result[0] = supplier.get() );
        return result[0]; 
    }
}
