package com.acmemail.judah.sandbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import com.acmemail.judah.Command;
import com.acmemail.judah.Equation;
import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.NotificationManager;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.app.FIUtils;
import com.acmemail.judah.cartesian_plane.app.FIUtils.ToPlotPointCommand;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

import net.objecthunter.exp4j.ValidationResult;

public class EquationDemo2MapToPlot
{
    private static final CartesianPlane plane   = new CartesianPlane();
    
    private final Equation          equation    = new Equation();
    private final String            prompt      = "Enter an equation for y: ";
    private final BufferedReader    reader;
    private String                  argString   = "";
    
    public static void main(String[] args)
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

        try ( 
            Reader inReader = new InputStreamReader( System.in );
            BufferedReader bufReader = new BufferedReader( inReader );
        )
        {
            EquationDemo2MapToPlot   demo    = new EquationDemo2MapToPlot( bufReader );
            demo.execute();
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
        }
    }

    public EquationDemo2MapToPlot( BufferedReader reader )
    {
        this.reader = reader;
    }
    
    public void execute() throws IOException
    {
        equation.setRange( -2, 2, .05 );
        while ( getCommand() != Command.EXIT )
        {
            ValidationResult    result  =
                equation.setYExpression( argString );
            displayResult( result );
            if ( result.isValid() )
                evaluate();
        }
    }
    
    private void evaluate()
    {
        ToPlotPointCommand      toPlotPoint = 
            FIUtils.toPlotPointCommand( plane );

        plane.setStreamSupplier(
            () -> equation.streamY()
            .map( toPlotPoint::of )
        );
        NotificationManager.INSTANCE.
            propagateNotification( CPConstants.REDRAW_NP );
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
        String  line    = "";
        while ( cmd == Command.NONE )
        {
            System.out.print( prompt );
            line = reader.readLine().trim();
            if ( line.isEmpty() )
                ;
            else if ( line.equalsIgnoreCase( "exit" ) )
                cmd = Command.EXIT;
            else
            {
                argString = line;
                cmd = Command.Y_STREAM;
            }
        }
        return cmd;
    }
}
