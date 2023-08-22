package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class JEPEquationTest
{
    private JEPEquation   equation;
    
    @BeforeEach
    public void beforeEach() throws Exception
    {
        equation = new JEPEquation();
    }

    @Test
    public void testJEPEquation()
    {
        validateDefaultVariables();
        validateDefaultRange();
        validateDefaultXExpression();
        validateDefaultYExpression();
    }

    @Test
    public void testJEPEquationString()
    {
        equation = new JEPEquation( "2x" );
        validateDefaultVariables();
        validateDefaultRange();
        validateDefaultXExpression();
        
        // verify that y expression is set to 2 
        // (see above "new JEPEquation( "2x" )").
        equation.setRange( 1, 1, 1 );
        equation.yPlot().forEach(
            p -> assertEquals( 2, p.getY(), "Y" ) 
        );
    }

    @Test
    public void testJEPEquationMapOfStringDoubleString()
    {
        Map<String,Double>  mapIn   = new HashMap<>();
        String[]            vars    = { "h", "j", "k", "l" };
        for ( String str : vars )
            mapIn.put( str, (double)str.charAt( 0 ) );
        
        equation = new JEPEquation( mapIn, "2t" );
        validateDefaultVariables();
        validateDefaultRange();
        
        // validate mapped variables declared
        Map<String,Double>  actMap  = equation.getVars();
        for ( String var : vars )
        {
            Double  val = actMap.get( var );
            assertNotNull( val );
            assertEquals( val, (double)var.charAt( 0 ) );
        }
        
        // validate expressions
        equation.setRange( 1, 1, 1 );
        equation.xyPlot().forEach(
            p -> {
                assertEquals( 1, p.getX(), "X" );
                assertEquals( 2, p.getY(), "Y" );
            }
        );
    }
    
    @Test
    public void testSetGetName()
    {
        String      name        = "Aristotle";
        equation.setName( name );
        assertEquals( name, equation.getName() );
    }

    @Test
    public void testNewEquation()
    {
        equation = (JEPEquation)equation.newEquation();
        validateDefaultVariables();
        validateDefaultRange();
        validateDefaultXExpression();
        validateDefaultYExpression();
    }


    @Test
    public void testSetRemoveVar()
    {
        double  val     = 3.14;
        String  name    = "abc";
        equation.setVar( name, val );
        
        Optional<Double>    actVal  = equation.getVar( name );
        assertTrue( actVal.isPresent() );
        assertEquals( val, actVal.get() );
        
        equation.removeVar( name );
        actVal  = equation.getVar( name );
        assertFalse( actVal.isPresent() );
    }

    @Test
    public void testGetVars()
    {
        String[]            vars    = { "h", "j", "k", "l" };
        for ( String var : vars )
            equation.setVar( var, var.charAt( 0 ) );
        
        Map<String,Double>  actVars = equation.getVars();
        for ( String var : vars )
        {
            Double  actVal  = actVars.get( var );
            assertNotNull( actVal );
            assertEquals( var.charAt( 0 ), actVal );
        }
    }

    @Test
    public void testSetXExpression()
    {
        double  xier    = 2;
        String  xExpr   = xier + "t";
        Result  result  = equation.setXExpression( xExpr );
        assertTrue( result.isSuccess() );
        assertEquals( xExpr, equation.getXExpression() );
        
        equation.setRange( 1, 1, 1 );
        equation.xyPlot()
            .forEach( p -> assertEquals( xier, p.getX() ) );
        
        // try setting an invalid expression
        result  = equation.setXExpression( "invalid" );
        assertFalse( result.isSuccess() );
    }

    @Test
    public void testSetXExpressionGoWrong()
    {
        String  oldXExpr    = equation.getXExpression();
        String  xExpr       = "notAVar * x";
        Result  result      = equation.setXExpression( xExpr );
        assertFalse( result.isSuccess() );
        assertEquals( oldXExpr, equation.getXExpression() );
    }

    @Test
    public void testSetYExpression()
    {
        double  xier    = 2;
        String  yExpr   = xier + "x";
        Result  result  = equation.setYExpression( yExpr );
        assertTrue( result.isSuccess() );
        assertEquals( yExpr, equation.getYExpression() );
        
        equation.setRange( 1, 1, 1 );
        equation.yPlot()
            .forEach( p -> assertEquals( p.getY(), xier ) );
        
        // try setting an invalid expression
        result  = equation.setYExpression( "invalid" );
        assertFalse( result.isSuccess() );
    }

    @Test
    public void testSetYExpressionGoWrong()
    {
        String  oldyExpr    = equation.getYExpression();
        String  yExpr       = "undeclaredVarName * x";
        Result  result      = equation.setYExpression( yExpr );
        assertFalse( result.isSuccess() );
        assertEquals( oldyExpr, equation.getYExpression() );
    }

    @Test
    public void testSetRExpression()
    {
        String  rExpr   = "0 + 1";
        Result  result  = equation.setRExpression( rExpr );
        assertTrue( result.isSuccess() );
        assertEquals( rExpr, equation.getRExpression() );
        
        equation.setRange( Math.PI, Math.PI, 1 );
        equation.rPlot()
            .forEach( p -> assertEquals( -1, p.getX(), .0001 ) );
        
        // try setting an invalid expression
        result  = equation.setRExpression( "invalid" );
        assertFalse( result.isSuccess() );
    }

    @Test
    public void testSetRExpressionGoWrong()
    {
        String  oldRExpr    = equation.getRExpression();
        String  rExpr       = "undeclaredVarName * x";
        Result  result      = equation.setRExpression( rExpr );
        assertFalse( result.isSuccess() );
        assertEquals( oldRExpr, equation.getRExpression() );
    }

    @Test
    public void testSetTExpression()
    {
        String  tExpr   = "pi";
        Result  result  = equation.setTExpression( tExpr );
        assertTrue( result.isSuccess() );
        assertEquals( tExpr, equation.getTExpression() );
        
        equation.setRange( 1, 1, 1 );
        equation.tPlot()
            .forEach( p -> assertEquals( -1, p.getX(), .0001 ) );
        
        // try setting an invalid expression
        result  = equation.setRExpression( "invalid" );
        assertFalse( result.isSuccess() );
    }

    @Test
    public void testSetTExpressionGoWrong()
    {
        String  oldTExpr    = equation.getTExpression();
        String  tExpr       = "undeclaredVarName * x";
        Result  result      = equation.setTExpression( tExpr );
        assertFalse( result.isSuccess() );
        assertEquals( oldTExpr, equation.getTExpression() );
    }

    @Test
    public void testYPlot()
    {
        double  xier    = 2;
        String  yExpr   = xier + "x";
        equation.setYExpression( yExpr );
        
        double  start   = -2;
        double  end     = 2;
        double  step    = .1;
        List<Point2D>   expPoints   =
            DoubleStream.iterate( start, x -> x <= end, x -> x + step )
                .mapToObj( x -> new Point2D.Double( x, xier * x ) )
                .collect( Collectors.toList() );
        
        equation.setRange( start, end, step );
        List<Point2D>   actPoints   =
            equation.yPlot()
            .collect( Collectors.toList() );
        
        assertEquals( expPoints, actPoints );
    }

    @Test
    public void testXYPlot()
    {
        double  xXier   = 2;
        double  yXier   = 3;
        String  xExpr   = xXier + "t";
        String  yExpr   = yXier + "t";
        equation.setXExpression( xExpr );
        equation.setYExpression( yExpr );
        
        double  start   = -2;
        double  end     = 2;
        double  step    = .1;
        List<Point2D>   expPoints   =
            DoubleStream.iterate( start, t -> t <= end, t -> t + step )
                .mapToObj( t -> new Point2D.Double( xXier * t, yXier * t ) )
                .collect( Collectors.toList() );
        
        equation.setRange( start, end, step );
        List<Point2D>   actPoints   =
            equation.xyPlot()
            .collect( Collectors.toList() );
        
        assertEquals( expPoints, actPoints );
    }

    @Test
    public void testRPlot()
    {
        // 4 points where unit circle intersects x/y axes
        List<Point2D>   expPoints   = List.of( 
            new Point2D.Double( 1, 0 ), 
            new Point2D.Double( 0, 1 ), 
            new Point2D.Double( -1, 0 ), 
            new Point2D.Double( 0, -1 )
        );
        
        equation.setRExpression( "1" );
        double  start   = 0;
        double  end     = 3 * Math.PI / 2;
        double  step    = Math.PI / 2;
        
        equation.setRange( start, end, step );
        List<Point2D>   actPoints   =
            equation.rPlot()
            .collect( Collectors.toList() );
        
        // Test equality after allowing for rounding errors
        double  epsilon = .000001;
        assertEquals( expPoints.size(), actPoints.size() );
        IntStream.range( 0, 4 ).forEach( i -> {
            Point2D ePoint  = expPoints.get( i );
            Point2D aPoint  = actPoints.get( i );
            assertEquals( ePoint.getX(), aPoint.getX(), epsilon, "" + i );
            assertEquals( ePoint.getY(), aPoint.getY(), epsilon, "" + i );
        });
    }

    @Test
    public void testTPlot()
    {
        double  theta   = Math.PI / 2;
        List<Point2D>   expPoints   = 
            IntStream.range( 0, 4 )
            .mapToObj( r -> 
                new Point2D.Double(
                        r * Math.cos( theta ),
                        r * Math.sin( theta )
                ))
            .collect( Collectors.toList() );
        
        equation.setTExpression( "pi / 2" );
        double  start   = 0;
        double  end     = 3;
        double  step    = 1;
        
        equation.setRange( start, end, step );
        List<Point2D>   actPoints   =
            equation.tPlot()
            .collect( Collectors.toList() );
        
        // Test equality after allowing for rounding errors
        double  epsilon = .000001;
        assertEquals( expPoints.size(), actPoints.size() );
        IntStream.range( 0, 4 ).forEach( i -> {
            Point2D ePoint  = expPoints.get( i );
            Point2D aPoint  = actPoints.get( i );
            assertEquals( ePoint.getX(), aPoint.getX(), epsilon, "" + i );
            assertEquals( ePoint.getY(), aPoint.getY(), epsilon, "" + i );
        });
    }

    @Test
    public void testGetParam()
    {
        String  pName   = "param";
        equation.setParam( pName );
        assertEquals( pName, equation.getParam() );
    }

    @Test
    public void testGetRadius()
    {
        String  pName   = "radius";
        equation.setRadiusName( pName );
        assertEquals( pName, equation.getRadiusName() );
    }

    @Test
    public void testGetTheta()
    {
        String  pName   = "theta";
        equation.setThetaName( pName );
        assertEquals( pName, equation.getThetaName() );
    }

    @Test
    public void testSetRange()
    {
        double  start   = -2;
        double  end     = 2;
        double  step    = .1;
        equation.setRange( start, end, step );
        assertEquals( start, equation.getRangeStart() );
        assertEquals( end, equation.getRangeEnd() );
        assertEquals( step, equation.getRangeStep() );
    }

    @Test
    public void testSetRangeStart()
    {
        double  val     = Math.PI;
        equation.setRangeStart( val );
        assertEquals( val, equation.getRangeStart() );
    }

    @Test
    public void testSetRangeEnd()
    {
        double  val     = Math.PI;
        equation.setRangeEnd( val );
        assertEquals( val, equation.getRangeEnd() );
    }

    @Test
    public void testSetRangeStep()
    {
        double  val     = Math.PI;
        equation.setRangeStep( val );
        assertEquals( val, equation.getRangeStep() );
    }

    @ParameterizedTest
    @ValueSource(strings={ "_", "a", "_Ab", "_99", "__a__b__1__0__" } )
    public void testIsValidNameTrue( String str )
    {
        assertTrue( equation.isValidName( str ), str );
    }

    @ParameterizedTest
    @ValueSource(strings={ "0_ab", "%", "$a", "" } )
    public void testIsValidNameFalse( String str )
    {
        assertFalse( equation.isValidName( str ), str );
    }
    
    // Trying to squeeze a little more coverage out of isValidName.
    @Test 
    public void testIsValidNameFalseMisc()
    {
        int[]   invalidCodes    =
            { '0' - 1, '9' + 1, 'A' - 1, 'Z' + 1, 'a' - 1, 'z' + 1 };
        Arrays.stream( invalidCodes )
            .mapToObj( i -> (char)i )
            .map( c -> "" + c )
            .forEach( s -> assertFalse( equation.isValidName( s ) ) );
        Arrays.stream( invalidCodes )
            .mapToObj( i -> (char)i )
            .map( c -> "a" + c )
            .forEach( s -> assertFalse( equation.isValidName( s ) ) );
    }

    @ParameterizedTest
    @ValueSource(strings={ "0", "0.1", "0.", "-.1", "-1.1", "pi", "cos(pi)" } )
    public void testIsValidValueTrue( String str )
    {
        assertTrue( equation.isValidValue( str ), str );
    }

    @ParameterizedTest
    @ValueSource(strings={ "A", "2X", "X^2", "cos(T)" } )
    public void testIsValidValueFalse( String str )
    {
        // These should all fail because they contain 
        // undeclared variables.
        assertFalse( equation.isValidValue( str ), str );
    }

    @Test
    public void testEvauatePass()
    {
        testEvaluatePass( "2", 2 );
        testEvaluatePass( "-.1", -.1 );
        testEvaluatePass( "2 * 3", 6 );
        testEvaluatePass( ".3^2", .09 );
        testEvaluatePass( "2pi", 2 * Math.PI );
        testEvaluatePass( "sin(pi/2)", 1 );
        testEvaluatePass( "ln(e)", 1 );
        testEvaluatePass( "log(10)", 1 );
    }

    @ParameterizedTest
    @ValueSource(strings={ "A", "2X", "X^2", "cos(T)" } )
    public void testEvaluateFail( String str )
    {
        Optional<Double>    optional    = equation.evaluate( str );
        assertFalse( optional.isPresent() );
    }
    
    private void testEvaluatePass( String expr, double expVal )
    {
        Optional<Double>    optional    = equation.evaluate( expr );
        assertTrue( optional.isPresent(), expr );
        assertEquals( expVal, optional.get(), .0001, expr );
    }
    
    private void validateDefaultXExpression()
    {
        equation.setRange( 1, 3, 1 );
        equation.xyPlot().forEach(
            p -> assertEquals( 1, p.getX(), "X" )
        );
    }
    
    private void validateDefaultYExpression()
    {
        equation.setRange( 1, 3, 1 );
        equation.yPlot().forEach(
            p -> assertEquals( 1, p.getY(), "Y" )
        );
    }
    
    private void validateDefaultRange()
    {
        double  start   = equation.getRangeStart();
        double  end     = equation.getRangeEnd();
        double  step    = equation.getRangeStep();
        assertTrue( start <= end );
        assertTrue( step > 0 );
    }
    
    private void validateDefaultVariables()
    {
        final String[]  defVars = { "a", "b", "c", "x", "y", "t" };
        Set<String> vars    = equation.getVars().keySet();
        for ( String  var : defVars )
            assertTrue( vars.contains( var ), var );
        assertEquals( "t", equation.getParam() );
    }
}
