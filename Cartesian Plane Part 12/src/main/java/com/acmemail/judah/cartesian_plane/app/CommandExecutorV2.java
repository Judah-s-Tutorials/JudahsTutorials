package com.acmemail.judah.cartesian_plane.app;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import javax.swing.JOptionPane;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.DefaultPlotter;
import com.acmemail.judah.cartesian_plane.input.Command;
import com.acmemail.judah.cartesian_plane.input.CommandProcessor;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.EquationFileChooser;
import com.acmemail.judah.cartesian_plane.input.Exp4jEquation;
import com.acmemail.judah.cartesian_plane.input.FileManager;
import com.acmemail.judah.cartesian_plane.input.IEquationFileChooser;
import com.acmemail.judah.cartesian_plane.input.MessageConsumer;
import com.acmemail.judah.cartesian_plane.input.ParsedCommand;
import com.acmemail.judah.cartesian_plane.input.Plotter;
import com.acmemail.judah.cartesian_plane.input.Result;

/**
 * This class serves as the basis for executing
 * miscellaneous applications that need to process
 * multiple commands in a loop.
 * The client calls the constructor to establish a connection
 * to a CartesianPlane,
 * then calls the exec method passing a ParsedCommand supplier.
 * The supplier is invoked repeatedly
 * for a new command to execute
 * until it passes EXIT or null.
 * <p>
 * The basis for this class is {@link CommandExecutorV1b}.
 * We have added logic to execute OPEN and SAVE commands.
 * For the purpose of testing,
 * the client may configure the IEquationFileChooser facility,
 * to redirect calls to the file chooser open and save methods.
 * <p>
 * An application based on this class
 * will not be robust;
 * error handling, in particular, is minimal.
 * 
 * @author Jack Straub
 * 
 * @see CommandExecutorV1a
 * @see CommandExecutorV1b
 */
public class CommandExecutorV2
{
    private static final String messageDialogTitle  = "Command Message";
    private static final String newl                = System.lineSeparator();
    private static final int    newlLen             = newl.length();
    
    /**
     * The interface to use to configure the Plotter facility
     * in the {@link CommandProcessor} class.
     */
    private final Plotter       plotter;
    
    /**
     *  The facility used to redirect open and save operations 
     *  against the EquationFileChooser;
     *  subject to lazy initialization,
     *  must be obtained via {@link #getEquationFileChooser()}.
     *  The default is the EquationFileChooser itself;
     *  the client can override this via the
     *  {@link #setEquationFileChooser(IEquationFileChooser)} method.
     *  @see #getEquationFileChooser()
     */
    private IEquationFileChooser fileChooser = null;
    
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
    public CommandExecutorV2( CartesianPlane plane )
    {
        Objects.requireNonNull( plane, "plane" );
        this.plotter = new DefaultPlotter( plane );
    }
    
    /**
     * Constructor.
     * Establishes connection to a Plotter
     * for displaying output from plot commands (YPLOT, XYPLOT, etc.).
     * 
     * @param plotter
     *      Plotter for displaying output from plot commands;
     *      may not be null
     *              
     * @throws NullPointerException if plotter is null
     */
    public CommandExecutorV2( Plotter plotter )
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
                case OPEN -> {
                    Equation newEq = open( parsedCommand.getArgString() );
                    if ( newEq != null )
                        cmdProc = new CommandProcessor( newEq, plotter );
                }
                case SAVE -> 
                    save( parsedCommand.getArgString(), cmdProc.getEquation() );
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
     * Sets the file chooser to use when executing <em>OPEN name</em>
     * and <em>SAVE name</em> commands.
     * 
     * @param fChooser  the file chooser to set; may be null
     */
    public void setEquationFileChooser( IEquationFileChooser fChooser )
    {
        fileChooser = fChooser;
    }
    
    /**
     * Gets the file chooser currently in use
     * for executing <em>OPEN name</em>
     * and <em>SAVE name</em> commands.
     * 
     * @return  the currently configure file chooser
     */
    public IEquationFileChooser getEquationFileChooser()
    {
        if ( fileChooser == null )
        {
            fileChooser = new EquationFileChooser();
            fileChooser.setMessageConsumer( messageConsumer );
        }
        return fileChooser;
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
        if ( fileChooser != null )
            fileChooser.setMessageConsumer( messageConsumer );
    }
    
    /**
     * Create a new equation from the input file
     * with the given name.
     * If the given name is empty,
     * the operator will be prompted
     * for the file to read.
     * If the process completes successfully
     * a new Exp4jEquation encapsulating
     * the file data is returned,
     * otherwise null will be returned.
     * 
     * @param name  the given file name;
     *              may be empty, may not be null
     * @return
     *      a new equation encapsulating the file data,
     *      or null on unsuccessful completion
     */
    private Equation open( String name )
    {
        Objects.requireNonNull( name, "name" );
        Equation    equation    = null;
        if ( name.isBlank() )
        {
            IEquationFileChooser    fChooser    = getEquationFileChooser();
            equation = fChooser.openDialog().orElse( null );
            if ( equation == null )
                showMessage( "Equation file not opened" );
        }
        else
        {
            equation = new Exp4jEquation();
            File        file        = new File( name );
            try
            {
                Result  result  = FileManager.load( file, equation );
                if ( !result.success() )
                {
                    showMessage( result );
                    equation = null;
                }
            }
            catch ( IOException exc )
            {
                showMessage( exc.getMessage() );
                equation = null;
            }
        }
        
        return equation;
    }
    
    /**
     * Saves the current equation to the output file
     * with the given name.
     * If the given name is empty,
     * the operator will be prompted
     * for the file to write.
     * 
     * @param name  the given file name;
     *              may be empty, may not be null
     */
    private void save( String name, Equation equation )
    {
        Objects.requireNonNull( name, "name" );
        if ( name.isBlank() )
        {
            IEquationFileChooser    fChooser    = getEquationFileChooser();
            if ( !fChooser.saveDialog( equation ) )
                showMessage( "Equation not saved" );
        }
        else
        {
            File    file    = new File( name );
            try
            {
                FileManager.save( file, equation );
            }
            catch ( IOException exc )
            {
                showMessage( exc.getMessage() );
            }
        }
    }
    
    /**
     * Posts a message to the MessageConsumer.
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
            message = "Unexpected error; failed Result with no message.";
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
