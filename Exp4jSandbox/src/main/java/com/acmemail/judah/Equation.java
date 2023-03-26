package com.acmemail.judah;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
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
    private String                      xExprStr    = "x";
    private String                      yExprStr    = "y";
    private String                      param       = "t";
    private Expression                  xExpr       = null;
    private Expression                  yExpr       = null;
    
    /**
     * Default constructor.
     * Sets the two expressions 
     * associated with this Equation
     * to the identity (<em>f(x) = x, f(y) = y</em>).
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
     * associated with this Equation.
     * A default set of variables
     * is registered; see {@linkplain Equation}.
     * 
     * @param expr  the expression associated with the equation
     */
    public Equation( String expr )
    {
        initMap();
        setXExpression( expr );
    }
    
    /**
     * Constructor.
     * Establishes the set of variables
     * and the expression 
     * associated with this Equation.
     * 
     * @param vars  the associated set of variables
     * @param expr  the associated expression
     */
    public Equation( Map<String,Double> vars, String expr )
    {
        vars.putAll( vars );
        setXExpression( expr );
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
     * 0 is returned.
     * 
     * @param name  the given name
     * 
     * @return  the value of the variable with the given name
     */
    public double getVar( String name )
    {
        double  val = vars.getOrDefault( name, 0. );
        return val;
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
    public ValidationResult parseExpression( String expr )
    {
        ValidationResult    result  = ValidationResult.SUCCESS;
        int                 split   = expr.indexOf( '=' );
        if ( split > 0 )
        {
            String  varPart     = expr.substring( 0, split ).trim();
            String  exprPart    = expr.substring( split + 1 ).trim();
            if ( !exprPart.isEmpty() )
                result = setXExpression( expr.trim() );
            else if ( varPart.equals( "x" ) )
                result = setXExpression( exprPart.trim() );
            else if ( varPart.equals( "y" ) )
                result = setYExpression( exprPart.trim() );
            else
            {
                String  error   =
                    "\"" + expr + "\"" + ": invalid expression";
                result = new ValidationResult( false, List.of( error ) );
            }
        }
        else
            result = setXExpression( expr.trim() );
        
        return result;
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
        Expression expr = new ExpressionBuilder( exprStr )
            .variables( vars.keySet() )
            .build();
        ValidationResult    result  = expr.validate();
        if ( result == ValidationResult.SUCCESS )
        {
            this.xExprStr = exprStr;
            this.xExpr = expr;
        }
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
        Expression expr = new ExpressionBuilder( exprStr )
            .variables( vars.keySet() )
            .build();
        ValidationResult    result  = expr.validate();
        if ( result == ValidationResult.SUCCESS )
        {
            this.yExprStr = exprStr;
            this.yExpr = expr;
        }
        return result;
    }
    
    /**
     * Iterates over the encapsulated range,
     * generating the (x,y) coordinates 
     * derived from an equation of the form <em>y=f(x)</em>.
     * 
     * @return the (x,y) coordinates derived from a parametric equation
     */
    public Stream<Point2D> streamY()
    {
        xExpr.setVariables( vars );
        ValidationResult    validationResult    = xExpr.validate( true );
        if ( validationResult != ValidationResult.SUCCESS )
            throw new InvalidExpressionException( validationResult );
        Stream<Point2D> stream  =
            DoubleStream.iterate( rStart, x -> x <= rEnd, x -> x += rStep )
                .peek( d -> xExpr.setVariable( "x", d ) )
                .mapToObj( d -> new Point2D.Double( d, xExpr.evaluate() ) );
        return stream;
    }
    
    /**
     * Iterates over the encapsulated range,
     * generating the (x,y) coordinates 
     * derived from a parametric equation.
     * 
     * @return the (x,y) coordinates derived from a parametric equation
     */
    public Stream<Point2D> streamXY()
    {
        xExpr.setVariables( vars );
        ValidationResult    validationResult    = xExpr.validate( true );
        if ( validationResult != ValidationResult.SUCCESS )
            throw new InvalidExpressionException( validationResult );
        Stream<Point2D> stream  =
            DoubleStream. iterate( rStart, t -> t <= rEnd, t -> t += rStep )
            .peek( t -> xExpr.setVariable( param, t ) )
            .peek( t -> yExpr.setVariable( param, t ) )
            .mapToObj( t -> new Point2D.Double( xExpr.evaluate(), yExpr.evaluate() ) );
        return stream;
    }
    
    /**
     * Parses a string consisting of
     * a comma-separated list of variable specifications.
     * A valid variable specification 
     * consists of a valid variable name alone,
     * or valid variable name/value pair
     * separated by an equal sign (=).
     * A variable name is valid
     * if it begins with an underscore or letter,
     * and otherwise consists solely 
     * of alphanumeric characters and underscores.
     * A valid value string
     * is any string that can be converted 
     * to a double.
     * Whitespace is ignored.
     * A result is returned
     * describing the status of the operation.
     * <p>
     * Examples:
     * </p>
     * <p style="margin-left: 2em;">
     * <code>a=5.1,b=-2.5 , c = -1, x,y,t</code><br>
     * <code>a=5.1</code><br>
     * <code>t</code>
     * </p>
     * 
     * @param varStr    the string to parse
     * 
     * @return  the result of the operation
     */
    public ValidationResult parseVars( String varStr )
    {
        List<String>    errors  = new ArrayList<>();
        StringTokenizer tizer   = new StringTokenizer( varStr );
        while ( tizer.hasMoreElements() )
        {
            String      varPair = tizer.nextToken();
            String[]    parts   = varPair.split( "=" );
            errors.addAll( parseVarPair( varPair ) );
        }
        
        ValidationResult    result  =
            errors.isEmpty() ? 
                ValidationResult.SUCCESS : 
                new ValidationResult( false, errors );
        return result;
    }
    
    private List<String> parseVarPair( String varPair )
    {
        List<String>    errors  = new ArrayList<>();
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
            
            if ( !isValidName( name ) )
            {
                String  err = 
                    varPair + ": \"" + name + "\""
                    + " is not a valid variable name";
                errors.add( err );
            }
            else if ( !isValidValue( valStr ) )
            {
                String  err = 
                    varPair + ": \"" + valStr + "\""
                    + " is not a valid variable value";
                errors.add( err );
            }
            else
            {
                double  val = Double.parseDouble( valStr );
                vars.put( name, val );
            }
        }
        
        return errors;
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
        rStart = rangeEnd;
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
        rStart = rangeStep;
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
    private boolean isValidValue( String valStr )
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
    
    private double getVal( String[] arr )
    {
        double  val = 0;
        if ( arr.length > 1 )
            val = Double.parseDouble( arr[1] );
        return val;
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
        vars.put( "a",  0. );
        vars.put( "b",  0. );
        vars.put( "c",  0. );
        vars.put( "t",  0. );
    }
}
