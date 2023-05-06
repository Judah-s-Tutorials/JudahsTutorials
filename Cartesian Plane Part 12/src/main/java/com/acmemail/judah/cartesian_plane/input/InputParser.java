package com.acmemail.judah.cartesian_plane.input;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Interprets an executes a command in the context
 * of an <em>Equation.</em>
 * 
 * @author Jack Straub
 * @see Equation
 */
public class InputParser
{
    /** 
     * List of error message associated
     * with an attempted operation.
     * Cleared at the start of every operation.
     */
    private final List<String>  errors      = new ArrayList<>();
    
    /** Encapsulated Equation. */
    private Equation            equation    = null;
    /** Command currently being processed. Set by parseInput method. */
    private Command             command     = Command.NONE;
    /** Argument currently being processed. Set by parseInput method. */
    private String              argString   = "";
    
    /**
     * Default constructor.
     * Creates and initializes a default Equation object.
     */
    public InputParser()
    {
        this( null );
    }
    
    /**
     * Constructor.
     * Established the encapsulated Equation.
     * 
     * @param equation  the encapsulated equation; may be null,
     *                  in which case a default will be instantiated
     */
    public InputParser( Equation equation )
    {
        this.equation = equation != null ? equation : new Exp4jEquation();
    }
    
    /**
     * Interprets and executes a command
     * and associated argument, if any.
     * For details,
     * see {@linkplain #parseInput(Command, String)}.
     * 
     * @param   pCommand    object encapsulating the command
     *                      and argument to interpret
     *                      
     * @return  Result object describing the outcome of the operation
     */
    public Result parseInput( ParsedCommand pCommand )
    {
        Result  result  = 
            parseInput( pCommand.getCommand(), pCommand.getArgString() );
        return result;
    }
    
    /**
     * Interprets and executes a command
     * and associated argument, if any.
     * The execution of each possible command
     * is described by the {@linkplain Equation} interface.
     * A Result object
     * describing the outcome of the operation
     * is returned.
     * 
     * @param command       command to execute 
     * @param argString     associated argument, may not be null
     * 
     * @return  Result object describing the outcome of the operation
     * 
     * @see Equation
     * @see Result
     */
    public Result parseInput( Command command, String argString )
    {
        if ( argString == null )
            throw new IllegalArgumentException( "argString may not be null" );
        
        errors.clear();
        this.command = command;
        this.argString = argString;
        switch ( command )
        {
        case EQUATION:
            equation = equation.newEquation();
            equation.setName( argString );
            break;
        case XEQUALS:
            parseArg( equation::setXExpression, equation::getXExpression );
            break;
        case YEQUALS:
            parseArg( equation::setYExpression, equation::getYExpression );
            break;
        case REQUALS:
            parseArg( equation::setRExpression, equation::getRExpression );
            break;
        case TEQUALS:
            parseArg( equation::setTExpression, equation::getTExpression );
            break;
        case SET:
            parseVars();
            break;
        case START:
            parseExpression( equation::setRangeStart, equation::getRangeStart );
            break;
        case END:
            parseExpression( equation::setRangeEnd, equation::getRangeEnd );
            break;
        case STEP:
            parseExpression( equation::setRangeStep, equation::getRangeStep );
            break;
        case PARAM:
            setName( equation::setParam, equation::getParam );
            break;
        case RADIUS:
            setName( equation::setRadius, equation::getRadius );
            break;
        case THETA:
            setName( equation::setTheta, equation::getTheta );
            break;
        case INVALID:
            invalidCommand();
            break;
        case EXIT:
        case NONE:
        case YPLOT:
        case XYPLOT:
        case RPLOT:
        case TPLOT:
        case OPEN:
        case SAVE:
            // ignore these
            break;
        default:
            String  error   = 
                "Malfunction: " + "enum constant not recognized";
            errors.add( error );
            break;
        }
        
        Result  result  = errors.isEmpty() ? 
            new Result( true ) : new Result( false, errors );
        return result;
    }
    
    /**
     * Returns the encapsulated equation.
     * 
     * @return  the encapsulated equation
     */
    public Equation getEquation()
    {
        return equation;
    }
 
    /**
     * Sets the value of a String resource
     * in the encapsulated Equation
     * from the current argument.
     * If the argument is empty,
     * prints the current value of the resource
     * to stdout.
     * If an error occurs
     * associated error messages
     * are stored in the <em>errors</em> list.
     * 
     * @param setter    method to invoke to set the resource
     * @param getter    method to invoke to obtain the current value
     *                  of the resource
     */
    private void parseArg( 
        Function<String,Result> setter,
        Supplier<Object> getter
    )
    {   
        if ( argString.isEmpty() )
            System.out.println( getter.get() );
        else
        {
            Result  result  = setter.apply( argString );
            if ( !result.isSuccess() )
                errors.addAll( result.getMessages() );
        }
    }
    
    /**
     * Interprets the current argument as an expression,
     * converts it to a double
     * and sets the value in the encapsulated Equation.
     * If the argument string is empty
     * the current value of the resource
     * is printed to stdout.
     * If an error occurs
     * associated messages 
     * are stored in the <em>errors</em> list.
     * 
     * @param setter    method to set the converted value
     * @param getter    method to obtain the current value
     *                  of the indicated resource
     */
    private void 
    parseExpression( DoubleConsumer setter, Supplier<Object> getter )
    {
        if ( argString.isEmpty() )
            System.out.println( getter.get() );
        else
        {
            Optional<Double>    opt     = equation.evaluate( argString );
            if ( opt.isPresent() )
                setter.accept( opt.get() );
            else
                formatError( argString, "is not a valid expression" );
        }
    }
    
    /**
     * Interprets the current argument
     * as a comma-separated list of variable specifications,
     * parses them,
     * and sets the results in the encapsulated Equation.
     * If the current argument is empty
     * prints the names and values 
     * of all currently declared variables
     * to stdout.
     * Messages associated with errors
     * are stored in the <em>errors</em> list.
     * 
     * @see #parseVarPair(String)
     */
    private void parseVars()
    {
        if ( argString.isEmpty() )
            printVars();
        else
        {
            StringTokenizer tizer   = new StringTokenizer( argString, "," );
            while ( tizer.hasMoreElements() )
            {
                String      varPair = tizer.nextToken();
                parseVarPair( varPair );
            }
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
     * A value consists of any valid expression
     * in the context of the current equation.
     * Whitespace is ignored.
     * If necessary,
     * parsing errors are recorded
     * in the <em>errors</em> list.
     * <p>
     * Examples:
     * </p>
     * <p style="margin-left: 2em;">
     * <code>a=5.1,b=-2.5 , c = -1, x,y,t</code><br>
     * <code>a=5.1</code><br>
     * <code>end=2pi</code><br>
     * <code>a=14.739,b=-17.2713,sum=a+b</code><br>
     * <code>side=4,hyp=5,theta=acos(side/hyp)</code><br>
     * <code>t</code>
     * </p>
     * 
     * @param varPair   the name/value pair to parse
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
            
            if ( !equation.isValidName( name ) )
            {
                formatError( name, "is not a valid variable name" );
            }
            else
            {
                Optional<Double>    optVal  = 
                    equation.evaluate( valStr );
                if ( optVal.isPresent() )
                {
                    double  val = optVal.get();
                    equation.setVar( name, val );
                }
                else
                    formatError( valStr, "is not a valid value" );
            }
        }
    }
    
    /**
     * Prints the name and value
     * of all currently declared variables
     * to stdout.
     */
    private void printVars()
    {
        final String    format  = "%s=%f%n";
        Set<Map.Entry<String,Double>>   entries =
            equation.getVars().entrySet();
        entries.forEach( 
            e -> System.out.printf( format, e.getKey(), e.getValue() )
        );
    }
    
    /**
     * Sets one of the parameter names
     * for a parametric or polar equation 
     * to the current argument.
     * If the argument is empty
     * the current name of the parameter
     * is printed to stdout.
     * If the argument is not a valid variable name
     * an error is stored in the <em>errors</em> list.
     */
    private void setName( Consumer<String> setter, Supplier<String> getter )
    {
        if ( argString.isEmpty() )
            System.out.println( getter.get() );
        else if ( !equation.isValidName( argString ) )
            formatError( argString, "is not a valid variable name" );
        else
            setter.accept( argString );
    }
    
    /**
     * Report an invalid command.
     */
    private void invalidCommand()
    {
        formatError( "is not a valid command in this context" );
    }
    
    /**
     * Formats an error message
     * and adds it to the list of errors.
     * The error message is of the form:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;"command" message<br>
     * for example:
     * <pre>    "PARAM" invalid name</pre>
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
     * <pre>    SET: "3..114" is not a valid value</pre>
     * 
     * @param arg       the argument to include in the message
     * @param message   the message to follow "command"
     */
    private void formatError( String arg, String message )
    {
        final String    fmt = "%s: \"%s\" %s";
        String  error   = String.format( fmt, command, arg, message );
        errors.add( error );
    }
}
