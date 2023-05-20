package com.acmemail.judah.cartesian_plane.jep_sandbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.NotificationManager;
import com.acmemail.judah.cartesian_plane.PlotPointCommand;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;
import com.acmemail.judah.cartesian_plane.input.Command;
import com.acmemail.judah.cartesian_plane.input.CommandReader;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.FileManager;
import com.acmemail.judah.cartesian_plane.input.InputParser;
import com.acmemail.judah.cartesian_plane.input.ParsedCommand;
import com.acmemail.judah.cartesian_plane.input.Result;

public class OpenEquationDemo1
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
        Equation    equation    = FileManager.open();
        Root    root    = new Root( plane );
        root.start();
        try (
            InputStreamReader inReader  = new InputStreamReader( System.in );
            BufferedReader bufReader = new BufferedReader( inReader );
        )
        {
            CommandReader reader  = new CommandReader( bufReader );
            exec( reader, equation );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }
    private static void exec( CommandReader commandReader, Equation equation )
        throws IOException
    {
        InputParser         inputParser     = new InputParser( equation );
        ParsedCommand       parsedCommand   = null;
        Command             command         = Command.NONE;
        do
        {
            parsedCommand = commandReader.nextCommand( prompt );
            command = parsedCommand.getCommand();
            Result  result  = inputParser.parseInput( parsedCommand );
            if ( command == Command.INVALID )
                System.err.println( Command.usage() );
            else if ( !result.isSuccess() )
                printError( result );
            else if ( command == Command.YPLOT )
                plotY( inputParser );
            else if ( command == Command.XYPLOT )
                plotXY( inputParser );
            else
                ;
        } while ( command != Command.EXIT );
    }
    
    /**
     * Print the messages associated
     * with a given Result object
     * to stderr.
     * 
     * @param result    the given Result object
     */
    private static void printError( Result result )
    {
        List<String>    list    = result.getMessages();
        if ( list.isEmpty() )
            System.err.println( "input error" );
        else
            list.forEach( s -> System.out.println( "Error: " + s ) );
    }
    
    /**
     * Generate a plot of the form y = f(x).
     * 
     * @param parser    the object that encapsulates the equation to plot
     */
    private static void plotY( InputParser parser )
    {
        plane.setStreamSupplier( () ->
            parser.getEquation().yPlot()
            .map( p -> PlotPointCommand.of( p, plane) )
        );
        NotificationManager.INSTANCE
            .propagateNotification( CPConstants.REDRAW_NP );
    }
    
    /**
     * Generate a plot of parametric equation.
     * 
     * @param parser    the object that encapsulates the equation to plot
     */
    private static void plotXY( InputParser parser )
    {
        plane.setStreamSupplier( () ->
            parser.getEquation().xyPlot()
            .map( p -> PlotPointCommand.of( p, plane) )
        );
        NotificationManager.INSTANCE
            .propagateNotification( CPConstants.REDRAW_NP );
    }
}

