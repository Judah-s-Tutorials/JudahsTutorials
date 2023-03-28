package com.acmemail.judah;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.function.DoubleConsumer;
import java.util.function.Function;

import net.objecthunter.exp4j.ValidationResult;

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
        this.equation = equation != null ? equation : new Equation();
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
            equation = new Equation();
            break;
        case EXPRESSION:
            parseExpression();
            break;
        case X_EXPRESSION:
            if ( argString.isEmpty() )
                System.out.println( equation.getXExpression() );
            else
                parseArg( equation::setXExpression );
            break;
        case Y_EXPRESSION:
            parseArg( equation::setYExpression );
            break;
        case VARIABLES:
            parseVars();
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
        case INVALID:
            invalidCommand();
            break;
        case EXIT:
        case NONE:
        case Y_STREAM:
        case XY_STREAM:
        case STREAM:
            // ignore these
            break;
        default:
            malfunction( "enum constant not recognized" );
            break;
        }
        
        ValidationResult    result  = errors.isEmpty() ? 
            ValidationResult.SUCCESS : new ValidationResult( false, errors );
//        reportResult( result );
        return result;
    }
    
    public Equation getEquation()
    {
        return equation;
    }
    
    private void parseArg( Function<String,ValidationResult> funk )
    {
        ValidationResult    result  = 
            argString.isEmpty() ? 
                requiresArgument() : 
                funk.apply( argString );
        if ( !result.isValid() )
            errors.addAll( result.getErrors() );
    }
    
    private void parseArg( DoubleConsumer funk )
    {
        try
        {
            double  val = Double.parseDouble( argString.trim() );
            funk.accept( val );
        }
        catch ( NumberFormatException exc )
        {
            formatError( argString, "is not a valid value" );
        }
    }
    
    /**
     * Parses a string consisting of
     * a comma-separated list of variable specifications.
     * 
     * @see #parseVarPair(String)
     * 
     */
    public void parseVars()
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
        if ( parts.length < 1 || parts.length > 2 )
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
            
            if ( !Equation.isValidName( name ) )
            {
                formatError( name, "is not a valid variable name" );
            }
            else if ( !Equation.isValidValue( valStr ) )
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
    
    /**
     * Parse an expression string.
     * A valid expression is one of:
     * <ul>
     * <li>
     *      "x=</em>non-empty string</em>"
     *      (whitespace ignored):
     *      the x-expression will be set to
     *      the non-empty string
     * </li>
     * <li>
     *      "y=</em>non-empty string</em>"
     *      (whitespace ignored):
     *      the y-expression will be set to
     *      the non-empty string
     * </li>
     * <li>
     *      All others (whitespace ignored):
     *      the x-expression will be set to
     *      the given expression
     * </li>
     * </ul>
     * <p>
     * A value describing the result of the operation
     * is returned.
     * </p>
     * 
     * @param expr  the given expression string
     * 
     * @return  a value describing the result of the operation
     */
    public void parseExpression()
    {
        ValidationResult    result  = ValidationResult.SUCCESS;
        int                 split   = argString.indexOf( '=' );
        if ( split > 0 )
        {
            String  varPart     = argString.substring( 0, split ).trim();
            String  exprPart    = argString.substring( split + 1 ).trim();
            if ( !exprPart.isEmpty() )
                result = equation.setXExpression( argString.trim() );
            else if ( varPart.equals( "x" ) )
                result = equation.setXExpression( exprPart.trim() );
            else if ( varPart.equals( "y" ) )
                result = equation.setYExpression( exprPart.trim() );
            else
            {
                formatError( argString, "invalid expression" );
            }
        }
        else
            result = equation.setXExpression( argString.trim() );
        
        List<String>    list    = result.getErrors();
        if ( list != null )
            errors.addAll( result.getErrors() );
    }
    
    private void setParameterName()
    {
        String  trimmed = argString.trim();
        if ( !Equation.isValidName( trimmed ) )
        {
            formatError( argString, "is not a valid variable name" );
        }
    }
    
    private void invalidCommand()
    {
        formatError( "is not a valid command in this context" );
    }
    
    private void malfunction( String msg )
    {
        String  error   = "Malfunction: " + msg;
        errors.add( error );
    }
    
    private ValidationResult requiresArgument()
    {
        String              error   = command + ": missing required argument";
        ValidationResult    result  = 
            new ValidationResult( false, List.of( error ) );
        return result;
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
