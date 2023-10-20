package com.acmemail.judah.cartesian_plane.app;

import javax.swing.JOptionPane;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;
import com.acmemail.judah.cartesian_plane.input.Command;
import com.acmemail.judah.cartesian_plane.input.CommandReader;
import com.acmemail.judah.cartesian_plane.input.ParsedCommand;

/**
 * Application to read operator input
 * from the console
 * and produce a plot.
 * 
 * @author Jack Straub
 */
public class DialogInputApp
{
    private static final String prompt      = "Enter a command> ";
    private static final String dialogTitle = "Command Input";
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        CartesianPlane  plane   = new CartesianPlane();
        Root            root    = new Root( plane );
        root.start();
        
        CommandExecutor executor    = new CommandExecutor( plane );
        executor.exec( DialogInputApp::getInput, null );
        
        System.exit( 0 );
    }
    
    /**
     * Prompt the operator for a command.
     * Loop on empty input;
     * generate EXIT cancel.
     * 
     * @return  a ParsedCommand reflecting the operator's input
     */
    private static ParsedCommand getInput()
    {
        ParsedCommand   command = null;
        while ( command == null )
        {
            String  input   =
                JOptionPane.showInputDialog(
                    null, 
                    prompt, 
                    dialogTitle, 
                    JOptionPane.QUESTION_MESSAGE
                );
            if ( input == null )    // exit if operator cancels
                command = new ParsedCommand( Command.EXIT, "", "" );
            else if ( input.isEmpty() )  // keep looping on empty input
                ;
            else
                command = CommandReader.parseCommand( input );
        }
        return command;
    }
}
