package com.acmemail.judah.cartesian_plane.sandbox.stream_supplier;

import java.awt.Color;
import java.util.Arrays;
import java.util.stream.Stream;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class StreamSupplierDemo
{
    public static void main(String[] args)
    {
        CommandStreamer streamer    = new CommandStreamer();
        scheduleCommandExecution( streamer );
        streamer.setStreamSupplier( () -> streamGetter() );
        
        int status  = JOptionPane.OK_OPTION;
        while ( status == JOptionPane.OK_OPTION )
        {
            status = JOptionPane.showConfirmDialog( null, "New stream?" );
            if ( status == JOptionPane.OK_OPTION )
                scheduleCommandExecution( streamer );
        }
    }
    
    private static void 
    scheduleCommandExecution( CommandStreamer streamer )
    {
        System.out.println( "*** Scheduling execution" );
        SwingUtilities.invokeLater( () -> streamer.getAndTraverseStream() );
        System.out.println( "*** Execution scheduled" );
    }

    private static Stream<Command> streamGetter()
    {
        Command[]   commands    =
        {
            new ColorCommand( Color.RED ),
            new ShapeCommand( "Square" ),
            new PointCommand( 10, 20 ),
            new ColorCommand( Color.GREEN ),
            new ShapeCommand( "Circle" ),
            new PointCommand( 15, 25 ),
        };
        Stream<Command> stream  = Arrays.stream( commands );
        return stream;
    }
}
