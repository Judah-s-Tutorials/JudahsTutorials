package com.acmemail.judah.cartesian_plane.input;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.function.DoubleConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import net.objecthunter.exp4j.ValidationResult;

/**
 * TODO add default methods to interface
 * @author Jack Straub
 */
public class InputParser
{
    private final List<String>  errors      = new ArrayList<>();
    
    private Equation            equation    = null;
    private Command             command     = Command.NONE;
    private String              argString   = "";
    
    public InputParser()
    {
        this( null );
    }
    
    public InputParser( Equation equation )
    {
        this.equation = equation != null ? equation : new Expr4Equation();
    }
    
    public ValidationResult parseInput( Command command, String argString )
    {
        if ( argString == null )
            throw new IllegalArgumentException( "argString may not be null" );
        
        errors.clear();
        this.command = command;
        this.argString = argString;
        switch ( command )
        {
        case EQUATION:
            equation = new Expr4Equation();
            break;
        case XEQUALS:
            parseArg( equation::setXExpression, equation::getXExpression );
            break;
        case YEQUALS:
            parseArg( equation::setYExpression, equation::getYExpression );
            break;
        case SET:
            parseVars();
            break;
        case START:
            parseArg( equation::setRangeStart, equation::getRangeStart );
            break;
        case END:
            parseArg( equation::setRangeEnd, equation::getRangeEnd );
            break;
        case STEP:
            parseArg( equation::setRangeStep, equation::getRangeStep );
            break;
        case PARAM:
            setParameterName();
            break;
        case INVALID:
            invalidCommand();
            break;
        case EXIT:
        case NONE:
        case YPLOT:
        case XYPLOT:
        case OPEN:
        case SAVE:
            // ignore these
            break;
        default:
            malfunction( "enum constant not recognized" );
            break;
        }
        
        ValidationResult    result  = errors.isEmpty() ? 
            ValidationResult.SUCCESS : new ValidationResult( false, errors );
        return result;
    }
    
    public Equation getEquation()
    {
        return equation;
    }
 
    private void parseArg( 
        Function<String,ValidationResult> setter,
        Supplier<Object> getter
    )
    {   if ( argString.isEmpty() )
            System.out.println( getter.get() );
        else
        {
            ValidationResult    result  = setter.apply( argString );
            if ( !result.isValid() )
                errors.addAll( result.getErrors() );
        }
    }
    
    private void parseArg( DoubleConsumer setter, Supplier<Object> getter )
    {
        if ( argString.isBlank() )
            System.out.println( getter.get() );
        else
        {
            try
            {
                double  val = Double.parseDouble( argString );
                setter.accept( val );
            }
            catch ( NumberFormatException exc )
            {
                formatError( argString, "is not a valid value" );
            }
        }
    }
    
    /**
     * Parses a string consisting of
     * a comma-separated list of variable specifications.
     * 
     * @see #parseVarPair(String)
     * 
     */
    private void parseVars()
    {
        StringTokenizer tizer   = new StringTokenizer( argString, "," );
        while ( tizer.hasMoreElements() )
        {
            String      varPair = tizer.nextToken();
            parseVarPair( varPair );
        }
    }
    
    /**
     * Parses a variable name/value specification.
     * A valid specification 
     * consists of a valid variable name alone,
     * or a valid variable name/value pair
     * separated by an equal sign (=).
     * A variable name alone
     * will be assigned a value of 0.
     * A variable name is valid
     * if it begins with an underscore or letter,
     * and otherwise consists solely 
     * of alphanumeric characters and underscores.
     * A valid value string
     * is any string that can be converted 
     * to a double.
     * Whitespace is ignored.
     * If necessary,
     * parsing error are recorded
     * in the instance <em>errors</em> list.
     * <p>
     * Examples:
     * </p>
     * <p style="margin-left: 2em;">
     * <code>a=5.1,b=-2.5 , c = -1, x,y,t</code><br>
     * <code>a=5.1</code><br>
     * <code>t</code>
     * </p>
     * 
     * @param varPair
     */
    private void parseVarPair( String varPair )
    {
        String[]        parts   = varPair.split( "=" );
        
        // var spec must be either "var" or "var=val" 
        if ( parts.length > 2 )
        {
            String  err =
                "\"" + varPair + "\""  
                + " is not a valid variable specification";
            errors.add( err );
        }
        else
        {
            String  name    = parts[0].trim();
            String  valStr  = 
                parts.length == 1 ? "0" : parts[1].trim();
            
            if ( !Expr4Equation.isValidName( name ) )
            {
                formatError( name, "is not a valid variable name" );
            }
            else if ( !Expr4Equation.isValidValue( valStr ) )
            {
                formatError( valStr, "is not a valid variable value" );
            }
            else
            {
                double  val = Double.parseDouble( valStr );
                equation.setVar( name, val );
            }
        }
    }
    
    private void setParameterName()
    {
        if ( argString.isEmpty() )
            System.out.println( equation.getParam() );
        else if ( !Expr4Equation.isValidName( argString ) )
            formatError( argString, "is not a valid variable name" );
        else
            equation.setParam( argString );
    }
    
    /**
     * Report an invalid command.
     */
    private void invalidCommand()
    {
        formatError( "is not a valid command in this context" );
    }
    
    /**
     * Report that a malfunction has occurred.
     * 
     * @param msg   text to add to tail of error message
     */
    private void malfunction( String msg )
    {
        String  error   = "Malfunction: " + msg;
        errors.add( error );
    }
    
    /**
     * Formats an error message
     * and adds it to the list of errors.
     * The error message is of the form:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;"command" message<br>
     * for example:
     * <pre>    "VARIABLES" missing argument</pre>
     * 
     * @param message   the message to follow "command"
     */
    private void formatError( String message )
    {
        final String    fmt = "\"%s\" %s";
        String  error   = String.format( fmt, command, message );
        errors.add( error );
    }
    
    /**
     * Formats an error message
     * and adds it to the list of errors.
     * The error message is of the form:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;command: "arg" message<br>
     * for example:
     * <pre>    VARIABLES: "3..114" is not a valid value</pre>
     * 
     * @param message   the message to follow "command"
     */
    private void formatError( String arg, String message )
    {
        final String    fmt = "%s: \"%s\" %s";
        String  error   = String.format( fmt, command, arg, message );
        errors.add( error );
    }
}
