package com.acmemail.judah.cartesian_plane.input;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
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
 * The default radius name
 * for polar equations is <em>r</em>.
 * The default angle name
 * for polar equations is <em>t</em>.
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
    private String                      name        = "New Equation";
    private double                      rStart      = -1;
    private double                      rEnd        = 1;
    private double                      rStep       = .05;
    private String                      rStartExpr  = "1";
    private String                      rEndExpr    = "1";
    private String                      rStepExpr   = "1";
    private String                      plot        = "YPlot";
    private int                         precision   = 3;
    private String                      xExprStr    = "1";
    private String                      yExprStr    = "1";
    private String                      tExprStr    = "1";
    private String                      rExprStr    = "1";
    private String                      param       = "t";
    private String                      radius      = "r";
    private String                      theta       = "t";
    private Expression                  xExpr       = null;
    private Expression                  yExpr       = null;
    private Expression                  tExpr       = null;
    private Expression                  rExpr       = null;
    
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
        setTExpression( tExprStr );
        setRExpression( rExprStr );
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
        setTExpression( tExprStr );
        setTExpression( rExprStr );
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
//        initMap();
        setXExpression( xExprStr );
        setYExpression( expr );
        setTExpression( tExprStr );
        setTExpression( rExprStr );
    }
    
    @Override
    public void setName( String name )
    {
        this.name = name;
    }
    
    @Override
    public String getName()
    {
        return name;
    }
    
    /**
     * Returns a newly initialized Equation.
     * 
     * @return  a newly initialized Equation
     */
    @Override
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
    
    @Override
    public Result setTExpression( String exprStr )
    {
        Result    result  = validateExpr( exprStr, e -> tExpr = e );
        if ( result.isSuccess() )
            this.tExprStr = exprStr;
        return result;
    }
    
    @Override
    public Result setRExpression( String exprStr )
    {
        Result    result  = validateExpr( exprStr, e -> rExpr = e );
        if ( result.isSuccess() )
            this.rExprStr = exprStr;
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
    
    @Override
    public boolean isValidExpression( String exprStr )
    {
        ValidationResult    exp4jResult = ValidationResult.SUCCESS;
        try
        {
            Expression expr = new ExpressionBuilder( exprStr )
                .variables( vars.keySet() )
                .functions( Exp4jFunctions.getFunctions() )
                .build();
            exp4jResult = expr.validate( false );
        }
        catch ( Exception exc )
        {
            List<String>    errors  = List.of( exc.getMessage() );
            exp4jResult = new ValidationResult( false, errors );
        }
        boolean             status          = exp4jResult.isValid();
        return status;
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
        plot = "YPlot";
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
        plot = "XYPlot";
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
                .mapToObj( t -> 
                    new Point2D.Double( 
                        xExpr.evaluate(), 
                        yExpr.evaluate() 
            ));
        return stream;
    }
    
    @Override
    public Stream<Point2D> rPlot()
    {
        plot = "RPlot";
        rExpr.setVariables( vars );
        ValidationResult    result    = rExpr.validate( true );
        if ( !result.isValid() )
        {
            String  message = "Unexpected r-expression validation failure.";
            throw new ValidationException( message );
        }

        Stream<Point2D> stream  =
            DoubleStream.iterate( rStart, t -> t <= rEnd, t -> t += rStep )
                .peek( t -> rExpr.setVariable( theta, t ) )
                .mapToObj( t -> Polar.of( rExpr.evaluate(), t ) )
                .map( Polar::toPoint );
        return stream;
    }
    
    @Override
    public Stream<Point2D> tPlot()
    {
        plot = "TPlot";
        tExpr.setVariables( vars );
        ValidationResult    result    = tExpr.validate( true );
        if ( !result.isValid() )
        {
            String  message = "Unexpected t-expression validation failure.";
            throw new ValidationException( message );
        }

        Stream<Point2D> stream  =
            DoubleStream.iterate( rStart, r -> r <= rEnd, r -> r += rStep )
                .peek( r -> tExpr.setVariable( radius, r ) )
                .mapToObj( r -> Polar.of( r, tExpr.evaluate() ) )
                .map( Polar::toPoint );
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
    
    @Override
    public String getTExpression()
    {
        return tExprStr;
    }
    
    @Override
    public String getRExpression()
    {
        return rExprStr;
    }
    
    @Override
    public String getParamName()
    {
        return param;
    }
    
    @Override
    public void setParamName( String param )
    {
        this.param = param;
    }
    
    @Override
    public String getRadiusName()
    {
        return radius;
    }
    
    public void setRadiusName( String radius )
    {
        this.radius = radius;
    }
    
    @Override
    public String getThetaName()
    {
        return theta;
    }
    
    @Override
    public void setThetaName( String theta )
    {
        this.theta = theta;
    }
    
    /**
     * Sets the start of the iteration range
     * from an expression.
     * 
     * @param exprStr   iteration range start
     */
    @Override
    public Result setRangeStart( String exprStr )
    {
        Result  result  =
            setExpr( exprStr, d -> rStart = d, s -> rStartExpr = s );
        return result;
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
     * Returns the start of the iteration range
     * as a String.
     * 
     *@return the start of the iteration range as a String
     */
    @Override
    public String getRangeStartExpr()
    {
        return rStartExpr;
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
     * Returns the end of the iteration range
     * as a String.
     * 
     *@return the end of the iteration range as a String
     */
    @Override
    public String getRangeEndExpr()
    {
        return rEndExpr;
    }
    
    /**
     * Sets the end of the iteration range
     * from an expression.
     * 
     * @param exprStr  iteration range start
     */
    @Override
    public Result setRangeEnd( String exprStr )
    {
        Result  result  =
            setExpr( exprStr, d -> rEnd = d, s -> rEndExpr = s );
        return result;
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
     * Returns the increment expression used
     * to iterate over the encapsulated range.
     * 
     * @return the start of the iteration range
     */
    @Override
    public String getRangeStepExpr()
    {
        return rStepExpr;
    }

    /**
     * Sets increment the expression used
     * to iterate over the encapsulated range.
     * 
     * @param rangeStep   iteration range increment
     */
    @Override
    public Result setRangeStep( String rangeStep )
    {
        Result  result  =
            setExpr( rangeStep, d -> rStep = d, s -> rStepExpr = s );
        return result;
    }
    
    /**
     * Sets the precision for displaying 
     * decimal values.
     * 
     * @param precision the given precision
     */
    public void setPrecision( int precision )
    {
        this.precision = precision;
    }
    
    /**
     * Gets the precision for displaying
     * decimal values.
     * 
     * @return  the precision for displaying decimal values
     */
    public int getPrecision()
    {
        return precision;
    }
    
    /**
     * Sets the type of plot for displaying.
     * Valid values are YPlot, XXPlot, RPlot, and TPlot.
     * 
     * @param precision the given precision
     */
    public void setPlot( String plot )
    {
        this.plot = plot;
    }
    
    /**
     * Returns the type of plot;
     * 
     * @return  the type of plot
     */
    public String getPlot()
    {
        return plot;
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
                    .functions( Exp4jFunctions.getFunctions() )
                    .build();
            expr.setVariables( vars );
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
     * Validates an expression and, if valid,
     * records the expression and it value.
     * 
     * @param exprStr       the expression to evaluate
     * @param valSetter     setter for the value of the expression
     * @param strSetter     setter for the expression string
     * 
     * @return  object indicating result of evaluation
     */
    private Result setExpr( 
        String exprStr, 
        DoubleConsumer valSetter, 
        Consumer<String> strSetter 
    )
    {
        Result              result  = null;
        String              str     = exprStr.trim();
        Optional<Double>    dVal    = evaluate( str );
        if ( dVal.isPresent() )
        {
            valSetter.accept( dVal.get() );
            strSetter.accept( str );
            result = new Result( true );
        }
        else
        {
            String  invExpr =
                "Invalid expression: \"" + str + "\"";
            result = new Result( false, List.of( invExpr ) );
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
                .functions( Exp4jFunctions.getFunctions() )
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
        vars.put( "r",  0. );
        vars.put( "t",  0. );
    }
}
