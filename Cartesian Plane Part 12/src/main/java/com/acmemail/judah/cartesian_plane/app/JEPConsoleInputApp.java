package com.acmemail.judah.cartesian_plane.app;

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
import com.acmemail.judah.cartesian_plane.input.InputParser;
import com.acmemail.judah.cartesian_plane.input.JEPEquation;
import com.acmemail.judah.cartesian_plane.input.ParsedCommand;
import com.acmemail.judah.cartesian_plane.input.Result;

/**
 * Application to read operator input
 * from the console
 * and produce a plot.
 * 
 * @author Jack Straub
 */
public class JEPConsoleInputApp
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
        
        System.exit( 0 );
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
        JEPEquation         equation        = new JEPEquation();
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
            else if ( command == Command.RPLOT )
                plotR( inputParser );
            else if ( command == Command.TPLOT )
                plotT( inputParser );
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
     * Generate a plot from a polar equation
     * r = f(t).
     * 
     * @param parser    the object that encapsulates the equation to plot
     */
    private static void plotR( InputParser parser )
    {
        plane.setStreamSupplier( () ->
            parser.getEquation().rPlot()
            .map( p -> PlotPointCommand.of( p, plane) )
        );
        NotificationManager.INSTANCE
            .propagateNotification( CPConstants.REDRAW_NP );
    }
    
    /**
     * Generate a plot from a polar equation
     * r = f(t).
     * 
     * @param parser    the object that encapsulates the equation to plot
     */
    private static void plotT( InputParser parser )
    {
        plane.setStreamSupplier( () ->
            parser.getEquation().tPlot()
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
