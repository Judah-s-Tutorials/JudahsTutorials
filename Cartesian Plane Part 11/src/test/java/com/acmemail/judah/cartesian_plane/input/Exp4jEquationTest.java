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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

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
    public void beforeEach()
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
    public void testSetGetVar()
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
        assertTrue( actVal.isPresent(), name );
        assertEquals( val, actVal.get(), name );
        
        equation.removeVar( name );
        actVal  = equation.getVar( name );
        assertFalse( actVal.isPresent(), name );
        
        String  invalidName = "3InvalidName";
        Result  result      = equation.setVar( invalidName, 0. );
        assertFalse( result.success(), invalidName );
        assertFalse( result.messages().isEmpty(), invalidName );
        
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
    public void testSetGetXExpression()
    {
        double  xier    = 2;
        String  expr    = xier + "t";
        testSetGetExpression(
            expr,
            equation::getXExpression,
            equation::setXExpression,
            () -> {
                equation.setRange( 1, 1, 1 );
                equation.xyPlot()
                    .forEach( p -> assertEquals( xier, p.getX() ) );
            }
        );
    }

    @Test
    public void testSetGetYExpression()
    {
        double  xier    = 2;
        String  expr    = xier + "x";
        testSetGetExpression(
            expr,
            equation::getYExpression,
            equation::setYExpression,
            () -> {
                equation.setRange( 1, 1, 1 );
                equation.yPlot()
                    .forEach( p -> assertEquals( xier, p.getY() ) );
            }
        );
    }

    @ParameterizedTest
    @ValueSource(strings={ "notAVar * x", "a=%", "a^_", "x +* 3" } )
    public void testSetExpressionGoWrong( String invExpr )
    {
        testSetExpressionGoWrong( 
            invExpr, equation::getYExpression, equation::setYExpression
        );
        testSetExpressionGoWrong(
            invExpr, equation::getXExpression, equation::setXExpression
        );
    }

    @Test
    public void testSetExpressionNull()
    {
        assertThrows( NPE_CLASS, () -> equation.setYExpression( null ) );
        assertThrows( NPE_CLASS, () -> equation.setXExpression( null ) );
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
        equation.setVar( varName, 0 );
        equation.setYExpression( yExpr );
        equation.setRange( 0, 10, -1 );
        
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
        equation.setRange( 0, 10, -1 );
        assertThrows( VE_CLASS, () -> equation.xyPlot() );
    }

    @Test
    public void testPlotInvalidRange()
    {
        assertDoesNotThrow( () -> equation.yPlot() );
        assertDoesNotThrow( () -> equation.xyPlot() );

        equation.setRange( 1, -1, 1 );
        assertThrows( VE_CLASS, () -> equation.yPlot() );
        assertThrows( VE_CLASS, () -> equation.xyPlot() );
    }

    @Test
    public void testSetGetEquationName()
    {
        String  str = "Equation Name";
        testSetGetString( 
            str, 
            equation::getName, 
            equation::setName
        );
    }

    @Test
    public void testSetGetParamName()
    {
        testSetGetString( 
            equation::getParamName, 
            equation::setParamName
        );
    }

    @Test
    public void testSetGetRangeProperties()
    {
        testSetGetVal( equation::getRangeEnd, equation::setRangeEnd );
        testSetGetVal( equation::getRangeStart, equation::setRangeStart );
        testSetGetVal( equation::getRangeStep, equation::setRangeStep );
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
        }
    }

    @ParameterizedTest
    @ValueSource(strings={ "_", "A", "a", "_Ab", "_99", "__a__b__1__0__" } )
    public void testValidateNameTrue( String str )
    {
        Result  result  = equation.validateName( str );
        assertTrue( result.success(), str );
    }

    @ParameterizedTest
    @ValueSource(strings={ "0_ab", "%", "$a", "", "a-b", "abc___%" } )
    public void testValidateNameFalse( String str )
    {
        Result  result  = equation.validateName( str );
        assertFalse( result.success() );
    }

    @ParameterizedTest
    @ValueSource(strings={ "0", ">b", "@b", "[b", "{b" } )
    public void testValidateNameFirstCharFalse( String str )
    {
        Result  result  = equation.validateName( str );
        assertFalse( result.success() );
    }

    @ParameterizedTest
    @ValueSource(strings={ "b>", "b@", "b[", "b{", "b^" } )
    public void testValidateNameSecondCharFalse( String str )
    {
        Result  result  = equation.validateName( str );
        assertFalse( result.success() );
    }

    @Test
    public void testValidateNameNullOrBlank()
    {
        Result  result  = equation.validateName( null );
        assertFalse( result.success(), "null" );
        result  = equation.validateName( "" );
        assertFalse( result.success(), "empty" );
        result  = equation.validateName( "   " );
        assertFalse( result.success(), "blank" );
    }

    @ParameterizedTest
    @ValueSource(strings={ "0", "0.1", "0.", "-.1", "-1.1", "pi", "cos(pi)" } )
    public void testValidateValueTrue( String str )
    {
        Result  result  = equation.validateValue( str );
        assertTrue( result.success(), str );
    }

    @ParameterizedTest
    @ValueSource(strings={ "n", "2o", "p^2", "cos(q)" } )
    public void testValidateValueFalse( String str )
    {
        // These should all fail because they contain 
        // undeclared variables.
        Result  result  = equation.validateValue( str );
        assertFalse( result.success(), str );
    }

    @Test
    public void testValidateValueFalse()
    {
        // These should all fail because the value strings
        // are null or empty they should not raise an exception.
        Result  result  = equation.validateValue( null );
        assertFalse( result.success(), "null" );
        result  = equation.validateValue( "" );
        assertFalse( result.success(), "empty string" );
        result  = equation.validateValue( "   " );
        assertFalse( result.success(), "blank string" );
    }

    @Test
    public void testValidateValueNull()
    {
        Result  result  = equation.validateValue( null );
        assertFalse( result.success(), "null" );
    }

    @Test
    public void testEvaluatePass()
    {
        testEvaluatePass( "2", 2 );
        testEvaluatePass( "-.1", -.1 );
        testEvaluatePass( "2 * 3", 6 );
        testEvaluatePass( ".3^2", .09 );
        testEvaluatePass( "2pi", 2 * Math.PI );
        testEvaluatePass( "sin(pi/2)", 1 );
        testEvaluatePass( "log(e)", 1 );
        
        equation.setVar( "a", 100 );
        testEvaluatePass( "sqrt(100)", 10 );
        testEvaluatePass( "sqrt(a)", 10 );

        equation.setVar( "a", 3 );
        equation.setVar( "b", 4 );
        testEvaluatePass( "sqrt( pow( a, 2 ) + pow( b, 2 ) )", 5 );
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

    private void testSetGetString( 
        Supplier<String> getter,
        Function<String, Result> setter
    )
    {
        final String    valStr = "ValidString";
        final String    invStr = "2InvalidString";
        testSetGetString( valStr, invStr, getter, setter );
    }

    private void testSetGetString( 
        String valStr,
        String invStr,
        Supplier<String> getter,
        Function<String, Result> setter
    )
    {
        testSetGetString( valStr, getter, setter );
        testSetStringGoWrong( invStr, getter, setter );
    }
    
    private void testSetGetString( 
        String str, 
        Supplier<String> getter,
        Function<String, Result> setter
    )
    {
        Result  result      = setter.apply( str );
        assertTrue( result.success() );
        assertEquals( str, getter.get() );
    }
    
    private void testSetStringGoWrong( 
        String invStr, 
        Supplier<String> getter,
        Function<String, Result> setter
    )
    {
        String  oldStr  = getter.get();
        Result  result  = setter.apply( invStr );
        assertFalse( result.success() );
        assertEquals( oldStr, getter.get() );
    }
    
    private void testSetGetVal( 
        Supplier<Double> getter,
        Consumer<Double> setter
    )
    {
        double  oldVal  = getter.get();
        double  newVal  = oldVal + 1;
        setter.accept( newVal );
        assertEquals( newVal, getter.get() );
    }
    
    private void testSetGetExpression( 
        String expr, 
        Supplier<String> getter,
        Function<String, Result> setter,
        Runnable validator
    )
    {
        Result  result  = setter.apply( expr );
        assertTrue( result.success() );
        assertTrue( result.messages().isEmpty() );
        validator.run();
    }
    
    private void testSetExpressionGoWrong( 
        String invExpr, 
        Supplier<String> getter,
        Function<String, Result> setter
    )
    {
        String  oldXExpr    = getter.get();
        Result  result      = setter.apply( invExpr );
        assertFalse( result.success() );
        assertEquals( oldXExpr, getter.get() );
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
        Set<String> defVars = Equation.INTRINSIC_VARIABLES.keySet();
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
