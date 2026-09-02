package com.acmemail.judah.battleship.artwork.awt;

import java.awt.Container;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GridFrame
{
    private static final String title   = "Grid Frame";
    private final JFrame        frame;
    private final Container     client;
    
    public GridFrame( Supplier<Container> supplier )
    {
        frame   = new JFrame( title );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        client = supplier.get();
        frame.setContentPane( client );
        frame.pack();
        frame.setVisible( true );
    }
    
    public Container getClient()
    {
        return client;
    }
    
    public static GridFrame getFrame( Supplier<Container> supplier )
    {
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
                exc.printStackTrace();
                System.exit( 1 );
            }
        }
        return gridFrame[0];
    }
}
