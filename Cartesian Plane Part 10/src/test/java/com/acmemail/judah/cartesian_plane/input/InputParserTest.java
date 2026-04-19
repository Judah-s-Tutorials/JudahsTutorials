package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class InputParserTest
{
    @Test
    public void testInputParser()
    {
        InputParser parser  = new InputParser();
        assertNotNull( parser.getEquation() );
    }

    @Test
    public void testInputParserEquation()
    {
        Equation    equation    = new Exp4jEquation();
        InputParser parser      = new InputParser( equation );
        assertEquals( equation, parser.getEquation() );
    }

    @Test
    public void testParseInputEND()
    {
        InputParser     parser      = new InputParser();
        Equation        equation    = parser.getEquation();
        double          oldVal      = equation.getRangeEnd();
        double          newVal      = oldVal + 1;
        Result          result      = 
            parser.parseInput( Command.END, "" + newVal );
        assertTrue( result.isSuccess() );
        assertEquals( newVal, equation.getRangeEnd() );
        
        // test invalid input
        result = parser.parseInput( Command.END, "%invalidvalue%" );
        assertFalse( result.isSuccess() );
        
        // test no-arg option
        testEmptyArg( parser, Command.END, "" + newVal );
    }

    @Test
    public void testParseInputEQUATION()
    {
        InputParser parser      = new InputParser();
        Equation    oldVal      = parser.getEquation();
        parser.parseInput( Command.EQUATION, "" );
        Equation    newVal      = parser.getEquation();
        assertNotNull( oldVal );
        assertNotNull( newVal );
        assertNotEquals( oldVal, newVal );        
    }

    @Test
    void testParseInputEXIT()
    {
        InputParser parser      = new InputParser();
        Result      result      = 
            parser.parseInput( Command.EXIT, "" );
        assertTrue( result.isSuccess() );
    }

    @Test
    public void testParseInputSTEP()
    {
        InputParser     parser      = new InputParser();
        Equation        equation    = parser.getEquation();
        double          oldVal      = equation.getRangeStep();
        double          newVal      = oldVal + 1;
        Result          result      = 
            parser.parseInput( Command.STEP, "" + newVal );
        assertTrue( result.isSuccess() );
        assertEquals( newVal, equation.getRangeStep() );
        
        // test invalid value
        result = parser.parseInput( Command.STEP, "invalidvalue" );
        assertFalse( result.isSuccess() );
        
        // test no-arg option
        testEmptyArg( parser, Command.STEP, "" + newVal );
    }

    @Test
    public void testParseInputINVALID()
    {
        InputParser parser      = new InputParser();
        Result      result      = 
            parser.parseInput( Command.INVALID, "" );
        assertFalse( result.isSuccess() );
    }

    @Test
    public void testParseInputNONE()
    {
        InputParser parser      = new InputParser();
        Result      result      = 
            parser.parseInput( Command.NONE, "" );
        assertTrue( result.isSuccess() );
    }

    @Test
    public void testParseInputPARAM()
    {
        InputParser     parser      = new InputParser();
        Equation        equation    = parser.getEquation();
        String          oldVal      = equation.getParam();
        String          newVal      = oldVal + "x";
        Result          result      = 
            parser.parseInput( Command.PARAM, newVal );
        assertTrue( result.isSuccess() );
        assertEquals( newVal, equation.getParam() );        
        
        // test invalid input
        result = parser.parseInput( Command.PARAM, "%invalidname%" );
        assertFalse( result.isSuccess() );
        
        // test no-arg option
        testEmptyArg( parser, Command.PARAM, "" + newVal );
    }

    @Test
    public void testParseInputSTART()
    {
        InputParser     parser      = new InputParser();
        Equation        equation    = parser.getEquation();
        double          oldVal      = equation.getRangeStart();
        double          newVal      = oldVal + 1;
        Result          result      = 
            parser.parseInput( Command.START, "" + newVal );
        assertTrue( result.isSuccess() );
        assertEquals( newVal, equation.getRangeStart() );        
        
        // test invalid value
        result = parser.parseInput( Command.START, "invalidvalue" );
        assertFalse( result.isSuccess() );
        
        // test no-arg option
        testEmptyArg( parser, Command.START, "" + newVal );
    }

    @Test
    public void testParseInputXYPLOT()
    {
        InputParser parser      = new InputParser();
        Result      result      = 
            parser.parseInput( Command.XYPLOT, "" );
        assertTrue( result.isSuccess() );
    }
    
    @Test
    public void testParseInputXEQUALS()
    {
        InputParser parser      = new InputParser();
        Equation    equation    = parser.getEquation();
        String      oldVal      = equation.getXExpression();
        String      newVal      = oldVal + "*4";
        Result      result      = 
            parser.parseInput( Command.XEQUALS, newVal );
        assertTrue( result.isSuccess() );
        assertEquals( newVal, equation.getXExpression() );        
        
        // test invalid value
        result = parser.parseInput( Command.XEQUALS, "invalidexpression" );
        assertFalse( result.isSuccess() );
        
        // test no-arg option
        testEmptyArg( parser, Command.XEQUALS, "" + newVal );
}
    
    @Test
    public void testParseInputYEQUALS()
    {
        InputParser parser      = new InputParser();
        Equation    equation    = parser.getEquation();
        String      oldVal      = equation.getYExpression();
        String      newVal      = oldVal + "*4";
        Result      result      = 
            parser.parseInput( Command.YEQUALS, newVal );
        assertTrue( result.isSuccess() );
        assertEquals( newVal, equation.getYExpression() );        
        
        // test invalid value
        result = parser.parseInput( Command.YEQUALS, "invalidexpression" );
        assertFalse( result.isSuccess() );
        
        // test no-arg option
        testEmptyArg( parser, Command.YEQUALS, "" + newVal );
    }

    @Test
    public void testParseInputYPLOT()
    {
        InputParser parser      = new InputParser();
        Result      result      = 
            parser.parseInput( Command.YPLOT, "" );
        assertTrue( result.isSuccess() );
    }

    @ParameterizedTest
    @ValueSource( strings= 
        { "", "p", "p,q", " a, b , c  ,  d  ",
          "abc", "def"
        })
    public void testParseVarsWithoutValues( String str )
    {
        InputParser parser      = new InputParser();
        Equation    equation    = parser.getEquation();
        Result      result      = 
            parser.parseInput( Command.SET, str );
        assertTrue( result.isSuccess() );
        
        String[]    names   = str.split( "," );
        for ( String name : names )
        {
            // If its input is the empty string, split( "," )
            // returns a 1-element array consisting of the empty string.
            // Don't try to test that.
            String  vName   = name.trim();
            if ( !vName.isEmpty() )
            {
                Optional<Double>    val = equation.getVar( vName );
                assertTrue( val.isPresent() );
                assertEquals( 0, val.get() );
            }
        }
    }

    @ParameterizedTest
    @ValueSource( strings= 
        { "p=5", "p=5,q=6", " a = 5 , b = 6 , c  =  7  ,  d = 8  ",
          "abc = 5 , def = 6 "
        })
    public void testParseVarsWithValues( String str )
    {
        InputParser parser      = new InputParser();
        Equation    equation    = parser.getEquation();
        Result      result      = 
            parser.parseInput( Command.SET, str );
        assertTrue( result.isSuccess() );
        
        String[]    specs   = str.split( "," );
        for ( String spec : specs )
        {
            String[]    pair    = spec.trim().split( "=" );
            // sanity check
            assertEquals( 2, pair.length );
            String              name    = pair[0].trim();
            String              value   = pair[1].trim();
            double              dValue  = Double.parseDouble( value );
            Optional<Double>    actVal  = equation.getVar( name );
            assertTrue( actVal.isPresent() );
            assertEquals( dValue, actVal.get() );
        }
    }

    @ParameterizedTest
    @ValueSource( strings= 
        { "%=5", "5=5,6=6", " 5a = 5 , 6b = 6 , ^c  =  7  ,  ^d = 8  ",
          "abc% = 5 , de%f = 6 "
        })
    void testParseVarsWithBadNames( String str )
    {
        InputParser parser      = new InputParser();
        Result      result      = 
            parser.parseInput( Command.SET, str );
        assertFalse( result.isSuccess() );
        List<String>        errors      = result.getMessages();
        assertNotNull( errors );
        assertFalse( errors.isEmpty() );
        errors.forEach( System.out::println );
    }

    @ParameterizedTest
    @ValueSource( strings= 
        { "p=.", "p=.,q=%", " a = 5..0 , b = ..6 , c  =  %7  ,  d = 8$  ",
          "abc = 55.x , def = x 6, ghi = 5 5 jkl = 5 6"
        })
    void testParseVarsWithBadValues( String str )
    {
        InputParser   parser      = new InputParser();
        Result        result      = 
            parser.parseInput( Command.SET, str );
        assertFalse( result.isSuccess() );
        List<String>        errors      = result.getMessages();
        assertNotNull( errors );
        assertFalse( errors.isEmpty() );
        errors.forEach( System.out::println );
    }
    
    public void testParseVarsGoodAndBadSpecs()
    {
        String      strVals = "p=10,q=10,%,r=5 5";
        InputParser parser  = new InputParser();
        Result      result  = 
            parser.parseInput( Command.SET, strVals );
        assertFalse( result.isSuccess() );
        List<String>        errors      = result.getMessages();
        assertNotNull( errors );
        assertFalse( errors.isEmpty() );
        errors.forEach( System.out::println );
        
        Equation            equation    = parser.getEquation();
        Optional<Double>    pVal        = equation.getVar( "p" );
        Optional<Double>    qVal        = equation.getVar( "q" );
        Optional<Double>    rVal        = equation.getVar( "r" );
        
        // p and q should be stored...
        assertTrue( pVal.isPresent() );
        assertEquals( 10, pVal.get() );
        assertTrue( qVal.isPresent() );
        assertEquals( 10, qVal.get() );
        
        // ... but r should not
        assertFalse( rVal.isPresent() );
    }
    
    @Test
    public void testIllegalArgumentException()
    {
        Class<IllegalArgumentException> clazz   =
            IllegalArgumentException.class;
        InputParser         parser      = new InputParser();
        
        assertThrows( clazz, () -> 
            parser.parseInput( Command.SET, null ) );
    }
    
    private void 
    testEmptyArg( InputParser parser, Command cmd, String expOutput )
    {
        String  actOutput   = getStdOutput( parser, cmd, "" );
        assertEquals( expOutput, actOutput );
    }
    
    private String getStdOutput( InputParser parser, Command cmd, String arg )
    {
        ByteArrayOutputStream   baoStream   = new ByteArrayOutputStream();
        PrintStream             printStream = new PrintStream( baoStream );
        PrintStream             stdOut      = System.out;
        System.setOut( printStream );
        
        parser.parseInput( cmd, arg );
        System.setOut( stdOut );
        
        String  str = baoStream.toString().trim();
        return str;
    }
}
