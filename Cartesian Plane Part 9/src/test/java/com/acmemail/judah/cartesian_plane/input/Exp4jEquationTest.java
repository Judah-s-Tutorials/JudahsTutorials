package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class Exp4jEquationTest
{
    private Exp4jEquation   equation;
    
    @BeforeEach
    void setUp() throws Exception
    {
        equation = new Exp4jEquation();
    }

    @Test
    void testExp4jEquation()
    {
        validateDefaultVariables();
        validateDefaultRange();
        validateDefaultXExpression();
        validateDefaultYExpression();
    }

    @Test
    void testExp4jEquationString()
    {
        equation = new Exp4jEquation( "2x" );
        validateDefaultVariables();
        validateDefaultRange();
        validateDefaultXExpression();
        
        // verify that y expression is set to 2 
        // (see above "new Exp4jEquation( "2x" )").
        equation.setRange( 1, 1, 1 );
        equation.yPlot().forEach(
            p -> assertEquals( 2, p.getY(), "Y" ) 
        );
    }

    @Test
    void testExp4jEquationMapOfStringDoubleString()
    {
        Map<String,Double>  mapIn   = new HashMap<>();
        String[]            vars    = { "h", "j", "k", "l" };
        for ( String str : vars )
            mapIn.put( str, (double)str.charAt( 0 ) );
        
        equation = new Exp4jEquation( mapIn, "2t" );
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
    void testNewEquation()
    {
        equation = (Exp4jEquation)equation.newEquation();
        validateDefaultVariables();
        validateDefaultRange();
        validateDefaultXExpression();
        validateDefaultYExpression();
    }

    @Test
    void testSetVar()
    {
        double  val     = 3.14;
        String  name    = "abc";
        equation.setVar( name, val );
        
        Optional<Double>    actVal  = equation.getVar( name );
        assertTrue( actVal.isPresent() );
        assertEquals( val, actVal.get() );
    }

    @Test
    void testRemoveVar()
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
    void testGetVars()
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
    void testParseFunction()
    {
//        fail("Not yet implemented");
    }

    @Test
    void testSetXExpression()
    {
        double  xier    = 2;
        String  xExpr   = xier + "t";
        Result  result  = equation.setXExpression( xExpr );
        assertTrue( result.isSuccess() );
        assertEquals( xExpr, equation.getXExpression() );
        
        equation.setRange( 1, 1, 1 );
        equation.xyPlot()
            .forEach( p -> assertEquals( p.getX(), xier ) );
        
        // try setting an invalid expression
        result  = equation.setXExpression( "invalid" );
        assertFalse( result.isSuccess() );
    }

    @Test
    void testSetYExpression()
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
    void testYPlot()
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
    void testXYPlot()
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
    void testGetParam()
    {
        String  pName   = "param";
        equation.setParam( pName );
        assertEquals( pName, equation.getParam() );
    }

    @Test
    void testSetRange()
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
    void testSetRangeStart()
    {
        double  val     = Math.PI;
        equation.setRangeStart( val );
        assertEquals( val, equation.getRangeStart() );
    }

    @Test
    void testSetRangeEnd()
    {
        double  val     = Math.PI;
        equation.setRangeEnd( val );
        assertEquals( val, equation.getRangeEnd() );
    }

    @Test
    void testSetRangeStep()
    {
        double  val     = Math.PI;
        equation.setRangeStep( val );
        assertEquals( val, equation.getRangeStep() );
    }

    @ParameterizedTest
    @ValueSource(strings={ "_", "a", "_Ab", "_99", "__a__b__1__0__" } )
    void testIsValidNameTrue( String str )
    {
        assertTrue( equation.isValidName( str ), str );
    }

    @ParameterizedTest
    @ValueSource(strings={ "0_ab", "%", "$a", "" } )
    void testIsValidNameFalse( String str )
    {
        assertFalse( equation.isValidName( str ), str );
    }

    @ParameterizedTest
    @ValueSource(strings={ "0", "0.1", "0.", "-.1", "-1.1", "pi", "cos(pi)" } )
    void testIsValidValueTrue( String str )
    {
        assertTrue( equation.isValidValue( str ), str );
    }

    @ParameterizedTest
    @ValueSource(strings={ "a", "2x", "x^2", "cos(t)" } )
    void testIsValidValueFalse( String str )
    {
        assertFalse( equation.isValidValue( str ), str );
    }

    @Test
    void testGetConstantValuePass()
    {
        testGetConstantValuePass( "2", 2 );
        testGetConstantValuePass( "-.1", -.1 );
        testGetConstantValuePass( "2 * 3", 6 );
        testGetConstantValuePass( ".3^2", .09 );
        testGetConstantValuePass( "2pi", 2 * Math.PI );
        testGetConstantValuePass( "sin(pi/2)", 1 );
        testGetConstantValuePass( "log(e)", 1 );
    }
    
    private void testGetConstantValuePass( String expr, double expVal )
    {
        Optional<Double>    optional    = equation.evaluate( expr );
        assertTrue( optional.isPresent(), expr );
        assertEquals( expVal, optional.get(), .0001, expr );
    }

    @ParameterizedTest
    @ValueSource(strings={ "a", "2x", "x^2", "cos(t)" } )
    void testGetConstantValueFail( String str )
    {
        Optional<Double>    optional    = equation.evaluate( str );
        assertFalse( optional.isPresent() );
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
