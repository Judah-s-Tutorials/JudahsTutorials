package com.acmemail.judah;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
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
    
    public Equation()
    {
        initMap();
        setXExpression( xExprStr );
        setYExpression( yExprStr );
    }
    
    public Equation( String expr )
    {
        initMap();
        setXExpression( expr );
    }
    
    public Equation( Map<String,Double> vars, String expr )
    {
        vars.putAll( vars );
        setXExpression( expr );
    }
    
    public void setVar( String name, double val )
    {
        vars.put( name, val );
    }
    
    public void removeVar( String name )
    {
        vars.remove( name );
    }
    
    public double getVar( String name )
    {
        double  val = vars.getOrDefault( name, 0. );
        return val;
    }
    
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
    
    public Stream<Point2D> streamXY()
    {
        xExpr.setVariables( vars );
        ValidationResult    validationResult    = xExpr.validate( true );
        if ( validationResult != ValidationResult.SUCCESS )
            throw new InvalidExpressionException( validationResult );
        Stream<Point2D> stream  =
            DoubleStream.iterate( rStart, t -> t <= rEnd, t -> t += rStep )
            .peek( t -> xExpr.setVariable( param, t ) )
            .peek( t -> yExpr.setVariable( param, t ) )
            .mapToObj( t -> new Point2D.Double( xExpr.evaluate(), yExpr.evaluate() ) );
        return stream;
    }
    
    public void setParam( String param )
    {
        this.param = param;
        vars.put( param, 0. );
    }
    
    public void setRangeStart( double start, double end, double step )
    {
        setRangeStart( start );
        setRangeEnd( end );
        setRangeStep( step );
    }
    
    public void setRangeStart( double rangeStart )
    {
        rStart = rangeStart;
    }
    
    public void setRangeEnd( double rangeEnd )
    {
        rStart = rangeEnd;
    }
    
    public void setRangeStep( double rangeStep )
    {
        rStart = rangeStep;
    }
    
    private void initMap()
    {
        vars.put( "x",  0. );
        vars.put( "a",  0. );
        vars.put( "b",  0. );
        vars.put( "c",  0. );
        vars.put( "t",  0. );
    }
}
