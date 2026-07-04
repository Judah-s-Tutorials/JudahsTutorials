package com.acmemail.judah.cartesian_plane.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import javax.swing.JOptionPane;

import com.acmemail.judah.cartesian_plane.input.Command;
import com.acmemail.judah.cartesian_plane.input.CommandProcessor;
import com.acmemail.judah.cartesian_plane.input.CommandReader;
import com.acmemail.judah.cartesian_plane.input.ParsedCommand;
import com.acmemail.judah.cartesian_plane.input.Result;

/**
 * This is a simple application that runs in a loop,
 * reading command from the console
 * and executing them via a {@link CommandProcessor} object;
 * the loop terminates when it encounters the EXIT command.
 * The <em>read</em> logic is extremely simple,
 * reading a line of text
 * and not attempting to detect blank lines or comments.
 * 
 * @see SimpleConsoleApp2_NoPlotsFiles
 */
public class SimpleConsoleApp1_NoPlotsFiles
{
    /**
     * Default constructor; not used.
     */
    public SimpleConsoleApp1_NoPlotsFiles()
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
        CommandProcessor    cmdProc     = new CommandProcessor();
        try ( 
            Reader inReader = new InputStreamReader( System.in );
            BufferedReader bReader = new BufferedReader( inReader );
        )
        {
            ParsedCommand   parsedCommand   = getParsedCommand( bReader );
            Command         command         = parsedCommand.getCommand();
            while ( command != Command.EXIT )
            {
                switch ( command )
                {
                case YPLOT, XYPLOT, RPLOT, TPLOT, SAVE, OPEN ->
                    showMessage( parsedCommand, "not implemented" );
                default -> {
                    Result  result  = cmdProc.processCommand( parsedCommand );
                    if ( !result.success() )
                        showMessage( result );
                    }
                }

                parsedCommand = getParsedCommand( bReader );
                command = parsedCommand.getCommand();
            }
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
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
    private static ParsedCommand getParsedCommand( BufferedReader bReader )
        throws IOException
    {
        System.out.print( "Enter a command> " );
        String          line    = bReader.readLine();
        ParsedCommand   command = CommandReader.parseCommand( line );
        return command;
    }
    
    /**
     * Posts a dialog containing a given command
     * and associated message, for example;<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;{@code SEET: unrecognized command}<br>
     * 
     * @param command   the given command
     * @param message   the associated message
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
     * Posts a dialog containing a the first message
     * encapsulated in a given Result object.
     * 
     * @param result    the given Result object
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
