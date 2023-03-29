package com.acmemail.judah.sandbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import com.acmemail.judah.Command;
import com.acmemail.judah.Equation;
import com.acmemail.judah.InvalidExpressionException;

import net.objecthunter.exp4j.ValidationResult;

public class EquationDemo1PrintStream
{
    private final Equation          equation    = new Equation();
    private final String            prompt      = "Enter an equation for y: ";
    private final BufferedReader    reader;
    private String                  argString   = "";
    
    public static void main(String[] args)
    {
        try ( 
            Reader inReader = new InputStreamReader( System.in );
            BufferedReader bufReader = new BufferedReader( inReader );
        )
        {
            EquationDemo1PrintStream   demo    = new EquationDemo1PrintStream( bufReader );
            demo.execute();
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
        }
    }

    public EquationDemo1PrintStream( BufferedReader reader )
    {
        this.reader = reader;
    }
    
    public void execute() throws IOException
    {
        equation.setRange( -2, 2, .5 );
        Command cmd = Command.NONE;
        while ( (cmd = getCommand()) != Command.EXIT )
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
        try
        {
            equation.streamY()
                .forEach( System.out::println );
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
