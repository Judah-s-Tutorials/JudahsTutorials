package com.acmemail.judah.cartesian_plane.exp4j;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.ValidationResult;

public class Equation
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
     * is registered; see {@linkplain Equation}.
     * 
     * @see Equation
     */
    public Equation()
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
     * is registered; see {@linkplain Equation}.
     * 
     * @param expr  the expression associated with the equation
     */
    public Equation( String expr )
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
    public Equation( Map<String,Double> vars, String expr )
    {
        this.vars.putAll( vars );
        setXExpression( xExprStr );
        setYExpression( expr );
    }
    
    /**
     * Sets the value of a variable to a given value.
     * 
     * @param name  the name of the variable
     * @param val   the given value
     */
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
    public Double getVar( String name )
    {
        Double  val = vars.get( name );
        return val;
    }
    
    public ValidationResult parseFunction( String funk )
    {
        ValidationResult    result  =
            new ValidationResult( false, List.of( "NOT IMPLEMENTED") );
        return result;
    }
    
    /**
     * Parses the expression used to derive
     * the x-coordinate of a point 
     * to the given value.
     * If a parsing error occurs
     * a description of the error is returned,
     * otherwise ValidationResult.SUCCESS is returned.
     * 
     * @param exprStr   the given value
     * 
     * @return  the status of the operation
     */
    public ValidationResult setXExpression( String exprStr )
    {
        ValidationResult    result  = validateExpr( exprStr, e -> xExpr = e );
        if ( result == ValidationResult.SUCCESS )
            this.xExprStr = exprStr;
        return result;
    }
    
    /**
     * Parses the expression used to derive
     * the y-coordinate of a point 
     * to the given value.
     * If a parsing error occurs
     * a description of the error is returned,
     * otherwise ValidationResult.SUCCESS is returned.
     * 
     * @param exprStr   the given value
     * 
     * @return  the status of the operation
     */
    public ValidationResult setYExpression( String exprStr )
    {
        ValidationResult    result  = validateExpr( exprStr, e -> yExpr = e );
        if ( result == ValidationResult.SUCCESS )
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
     * @throws InvalidExpressionException if the equation is invalid
     */
    public Stream<Point2D> streamY()
    {
        yExpr.setVariables( vars );
        ValidationResult    validationResult    = yExpr.validate( true );
        if ( validationResult != ValidationResult.SUCCESS )
            throw new InvalidExpressionException( validationResult );
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
     * @throws InvalidExpressionException if the equation is invalid
     */
    public Stream<Point2D> streamXY()
    {
        xExpr.setVariables( vars );
        yExpr.setVariables( vars );
        ValidationResult    validationResult    = xExpr.validate( true );
        if ( validationResult != ValidationResult.SUCCESS )
            throw new InvalidExpressionException( validationResult );
        validationResult    = yExpr.validate( true );
        if ( validationResult != ValidationResult.SUCCESS )
            throw new InvalidExpressionException( validationResult );
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
    public String getXExpression()
    {
        return xExprStr;
    }
    
    /**
     * Gets the currently set y-expression.
     * 
     * @return  the currently set y-expression
     */
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
    public void setRangeStart( double rangeStart )
    {
        rStart = rangeStart;
    }
    
    /**
     * Returns the start of the iteration range.
     * 
     * @return the start of the iteration range
     */
    public double getRangeStart()
    {
        return rStart;
    }
    
    /**
     * Returns the end of the iteration range.
     * 
     * @return the end of the iteration range
     */
    public double getRangeEnd()
    {
        return rEnd;
    }
    

    /**
     * Sets the end of the iteration range.
     * 
     * @param rangeEnd  iteration range end
     */
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
    public void setRangeStep( double rangeStep )
    {
        rStep = rangeStep;
    }
    
    /**
     * Generate and validate an exp4j Expression
     * from a given string.
     * Validation takes place
     * by attempting to build an expression
     * using ExpressionBuilder.
     * This can result in an 
     * undocumented exception being thrown,
     * in which case a ValidationResult
     * describing the exception is returned.
     * If no exception is thrown
     * the Expression <em>validate<em> method is invoked;
     * if this indicates an error,
     * the ValidationResult
     * obtained from the <em>validate<em> method
     * is returned.
     * If no error is detected,
     * the generated Expression 
     * is stored at the given destination
     * and ValidationResult.SUCCESS is returned.
     * 
     * @param exprStr
     * @param destination
     * @return
     */
    private ValidationResult 
    validateExpr( String exprStr, Consumer<Expression> destination )
    {
        ValidationResult    result  = null;
        try
        {
            Expression expr = new ExpressionBuilder( exprStr )
                .variables( vars.keySet() )
                .build();
            result = expr.validate( false );
            if ( result == ValidationResult.SUCCESS )
                destination.accept( expr );
        }
        catch ( Exception exc )
        {
            List<String>    list    =
                List.of( 
                    "Unexpected exception",
                    exc.getClass().getName(),
                    exc.getMessage()
                );
            result = new ValidationResult( false, list );
        }
        return result;
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
    public static boolean isValidName( String name )
    {
        boolean status  = false;
        int     len     = name.length();
        if ( len == 0 )
            ; // invalid
        else if ( !isAlpha( name.charAt( 0 ) ) )
            ; // invalid
        else
        {   
            int inx = 1;
            while ( inx < len && isAlphaNumeric( name.charAt( inx ) ) )
                ++inx;
            status = (inx == len);
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
    public static boolean isValidValue( String valStr )
    {
        boolean result  = false;
        try
        {
            Double.parseDouble( valStr );
            result = true;
        }
        catch ( NumberFormatException exc )
        {
            result = false;
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
     * 
     *
     */
    private static boolean isAlphaNumeric( char ccc )
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
     * to the default values; see {@linkplain Equation}.
     * 
     * @see Equation
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
