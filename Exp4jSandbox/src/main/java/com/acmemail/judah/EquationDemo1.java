package com.acmemail.judah;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.function.DoubleConsumer;
import java.util.function.Function;

import net.objecthunter.exp4j.ValidationResult;

public class EquationDemo1
{
    private final String            prompt      = "Enter a command: ";
    private final BufferedReader    reader;
    private Equation                equation    = new Equation();
    private String                  cmdString   = "";
    private String                  argString   = "";
    
    public static void main(String[] args)
    {
        // TODO Auto-generated method stub

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
            switch ( cmd )
            {
            case EQUATION:
                equation = new Equation();
                break;
            case EXPRESSION:
                parseArg( equation::parseExpression );
                break;
            case VARIABLES:
                parseArg( equation::parseVars );
                break;
            case START:
                parseArg( equation::setRangeStart );
                break;
            case END:
                parseArg( equation::setRangeEnd );
                break;
            case INCREMENT:
                parseArg( equation::setRangeStep );
                break;
            case PARAM:
                setParameterName();
                break;
            case EVALUATE:
                evaluate();
                break;
            case INVALID:
                invalidCommand();
                break;
            case EXIT:
            case NONE:
                // ignore these
                break;
            default:
                malfunction( "enum constant not recognized" );
                break;
            }
        }
    }
    
    private void parseArg( Function<String,ValidationResult> funk )
    {
        ValidationResult    result  = 
            argString.isEmpty() ? 
                requiresArgument() : 
                funk.apply( argString );
        displayResult( result );
    }
    
    private void parseArg( DoubleConsumer funk )
    {
        ValidationResult    result  = null;
        try
        {
            double  val = Double.parseDouble( argString.trim() );
            funk.accept( val );
            result = ValidationResult.SUCCESS;
        }
        catch ( NumberFormatException exc )
        {
            String  error   =
                "\"" + argString + "\" is not a valid value";
            result = new ValidationResult( false, List.of( error ) );
        }
        displayResult( result );
    }
    
    private void setParameterName()
    {
        ValidationResult    result  = ValidationResult.SUCCESS;
        String  trimmed = argString.trim();
        if ( !Equation.isValidName( trimmed ) )
        {
            String  error   = 
                "\"" + argString + "\" is not a valid variable name";
            result = new ValidationResult( false, List.of( error ) );
        }
        displayResult( result );
    }
    
    private void evaluate()
    {
        
    }
    
    private void invalidCommand()
    {
        String  error   = 
            "\"" + cmdString + "\" is not a valid command";
        ValidationResult    result  =
            new ValidationResult( false, List.of( error ) );
        displayResult( result );
    }
    
    private void malfunction( String msg )
    {
        String  error   = "Malfunction: " + msg;
        ValidationResult    result  =
            new ValidationResult( false, List.of( error ) );
        displayResult( result );
    }
    
    private ValidationResult requiresArgument()
    {
        String              message = 
            cmdString + ": missing argument";
        ValidationResult    result  = 
            new ValidationResult( false, List.of( message ) );
        return result;
    }
    
    private void displayResult( ValidationResult result )
    {
        displayMessage( result.toString() );
    }
    
    private void displayMessage( String msg )
    {
        System.err.println( msg );
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
        }
        
        return cmd;
    }
}
