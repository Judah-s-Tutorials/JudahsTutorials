package com.acmemail.judah.battleship.artwork.awt;

import java.awt.Container;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.function.Supplier;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.acmemail.judah.battleship.BattleshipException;

/**
 * Encapsulate an application frame
 * for a {@link GridWindow} window hierarchy.
 * The class method {@link #getFrame(Supplier)} is designed
 * to make sure that the frame
 * is instantiated and configured on the EDT.
 */
public class GridFrame
{
    /** The title for the frame. */
    private static final String title   = "Grid Frame";
    /** The frame component. */
    private final JFrame        frame;
    
    /**
     * Constructor.
     * Fully configures the application frame
     * and makes it visible.
     * 
     * @param supplier  
     *      supplier for the application window
     *      to be encapsulated in the frame's content pane
     */
    private GridFrame( Supplier<Container> supplier )
    {
        frame   = new JFrame( title );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        Container   client = supplier.get();
        frame.setContentPane( client );
        frame.pack();
        frame.setVisible( true );
    }
    
    /**
     * Gets the client window encapsulated
     * in the frame's content pane.
     * 
     * @return  client window encapsulated in the frame's content pane
     */
    public Container getClient()
    {
        Container   client  = frame.getContentPane();
        return client;
    }
    
    /**
     * Instantiate a {@code GridFrame},
     * ensuring that the instantiation occurs on the EDT.
     * The client passes a {@code Supplier}
     * that is used to obtain the application's main window.
     * Unless called from the EDT,
     * the supplier should instantiate the application window,
     * thereby ensuring that instantiation
     * occurs on the EDT.
     * 
     * @param supplier  supplier of the application's main window
     * 
     * @return  the instantiated {@code GridFrame}
     * 
     * @throws NullPointerException if supplier is null
     * @throws BattleshipException 
     *      if an error occurs while executing on the EDT
     */
    public static GridFrame getFrame( Supplier<Container> supplier )
    {
        Objects.requireNonNull( supplier, "supplier" );
        GridFrame[] gridFrame   = new GridFrame[1];
        if ( SwingUtilities.isEventDispatchThread() )
            gridFrame[0] = new GridFrame( supplier );
        else
        {
            try
            {
                SwingUtilities.invokeAndWait( () -> 
                    gridFrame[0] = new GridFrame( supplier )
                );
            }
            catch ( InvocationTargetException | InterruptedException exc )
            {
                String  msg = "Unexpected exception on EDT";
                throw new BattleshipException( msg, exc );
            }
        }
        return gridFrame[0];
    }
}
