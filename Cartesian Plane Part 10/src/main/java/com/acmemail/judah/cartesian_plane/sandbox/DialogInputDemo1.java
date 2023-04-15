package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.IOException;

import javax.swing.JOptionPane;

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
 * Demonstration of obtaining operator input
 * from a dialog.
 * 
 * @author Jack Straub
 */
public class DialogInputDemo1
{
    private static final CartesianPlane plane       = new CartesianPlane();
    private static final InputParser    inputParser = new InputParser();

    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        Root    root    = new Root( plane );
        root.start();
        exec();
        System.exit( 0 );
    }
    
    /**
     * Get and execute a command from a dialog
     * Stop on EXIT command.
     * 
     * @param commandReader console
     * 
     * @throws IOException  if an I/O error occurs
     */
    private static void exec()
    {
        Command command = Command.NONE;
        do
        {
            ParsedCommand   parsedCommand   = getCommand();         
            command = parsedCommand.getCommand();
            if ( command == Command.INVALID )
                Utils.showUsageDialog();
            else
                execCommand( parsedCommand );
        } while ( command != Command.EXIT );
    }
    
    private static void execCommand( ParsedCommand parsedCommand )
    {
        Result      result  = inputParser.parseInput( parsedCyplotommand );
        Command     command = parsedCommand.getCommand();
        if ( !result.isSuccess() )
            Utils.showResultPopup( result );
        else if ( command == Command.YPLOT )
            plotY();
        else if ( command == Command.XYPLOT )
            plotXY();
        else
            ;
    }
    
    private static ParsedCommand getCommand()
    {
        final String    prompt  = "Enter a command";
        ParsedCommand   command = null;
        while ( command == null )
        {
            String  line    = 
                JOptionPane.showInputDialog( prompt );
            if ( line == null )
                command = new ParsedCommand( Command.EXIT, "", "" );
            else if ( line.isEmpty() )
                ;
            else
                command = CommandReader.parseCommand( line.trim() );
        }
        return command;
    }
    
    private static void plotY()
    {
        ToPlotPointCommand  toPlotPointCommand =
            FIUtils.toPlotPointCommand( plane );

        plane.setStreamSupplier( () ->
            inputParser.getEquation().yPlot()
            .map( toPlotPointCommand::of )
        );
        NotificationManager.INSTANCE
            .propagateNotification( CPConstants.REDRAW_NP );
    }
    
    private static void plotXY()
    {
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
