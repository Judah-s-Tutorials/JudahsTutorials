package com.acmemail.judah.cartesian_plane.input;

import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Consumer;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.ValidationResult;

/**
 * Implementation of the Equation interface
 * using the exp4j API.
 * Upon instantiation
 * simple expressions for evaluating x and y
 * are set to "1",
 * the iteration range is 
 * initialized to valid values
 * and the following
 * variables are declared:
 * <em>x, y, a, b, c, r</em> and <em>t</em>.
 * The default parameter name
 * for parametric equations is <em>t</em>.
 *
 * @author Jack Straub
 * 
 * @see Equation
 * @see <a href="https://www.objecthunter.net/exp4j/apidocs/index.html">
 *          exp4j API Documentation
 *      </a>
 * @see <a href="https://www.objecthunter.net/exp4j/">
 *          exp4j Introduction
 *      </a>
 */
public class Exp4jEquation implements Equation
{
    private static final String noNameGiven            = 
        "no name provided";
    private static final String firstCharMustBeAlpha    =
        "first char of name must be underscore or alphabetic";
    private static final String mustBeAlNum             =
        "name must be alphanumeric (may include underscores)";
    private static final String notNumeric              =
        "value is not numeric";
    private static final String stepMayNotBeZero        =
        "step value may not be zero";
    private static final String unreachableFromStart    =
        "the end value is unreachable from the start value";
    
    private final Map<String,Double>    vars        = new HashMap<>();
    private String                      name        = "";
    private double                      rStart      = -1;
    private double                      rEnd        = 1;
    private double                      rStep       = .05;
    private String                      xExprStr    = "1";
    private String                      yExprStr    = "1";
    private String                      param       = "t";
    private Expression                  xExpr       = null;
    private Expression                  yExpr       = null;
    
    /**
     * Default constructor.
     * Sets the two expressions
     * associated with this Equation
     * to the constant expression "1"
     * (<em>f(x) = 1, f(y) = 1</em>).
     * A default set of variables
     * is registered; see {@linkplain Exp4jEquation}.
     *
     * @see Exp4jEquation
     */
    public Exp4jEquation()
    {
        initIntrinsicVariables();
        setXExpression( xExprStr );
        setYExpression( yExprStr );
    }
    
    /**
     * Constructor.
     * Establishes the expression 
     * associated with the equation <em>y=f(x)</em>.
     * A default set of variables
     * is registered; see {@linkplain Exp4jEquation}.
     * 
     * @param expr  
     *      the expression associated with the equation;
     *      must be non-null
     *      
     * @throws NullPointerException if expr is null
     */
    public Exp4jEquation( String expr )
    {
        Objects.requireNonNull( expr, "expr" );
        initIntrinsicVariables();
        setXExpression( xExprStr );
        setYExpression( expr );
    }

    /**
     * Constructor.
     * Establishes the set of variables
     * and the expression <em>y=f(x)</em>
     * associated with this Equation.
     * 
     * @param vars  the associated set of variables;
     *              may be null
     * @param expr  the associated expression;
     *              must be non-null
     *              
     * @throws  NullPointerException if expr is null
     */
    public Exp4jEquation( Map<String,Double> vars, String expr )
    {
        Objects.requireNonNull( expr, "expr" );
        if ( vars != null )
            this.vars.putAll( vars );
        initIntrinsicVariables();
        setXExpression( xExprStr );
        setYExpression( expr );
    }
    
    @Override
    public Result setName( String name )
    {
        Objects.requireNonNull( name, "name" );
        this.name = name;
        return new Result( true );
    }
    
    @Override
    public String getName()
    {
        return name;
    }
    
    @Override
    public Equation newEquation()
    {
        return new Exp4jEquation();
    }
    
    @Override
    public Result setVar( String name, double val )
    {
        Objects.requireNonNull( name, "name" );
        Result  result  = validateName( name );
        if ( result.success() )
            vars.put( name, val );
        return result;
    }
    
    @Override
    public void removeVar( String name )
    {
        Objects.requireNonNull( name, "name" );
        vars.remove( name );
    }
    
    @Override
    public Optional<Double> getVar( String name )
    {
        Objects.requireNonNull( name, "name" );
        Optional<Double>    result  = 
            Optional.ofNullable( vars.get( name ) );
        return result;
    }
    
    @Override
    public Map<String,Double> getVars()
    {
        Map<String,Double>  varsRet = Collections.unmodifiableMap( vars );
        return varsRet;
    }
    
    @Override
    public Result setXExpression( String exprStr )
    {
        Objects.requireNonNull( exprStr, "exprStr" );
        Result    result  = validateExpr( exprStr, e -> xExpr = e );
        if ( result.success() )
            this.xExprStr = exprStr;
        return result;
    }
    
    @Override
    public Result setYExpression( String exprStr )
    {
        Objects.requireNonNull( exprStr, "exprStr" );
        Result    result  = validateExpr( exprStr, e -> yExpr = e );
        if ( result.success() )
            this.yExprStr = exprStr;
        return result;
    }
    
    @Override
    public String getXExpression()
    {
        return xExprStr;
    }
    
    @Override
    public String getYExpression()
    {
        return yExprStr;
    }
    
    @Override
    public String getParamName()
    {
        return param;
    }
    
    @Override
    public Result setParamName( String param )
    {
        Objects.requireNonNull( param, "param" );
        Result  result  = validateName( param );
        if ( result.success() )
            this.param = param;
        return result;
    }
    
    @Override
    public void setRange( double start, double end, double step )
    {
        setRangeStart( start );
        setRangeEnd( end );
        setRangeStep( step );
    }
    
    @Override
    public void setRangeStart( double rangeStart )
    {
        rStart = rangeStart;
    }
    
    @Override
    public double getRangeStart()
    {
        return rStart;
    }
    
    @Override
    public double getRangeEnd()
    {
        return rEnd;
    }
    
    @Override
    public void setRangeEnd( double rangeEnd )
    {
        rEnd = rangeEnd;
    }
    
    @Override
    public double getRangeStep()
    {
        return rStep;
    }

    @Override
    public void setRangeStep( double rangeStep )
    {
        rStep = rangeStep;
    }
    
    @Override
    public Result validateRange()
    {
        String    error    = "";
        if ( rStep == 0 )
        {
            error = stepMayNotBeZero;
        }
        else if ( rStep < 0 )
        {
            if ( rStart < rEnd )
                error = unreachableFromStart;
        }
        else
        {
            if ( rStart > rEnd )
                error = unreachableFromStart;
        }
        
        Result result  = null;
        if ( error.isEmpty() )
            result = new Result( true );
        else
        {
            StringBuilder bldr        = new StringBuilder( error );
            bldr.append( "start = " ).append( rStart )
                .append( ", end = " ).append( rEnd )
                .append( ", step = " ).append( rStep );
            List<String>  messages    = List.of( bldr.toString() );
            result = new Result( false, messages );
        }

        return result;
    }

    @Override
    public Result validateName( String name )
    {
        String  status  = "";
        int     len     = name != null ? name.length() : 0;
        
        if ( len == 0 )
            status = noNameGiven;
        else if ( !isAlpha( name.charAt( 0 ) ) )
            status = firstCharMustBeAlpha;
        else
        {   
            OptionalInt optional    =
                name.chars()
                .filter( c -> !isAlphanumeric( c ) )
                .findAny();
            if ( !optional.isEmpty() )
                status = mustBeAlNum;
        }
        Result  result  = getResult( status.isEmpty(), status );
        return result;
    }
    
    @Override
    public Result validateValue( String valStr )
    {
        Optional<Double>    status  = valStr == null ?
            Optional.empty() : evaluate( valStr );
        Result              result  = status.isPresent() ?
            new Result( true ) : getResult( false, notNumeric );
        
        return result;
    }
    
    @Override
    public Optional<Double> evaluate( String exprStr )
    {
        Objects.requireNonNull( exprStr, "exprStr" );
        Optional<Double>    result  = Optional.empty();
        try
        {
            Expression          expr    =
                new ExpressionBuilder( exprStr )
                    .variables( vars.keySet() )
                    .build();
            expr.setVariables( vars );
            if ( expr.validate( true ).isValid() )
            {
                double  val     = expr.evaluate();
                result = Optional.of(val );
            }
        }
        catch ( Exception exc )
        {
            // .build may throw an unexpected exception. If it
            // does, catch it, and return empty Optional
        }
        return result;
    }
    
    @Override
    public Stream<Point2D> yPlot()
    {
        Result  isRangeValid    = validateRange();
        if ( !isRangeValid.success() )
            throw new ValidationException( getMessage( isRangeValid ) );

        yExpr.setVariables( vars );
        Stream<Point2D> stream  =
            DoubleStream.iterate( rStart, x -> x <= rEnd, x -> x + rStep )
                .mapToObj( d -> {
                    yExpr.setVariable( "x", d );
                    return new Point2D.Double( d, yExpr.evaluate() );
                });
        return stream;
    }
    
    @Override
    public Stream<Point2D> xyPlot()
    {
        Result  isRangeValid    = validateRange();
        if ( !isRangeValid.success() )
            throw new ValidationException( getMessage( isRangeValid ) );

        xExpr.setVariables( vars );
        yExpr.setVariables( vars );
        Stream<Point2D> stream  =
            DoubleStream.iterate( rStart, t -> t <= rEnd, t -> t + rStep )
                .mapToObj( t -> { 
                    xExpr.setVariable( param, t );
                    yExpr.setVariable( param, t );
                    return new Point2D.Double( 
                        xExpr.evaluate(), 
                        yExpr.evaluate()
                    );
                });
        return stream;
    }
    
    /**
     * Generate and validate an exp4j Expression
     * from a given string.
     * Validation takes place
     * by attempting to build an expression
     * using ExpressionBuilder.
     * This can result in an 
     * undocumented exception being thrown,
     * in which case a Result
     * describing the exception is returned.
     * If no exception is thrown
     * the Expression <em>validate</em> method is invoked;
     * if this indicates an error,
     * the Result
     * obtained from the <em>validate</em> method
     * is returned.
     * If no error is detected,
     * the generated Expression 
     * is stored at the given destination
     * and a successful result is returned.
     * 
     * @param exprStr       source string for generated expression
     * @param destination   destination for generated expression
     * 
     * @return  Result object describing the result of the operation
     */
    private Result 
    validateExpr( String exprStr, Consumer<Expression> destination )
    {
        Result    result  = null;
        try
        {
            Expression expr = new ExpressionBuilder( exprStr )
                .variables( vars.keySet() )
                .build();
            expr.setVariables( vars );
            ValidationResult    expr4jResult = expr.validate( false );
            boolean             validExpr    = expr4jResult.isValid();
            if ( validExpr )
                destination.accept( expr );
            
            result = new Result( validExpr, expr4jResult.getErrors() );
        }
        catch ( Exception exc )
        {
            String    message    = exc.getMessage();
            if ( message == null )
                message = "no message found";
            List<String>    list    =
                List.of( 
                    "Unexpected exception",
                    exc.getClass().getName(),
                    message
                );
            result = new Result( false, list );
        }
        return result;
    }
    
    /**
     * Determine if a given character is alphabetic:
     * _, or [a-z] or [A-Z].
     * 
     * @param ccc   the given character
     * 
     * @return  true if the given character is alphabetic.
     */
    private static boolean isAlpha( int ccc )
    {
        boolean result  =
            ccc == '_'
            || (ccc >= 'A' && ccc <= 'Z')
            || (ccc >= 'a' && ccc <= 'z');
        return result;
    }
    
    /**
     * Determine if a given character is alphanumeric:
     * _, or [a-z], or [A-Z] or [0-9].
     *  
     * @param ccc   the given character
     * 
     * @return  true if the given character is alphanumeric.
     */
    private static boolean isAlphanumeric( int ccc )
    {
        boolean result  =
            ccc == '_'
            || (ccc >= 'A' && ccc <= 'Z')
            || (ccc >= 'a' && ccc <= 'z')
            || (ccc >= '0' && ccc <= '9');
        return result;
    }
    
    /**
     * Initializes the variable map
     * to the default values; see {@linkplain Exp4jEquation}.
     * Intrinsically declared variables
     * (x, y, a, b, c, r, t)
     * are set to their default values.
     * 
     * @see Exp4jEquation
     * @see Equation
     * @see Equation#INTRINSIC_VARIABLES
     */
    private void initIntrinsicVariables()
    {
        Equation.INTRINSIC_VARIABLES.entrySet()
            .forEach( entry -> 
                vars.putIfAbsent( entry.getKey(), entry.getValue() ) );
    }
    
    /** 
     * Get a Result object containing the given status and message.
     * 
     * @param   status  the given status
     * @param   message the given message
     */
    private static Result getResult( boolean status, String message )
    {
        String  workMessage = message.trim();// == null ? "" : message.trim();
        Result  result      = workMessage.isEmpty() ?
            new Result( status ) : 
            new Result( status, List.of( workMessage ) );
        return result;
    }
    
    /**
     * If the list of messages in a given Result object is non-empty,
     * returns the first message in the list.
     * If the list is empty, the empty string is returned.
     * 
     * @param result    the given Result object
     * 
     * @return
     *      the first message in the Result object's list of messages,
     *      or the empty string if the list is empty.
     */
    private static String getMessage( Result result )
    {
        List<String>    list    = result.messages();
        String          message = list.isEmpty() ? "" : list.get( 0 );
        return message;
    }
}
