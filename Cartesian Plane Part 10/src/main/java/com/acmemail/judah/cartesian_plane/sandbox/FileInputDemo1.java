package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Stream;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.NotificationManager;
import com.acmemail.judah.cartesian_plane.app.FIUtils;
import com.acmemail.judah.cartesian_plane.app.FIUtils.ToPlotPointCommand;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;
import com.acmemail.judah.cartesian_plane.input.Command;
import com.acmemail.judah.cartesian_plane.input.CommandReader;
import com.acmemail.judah.cartesian_plane.input.InputParser;
import com.acmemail.judah.cartesian_plane.input.ParsedCommand;

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
        Stream
            .generate( () -> nextCommand( commandReader ) )
            .takeWhile( pc -> pc.getCommand() != Command.NONE )
            .forEach( inputParser::parseInput );
        
        ToPlotPointCommand  toPlotPointCommand =
            FIUtils.toPlotPointCommand( plane );

        plane.setStreamSupplier( () ->
            inputParser.getEquation().xyPlot()
            .map( toPlotPointCommand::of )
        );
        NotificationManager.INSTANCE
            .propagateNotification( CPConstants.REDRAW_NP );
    }
    
    private static ParsedCommand nextCommand( CommandReader reader )
    {
        ParsedCommand   pCommand    = null;
        try
        {
            pCommand = reader.nextCommand();
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        return pCommand;
    }
}
