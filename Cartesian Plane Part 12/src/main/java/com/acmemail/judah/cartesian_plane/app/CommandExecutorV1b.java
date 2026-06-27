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
import com.acmemail.judah.cartesian_plane.input.MessageConsumer;
import com.acmemail.judah.cartesian_plane.input.ParsedCommand;
import com.acmemail.judah.cartesian_plane.input.Plotter;
import com.acmemail.judah.cartesian_plane.input.Result;

/**
 * This class builds on {@link CommandExecutorV1a},
 * which is designed to process a stream of ParsedCommands.
 * In this class we add facilities 
 * to aid in testing:
 * <ul>
 * <li>
 *     A {@code Plotter} facility,
 *     which allows the output of plot commands
 *     such as YPLOT and XYPLOT
 *     to be to be directed to a buffer
 *     that can be examined by a unit test,
 *     and is not reliant on the {@code CartesianPlane}
 *     for execution.
 * </li>
 * <li>
 *     A {@code MessageConsumer} facility,
 *     which allows messages to be directed to a buffer
 *     that can be examined by a unit test,
 *     and is not reliant on message dialogs.
 * </li>
 * </ul>
 * <p>
 * For the purpose of testing,
 * the client may configure the {@code Plotter} 
 * and/or the {@code MessageConsumer} facilities.
 * <p>
 * An application based on this class
 * will not be robust;
 * error handling, in particular, is minimal.
 * This version of the class
 * does not support OPEN or SAVE commands.
 * 
 * @author Jack Straub
 * 
 * @see CommandExecutorV1a
 * @see CommandExecutorV2
 */
public class CommandExecutorV1b
{
    private static final String openNotSupported    = 
        "Not supported in this version: OPEN";
    private static final String saveNotSupported    = 
        "Not supported in this version: SAVE";
    private static final String messageDialogTitle  = "Command Message";
    private static final String newl                = System.lineSeparator();
    private static final int    newlLen             = newl.length();
    
    /**
     * The interface to use to configure the Plotter facility
     * in the {@link CommandProcessor} class.
     */
    private final Plotter       plotter;
    
    /** Default MessageConsumer implementation. */
    private static final MessageConsumer defaultMessageConsumer = 
        JOptionPane::showMessageDialog;
            
    /** The current message consumer. */
    private MessageConsumer messageConsumer = defaultMessageConsumer;
    
    /**
     * Constructor.
     * Establishes connection to CartesianPlane
     * for displaying output.
     * Plotting commands (YPLOT, XYPLOT, etc.)
     * will be executed against the DefaultPlotter.
     * 
     * @param plane CartesianPlane for displaying output;
     *              may not be null
     *              
     * @throws NullPointerException if plane is null
     */
    public CommandExecutorV1b( CartesianPlane plane )
    {
        Objects.requireNonNull( plane, "plane" );
        this.plotter = new DefaultPlotter( plane );
    }
    
    /**
     * Constructor.
     * Establishes connection to CartesianPlane a Plotter
     * for displaying output from plot commands (YPLOT, XYPLOT, etc.).
     * 
     * @param plotter
     *      Plotter for displaying output from plot commands;
     *      may not be null
     *              
     * @throws NullPointerException if plotter is null
     */
    public CommandExecutorV1b( Plotter plotter )
    {
        Objects.requireNonNull( plotter, "plotter" );
        this.plotter = plotter;
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
        CommandProcessor cmdProc         = 
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
     * Gets the current plotter.
     * 
     * @return  the current plotter
     */
    public Plotter getPlotter()
    {
        return plotter;
    }
    
    /**
     * Gets the current MessageConsumer.
     * 
     * @return  the current MessageConsumer
     * 
     * @see #setMessageConsumer(MessageConsumer)
     */
    public MessageConsumer getMessageConsumer()
    {
        return messageConsumer;
    }
    
    /**
     * Sets the current MessageConsumer.
     * The caller may pass null,
     * in which case a default is applied.
     * 
     * @param consumer  the consumer to set, or null to restore default
     * 
     * @see #getMessageConsumer()
     */
    public void setMessageConsumer( MessageConsumer consumer )
    {
        messageConsumer = consumer != null ? 
            consumer : defaultMessageConsumer;
    }
    
    /**
     * Posts the given message to the MessageConsumer.
     * 
     * @param message    the given message
     */
    private void showMessage( String message )
    {        
        messageConsumer.postMessage( 
            null, 
            message, 
            messageDialogTitle, 
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Posts the messages associated
     * with a given Result object
     * to the MessageConsumer.
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
