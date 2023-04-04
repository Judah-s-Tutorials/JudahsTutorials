package com.acmemail.judah.sandbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.stream.Stream;

import com.acmemail.judah.Command;
import com.acmemail.judah.CommandStringReader;
import com.acmemail.judah.InputParser;
import com.acmemail.judah.InvalidExpressionException;
import com.acmemail.judah.ParsedCommand;
import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.NotificationManager;
import com.acmemail.judah.cartesian_plane.PlotCommand;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.app.FIUtils;
import com.acmemail.judah.cartesian_plane.app.FIUtils.ToPlotPointCommand;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

import net.objecthunter.exp4j.ValidationResult;

public class EquationDemo3UserInput
{
    private final CartesianPlane    plane       = new CartesianPlane();
    private final InputParser       parser      = new InputParser();
    private final BufferedReader    reader;
    
    public static void main( String[] args )
    {
        try ( 
            Reader inReader = new InputStreamReader( System.in );
            BufferedReader bufReader = new BufferedReader( inReader );
        )
        {
            EquationDemo3UserInput   demo    = 
                new EquationDemo3UserInput( bufReader );
            demo.execute();
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
        }
    }

    public EquationDemo3UserInput( BufferedReader reader )
    {
        PropertyManager pmgr    = PropertyManager.INSTANCE;
        
        pmgr.setProperty( CPConstants.TIC_MAJOR_LEN_PN, 21 );
        pmgr.setProperty( CPConstants.TIC_MAJOR_WEIGHT_PN, 1 );
        pmgr.setProperty( CPConstants.TIC_MAJOR_MPU_PN, 1 );
        pmgr.setProperty( CPConstants.TIC_MINOR_LEN_PN, 11 );
        pmgr.setProperty( CPConstants.TIC_MINOR_WEIGHT_PN, 1 );
        pmgr.setProperty( CPConstants.TIC_MINOR_MPU_PN, 5 );
        pmgr.setProperty( CPConstants.GRID_UNIT_PN, 50 );
        Root    root    = new Root( plane );
        root.start();

        this.reader = reader;
    }
    
    public void execute() throws IOException
    {
        CommandStringReader cmdReader   = new CommandStringReader( reader );
        
        Command cmd = Command.NONE;
        while ( cmd != Command.EXIT )
        {
            System.out.println( "Enter a command: " );
            ParsedCommand   parsed  = cmdReader.nextCommand();
            cmd = parsed.getCommand();
            ValidationResult    result  =
                parser.parseInput( cmd, parsed.getArgString() );
            displayResult( result );
            if ( cmd == Command.Y_STREAM )
                evaluateY();
            else if ( cmd ==Command.XY_STREAM )
                evaluateXY();
            else
                ;
        }
    }
    
    private void evaluateY()
    {
        ToPlotPointCommand   toPlotPointCommand =
            FIUtils.toPlotPointCommand( plane );
        try
        {
            plane.setStreamSupplier( () -> 
                parser.getEquation()
                .streamY()
                .map( toPlotPointCommand::of )
            );
            NotificationManager.INSTANCE.
                propagateNotification( CPConstants.REDRAW_NP );
        }
        catch ( InvalidExpressionException exc )
        {
            exc.printStackTrace();
            displayResult( exc.getValidationResult() );
        }
    }
    
    private void evaluateXY()
    {
        ToPlotPointCommand   toPlotPointCommand =
            FIUtils.toPlotPointCommand( plane );
        try
        {
            plane.setStreamSupplier( () -> 
                parser.getEquation()
                .streamXY()
                .map( toPlotPointCommand::of )
            );
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
}
