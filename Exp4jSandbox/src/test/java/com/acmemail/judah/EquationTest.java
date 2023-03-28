package com.acmemail.judah;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class EquationTest
{
    @Test
    void testEquation()
    {
        Equation    equation    = new Equation();
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
        Equation    equation    = new Equation( "2x" );
        equation.setRange( 2, 2, 1 );
        
        equation.streamY().forEach(
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
        Equation    equation    = new Equation( map, abcStr );

        assertEquals( abcVal, equation.getVar( abcStr ) );
        assertEquals( defVal, equation.getVar( defStr ) );
        
        equation.setRange( 2, 2, 1 );
        equation.streamY().forEach(
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
        Equation    equation    = new Equation();
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
        Equation    equation    = new Equation();
        
        equation.setVar( abcStr, abcVal );
        assertEquals( abcVal, equation.getVar( abcStr ) );
        
        equation.removeVar( abcStr );
        assertEquals( 0, equation.getVar( abcStr ) );
    }

    @Test
    void testParseFunction()
    {
        fail("Not yet implemented");
    }

    @Test
    void testSetXExpression()
    {
        fail("Not yet implemented");
    }

    @Test
    void testSetYExpression()
    {
        Equation    equation    = new Equation();
        String      yExpr       = "2x";
        equation.setRange( 1, 1, 1 );
        
        equation.setYExpression( yExpr );
        assertEquals( yExpr, equation.getYExpression() );
        
        equation.streamY().forEach(
            p -> {
                assertEquals( 1, p.getX(), "X" );
                assertEquals( 2, p.getY(), "Y" );
            }
        );
    }

    @Test
    void testStreamY()
    {
        fail("Not yet implemented");
    }

    @Test
    void testStreamXY()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetXExpression()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetYExpression()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetParam()
    {
        fail("Not yet implemented");
    }

    @Test
    void testSetParam()
    {
        fail("Not yet implemented");
    }

    @Test
    void testSetRange()
    {
        double      start       = 10;
        double      end         = 2 * start;
        double      incr        = 2 * end;
        Equation    equation    = new Equation();
        equation.setRange( start, end, incr );
        assertEquals( start, equation.getRangeStart() );
        assertEquals( end, equation.getRangeEnd() );
        assertEquals( incr, equation.getRangeStep() );
    }

    @Test
    void testSetRangeStart()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetRangeStart()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetRangeEnd()
    {
        fail("Not yet implemented");
    }

    @Test
    void testSetRangeEnd()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetRangeStep()
    {
        fail("Not yet implemented");
    }

    @Test
    void testSetRangeStep()
    {
        fail("Not yet implemented");
    }

    @Test
    void testIsValidName()
    {
        fail("Not yet implemented");
    }

    @Test
    void testIsValidValue()
    {
        fail("Not yet implemented");
    }

}
