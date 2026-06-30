package com.acmemail.judah.cartesian_plane.input;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Interprets and executes a command in the context
 * of an {@linkplain Equation}.
 * <p>
 * A command processor is bound to a single equation.
 * To process commands for a new equation,
 * construct another CommandProcessor object.
 * </p>
 * <p>
 * Full execution of the following commands
 * is dependent upon configuration of the Plotter facility
 * by the client.
 * If the facility is not configured
 * execution of these commands
 * will return a failed Result
 * with the message "No plotter configured."
 * Configuration of the Plotter
 * is established via the
 * {@link CommandProcessor#CommandProcessor(Equation, Plotter) constructor}.
 * See also {@link com.acmemail.judah.cartesian_plane.DefaultPlotter}.
 * </p>
 * <ul>
 * <li>YPLOT</li>
 * <li>XYPLOT</li>
 * <li>RPLOT</li>
 * <li>TPLOT</li>
 * </ul>
 *
 * <p>
 * The following commands are silently ignored
 * pending a revised implementation:
 * </p>
 * <ul>
 * <li>EXIT</li>
 * <li>SAVE</li>
 * <li>OPEN</li>
 * <li>SELECT</li>
 * </ul>
 *
 * @author Jack Straub
 * @see Equation
 */
public class CommandProcessor
{
    private static final String invalidName = "is not a valid name";
    private static final String invalidExpr = "is not a valid expression";
    private static final String invalidSpec = "is not a valid specification";
    private static final String invalidCmd  = "is not a valid command";
    private static final String notEquation =
        "can only process EQUATION command";
    private static final String noPlotter   =
        "No Plotter configured";

    /** Encapsulated Equation. */
    private final Equation      equation;
    private final Plotter       plotter;

    /**
     * Default constructor.
     * Creates and initializes a default Equation object.
     */
    public CommandProcessor()
    {
        this( null );
    }

    /**
     * Constructor.
     * Establishes the encapsulated Equation.
     *
     * @param equation  the encapsulated equation; may be null,
     *                  in which case a default will be instantiated
     */
    public CommandProcessor( Equation equation )
    {
        this( equation, null );
    }

    /**
     * Constructor.
     * Establishes the encapsulated Equation.
     *
     * @param equation  the encapsulated equation; may be null,
     *                  in which case a default will be instantiated
     * @param plotter   method to invoke when plotting an equation
     */
    public CommandProcessor( Equation equation, Plotter plotter )
    {
        this.equation = equation != null ? equation : new Exp4jEquation();
        this.plotter  = plotter;
    }

    /**
     * Creates a new CommandProcessor with a new Equation.
     *
     * This method complements {@linkplain #processCommand(ParsedCommand)},
     * and for that reason requires a ParsedCommand argument.
     * However, the only command accepted is EQUATION.
     * It creates a new Equation,
     * encapsulates it in a CommandProcessor object,
     * and returns the CommandProcessor.
     * If the ParsedCommand object has a non-empty argString
     * it is taken to be the name of the new equation;
     * if the argString is empty,
     * the Equation will be unnamed.
     * <p>
     * If the ParsedCommand object contains any command
     * other than EQUATION an illegal argument is thrown.
     *
     * @param command   the ParsedCommand object to process;
     *                  may not be null
     *
     * @return a new CommandProcessor object
     *
     * @throws IllegalArgumentException
     *      if the input command is not EQUATION
     * @throws NullPointerException if command is null
     */
    public CommandProcessor newEquation( ParsedCommand command )
    {
        Objects.requireNonNull( command, "command" );
        if ( command.getCommand() != Command.EQUATION )
        {
            String  error   = command.getCommand() + ": " + notEquation;
            throw new IllegalArgumentException( error );
        }

        Equation            equation    = this.equation.newEquation();
        String              name        = command.getArgString().trim();
        if ( !name.isEmpty() )
            equation.setName( name );
        CommandProcessor    processor   =
            new CommandProcessor( equation, plotter );
        return processor;
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
     * @param parsedCommand the command to process; may not be null
     *
     * @return  Result object describing the outcome of the operation
     *
     * @throws IllegalArgumentException
     *      if parsedCommand contains the EQUATION command
     * @throws NullPointerException if parsedCommand is null
     * @throws IllegalStateException if a command is not recognized
     *
     * @see Equation
     * @see Result
     */
    public Result processCommand( ParsedCommand parsedCommand )
    {
        Objects.requireNonNull( parsedCommand, "parsedCommand" );

        Context context = new Context( parsedCommand );
        Command command = context.getCommand();
        switch ( command )
        {
        case EQUATION:
            parseArg(
                context,
                equation::setName,
                equation::getName
            );
            break;
        case XEQUALS:
            parseArg(
                context,
                equation::setXExpression,
                equation::getXExpression
            );
            break;
        case YEQUALS:
            parseArg(
                context,
                equation::setYExpression,
                equation::getYExpression
            );
            break;
        case REQUALS:
            parseArg(
                context,
                equation::setRExpression,
                equation::getRExpression
            );
            break;
        case TEQUALS:
            parseArg(
                context,
                equation::setTExpression,
                equation::getTExpression
            );
            break;
        case SET:
            parseVars( context );
            break;
        case START:
            parseExpression(
                context,
                equation::setRangeStart,
                equation::getRangeStart
            );
            break;
        case END:
            parseExpression(
                context,
                equation::setRangeEnd,
                equation::getRangeEnd
            );
            break;
        case STEP:
            parseExpression(
                context,
                equation::setRangeStep,
                equation::getRangeStep
            );
            break;
        case RADIUS:
            setName(
                context,
                equation::setRadiusName,
                equation::getRadiusName
            );
            break;
        case THETA:
            setName(
                context,
                equation::setThetaName,
                equation::getThetaName
            );
            break;
        case PARAM:
            setName(
                context,
                equation::setParamName,
                equation::getParamName
            );
            break;
        case INVALID:
            invalidCommand( context );
            break;
        case YPLOT:
            plot( context, equation::yPlot );
            break;
        case XYPLOT:
            plot( context, equation::xyPlot );
            break;
        case RPLOT:
            plot( context, equation::rPlot );
            break;
        case TPLOT:
            plot( context, equation::tPlot );
            break;
        case EXIT:
        case NONE:
        case SELECT:
        case OPEN:
        case SAVE:
            // ignore these for now
            break;
        default:
            throw new IllegalStateException(
                "Unhandled command: " + command
            );
        }

        Result  result  = context.getResult();
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
     * Gets the current Plotter.
     *
     * @return the current Plotter
     */
    public Plotter getPlotter()
    {
        return plotter;
    }

    /**
     * Executes a plot command (YPLOT, XYPLOT, etc.)
     * via the Plotter facility.
     *
     * @param context   the command context
     * @param supplier  the supplier of the Point2D stream
     */
    private void plot( Context context, Supplier<Stream<Point2D>> supplier )
    {
        if ( plotter == null )
            context.formatError( context.getArgString(), noPlotter );
        else
            plotter.plot( supplier );
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
     * are stored in the command context.
     *
     * @param context   the command context
     * @param setter    method to invoke to set the resource
     * @param getter    method to invoke to obtain the current value
     *                  of the resource
     */
    private void parseArg(
        Context context,
        Function<String,Result> setter,
        Supplier<String> getter
    )
    {
        String  argString   = context.getArgString();
        if ( argString.isEmpty() )
            System.out.println( getter.get() );
        else
        {
            Result  result  = setter.apply( argString );
            context.mergeResult( result );
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
     * are stored in the command context.
     *
     * @param context   the command context
     * @param setter    method to set the converted value
     * @param getter    method to obtain the current value
     *                  of the indicated resource
     */
    private void parseExpression(
        Context context,
        DoubleConsumer setter,
        DoubleSupplier getter
    )
    {
        String  argString   = context.getArgString();
        if ( argString.isEmpty() )
            System.out.println( getter.getAsDouble() );
        else
        {
            Optional<Double>    opt = equation.evaluate( argString );
            opt.ifPresentOrElse(
                setter::accept,
                () -> context.formatError( argString, invalidExpr )
            );
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
     * are stored in the command context.
     *
     * @param context   the command context
     *
     * @see #parseVarPair(Context, String)
     */
    private void parseVars( Context context )
    {
        String  argString   = context.getArgString();
        if ( argString.isEmpty() )
            printVars();
        else
        {
            String[]    pairs   = argString.split( "," );
            for ( String varPair : pairs )
               parseVarPair( context, varPair );
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
     * in the command context.
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
     * @param context   the command context
     * @param varPair   the name/value pair to parse
     */
    private void parseVarPair( Context context, String varPair )
    {
        // var spec must be either "var" or "var=val";
        // spaces permitted: "  var = val ".
        String[]        parts       = varPair.split( "=", -1 );
        int             partsLen    = parts.length;
        String          name        = parts[0].trim();
        if ( partsLen < 1 || partsLen > 2 )
            context.formatError( varPair, invalidSpec );
        else if ( partsLen == 1 )
        {
                Double  val     = equation.getVar( name ).orElse( null );
                String  valStr  = val == null ? 
                    "\"" + name + "\" not found" : val.toString();
                System.out.println( valStr );
        }
        else
        {
            if ( !equation.validateName( name ).success() )
                context.formatError( name, invalidName );
            else
            {
                String              valStr  = parts[1];
                Optional<Double>    optVal  = equation.evaluate( valStr );
                optVal.ifPresentOrElse(
                    v -> equation.setVar( name, v ),
                    () -> context.formatError( valStr, invalidExpr )
                );
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
     * the operation is failed
     * and an error message is recorded in the Context.
     *
     * @param context   the command context
     * @param setter    the method to set the name, if valid
     * @param getter    the getter to provide the current value of the name
     */
    private void setName(
        Context context,
        Consumer<String> setter,
        Supplier<String> getter
    )
    {
        String  argString   = context.getArgString();
        if ( argString.isEmpty() )
            System.out.println( getter.get() );
        else if ( !equation.validateName( argString ).success() )
            context.formatError( argString, invalidName );
        else
            setter.accept( argString );
    }

    /**
     * Report an invalid command.
     *
     * @param context   the command context
     */
    private void invalidCommand( Context context )
    {
        String  command = context.getCommandString();
        context.formatError( command, invalidCmd );
    }

    /**
     * Encapsulates the context
     * in which command is being processed.
     * Provides access to the user's ParsedCommand object,
     * and maintains a list of errors
     * to be included in the final result;
     */
    private static class Context
    {
        private final ParsedCommand parsedCommand;
        private final List<String>  errors;

        /**
         * Constructor.
         * Establishes the encapsulated ParsedCommand.
         *
         * @param parsedCommand the ParsedCommand to encapsulate
         */
        public Context( ParsedCommand parsedCommand )
        {
            this.parsedCommand = parsedCommand;
            errors = new ArrayList<>();
        }

        /**
         * Returns the Command from the encapsulated ParsedCommand.
         *
         * @return  the Command from the encapsulated ParsedCommand
         */
        public Command getCommand()
        {
            return parsedCommand.getCommand();
        }

        /**
         * Returns the command string from the encapsulated ParsedCommand.
         * In the event that the command string is empty
         * (as will be the case when the encapsulated ParsedCommand
         * is not built from operator keyboard input)
         * the stringified command is returned.
         *
         * @return  the command string from the encapsulated ParsedCommand
         */
        public String getCommandString()
        {
            String  commandString   = parsedCommand.getCommandString();
            if ( commandString.trim().isEmpty() )
                commandString = parsedCommand.getCommand().toString();
            return commandString;
        }

        /**
         * Returns the argument string from the encapsulated ParsedCommand.
         *
         * @return  the argument string from the encapsulated ParsedCommand
         */
        public String getArgString()
        {
            return parsedCommand.getArgString();
        }

        /**
         * If the Result is unsuccessful,
         * its messages are appended to the internal list.
         *
         * @param result    the given result
         */
        public void mergeResult( Result result )
        {
            errors.addAll( result.messages() );
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
        public void formatError( String arg, String message )
        {
            final String    fmt = "%s: \"%s\" %s";
            Command command = getCommand();
            String  error   = String.format( fmt, command, arg, message );
            errors.add( error );
        }

        /**
         * Formulates a final Result object.
         * Internal error messages, if any,
         * are transferred to the Result.
         * The status of the result
         * is determined from the list of error messages;
         * if there are no error messages,
         * the status is set to successful.
         *
         * @return  the formulated Result object
         */
        public Result getResult()
        {
            boolean isValid = errors.isEmpty();
            Result  result  = new Result( isValid, errors );
            return result;
        }
    }
}
