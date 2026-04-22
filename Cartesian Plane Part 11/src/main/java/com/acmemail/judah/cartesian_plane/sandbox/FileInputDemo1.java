package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.NotificationManager;
import com.acmemail.judah.cartesian_plane.app.FIUtils;
import com.acmemail.judah.cartesian_plane.app.FIUtils.ToPlotPointCommand;
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
    private static CartesianPlane plane;
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        plane = new CartesianPlane();
        Root    root        = new Root( plane );
        root.start();
        String  filePath    = "files/InputDemo1.txt";
        File    file        = new File( filePath );
        if ( !file.exists() )
        {
            String  errorMessage    = 
                "Demo file not found: \"" + filePath + "\"";
            System.err.println( errorMessage );
            System.exit( 1 );
        }
        
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
        
        ToPlotPointCommand  toPlotPointCommand =
            FIUtils.toPlotPointCommand( plane );

        plane.setStreamSupplier( () ->
            inputParser.getEquation().xyPlot()
            .map( toPlotPointCommand::of )
        );
        NotificationManager.INSTANCE
            .propagateNotification( CPConstants.REDRAW_NP );
    }
}
