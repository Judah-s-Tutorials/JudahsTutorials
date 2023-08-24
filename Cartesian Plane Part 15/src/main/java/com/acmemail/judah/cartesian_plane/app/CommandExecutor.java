package com.acmemail.judah.cartesian_plane.app;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

import javax.swing.JOptionPane;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.NotificationManager;
import com.acmemail.judah.cartesian_plane.PlotPointCommand;
import com.acmemail.judah.cartesian_plane.input.Command;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.EquationMap;
import com.acmemail.judah.cartesian_plane.input.FileManager;
import com.acmemail.judah.cartesian_plane.input.InputParser;
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
    private InputParser             inputParser;
    
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
     * 
     * @throws IOException  if an I/O error occurs
     */
    public void exec( Supplier<ParsedCommand> reader, Equation equation )
    {
        inputParser = new InputParser( equation );
        ParsedCommand       parsedCommand   = null;
        Command             command         = Command.NONE;
        do
        {
            parsedCommand = reader.get();
            command = parsedCommand.getCommand();
            Result  result  = inputParser.parseInput( parsedCommand );
            if ( command == Command.INVALID )
                showUsage();
            else if ( !result.isSuccess() )
                showError( result );
            else if ( command == Command.YPLOT )
                plotY();
            else if ( command == Command.XYPLOT )
                plotXY();
            else if ( command == Command.RPLOT )
                plotR();
            else if ( command == Command.TPLOT )
                plotT();
            else if ( command == Command.OPEN )
                open( parsedCommand.getArgString() );
            else if ( command == Command.LOAD )
                load( parsedCommand.getArgString() );
            else if ( command == Command.SELECT )
                select( parsedCommand.getArgString() );
            else
                ;
        } while ( command != Command.EXIT );
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
    
    /**
     * Generate a plot of the form y = f(x).
     */
    private void plotY()
    {
        plane.setStreamSupplier( () ->
            inputParser.getEquation().yPlot()
            .map( p -> PlotPointCommand.of( p, plane) )
        );
        NotificationManager.INSTANCE
            .propagateNotification( CPConstants.REDRAW_NP );
    }
    
    /**
     * Generate a plot from a polar equation
     * r = f(t).
     */
    private void plotR()
    {
        plane.setStreamSupplier( () ->
            inputParser.getEquation().rPlot()
            .map( p -> PlotPointCommand.of( p, plane) )
        );
        NotificationManager.INSTANCE
            .propagateNotification( CPConstants.REDRAW_NP );
    }
    
    /**
     * Generate a plot from a polar equation
     * r = f(t).
     */
    private void plotT()
    {
        plane.setStreamSupplier( () ->
            inputParser.getEquation().tPlot()
            .map( p -> PlotPointCommand.of( p, plane) )
        );
        NotificationManager.INSTANCE
            .propagateNotification( CPConstants.REDRAW_NP );
    }
    
    /**
     * Generate a plot of parametric equation.
     */
    private void plotXY()
    {
        plane.setStreamSupplier( () ->
            inputParser.getEquation().xyPlot()
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
        Equation    equation    = name.isEmpty() ? 
            FileManager.open() : FileManager.open( name );
        if ( equation != null )
            inputParser = new InputParser( equation );
    }
    
    private void load( String fileName )
    {
        EquationMap.parseEquationFiles();
    }
    
    private void select( String name )
    {
        Equation    equation    = EquationMap.getEquation();
        if ( equation != null )
            inputParser = new InputParser( equation );
    }
}
