package com.acmemail.judah.cartesian_plane.app;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.swing.JOptionPane;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.NotificationManager;
import com.acmemail.judah.cartesian_plane.PlotPointCommand;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;
import com.acmemail.judah.cartesian_plane.input.Command;
import com.acmemail.judah.cartesian_plane.input.CommandProcessor;
import com.acmemail.judah.cartesian_plane.input.CommandReader;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.ParsedCommand;
import com.acmemail.judah.cartesian_plane.input.Result;

/**
 * This is a refinement of {@link SimpleConsoleApp2_NoPlotsFiles},
 * in which we add logic to support plotting.
 * 
 * @see #plot(Supplier)
 */
public class SimpleConsoleApp3_NoFiles2
{
    private static final String         PROMPT  = "Enter a command: ";
    private static final CartesianPlane plane   = new CartesianPlane();
    private static final Root           root    = new Root( plane );
    
    /**
     * Default constructor; not used.
     */
    public SimpleConsoleApp3_NoFiles2()
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
        CommandProcessor    cmdProc     = new CommandProcessor();
        Equation            equation    = cmdProc.getEquation();
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
                case YPLOT:
                    plot( () -> equation.yPlot() );
                    break;
                case XYPLOT:
                    plot( () -> equation.xyPlot() );
                    break;
                case RPLOT:
                    plot( () -> equation.rPlot() );
                    break;
                case TPLOT:
                    plot( () -> equation.tPlot() );
                    break;
                case SAVE: 
                case OPEN:
                    showMessage( parsedCommand, "not implemented" );
                    break;
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
     * Acquires a stream of (x,y) coordinates,
     * and supplementing the stream 
     * by mapping each point to a PlotPointCommand.
     * The modified stream is registered
     * with the encapsulated CartesianPlane object
     * as a Stream<PlotCommand> supplier.
     * After registration it issued a redraw notification,
     * telling the CartesianPlane to update itself.
     * 
     * @param supplier  the supplier of the stream of (x,y) coordinates
     */
    private static void plot( Supplier<Stream<Point2D>> supplier )
    {
        Objects.requireNonNull( supplier, "supplier" );
        plane.setStreamSupplier( () ->
            supplier.get().map( p -> PlotPointCommand.of( p, plane ) )
        );
        NotificationManager.INSTANCE
            .propagateNotification( CPConstants.REDRAW_NP );
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
