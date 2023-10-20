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

import org.nfunk.jep.JEP;

/**
 * Implementation of the Equation interface
 * using the Java Expression Parser (JEP) API.
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
 * @see <a href="http://sens.cse.msu.edu/Software/jep-2.23/doc/website/doc/javadoc/index.html">
 *          JEP API Documentation
 *      </a>
 * @see <a href="http://sens.cse.msu.edu/Software/jep-2.23/doc/website/doc/doc_usage.htm">
 *          JEP Introduction
 *      </a>
 */
public class JEPEquation implements Equation
{
    private final Map<String,Double>    vars        = new HashMap<>();
    private String                      name        = "";
    private double                      rStart      = -1;
    private double                      rEnd        = 1;
    private double                      rStep       = .05;
    private String                      xExprStr    = "1";
    private String                      yExprStr    = "1";
    private String                      tExprStr    = "1";
    private String                      rExprStr    = "1";
    private String                      param       = "t";
    private String                      radius      = "r";
    private String                      theta       = "t";
    private JEP                         xExpr       = null;
    private JEP                         yExpr       = null;
    private JEP                         tExpr       = null;
    private JEP                         rExpr       = null;
    
    /**
     * Default constructor.
     */
    public JEPEquation()
    {
        init();
    }
    
    /**
     * Constructor.
     * Initializes the y-expression
     * to a given value.
     * 
     * @param yExpression   the given y-expression
     */
    public JEPEquation( String yExpression )
    {
        init();
        setYExpression( yExpression );
    }
    
    /**
     * Constructor.
     * Initializes the y-expression
     * to a given value,
     * and adds the given variable declarations
     * to this equation.
     * 
     * @param yExpression   the given y-expression
     * @param vars          the given variable declarations
     */
    public JEPEquation( Map<String,Double> vars, String yExpression )
    {
        init();
        this.vars.putAll( vars );
        setYExpression( yExpression );
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

    @Override
    public Equation newEquation()
    {
        return new JEPEquation();
    }

    @Override
    public void setVar(String name, double val)
    {
        vars.put( name, val );
    }

    @Override
    public void removeVar(String name)
    {
        vars.remove( name );
    }

    @Override
    public Optional<Double> getVar(String name)
    {
        Optional<Double>    result  = Optional.empty();
        Double              val     = vars.get( name );
        if ( val != null )
            result = Optional.of( val );
        return result;
    }

    @Override
    public Map<String, Double> getVars()
    {
        Map<String,Double>  varsRet = Map.copyOf( vars );
        return varsRet;
    }

    @Override
    public Result setXExpression(String exprStr)
    {
        Result    result  = 
            validateExpr( exprStr, s -> xExprStr = s, e -> xExpr = e );
        return result;
    }

    @Override
    public Result setYExpression(String exprStr)
    {
        Result    result  = 
            validateExpr( exprStr, s -> yExprStr = s, e -> yExpr = e );
        return result;
    }

    @Override
    public Result setTExpression(String exprStr)
    {
        Result    result  = 
            validateExpr( exprStr, s -> tExprStr = s, e -> tExpr = e );
        return result;
    }

    @Override
    public Result setRExpression(String exprStr)
    {
        Result    result  = 
            validateExpr( exprStr, s -> rExprStr = s, e -> rExpr = e );
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
    public Stream<Point2D> yPlot()
    {
        updateVars( yExpr );
        Stream<Point2D> stream  =
            DoubleStream.iterate( rStart, d -> d <= rEnd, d -> d + rStep )
                .peek( d -> yExpr.addVariable( "x", d ) )
                .mapToObj( d -> new Point2D.Double( d, yExpr.getValue() ) );
        return stream;
    }

    @Override
    public Stream<Point2D> xyPlot()
    {
        updateVars( xExpr );
        updateVars( yExpr );
        Stream<Point2D> stream  =
            DoubleStream.iterate( rStart, t -> t <= rEnd, t -> t + rStep )
                .peek( t -> xExpr.addVariable( "t", t ) )
                .peek( t -> yExpr.addVariable( "t", t ) )
                .mapToObj( t -> 
                    new Point2D.Double( xExpr.getValue(), yExpr.getValue() )
                );
        return stream;
    }

    @Override
    public Stream<Point2D> rPlot()
    {
        updateVars( rExpr );
        Stream<Point2D> stream  =
            DoubleStream.iterate( rStart, t -> t <= rEnd, t -> t + rStep )
                .peek( t -> rExpr.addVariable( theta, t ) )
                .mapToObj( t -> Polar.of( rExpr.getValue(), t ) )
                .map( p -> p.toPoint() );
        return stream;
    }

    @Override
    public Stream<Point2D> tPlot()
    {
        Stream<Point2D> stream  =
            DoubleStream.iterate( rStart, r -> r <= rEnd, r -> r += rStep )
                .peek( r -> tExpr.addVariable( radius, r ) )
                .mapToObj( r -> Polar.of( r, tExpr.getValue() ) )
                .map( p -> p.toPoint() );
        return stream;
    }

    @Override
    public String getParam()
    {
        return param;
    }

    @Override
    public void setParam(String param)
    {
        this.param = param;
    }

    @Override
    public String getRadiusName()
    {
        return radius;
    }

    @Override
    public void setRadiusName(String radius)
    {
        this.radius = radius;
    }

    @Override
    public String getThetaName()
    {
        return theta;
    }

    @Override
    public void setThetaName(String theta)
    {
        this.theta = theta;
    }

    @Override
    public void setRange(double start, double end, double step)
    {
        this.rStart = start;
        this.rEnd = end;
        this.rStep = step;
    }

    @Override
    public void setRangeStart(double rangeStart)
    {
        this.rStart = rangeStart;
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
    public void setRangeEnd(double rangeEnd)
    {
        this.rEnd = rangeEnd;
    }

    @Override
    public double getRangeStep()
    {
        return rStep;
    }

    @Override
    public void setRangeStep(double rangeStep)
    {
        this.rStep = rangeStep;
    }

    @Override
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

    @Override
    public boolean isValidValue(String valStr)
    {
        Optional<Double>    result  = evaluate( valStr );
        
        return result.isPresent();
    }

    @Override
    public Optional<Double> evaluate(String exprStr)
    {
        Optional<Double>    result  = Optional.empty();
        JEP parser  = newParser();
        parser.parseExpression( exprStr );
        if ( !parser.hasError() )
            result = Optional.of( parser.getValue() );
        return result;
    }
    
    /**
     * Common initializer for constructors.
     */
    private void init()
    {
        vars.put( "x",  0. );
        vars.put( "y",  0. );
        vars.put( "a",  0. );
        vars.put( "b",  0. );
        vars.put( "c",  0. );
        vars.put( "r",  0. );
        vars.put( "t",  0. );
        setXExpression( xExprStr );
        setYExpression( yExprStr );
        setTExpression( tExprStr );
        setRExpression( rExprStr );
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
     * @param exprStr   source string for generated expression
     * @param strDest   destination for expression string representation
     * @param objDest   destination for generated expression
     * 
     * @return  Result object describing the result of the operation
     */
    private Result validateExpr( 
        String exprStr, 
        Consumer<String> strDest, 
        Consumer<JEP> objDest 
    )
    {
        Result  result  = null;
        JEP parser  = newParser();
        parser.parseExpression( exprStr );
        if ( parser.hasError() )
            result = new Result( false, List.of( parser.getErrorInfo() ) );
        else
        {
            result = new Result( true, null );
            objDest.accept( parser );
            strDest.accept( exprStr );
        }
        return result;
    }
    
    /**
     * Instantiate and initialize a JEP object.
     * 
     * @return  the new JEP object
     */
    private JEP newParser()
    {
        JEP parser  = new JEP();
        parser.addStandardConstants();
        parser.addStandardFunctions();
        JEPFunctions.addFunctions( parser );
        parser.setImplicitMul( true );
        updateVars( parser );
        return parser;
    }
    
    /**
     * Add all existing variables
     * to the given parser.
     * 
     * @param parser    the given parser
     */
    private void updateVars( JEP parser )
    {
        vars.forEach( (s,d) -> parser.addVariable( s,  d ) );
    }
}
