package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void setUp()
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
        assertEquals( "2x", equation.getYExpression() );
        
        // verify that y expression is set to 2 
        // (see above "new Exp4jEquation( "2x" )").
        equation.setRange( 1, 1, 1 );
        equation.yPlot().forEach(
            p -> assertEquals( 2, p.getY() ) 
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
            assertEquals( (double)var.charAt( 0 ), val );
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
    void testExp4jEquationNullMap()
    {
        equation = new Exp4jEquation( null, "2t" );
        validateDefaultVariables();
        validateDefaultRange();
        
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
            assertEquals( (double)var.charAt( 0 ), actVal );
        }
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
            .forEach( p -> assertEquals( xier, p.getX() ) );
        
        String[]	invalidExprs	= { "invalid", ")(", ";" };
        for ( String str : invalidExprs )
        {
        	// try setting an invalid expression
	        result  = equation.setXExpression( str );
	        assertFalse( result.isSuccess() );
	        assertFalse( result.getMessages().isEmpty() );
        }
    }

    @Test
    void testSetXExpressionGoWrong()
    {
        String  oldXExpr    = equation.getXExpression();
        String  xExpr       = "notAVar * x";
        Result  result      = equation.setXExpression( xExpr );
        assertFalse( result.isSuccess() );
        assertEquals( oldXExpr, equation.getXExpression() );
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
            .forEach( p -> assertEquals( xier, p.getY() ) );
        
        
        String[]	invalidExprs	= { "invalid", ")(", ";" };
        for ( String str : invalidExprs )
        {
        	// try setting an invalid expression
	        result  = equation.setYExpression( str );
	        assertFalse( result.isSuccess() );
	        assertFalse( result.getMessages().isEmpty() );
        }
    }

    @Test
    void testSetYExpressionGoWrong()
    {
        String  oldyExpr    = equation.getYExpression();
        String  yExpr       = "undeclaredVarName * x";
        Result  result      = equation.setYExpression( yExpr );
        assertFalse( result.isSuccess() );
        assertFalse( result.getMessages().isEmpty() );
        assertEquals( oldyExpr, equation.getYExpression() );
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
    void testYPlotGoWrong()
    {
        String  varName = "varName";
        String  yExpr   = varName + " + x";
        
        // set a variable on the equation...
        // enter a valid y expression including the variable...
        // remove the variable, causing y-expression to become invalid...
        // verify that yPlot throws a validation exception
        equation.setVar( varName, 0 );
        equation.setYExpression( yExpr );
        equation.removeVar( varName );
        
        Class<ValidationException>  clazz   = ValidationException.class;
        assertThrows( clazz, () -> equation.yPlot() );
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
    void testXYPlotGoWrong()
    {
        String  xVarName    = "xVarName";
        String  yVarName    = "yVarName";
        String  xExpr       = xVarName + " + t";
        String  yExpr       = yVarName + " + t";
        equation.setVar( xVarName, 0 );
        equation.setVar( yVarName, 0 );
        equation.setXExpression( xExpr );
        equation.setYExpression( yExpr );
        
        Class<ValidationException>  clazz   = ValidationException.class;
        
        // expect x-expression to throw an exception
        equation.removeVar( xVarName );
        assertThrows( clazz, () -> equation.xyPlot() );
        
        // expect y-expression to throw an exception
        equation.setVar( xVarName, 0 );
        equation.removeVar( yVarName );
        assertThrows( clazz, () -> equation.xyPlot() );
    }

    @Test
    void testGetParam()
    {
        String  pName   = "param";
        equation.setParam( pName );
        assertEquals( pName, equation.getParam() );
        assertTrue( equation.getVar( pName ).isPresent() );
    }

    @Test
    void testSetRange()
    {
    	// Some of these ranges are invalid. Nevertheless, setRange
    	// should silently accept them because range validation
    	// doesn't take place until plotting is executed.
    	Range[]	ranges	=
   		{
		    new Range( 1, 1, 0 ),
		    new Range( -1, -1, 0 ),
		    new Range( 1, 2, 1 ),
		    new Range( 2, 1, -1 ),
		    new Range( -1, 1, 1 ),
		    new Range( 1, -1, -1 ),
		    new Range( -2, -1, 1 ),
		    new Range( -1, -2, -1 ),
		    new Range( 1, 2, -1 ),
		    new Range( 2, 1, 1 ),
		    new Range( -1, 1, -1 ),
		    new Range( 1, -1, 1 ),
		    new Range( -2, -1, -1 ),
		    new Range( -1, -2, 1 ),
		};
    	
    	for ( Range range : ranges )
    	{
	    	range.set( equation );
	    	range.validate( equation );
    	}
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
    
    @Test
    public void testValidateRangeGoRight()
    {
    	Range[]	goRightRanges	=
		{
		    new Range( 1, 2, 1 ),
		    new Range( 2, 1, -1 ),
		    new Range( -1, 1, 1 ),
		    new Range( 1, -1, -1 ),
		    new Range( -2, -1, 1 ),
		    new Range( -1, -2, -1 ),
		};    	
    	equation.setXExpression( "4" );
    	equation.setYExpression( "5" );
    	
    	for ( Range range : goRightRanges )
    	{
	    	range.set( equation );
	    	assertDoesNotThrow( () -> equation.yPlot(), range.toString() );
	    	assertDoesNotThrow( () -> equation.xyPlot(), range.toString() );
    	}
    }
    
    @Test
    public void testValidateRangeGoWrong()
    {
    	Range[]	goRightRanges	=
		{
            new Range( 1, 1, 0 ),
            new Range( -1, -1, 0 ),
		    new Range( 1, 2, -1 ),
		    new Range( 2, 1, 1 ),
		    new Range( -1, 1, -1 ),
		    new Range( 1, -1, 1 ),
		    new Range( -2, -1, -1 ),
		    new Range( -1, -2, 1 ),
		};    	
    	equation.setXExpression( "4" );
    	equation.setYExpression( "5" );
    	Class<ValidationException>	clazz	= ValidationException.class;
    	
    	for ( Range range : goRightRanges )
    	{
	    	range.set( equation );
	    	assertThrows( clazz, () -> equation.yPlot(), range.toString() );
	    	assertThrows( clazz, () -> equation.xyPlot(), range.toString() );
    	}
    }

    @ParameterizedTest
    @ValueSource(strings={ "_", "a", "_Ab", "_99", "__a__b__1__0__" } )
    void testIsValidNameTrue( String str )
    {
        assertTrue( equation.isValidName( str ), str );
    }

    @ParameterizedTest
    @ValueSource(strings={ "0_ab", "%", "$a", "", "a-b" } )
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
    
    private static class Range
    {
    	private final double	start;
    	private final double	end;
    	private final double	step;
    	
		public Range(double start, double end, double step)
		{
			super();
			this.start = start;
			this.end = end;
			this.step = step;
		}
		
		public void set( Equation equation )
		{
			equation.setRange( start, end, step );
		}
		
		public void validate( Equation equation )
		{
	        assertEquals( start, equation.getRangeStart() );
	        assertEquals( end, equation.getRangeEnd() );
	        assertEquals( step, equation.getRangeStep() );
		}
		
		public String toString()
		{
			StringBuilder	bldr	= new StringBuilder();
			bldr.append( "start=" ).append( start ).append( "," )
				.append( "end=" ).append( end ).append( "," )
				.append( "step=" ).append( step );
			return bldr.toString();
		}
    }
}
