package com.acmemail.judah;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.objecthunter.exp4j.ValidationResult;

class EquationTest
{
    private Equation    equation;
    
    @BeforeEach
    public void beforeEach()
    {
        equation = new Equation();
    }
    
    @Test
    void testEquation()
    {
        equation.setRange( 1, 1, 1 );
        
        equation.streamY().forEach(
            p -> {
                assertEquals( 1, p.getX(), "X" );
                assertEquals( 1, p.getY(), "Y" );
            }
        );
        
        equation.streamXY().forEach(
            p -> {
                assertEquals( 1, p.getX(), "X" );
                assertEquals( 1, p.getY(), "Y" );
            }
        );
    }

    @Test
    void testEquationString()
    {
        Equation    testEqu = new Equation( "2x" );
        testEqu.setRange( 2, 2, 1 );
        
        testEqu.streamY().forEach(
            p -> {
                assertEquals( 2, p.getX(), "X" );
                assertEquals( 4, p.getY(), "Y" );
            }
        );
    }

    @Test
    void testEquationMapOfStringDoubleString()
    {
        String              abcStr      = "abc";
        double              abcVal      = 5;
        String              defStr      = "def";
        double              defVal      = 10;
        Map<String,Double>  map         = new HashMap<>();
        map.put( abcStr, abcVal );
        map.put( defStr, defVal );
        Equation            testEqu     = new Equation( map, abcStr );

        assertEquals( abcVal, testEqu.getVar( abcStr ) );
        assertEquals( defVal, testEqu.getVar( defStr ) );
        
        testEqu.setRange( 2, 2, 1 );
        testEqu.streamY().forEach(
            p -> {
                assertEquals( 2, p.getX(), 2 );
                assertEquals( 4, p.getY(), abcVal );
            }
        );
    }

    @Test
    void testSetVar()
    {
        String      abcStr      = "abc";
        double      abcVal      = 5;
        String      defStr      = "def";
        double      defVal      = 10;;
        equation.setVar( abcStr, abcVal );
        equation.setVar( defStr, defVal );
        assertEquals( abcVal, equation.getVar( abcStr ) );
        assertEquals( defVal, equation.getVar( defStr ) );
    }

    @Test
    void testRemoveVar()
    {
        String      abcStr      = "abc";
        double      abcVal      = 5;
        
        equation.setVar( abcStr, abcVal );
        assertEquals( abcVal, equation.getVar( abcStr ) );
        
        equation.removeVar( abcStr );
        assertEquals( 0, equation.getVar( abcStr ) );
    }

    @Test
    void testSetXExpression()
    {
        // See also: testStreamXY
        String      xExpr       = "2x";
        testSetGetVal( 
            xExpr, 
            equation::setXExpression, 
            equation::getXExpression
        );
        
        ValidationResult    result  = equation.setXExpression( "2x" );
        assertTrue( result.isValid() );
        result  = equation.setXExpression( "%" );
        assertFalse( result.isValid() );
    }

    @Test
    void testSetYExpression()
    {
        // See also: testStreamXY
        String      yExpr       = "2x";
        testSetGetVal( 
            yExpr, 
            equation::setYExpression, 
            equation::getYExpression
        );
        
        ValidationResult    result  = equation.setYExpression( "2x" );
        assertTrue( result.isValid() );
        result  = equation.setYExpression( "%" );
        assertFalse( result.isValid() );
    }

    @Test
    void testStreamY()
    {
        String          yExpr       = "2x";
        List<Point2D>   expList     = 
            IntStream.range( 1, 5 )
                .mapToObj( i -> new Point2D.Double( i, 2 * i ) )
                .collect( Collectors.toList() );

        equation.setRange( 1, 4, 1 );        
        equation.setYExpression( yExpr );
        
        List<Point2D>   actList     =
            equation.streamY().collect( Collectors.toList() );
        assertEquals( expList, actList );
    }
    
    @Test
    public void testStreamYGoWrong()
    {
        try
        {
            equation.setYExpression( "3" );
            equation.streamY();
            fail( "expected exception not thrown" );
        }
        catch ( InvalidExpressionException exc )
        {
            ValidationResult    result  = exc.getValidationResult();
            assertNotNull( result );
            List<String>        errors  = result.getErrors();
            assertNotNull( errors );
            assertFalse( result.isValid() );
        }
    }

    @Test
    void testStreamXY()
    {
        String          yExpr       = "2t";
        String          xExpr       = "3t";
        List<Point2D>   expList     = 
            IntStream.range( 1, 5 )
                .mapToObj( i -> new Point2D.Double( 3 * i, 2 * i ) )
                .collect( Collectors.toList() );

        equation.setRange( 1, 4, 1 );        
        equation.setYExpression( yExpr );
        equation.setXExpression( xExpr );
        
        List<Point2D>   actList     =
            equation.streamXY().collect( Collectors.toList() );
        actList.forEach( System.out::println );
        assertEquals( expList, actList );
    }

    @Test
    void testGetParam()
    {
        testSetGetVal( "param", equation::setParam, equation::getParam );
    }

    @Test
    void testSetParam()
    {
        String          param       = "param";
        String          yExpr       = "2" + param;
        String          xExpr       = "3" + param;
        List<Point2D>   expList     = 
            IntStream.range( 1, 5 )
                .mapToObj( i -> new Point2D.Double( 3 * i, 2 * i ) )
                .collect( Collectors.toList() );

        equation.setRange( 1, 4, 1 );        
        equation.setVar( param, 0 );
        equation.setParam( param );
        equation.setYExpression( yExpr );
        equation.setXExpression( xExpr );
        
        List<Point2D>   actList     =
            equation.streamXY().collect( Collectors.toList() );
        actList.forEach( System.out::println );
        assertEquals( expList, actList );
    }
    
    @Test
    public void testParseFunction()
    {
        ValidationResult    result  = equation.parseFunction( "" );
        assertFalse( result.isValid() );
    }

    @Test
    void testSetRange()
    {
        double      start       = 10;
        double      end         = 2 * start;
        double      incr        = 2 * end;
        equation.setRange( start, end, incr );
        assertEquals( start, equation.getRangeStart() );
        assertEquals( end, equation.getRangeEnd() );
        assertEquals( incr, equation.getRangeStep() );
    }

    @Test
    void testSetRangeStart()
    {
        testSetGetVal( 5., equation::setRangeStart, equation::getRangeStart );
    }

    @Test
    void testSetRangeEnd()
    {
        testSetGetVal( 10., equation::setRangeEnd, equation::getRangeEnd );
    }

    @Test
    void testSetRangeStep()
    {
        testSetGetVal( 10., equation::setRangeStep, equation::getRangeStep );
    }

    @Test
    void testIsValidName()
    {
        String[]    validNames      =
        { "_", "a", "A", "_0", "a0", "A0", "a_0", "A_0", "abc_123" };
        String[]    invalidNames    = 
        { "0", "0a", "0_", "abc%", "abc$", "abc.0" };
        
        Arrays.stream( validNames )
        .forEach( n -> assertTrue( Equation.isValidName( n ), n ) );
        
        Arrays.stream( invalidNames )
        .forEach( n -> assertFalse( Equation.isValidName( n ), n ) );
    }

    @Test
    void testIsValidValue()
    {
        String[]    validValues     =
        { "0", "1", "0.", "1.1", ".1", ".9999999999", "999.999999" };
        String[]    invalidValues   =
        { ".", "..1", "1..", "1..0", "a", "1%", "pii", "ee" };
        
        Arrays.stream( validValues )
        .forEach( n -> assertTrue( Equation.isValidValue( n ), n ) );
        
        Arrays.stream( invalidValues )
        .forEach( n -> assertFalse( Equation.isValidValue( n ), n ) );
    }

    private static <T> void testSetGetVal( 
        T val, 
        Consumer<T> setter, 
        Supplier<T> getter
    )
    {
        setter.accept( val );
        assertEquals( val, getter.get() );
    }
}
