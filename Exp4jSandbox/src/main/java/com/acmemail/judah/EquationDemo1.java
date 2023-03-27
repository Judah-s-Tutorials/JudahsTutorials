package com.acmemail.judah;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import net.objecthunter.exp4j.ValidationResult;

public class EquationDemo1
{
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
            EquationDemo1   demo    = new EquationDemo1( bufReader );
            demo.execute();
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
        }
    }

    public EquationDemo1( BufferedReader reader )
    {
        this.reader = reader;
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
        try
        {
            parser.getEquation().streamY()
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
                System.err.println( cmdString + ": unrecognized command" );
        }
        
        return cmd;
    }
}
