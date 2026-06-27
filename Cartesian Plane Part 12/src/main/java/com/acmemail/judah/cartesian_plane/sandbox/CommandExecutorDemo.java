package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.List;
import java.util.ListIterator;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.app.CommandExecutorV1a;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;
import com.acmemail.judah.cartesian_plane.input.CommandReader;

/**
 * This application provides a quick example
 * of how to interface with CommandExecutorV1a.
 */
public class CommandExecutorDemo
{
    private static final List<String>   commands    = 
        List.of( 
            "set a=1, b=2, c=0", 
            "y= ax^2 + bx + c", 
            "start -4",
            "end 4",
            "step .01",
            "yplot"
        );
    
    /**
     * Default constructor; not used.
     */
    private CommandExecutorDemo()
    {
        // not used
    }

    /**
     * Application entry point.
     * 
     * @param args  Command line arguments; not used.
     */
    public static void main(String[] args)
    {
        CartesianPlane          plane       = new CartesianPlane();
        Root                    root        = new Root( plane );
        root.start();

        CommandExecutorV1a   executor        = new CommandExecutorV1a( plane );
        ListIterator<String>    iter        = commands.listIterator();
        executor.exec( () ->
            iter.hasNext() ? CommandReader.parseCommand( iter.next() ) : null );
    }
}
