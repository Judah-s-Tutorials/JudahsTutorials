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
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class Exp4jEquationTest
{
    private static final Class<NullPointerException>    NPE_CLASS   =
        NullPointerException.class;
    private static final Class<ValidationException>     VE_CLASS    =
        ValidationException.class;
    
    private Exp4jEquation   equation;
    
    @BeforeEach
    public void beforeEach() throws Exception
    {
        equation = new Exp4jEquation();
    }

    @Test
    public void testExp4jEquation()
    {
        validateDefaultVariables();
        validateDefaultRange();
        validateDefaultXExpression();
        validateDefaultYExpression();
    }

    @Test
    public void testExp4jEquationString()
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
    public void testExp4jEquationMapOfStringDoubleString()
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
    public void testExp4jEquationNullMap()
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
    public void testNewEquation()
    {
        equation = (Exp4jEquation)equation.newEquation();
        validateDefaultVariables();
        validateDefaultRange();
        validateDefaultXExpression();
        validateDefaultYExpression();
    }

    @Test
    public void testSetVar()
    {
        double  val     = 3.14;
        String  name    = "abc";
        equation.setVar( name, val );
        
        Optional<Double>    actVal  = equation.getVar( name );
        assertTrue( actVal.isPresent() );
        assertEquals( val, actVal.get() );
        
        assertThrows( NPE_CLASS, 
            () -> equation.setVar( null, 0 ), "setVar" );
        assertThrows( NPE_CLASS, 
            () -> equation.getVar( null ), "getVar" );
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
        
        assertThrows( NPE_CLASS, 
            () -> equation.removeVar( null ), "removeVar" 
        );

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
        
        String[]    invalidExprs    = { "invalid", ")(", ";" };
        for ( String str : invalidExprs )
        {
            // try setting an invalid expression
            result  = equation.setXExpression( str );
            assertFalse( result.isSuccess() );
            assertFalse( result.getMessages().isEmpty() );
        }
    }

    @ParameterizedTest
    @ValueSource(strings={ "notAVar * x", "a=%", "a^_", "x +* 3" } )
    public void testSetExpressionGoWrong( String invExpr)
    {
        testSetExpressionGoWrong( 
            invExpr, equation::getYExpression, equation::setYExpression
        );
        testSetExpressionGoWrong( 
            invExpr, equation::getXExpression, equation::setXExpression
        );
        testSetExpressionGoWrong( 
            invExpr, equation::getRExpression, equation::setRExpression
        );
        testSetExpressionGoWrong( 
            invExpr, equation::getTExpression, equation::setTExpression
        );
    }

    @Test
    public void testSetExpressionNull()
    {
        assertThrows( NPE_CLASS, () -> equation.setYExpression( null ) );
        assertThrows( NPE_CLASS, () -> equation.setXExpression( null ) );
        assertThrows( NPE_CLASS, () -> equation.setRExpression( null ) );
        assertThrows( NPE_CLASS, () -> equation.setTExpression( null ) );
    }
    
    private void testSetExpressionGoWrong( 
        String invExpr, 
        Supplier<String> getter,
        Function<String, Result> setter
    )
    {
        String  oldXExpr    = getter.get();
        Result  result      = setter.apply( invExpr );
        assertFalse( result.isSuccess() );
        assertEquals( oldXExpr, getter.get() );
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
            .forEach( p -> assertEquals( xier, p.getY() ) );
        
        
        String[]    invalidExprs    = { "invalid", ")(", ";" };
        for ( String str : invalidExprs )
        {
            // try setting an invalid expression
            result  = equation.setYExpression( str );
            assertFalse( result.isSuccess() );
            assertFalse( result.getMessages().isEmpty() );
        }
    }

    @Test
    public void testSetYExpressionGoWrong()
    {
        String  oldyExpr    = equation.getYExpression();
        String  yExpr       = "undeclaredVarName * x";
        Result  result      = equation.setYExpression( yExpr );
        assertFalse( result.isSuccess() );
        assertFalse( result.getMessages().isEmpty() );
        assertEquals( oldyExpr, equation.getYExpression() );
    }

    @Test
    public void testSetYExpressionNull()
    {
        assertThrows( NPE_CLASS, () -> equation.setYExpression( null ) );
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
    public void testSetRExpressionNull()
    {
        assertThrows( NPE_CLASS, () -> equation.setRExpression( null ) );
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
        result  = equation.setTExpression( "invalid" );
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
    public void testSetTExpressionNull()
    {
        assertThrows( NPE_CLASS, () -> equation.setTExpression( null ) );
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
    public void testYPlotGoWrong()
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
        
        assertThrows( VE_CLASS, () -> equation.yPlot() );
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
    public void testXYPlotGoWrong()
    {
        String  xVarName    = "xVarName";
        String  yVarName    = "yVarName";
        String  xExpr       = xVarName + " + t";
        String  yExpr       = yVarName + " + t";
        equation.setVar( xVarName, 0 );
        equation.setVar( yVarName, 0 );
        equation.setXExpression( xExpr );
        equation.setYExpression( yExpr );
        
        // expect x-expression to throw an exception
        equation.removeVar( xVarName );
        assertThrows( VE_CLASS, () -> equation.xyPlot() );
        
        // expect y-expression to throw an exception
        equation.setVar( xVarName, 0 );
        equation.removeVar( yVarName );
        assertThrows( VE_CLASS, () -> equation.xyPlot() );
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
    public void testRPlotGoWrong()
    {
        String  varName = "varName";
        String  rExpr   = varName + " + x";
        equation.setVar( varName, 0 );
        equation.setRExpression( rExpr );
        equation.removeVar( varName );
        
        assertThrows( VE_CLASS, () -> equation.rPlot() );
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
    public void testTPlotGoWrong()
    {
        String  varName = "varName";
        String  tExpr   = varName + " + x";
        equation.setVar( varName, 0 );
        equation.setTExpression( tExpr );
        equation.removeVar( varName );
        
        assertThrows( VE_CLASS, () -> equation.tPlot() );
    }
    
    @Test
    public void testPlotInvalidRange()
    {
        assertDoesNotThrow( () -> equation.yPlot() );
        assertDoesNotThrow( () -> equation.xyPlot() );
        assertDoesNotThrow( () -> equation.rPlot() );
        assertDoesNotThrow( () -> equation.tPlot() );
        
        equation.setRange( 1, -1, 1 );
        assertThrows( VE_CLASS, () -> equation.yPlot() );
        assertThrows( VE_CLASS, () -> equation.xyPlot() );
        assertThrows( VE_CLASS, () -> equation.rPlot() );
        assertThrows( VE_CLASS, () -> equation.tPlot() );
    }
    
    @Test
    public void testGetName()
    {
        String  tName   = "testName";
        equation.setName( tName );
        assertEquals( tName, equation.getName() );
    }

    @Test
    public void testGetParam()
    {
        String  pName   = "param";
        equation.setParamName( pName );
        assertEquals( pName, equation.getParamName() );
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
        // Some of these ranges are invalid. Nevertheless, setRange
        // should silently accept them because range validation
        // doesn't take place until plotting is executed.
        Range[]    ranges    =
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
    
    @Test
    public void testValidateRangeGoRight()
    {
        Range[]    goRightRanges    =
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
        Range[]    goWrongRanges    =
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
        Class<ValidationException>    clazz    = ValidationException.class;
        
        for ( Range range : goWrongRanges )
        {
            range.set( equation );
            assertThrows( clazz, () -> equation.yPlot(), range.toString() );
            assertThrows( clazz, () -> equation.xyPlot(), range.toString() );
            assertThrows( clazz, () -> equation.rPlot(), range.toString() );
            assertThrows( clazz, () -> equation.tPlot(), range.toString() );
        }
    }

    @ParameterizedTest
    @ValueSource(strings={ "_", "A", "a", "_Ab", "_99", "__a__b__1__0__" } )
    public void testValidateNameTrue( String str )
    {
        Result  result  = equation.validateName( str );
        assertTrue( result.isSuccess(), str );
    }

    @ParameterizedTest
    @ValueSource(strings={ "0_ab", "%", "$a", "", "a-b", "abc___%" } )
    public void testValidateNameFalse( String str )
    {
        Result  result  = equation.validateName( str );
        assertFalse( result.isSuccess() );
    }

    @ParameterizedTest
    @ValueSource(strings={ "0", ">b", "@b", "[b", "{b" } )
    public void testValidateNameFirstCharFalse( String str )
    {
        Result  result  = equation.validateName( str );
        assertFalse( result.isSuccess() );
    }

    @ParameterizedTest
    @ValueSource(strings={ "b>", "b@", "b[", "b{", "b^" } )
    public void testValidateNameSecondCharFalse( String str )
    {
        Result  result  = equation.validateName( str );
        assertFalse( result.isSuccess() );
    }

    @Test
    public void testValidateNameNull()
    {
        Result  result  = equation.validateName( null );
        assertFalse( result.isSuccess() );
    }

    @ParameterizedTest
    @ValueSource(strings={ "0", "0.1", "0.", "-.1", "-1.1", "pi", "cos(pi)" } )
    public void testValidateValueTrue( String str )
    {
        Result  result  = equation.validateValue( str );
        assertTrue( result.isSuccess(), str );
    }

    @ParameterizedTest
    @ValueSource(strings={ "a", "2x", "x^2", "cos(t)" } )
    public void testValidateValueFalse( String str )
    {
        // These should all fail because they contain 
        // undeclared variables.
        Result  result  = equation.validateValue( str );
        assertFalse( result.isSuccess(), str );
    }

    @Test
    public void testValidateValueNull()
    {
        Result  result  = equation.validateValue( null );
        assertFalse( result.isSuccess(), "null" );
    }

    @Test
    public void testGetConstantValuePass()
    {
        testEvaluatePass( "2", 2 );
        testEvaluatePass( "-.1", -.1 );
        testEvaluatePass( "2 * 3", 6 );
        testEvaluatePass( ".3^2", .09 );
        testEvaluatePass( "2pi", 2 * Math.PI );
        testEvaluatePass( "sin(pi/2)", 1 );
        testEvaluatePass( "log(e)", 1 );
    }

    @ParameterizedTest
    @ValueSource(strings={ "l", "2m", "l^2", "cos(m)" } )
    public void testEvaluateFail( String str )
    {
        Optional<Double>    optional    = equation.evaluate( str );
        assertFalse( optional.isPresent() );
    }

    @Test
    public void testEvaluateNull()
    {
        assertThrows( NPE_CLASS, () -> equation.evaluate( null ) );
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
        final String[]  defVars = { "a", "b", "c", "x", "y", "r", "t" };
        Set<String> vars    = equation.getVars().keySet();
        for ( String  var : defVars )
            assertTrue( vars.contains( var ), var );
        assertEquals( "t", equation.getParamName() );
    }
    
    private static class Range
    {
        private final double    start;
        private final double    end;
        private final double    step;
        
        public Range(double start, double end, double step)
        {
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
        
        @Override
        public String toString()
        {
            StringBuilder    bldr    = new StringBuilder();
            bldr.append( "start=" ).append( start ).append( "," )
                .append( "end=" ).append( end ).append( "," )
                .append( "step=" ).append( step );
            return bldr.toString();
        }
    }
}
