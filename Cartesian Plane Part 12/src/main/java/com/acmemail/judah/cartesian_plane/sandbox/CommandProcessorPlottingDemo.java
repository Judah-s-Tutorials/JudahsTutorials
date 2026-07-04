package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import javax.swing.JOptionPane;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.DefaultPlotter;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;
import com.acmemail.judah.cartesian_plane.input.Command;
import com.acmemail.judah.cartesian_plane.input.CommandProcessor;
import com.acmemail.judah.cartesian_plane.input.CommandReader;
import com.acmemail.judah.cartesian_plane.input.ParsedCommand;
import com.acmemail.judah.cartesian_plane.input.Result;

/**
 * This is a sample application
 * that takes advantage of the plotting facility
 * in the CommandProcessor class.
 * 
 * @see CommandProcessor
 */
public class CommandProcessorPlottingDemo
{
    private static final String         PROMPT  = "Enter a command: ";
    private static final CartesianPlane plane   = new CartesianPlane();
    private static final Root           root    = new Root( plane );
    
    /**
     * Default constructor; not used.
     */
    public CommandProcessorPlottingDemo()
    {
        // not used
    }

    /**
     * Application entry point.
     * 
     * @param args  command-line arguments; not used
     */
    public static void main(String[] args)
    {
        root.start();
        DefaultPlotter      plotter     = new DefaultPlotter( plane );
        CommandProcessor    cmdProc     = 
            new CommandProcessor( null, plotter );
        try ( Reader inReader = new InputStreamReader( System.in );
            BufferedReader bReader = new BufferedReader( inReader )
        )
        {
            CommandReader   reader          = new CommandReader( bReader );
            ParsedCommand   parsedCommand   = reader.nextCommand( PROMPT );
            Command         command         = parsedCommand.getCommand();
            while ( command != Command.EXIT )
            {
                switch ( command )
                {
                case SAVE:
                case OPEN:
                    showMessage( parsedCommand, "not implemented" );
                default:
                    Result  result  = cmdProc.processCommand( parsedCommand );
                    if ( !result.success() )
                        showMessage( result );
                }

                parsedCommand = reader.nextCommand( PROMPT );
                command = parsedCommand.getCommand();
            }
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        System.exit( 0 );
    }
    
    /**
     * Read a line of text from a given BufferedReader
     * and convert it to a ParsedCommand.
     * No attempt is made to interpret the input;
     * the disposition of blank lines, comments, and malformed commands
     * will be determined by the parseCommand method 
     * of the CommandReader class
     * 
     * @param bReader   the given BufferedReader
     * 
     * @return  the derived ParsedCommand
     * 
     * @throws IOException  if an I/O error occurs
     */
    private static void showMessage( ParsedCommand command, String message )
    {
        String  remark  = command.getCommandString() + ": " + message;
        JOptionPane.showMessageDialog(
            null,
            remark,
            "Sample Console App 1",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Posts a dialog containing a given command
     * and associated message, for example;<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;{@code SEET: unrecognized command}<br>
     * 
     * @param command   the given command
     * @param message   the associated message
     */
    private static void showMessage( Result result )
    {
        List<String>    messages    = result.messages();
        String          message0    = messages.isEmpty() ?
            "No diagnostic" : messages.get( 0 );
        JOptionPane.showMessageDialog(
            null,
            message0,
            "Sample Console App 1",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
