package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

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
import com.acmemail.judah.cartesian_plane.input.Result;

/**
 * Demonstration of reading operator input
 * from the console.
 * 
 * @author Jack Straub
 */
public class ConsoleInputDemo1
{
    private static final CartesianPlane plane   = new CartesianPlane();
    private static final String         prompt  = "Enter a command> ";
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        Root    root    = new Root( plane );
        root.start();
        try (
            InputStreamReader inReader  = new InputStreamReader( System.in );
            BufferedReader bufReader = new BufferedReader( inReader );
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
    
    /**
     * Get and execute a command from the console.
     * Stop on EXIT command.
     * 
     * @param commandReader console
     * 
     * @throws IOException  if an I/O error occurs
     */
    private static void exec( CommandReader commandReader )
        throws IOException
    {
        InputParser         inputParser     = new InputParser();
        ParsedCommand       parsedCommand   = null;
        Command             command         = Command.NONE;
        do
        {
            parsedCommand = commandReader.nextCommand( prompt );
            command = parsedCommand.getCommand();
            Result  result  = inputParser.parseInput( parsedCommand );
            if ( !result.isSuccess() )
                printError( result );
            else if ( command == Command.YPLOT )
                plotY( inputParser );
            else if ( command == Command.XYPLOT )
                plotXY( inputParser );
            else
                ;
        } while ( command != Command.EXIT );
    }
    
    private static void printError( Result result )
    {
        List<String>    list    = result.getMessages();
        if ( list.isEmpty() )
            System.err.println( "input error" );
        else
            list.forEach( s -> System.out.println( "Error: " + s ) );
    }
    
    private static void plotY( InputParser parser )
    {
        ToPlotPointCommand  toPlotPointCommand =
            FIUtils.toPlotPointCommand( plane );

        plane.setStreamSupplier( () ->
            parser.getEquation().yPlot()
            .map( toPlotPointCommand::of )
        );
        NotificationManager.INSTANCE
            .propagateNotification( CPConstants.REDRAW_NP );
    }
    
    private static void plotXY( InputParser parser )
    {
        ToPlotPointCommand  toPlotPointCommand =
            FIUtils.toPlotPointCommand( plane );

        plane.setStreamSupplier( () ->
            parser.getEquation().xyPlot()
            .map( toPlotPointCommand::of )
        );
        NotificationManager.INSTANCE
            .propagateNotification( CPConstants.REDRAW_NP );
    }
}
