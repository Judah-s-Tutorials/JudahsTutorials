package com.acmemail.judah.battleship.components;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import com.acmemail.judah.battleship.BattleshipException;

public class ComponentUtils
{
    public static void invokeLater( Runnable task )
    {
        if ( SwingUtilities.isEventDispatchThread() )
            task.run();
        else
            SwingUtilities.invokeLater( task );
    }
    
    public static void invokeAndWait( Runnable task )
    {
        if ( SwingUtilities.isEventDispatchThread() )
            task.run();
        else
        {
            try
            {
                SwingUtilities.invokeAndWait( task );
            }
            catch ( InterruptedException | InvocationTargetException exc )
            {
                BattleshipException reraise = 
                    new BattleshipException( "Unexpected exception", exc );
                if ( exc instanceof InterruptedException )
                    Thread.currentThread().interrupt();
                throw reraise;
            }
        }
    }
}
