package com.acmemail.judah.cartesian_plane.input;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * <em>x, y, a, b, c</em> and <em>t</em>.
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
    private final Map<String,Double>    vars        = new HashMap<>();
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
        initMap();
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
     * @param expr  the expression associated with the equation
     */
    public Exp4jEquation( String expr )
    {
        initMap();
        setXExpression( xExprStr );
        setYExpression( expr );
    }
    
    /**
     * Constructor.
     * Establishes the set of variables
     * and the expression <em>y=f(x)</em>
     * associated with this Equation.
     * 
     * @param vars  the associated set of variables
     * @param expr  the associated expression
     */
    public Exp4jEquation( Map<String,Double> vars, String expr )
    {
        this.vars.putAll( vars );
        initMap();
        setXExpression( xExprStr );
        setYExpression( expr );
    }
    
    /**
     * Returns a newly initialized Equation.
     * 
     * @return  a newly initialized Equation
     */
    public Equation newEquation()
    {
        return new Exp4jEquation();
    }
    
    /**
     * Sets the value of a variable to a given value.
     * 
     * @param name  the name of the variable
     * @param val   the given value
     */
    @Override
    public void setVar( String name, double val )
    {
        vars.put( name, val );
    }
    
    /**
     * Removes from the set of variables
     * the variable with the given name.
     * If the name is not found
     * the operation is ignored.
     * 
     * @param name  the given name
     */ 
    @Override
    public void removeVar( String name )
    {
        vars.remove( name );
    }
    
    /**
     * Gets the value of the variable
     * with the given name.
     * If the name is not found
     * null is returned.
     * 
     * @param name  the given name
     * 
     * @return  the value of the variable with the given name
     */
    @Override
    public Optional<Double> getVar( String name )
    {
        Optional<Double>    result  = Optional.empty();
        Double              val     = vars.get( name );
        if ( val != null )
            result = Optional.of( val );
        return result;
    }
    
    @Override
    public Map<String,Double> getVars()
    {
        Map<String,Double>  varsRet = Map.copyOf( vars );
        return varsRet;
    }
    
    /**
     * Parses the expression used to derive
     * the x-coordinate of a point 
     * to the given value.
     * If a parsing error occurs
     * a description of the error is returned,
     * otherwise Result.SUCCESS is returned.
     * 
     * @param exprStr   the given value
     * 
     * @return  the status of the operation
     */
    @Override
    public Result setXExpression( String exprStr )
    {
        Result    result  = validateExpr( exprStr, e -> xExpr = e );
        if ( result.isSuccess() )
            this.xExprStr = exprStr;
        return result;
    }
    
    /**
     * Parses the expression used to derive
     * the y-coordinate of a point 
     * to the given value.
     * If a parsing error occurs
     * a description of the error is returned,
     * otherwise Result.SUCCESS is returned.
     * 
     * @param exprStr   the given value
     * 
     * @return  the status of the operation
     */
    @Override
    public Result setYExpression( String exprStr )
    {
        Result    result  = validateExpr( exprStr, e -> yExpr = e );
        if ( result.isSuccess() )
            this.yExprStr = exprStr;
        return result;
    }
    
    /**
     * Iterates over the encapsulated range,
     * generating the (x,y) coordinates 
     * derived from an equation of the form <em>y=f(x)</em>.
     * 
     * @return the (x,y) coordinates derived from a parametric equation
     * 
     * @throws ValidationException if the equation is invalid
     */
    @Override
    public Stream<Point2D> yPlot()
    {
        yExpr.setVariables( vars );
        ValidationResult    result    = yExpr.validate( true );
        if ( result != ValidationResult.SUCCESS )
        {
            String  message = "Unexpected expression validation failure.";
            throw new ValidationException( message );
        }
        Stream<Point2D> stream  =
            DoubleStream.iterate( rStart, x -> x <= rEnd, x -> x += rStep )
                .peek( d -> yExpr.setVariable( "x", d ) )
                .mapToObj( d -> new Point2D.Double( d, yExpr.evaluate() ) );
        return stream;
    }
    
    /**
     * Iterates over the encapsulated range,
     * generating the (x,y) coordinates 
     * derived from a parametric equation.
     * 
     * @return the (x,y) coordinates derived from a parametric equation
     * 
     * @throws ValidationException if the equation is invalid
     */
    @Override
    public Stream<Point2D> xyPlot()
    {
        xExpr.setVariables( vars );
        ValidationResult    result    = xExpr.validate( true );
        if ( !result.isValid() )
        {
            String  message = "Unexpected x-expression validation failure.";
            throw new ValidationException( message );
        }
        yExpr.setVariables( vars );
        result = yExpr.validate( true );
        if ( !result.isValid() )
        {
            String  message = "Unexpected y-expression validation failure.";
            throw new ValidationException( message );
        }
        
        Stream<Point2D> stream  =
            DoubleStream.iterate( rStart, t -> t <= rEnd, t -> t += rStep )
                .peek( t -> xExpr.setVariable( param, t ) )
                .peek( t -> yExpr.setVariable( param, t ) )
                .mapToObj( t -> new Point2D.Double( xExpr.evaluate(), yExpr.evaluate() ) );
        return stream;
    }
    
    /**
     * Gets the currently set x-expression.
     * 
     * @return  the currently set x-expression
     */
    @Override
    public String getXExpression()
    {
        return xExprStr;
    }
    
    /**
     * Gets the currently set y-expression.
     * 
     * @return  the currently set y-expression
     */
    @Override
    public String getYExpression()
    {
        return yExprStr;
    }
    
    /**
     * Gets the name of the parameter
     * in a parametric equation.
     * 
     * @return the name of the parameter
     */
    @Override
    public String getParam()
    {
        return param;
    }
    
    /**
     * Sets the name of the parameter
     * in a parametric equation.
     * 
     * @param param the name of the parameter
     */
    @Override
    public void setParam( String param )
    {
        this.param = param;
        vars.put( param, 0. );
    }
    
    /**
     * Establishes the iteration range for this Equation.
     * 
     * @param start the start of the iteration range
     * @param end   the end of the iteration range
     * @param step  the increment to use when traversing the iteration range
     */
    @Override
    public void setRange( double start, double end, double step )
    {
        setRangeStart( start );
        setRangeEnd( end );
        setRangeStep( step );
    }
    
    /**
     * Sets the start of the iteration range.
     * 
     * @param rangeStart   iteration range start
     */
    @Override
    public void setRangeStart( double rangeStart )
    {
        rStart = rangeStart;
    }
    
    /**
     * Returns the start of the iteration range.
     * 
     * @return the start of the iteration range
     */
    @Override
    public double getRangeStart()
    {
        return rStart;
    }
    
    /**
     * Returns the end of the iteration range.
     * 
     * @return the end of the iteration range
     */
    @Override
    public double getRangeEnd()
    {
        return rEnd;
    }
    

    /**
     * Sets the end of the iteration range.
     * 
     * @param rangeEnd  iteration range end
     */
    @Override
    public void setRangeEnd( double rangeEnd )
    {
        rEnd = rangeEnd;
    }
    
    /**
     * Returns the increment used
     * to iterate over the encapsulated range.
     * 
     * @return the start of the iteration range
     */
    @Override
    public double getRangeStep()
    {
        return rStep;
    }

    /**
     * Sets the increment used
     * to iterate over the encapsulated range.
     * 
     * @param rangeStep   iteration range increment
     */
    @Override
    public void setRangeStep( double rangeStep )
    {
        rStep = rangeStep;
    }
    
    /**
     * Determines if a given string
     * is a valid variable name.
     * Given that underscore is an alphabetic character,
     * a valid variable name is one that
     * begins with an alphabetic character,
     * and whose remaining are characters alphanumeric.
     * 
     * @param name  the given string
     * 
     * @return  true if the given string is a valid variable name
     */
    public boolean isValidName( String name )
    {
        boolean status  = false;
        int     len     = name.length();
        if ( len == 0 )
            ; // invalid
        else if ( !isAlpha( name.charAt( 0 ) ) )
            ; // invalid
        else
        {   
            OptionalInt optional    =
                name.chars()
                .filter( c -> !isAlphanumeric( c ) )
                .findAny();
            status = optional.isEmpty();
        }
        return status;
    }
    
    /**
     * Determines if a given string
     * is a valid double value.
     * 
     * @param valStr  the given string
     * 
     * @return  true if the given string is a valid double value
     */
    public boolean isValidValue( String valStr )
    {
        Optional<Double>    result  = evaluate( valStr );
        
        return result.isPresent();
    }
    
    @Override
    public Optional<Double> evaluate( String exprStr )
    {
        Optional<Double>    result  = Optional.empty();
        try
        {
            Expression  expr    =
                new ExpressionBuilder( exprStr )
                    .variables( vars.keySet() )
                    .build();
            ValidationResult    exp4jResult = expr.validate( true );
            if ( !exp4jResult.isValid() )
                throw new ValidationException();
            double      val     = expr.evaluate();
            result = Optional.of(val );
        }
        catch ( Exception exc )
        {
            // Exactly how we get here is unclear. If everything's
            // working correctly we shouldn't get here at all, but
            // sometimes ExpressionBuilder throws an undocumented
            // exception in the face of an invalid expression.
        }
        return result;
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
     * and Result.SUCCESS is returned.
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
            ValidationResult    expr4jResult = expr.validate( false );
            if ( expr4jResult.isValid() )
                destination.accept( expr );
            
            result = new Result( 
                expr4jResult.isValid(), 
                expr4jResult.getErrors()
            );
        }
        catch ( Exception exc )
        {
            List<String>    list    =
                List.of( 
                    "Unexpected exception",
                    exc.getClass().getName(),
                    exc.getMessage()
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
     * 
     *
     */
    private static boolean isAlpha( char ccc )
    {
        boolean result  =
            ccc == '_'
            || (ccc >= 'A' && ccc <= 'Z')
            || (ccc >= 'a' && ccc <= 'z');
        return result;
    }
    
    /**
     * Determine if a given character is alphanumeric:
     * _, or [a-z], or [A-Z] or [-,9].
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
     * 
     * @see Exp4jEquation
     */
    private void initMap()
    {
        vars.put( "x",  0. );
        vars.put( "y",  0. );
        vars.put( "a",  0. );
        vars.put( "b",  0. );
        vars.put( "c",  0. );
        vars.put( "t",  0. );
    }
}
