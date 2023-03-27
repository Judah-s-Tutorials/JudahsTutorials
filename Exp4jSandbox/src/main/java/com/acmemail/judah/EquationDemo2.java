package com.acmemail.judah;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.stream.Stream;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.NotificationManager;
import com.acmemail.judah.cartesian_plane.PlotCommand;
import com.acmemail.judah.cartesian_plane.app.FIUtils;
import com.acmemail.judah.cartesian_plane.app.FIUtils.ToPlotPointCommand;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

import net.objecthunter.exp4j.ValidationResult;

public class EquationDemo2
{
    private final CartesianPlane    plane       = new CartesianPlane();
    private final Root              root        = new Root( plane );
    private final InputParser       parser      = new InputParser();
    private final String            prompt      = "Enter a command: ";
    private final BufferedReader    reader;
    private String                  cmdString   = "";
    private String                  argString   = "";
    
    public static void main(String[] args)
    {
        try ( 
            Reader inReader = new InputStreamReader( System.in );
            BufferedReader bufReader = new BufferedReader( inReader );
        )
        {
            EquationDemo2   demo    = new EquationDemo2( bufReader );
            demo.execute();
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
        }
    }

    public EquationDemo2( BufferedReader reader )
    {
        this.reader = reader;
        root.start();
    }
    
    public void execute() throws IOException
    {
        Command cmd = Command.NONE;
        while ( (cmd = getCommand()) != Command.EXIT )
        {
            ValidationResult    result  =
                parser.parseInput( cmd, argString );
            displayResult( result );
            if ( cmd == Command.STREAM )
                evaluate();
        }
    }
    
    private void evaluate()
    {
        ToPlotPointCommand   toPlotPointCommand =
            FIUtils.toPlotPointCommand( plane );
        try
        {
            Stream<PlotCommand> stream  =
                parser.getEquation().streamY()
                    .map( toPlotPointCommand::of );
            plane.setStreamSupplier( () -> stream );
            NotificationManager.INSTANCE.
                propagateNotification( CPConstants.REDRAW_NP );
        }
        catch ( InvalidExpressionException exc )
        {
            exc.printStackTrace();
            displayResult( exc.getValidationResult() );
        }
    }
    
    private void displayResult( ValidationResult result )
    {
        List<String>    list    = result.getErrors();
        if ( list != null )
            list.forEach( System.err::println );
        else
            System.err.println( "SUCCESS" );
    }
    
    private Command getCommand() throws IOException
    {
        Command cmd = Command.NONE;
        System.out.print( prompt );
        System.out.flush();
        String  line    = reader.readLine().strip();
        int     len     = line.length();
        int     split   = line.indexOf( ' ' );
        if ( len == 0 )
            ;
        else
        {
            cmdString = line;
            argString = "";
            if ( split > 0 )
            {
                cmdString = line.substring( 0, split );
                argString = line.substring( split ).strip();
            }
            cmd = Command.toCommand( cmdString );
            if ( cmd == Command.INVALID )
            {
                System.err.println( cmdString + ": unrecognized command" );
                System.err.println( Command.usage() );
                cmd = Command.NONE;
            }
        }
        
        return cmd;
    }
}
