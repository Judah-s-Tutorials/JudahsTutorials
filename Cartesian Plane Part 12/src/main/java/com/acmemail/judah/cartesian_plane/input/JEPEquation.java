package com.acmemail.judah.cartesian_plane.input;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import org.nfunk.jep.JEP;

public class JEPEquation implements Equation
{
    private final Map<String,Double>    vars        = new HashMap<>();
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
        Result    result  = validateExpr( exprStr, e -> xExpr = e );
        if ( result.isSuccess() )
            this.xExprStr = exprStr;
        return result;
    }

    @Override
    public Result setYExpression(String exprStr)
    {
        Result    result  = validateExpr( exprStr, e -> yExpr = e );
        if ( result.isSuccess() )
            this.yExprStr = exprStr;
        return result;
    }

    @Override
    public Result setTExpression(String exprStr)
    {
        Result    result  = validateExpr( exprStr, e -> tExpr = e );
        if ( result.isSuccess() )
            this.tExprStr = exprStr;
        return result;
    }

    @Override
    public Result setRExpression(String exprStr)
    {
        Result    result  = validateExpr( exprStr, e -> rExpr = e );
        if ( result.isSuccess() )
            this.rExprStr = exprStr;
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
                .peek( t -> yExpr.addVariable( "x", t ) )
                .peek( t -> yExpr.addVariable( "x", t ) )
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getParam()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setParam(String param)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public String getRadius()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setRadius(String radius)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public String getTheta()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setTheta(String theta)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void setRange(double start, double end, double step)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void setRangeStart(double rangeStart)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public double getRangeStart()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getRangeEnd()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setRangeEnd(double rangeEnd)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public double getRangeStep()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setRangeStep(double rangeStep)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isValidName(String name)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isValidValue(String valStr)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Optional<Double> evaluate(String exprStr)
    {
        // TODO Auto-generated method stub
        return Optional.empty();
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
    validateExpr( String exprStr, Consumer<JEP> destination )
    {
        Result  result  = null;
        JEP parser  = newParser();
        parser.parseExpression( exprStr );
        if ( parser.hasError() )
            result = new Result( false, List.of( parser.getErrorInfo() ) );
        else
        {
            result = new Result( true, null );
            destination.accept( parser );
        }
        return result;
    }
    
    private JEP newParser()
    {
        JEP parser  = new JEP();
        parser.addStandardConstants();
        parser.addStandardFunctions();
        parser.setImplicitMul( true );
        updateVars( parser );
        return parser;
    }
    
    private void updateVars( JEP parser )
    {
        vars.forEach( (s,d) -> parser.addVariable( s,  d ) );
    }
}
