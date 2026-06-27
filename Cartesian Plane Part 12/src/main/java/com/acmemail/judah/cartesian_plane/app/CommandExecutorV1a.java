package com.acmemail.judah.cartesian_plane.app;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import javax.swing.JOptionPane;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.DefaultPlotter;
import com.acmemail.judah.cartesian_plane.input.Command;
import com.acmemail.judah.cartesian_plane.input.CommandProcessor;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.ParsedCommand;
import com.acmemail.judah.cartesian_plane.input.Plotter;
import com.acmemail.judah.cartesian_plane.input.Result;

/**
 * This class serves as the basis for executing
 * miscellaneous applications.
 * The client calls the constructor to establish a connection
 * to a CartesianPlane,
 * then calls the exec method passing a ParsedCommand supplier.
 * The supplier is invoked repeatedly
 * for a new command to execute
 * until it returns EXIT or null.
 * <p>
 * An application based on this class
 * will not be robust;
 * error handling, in particular, is minimal.
 * This version of the class
 * does not support OPEN or SAVE commands.
 * 
 * @author Jack Straub
 * 
 * @see CommandExecutorV1b
 * @see CommandExecutorV2
 */
public class CommandExecutorV1a
{
    private static final String messageDialogTitle  = "Command Message";
    private static final String openNotSupported    = 
        "Not supported in this version: OPEN";
    private static final String saveNotSupported    = 
        "Not supported in this version: SAVE";
    private static final String newl                = System.lineSeparator();
    private static final int    newlLen             = newl.length();
    
    /**
     * The interface to use to configure the Plotter facility
     * in the {@link CommandProcessor} class.
     * Set to {@code DefaultPlotter} in the constructor.
     */
    private final Plotter       plotter;
    
    /**
     * Constructor.
     * Establishes connection to CartesianPlane
     * for displaying output.
     * 
     * @param plane     CartesianPlane for displaying output;
     *                  may not be null
     *              
     * @throws NullPointerException if plane is null
     */
    public CommandExecutorV1a( CartesianPlane plane )
    {
        Objects.requireNonNull( plane, "plane" );
        this.plotter = new DefaultPlotter( plane );
    }
    
    /**
     * Repeatedly get a command
     * from a given supplier,
     * and execute it via a CommandProcessor.
     * Stop on EXIT command or null.
     * Returns the equation configured via the CommandProcessor.
     * 
     * @param reader    the given supplier
     * 
     * @return the equation configured via the CommandProcessor
     */
    public Equation exec( Supplier<ParsedCommand> reader )
    {
        Objects.requireNonNull( reader, "reader" );
        CommandProcessor cmdProc            = 
            new CommandProcessor( null, plotter );
        ParsedCommand       parsedCommand   = null;
        Command             command         = Command.NONE;
        do
        {
            parsedCommand = reader.get();
            if ( parsedCommand == null )
                parsedCommand = new ParsedCommand( 
                    Command.EXIT, 
                    Command.EXIT.toString(), 
                    ""
                );
            command = parsedCommand.getCommand();
            switch ( command )
            {
                case OPEN -> showMessage( openNotSupported );
                case SAVE -> showMessage( saveNotSupported );
                default -> {
                    Result  result  = cmdProc.processCommand( parsedCommand );
                    if ( !result.success() )
                        showMessage( result );
                }
            }
        } while ( command != Command.EXIT );
        Equation    equation    = cmdProc.getEquation();
        return equation;
    }
    
    /**
     * Displays the given message in a dialog.
     * 
     * @param message    the given message
     */
    private void showMessage( String message )
    {
        JOptionPane.showMessageDialog(
            null, 
            message, 
            messageDialogTitle, 
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Displays the messages associated
     * with a given Result object in a dialog.
     * 
     * @param result    the given Result object
     */
    private void showMessage( Result result )
    {
        List<String>    list    = result.messages();
        String          message = null;
        
        if ( list.isEmpty() )
            message = "Unrecognized error";
        else
        {
            StringBuilder   bldr    = new StringBuilder();
            list.forEach( s -> bldr.append( s ).append( newl ) );
            int             msgLen  = bldr.length();
            bldr.setLength( msgLen - newlLen );
            message = bldr.toString();
        }
        showMessage( message );
    }

}
