package com.acmemail.judah.cartesian_plane.sandbox;

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
import com.acmemail.judah.cartesian_plane.input.CommandProcessor;
import com.acmemail.judah.cartesian_plane.input.CommandReader;
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
    private static final String     prompt  = "Enter a command> ";
    private static CartesianPlane   plane;
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        plane = new CartesianPlane();
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
        CommandProcessor    processor       = new CommandProcessor();
        ParsedCommand       parsedCommand   = null;
        Command             command         = Command.NONE;
        do
        {
            parsedCommand = commandReader.nextCommand( prompt );
            command = parsedCommand.getCommand();
            Result  result  = processor.processCommand( parsedCommand  );
            if ( command == Command.INVALID )
                System.err.println( Command.usage() );
            else if ( !result.success() )
                printError( result );
            else if ( command == Command.YPLOT )
                plotY( processor );
            else if ( command == Command.XYPLOT )
                plotXY( processor );
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
        List<String>    list    = result.messages();
        if ( list.isEmpty() )
            System.err.println( "input error" );
        else
            list.forEach( s -> System.out.println( "Error: " + s ) );
    }
    
    /**
     * Generate a plot of the form y = f(x).
     * 
     * @param processor the object that encapsulates the equation to plot
     */
    private static void plotY( CommandProcessor processor )
    {
        plane.setStreamSupplier( () ->
            processor.getEquation().yPlot()
            .map( p -> PlotPointCommand.of( p, plane) )
        );
        NotificationManager.INSTANCE
            .propagateNotification( CPConstants.REDRAW_NP );
    }
    
    /**
     * Generate a plot of parametric equation.
     * 
     * @param processor the object that encapsulates the equation to plot
     */
    private static void plotXY( CommandProcessor processor )
    {
        plane.setStreamSupplier( () ->
            processor.getEquation().xyPlot()
            .map( p -> PlotPointCommand.of( p, plane) )
        );
        NotificationManager.INSTANCE
            .propagateNotification( CPConstants.REDRAW_NP );
    }
}
