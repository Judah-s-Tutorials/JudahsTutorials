package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.NotificationManager;
import com.acmemail.judah.cartesian_plane.PlotPointCommand;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;
import com.acmemail.judah.cartesian_plane.input.CommandReader;
import com.acmemail.judah.cartesian_plane.input.InputParser;

/**
 * Demonstration of reading equation from a file.
 * 
 * @author Jack Straub
 */
public class FileInputDemo1
{
    private static final CartesianPlane plane   = new CartesianPlane();
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        Root    root    = new Root( plane );
        root.start();
        File    file    = new File( "files/InputDemo1.txt" );
        try (
            FileReader fileReader  = new FileReader( file );
            BufferedReader bufReader = new BufferedReader( fileReader );
        )
        {
            CommandReader reader  = new CommandReader( bufReader );
            exec( reader );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }
    
    private static void exec( CommandReader commandReader )
    {
        InputParser         inputParser     = new InputParser();
        commandReader.stream()
            .forEach( inputParser::parseInput );

        plane.setStreamSupplier( () ->
            inputParser.getEquation().xyPlot()
            .map( p -> PlotPointCommand.of( p, plane ) )
        );
        NotificationManager.INSTANCE
            .propagateNotification( CPConstants.REDRAW_NP );
    }
}
