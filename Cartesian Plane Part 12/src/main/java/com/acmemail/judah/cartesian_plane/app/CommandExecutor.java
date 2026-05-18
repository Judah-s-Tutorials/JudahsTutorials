package com.acmemail.judah.cartesian_plane.app;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.swing.JOptionPane;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.NotificationManager;
import com.acmemail.judah.cartesian_plane.PlotPointCommand;
import com.acmemail.judah.cartesian_plane.input.Command;
import com.acmemail.judah.cartesian_plane.input.CommandProcessor;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.EquationFileChooser;
import com.acmemail.judah.cartesian_plane.input.Exp4jEquation;
import com.acmemail.judah.cartesian_plane.input.FileManager;
import com.acmemail.judah.cartesian_plane.input.ParsedCommand;
import com.acmemail.judah.cartesian_plane.input.Result;

/**
 * Application to read operator input
 * from the console
 * and produce a plot.
 * 
 * @author Jack Straub
 */
public class CommandExecutor
{
    private static final String errorDialogTitle    = "Parsing Error";
    private static final String newl                = System.lineSeparator();
    private static final int    newlLen             = newl.length();
    private static final String nullPlaneError      = 
        "CartesianPlane may not be null";
    
    private final CartesianPlane    plane;
    private CommandProcessor             inputParser;
    
    /**
     * Constructor.
     * Establishes connection to CartesianPlane
     * for displaying output.
     * 
     * @param plane CartesianPlane for displaying output;
     *              may not  be null;
     */
    public CommandExecutor( CartesianPlane plane )
    {
        if ( plane == null )
            throw new IllegalArgumentException( nullPlaneError );
        this.plane = plane;
    }
    
    /**
     * Get a command
     * from a given supplier,
     * and execute it via a given Equation.
     * The given equation may be null,
     * in which case a default will be applied.
     * Stop on EXIT command.
     * 
     * @param reader    the given supplier
     * @param equation  the given equation; may be null
     */
    public void exec( Supplier<ParsedCommand> reader, Equation equation )
    {
        inputParser = new CommandProcessor( equation );
        ParsedCommand       parsedCommand   = null;
        Command             command         = Command.NONE;
        do
        {
            parsedCommand = reader.get();
            command = parsedCommand.getCommand();
            Result  result  = inputParser.processCommand( parsedCommand );
            if ( command == Command.INVALID )
                showUsage();
            else if ( !result.isSuccess() )
                showError( result );
            else if ( command == Command.YPLOT )
                plot( () -> inputParser.getEquation().yPlot() );
            else if ( command == Command.XYPLOT )
                plot( () -> inputParser.getEquation().xyPlot() );
            else if ( command == Command.RPLOT )
                plot( () -> inputParser.getEquation().rPlot() );
            else if ( command == Command.TPLOT )
                plot( () -> inputParser.getEquation().tPlot() );
            else if ( command == Command.OPEN )
                open( parsedCommand.getArgString() );
            else if ( command == Command.SAVE )
                save( parsedCommand.getArgString() );
            else
                ;
        } while ( command != Command.EXIT );
    }
    
    /**
     * Generate a plot from a given type of equation.
     * The type of equation is encoded
     * in the given stream supplier:
     * <ul>
     * <li>y = f(x): equation.yPlot()</li>
     * <li>(x,y) = f(x): equation.xyPlot()</li>
     * <li>r = f(t): equation.rPlot()</li>
     * <li>t = f(r): equation.tPlot()</li>
     * </ul>
     * 
     * @param pointStreamSuppier    the given stream supplier
     */
    private void plot( Supplier<Stream<Point2D>> pointStreamSuppier )
    {
        plane.setStreamSupplier( () ->
            pointStreamSuppier.get()
            .map( p -> PlotPointCommand.of( p, plane) )
        );
        NotificationManager.INSTANCE
            .propagateNotification( CPConstants.REDRAW_NP );
    }
    
    /**
     * Create a new equation from the input file
     * with the given name.
     * If the given name is empty,
     * the operator will be prompted
     * for the file to read.
     * 
     * @param name  the given file name;
     *              may be empty, may not be null
     *              
     * Postcondition: 
     *     If an equation is successfully derived
     *     from the input file,
     *     a new InputParser will be instantiated
     *     using the derived equation.
     */
    private void open( String name )
    {
        Equation    equation    = null;
        if ( name.isBlank() )
        {
            EquationFileChooser chooser  = new EquationFileChooser();
            equation = chooser.openDialog().orElse( null );
        }
        else
        {
            equation = new Exp4jEquation();
            File    file    = new File( name );
            Result  result  = null;
            try
            {
                result  = FileManager.load( file, equation );
                equation = null;
                showError( result );
            }
            catch ( IOException exc )
            {
                equation = null;
                result = new Result( false, List.of( exc.getMessage() ) );
            }
        }
        if ( equation != null )
            inputParser = new CommandProcessor( equation );
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
    private void save( String name )
    {
        Equation    equation    = inputParser.getEquation();
        if ( name.isEmpty() )
        {
            EquationFileChooser chooser = new EquationFileChooser();
            boolean             status  = chooser.saveDialog( equation );
            if ( !status )
            {
                String  message = "File not saved: " + name;
                Result  result  = new Result( false, List.of( message ) );
                showError( result );
            }
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
                Result  result  = new 
                    Result( false, List.of( exc.getMessage() ) );
                showError( result );
            }
        }
    }
    
    /**
     * Displays a message dialog
     * showing the <em>usage</em> string
     * from the Command class.
     */
    private void showUsage()
    {
        JOptionPane.showMessageDialog( 
            null, 
            Command.usage(), 
            errorDialogTitle, 
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    /**
     * Print the messages associated
     * with a given Result object
     * to stderr.
     * 
     * @param result    the given Result object
     */
    private void showError( Result result )
    {
        List<String>    list    = result.getMessages();
        String          message = null;
        
        if ( list.isEmpty() )
            System.err.println( "input error" );
        else
        {
            StringBuilder   bldr    = new StringBuilder();
            list.forEach( s -> bldr.append( s ).append( newl ) );
            int             msgLen  = bldr.length();
            bldr.delete( msgLen - newlLen, newlLen );
            message = bldr.toString();
        }
        
        JOptionPane.showMessageDialog( 
            null, 
            message, 
            errorDialogTitle, 
            JOptionPane.ERROR_MESSAGE
        );
    }

}
