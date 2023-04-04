package com.acmemail.judah;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import net.objecthunter.exp4j.ValidationResult;

class InputParserTest
{

    @BeforeEach
    public void setUp() throws Exception
    {
    }

    @Test
    public void testInputParser()
    {
        InputParser parser  = new InputParser();
        assertNotNull( parser.getEquation() );
    }

    @Test
    public void testInputParserEquation()
    {
        Equation    equation    = new Equation();
        InputParser parser      = new InputParser( equation );
        assertEquals( equation, parser.getEquation() );
    }

    @Test
    public void testParseInputEND()
    {
        InputParser         parser      = new InputParser();
        Equation            equation    = parser.getEquation();
        double              oldVal      = equation.getRangeEnd();
        double              newVal      = oldVal + 1;
        ValidationResult    result      = 
            parser.parseInput( Command.END, "" + newVal );
        assertTrue( result.isValid() );
        assertEquals( newVal, equation.getRangeEnd() );
        
        // test invalid input
        result = parser.parseInput( Command.END, "%invalidvalue%" );
        assertFalse( result.isValid() );
        
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
        InputParser         parser      = new InputParser();
        ValidationResult    result      = 
            parser.parseInput( Command.EXIT, "" );
        assertTrue( result.isValid() );
    }

    @Test
    public void testParseInputIncrement()
    {
        InputParser         parser      = new InputParser();
        Equation            equation    = parser.getEquation();
        double              oldVal      = equation.getRangeStep();
        double              newVal      = oldVal + 1;
        ValidationResult    result      = 
            parser.parseInput( Command.STEP, "" + newVal );
        assertTrue( result.isValid() );
        assertEquals( newVal, equation.getRangeStep() );
        
        // test invalid value
        result = parser.parseInput( Command.STEP, "invalidvalue" );
        assertFalse( result.isValid() );
        
        // test no-arg option
        testEmptyArg( parser, Command.STEP, "" + newVal );
    }

    @Test
    public void testParseInputINVALID()
    {
        InputParser         parser      = new InputParser();
        ValidationResult    result      = 
            parser.parseInput( Command.INVALID, "" );
        assertFalse( result.isValid() );
    }

    @Test
    public void testParseInputNONE()
    {
        InputParser         parser      = new InputParser();
        ValidationResult    result      = 
            parser.parseInput( Command.NONE, "" );
        assertTrue( result.isValid() );
    }

    @Test
    public void testParseInputPARAM()
    {
        InputParser         parser      = new InputParser();
        Equation            equation    = parser.getEquation();
        String              oldVal      = equation.getParam();
        String              newVal      = oldVal + "x";
        ValidationResult    result      = 
            parser.parseInput( Command.PARAM, newVal );
        assertTrue( result.isValid() );
        assertEquals( newVal, equation.getParam() );        
        
        // test invalid input
        result = parser.parseInput( Command.PARAM, "%invalidname%" );
        assertFalse( result.isValid() );
        
        // test no-arg option
        testEmptyArg( parser, Command.PARAM, "" + newVal );
    }

    @Test
    public void testParseInputSTART()
    {
        InputParser         parser      = new InputParser();
        Equation            equation    = parser.getEquation();
        double              oldVal      = equation.getRangeStart();
        double              newVal      = oldVal + 1;
        ValidationResult    result      = 
            parser.parseInput( Command.START, "" + newVal );
        assertTrue( result.isValid() );
        assertEquals( newVal, equation.getRangeStart() );        
        
        // test invalid value
        result = parser.parseInput( Command.START, "invalidvalue" );
        assertFalse( result.isValid() );
        
        // test no-arg option
        testEmptyArg( parser, Command.START, "" + newVal );
    }

    @Test
    public void testParseInputXY_STREAM()
    {
        InputParser         parser      = new InputParser();
        ValidationResult    result      = 
            parser.parseInput( Command.XY_STREAM, "" );
        assertTrue( result.isValid() );
    }
    
    @Test
    public void testParseInputX_EQUALS()
    {
        InputParser         parser      = new InputParser();
        Equation            equation    = parser.getEquation();
        String              oldVal      = equation.getXExpression();
        String              newVal      = oldVal + "*4";
        ValidationResult    result      = 
            parser.parseInput( Command.X_EQUALS, newVal );
        assertTrue( result.isValid() );
        assertEquals( newVal, equation.getXExpression() );        
        
        // test invalid value
        result = parser.parseInput( Command.X_EQUALS, "invalidexpression" );
        assertFalse( result.isValid() );
        
        // test no-arg option
        testEmptyArg( parser, Command.X_EQUALS, "" + newVal );
}
    
    @Test
    public void testParseInputY_EQUALS()
    {
        InputParser         parser      = new InputParser();
        Equation            equation    = parser.getEquation();
        String              oldVal      = equation.getYExpression();
        String              newVal      = oldVal + "*4";
        ValidationResult    result      = 
            parser.parseInput( Command.Y_EQUALS, newVal );
        assertTrue( result.isValid() );
        assertEquals( newVal, equation.getYExpression() );        
        
        // test invalid value
        result = parser.parseInput( Command.Y_EQUALS, "invalidexpression" );
        assertFalse( result.isValid() );
        
        // test no-arg option
        testEmptyArg( parser, Command.Y_EQUALS, "" + newVal );
    }

    @Test
    public void testParseInputY_STREAM()
    {
        InputParser         parser      = new InputParser();
        ValidationResult    result      = 
            parser.parseInput( Command.Y_STREAM, "" );
        assertTrue( result.isValid() );
    }

    @ParameterizedTest
    @ValueSource( strings= 
        { "", "p", "p,q", " a, b , c  ,  d  ",
          "abc", "def"
        })
    public void testParseVarsWithoutValues( String str )
    {
        InputParser         parser      = new InputParser();
        Equation            equation    = parser.getEquation();
        ValidationResult    result      = 
            parser.parseInput( Command.VARIABLES, str );
        assertTrue( result.isValid() );
        
        String[]    names   = str.split( "," );
        for ( String name : names )
        {
            // If its input is the empty string, split( "," )
            // returns a 1-element array consisting of the empty string.
            // Don't try to test that.
            String  vName   = name.trim();
            if ( !vName.isEmpty() )
            {
                Double  val = equation.getVar( vName );
                assertNotNull( val );
                assertEquals( 0, val );
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
        InputParser         parser      = new InputParser();
        Equation            equation    = parser.getEquation();
        ValidationResult    result      = 
            parser.parseInput( Command.VARIABLES, str );
        assertTrue( result.isValid() );
        
        String[]    specs   = str.split( "," );
        for ( String spec : specs )
        {
            String[]    pair    = spec.trim().split( "=" );
            // sanity check
            assertEquals( 2, pair.length );
            String      name    = pair[0].trim();
            String      value   = pair[1].trim();
            double      dValue  = Double.parseDouble( value );
            Double      actVal  = equation.getVar( name );
            assertNotNull( actVal );
            assertEquals( dValue, actVal );
        }
    }

    @ParameterizedTest
    @ValueSource( strings= 
        { "%=5", "5=5,6=6", " 5a = 5 , 6b = 6 , ^c  =  7  ,  ^d = 8  ",
          "abc% = 5 , de%f = 6 "
        })
    void testParseVarsWithBadNames( String str )
    {
        InputParser         parser      = new InputParser();
        ValidationResult    result      = 
            parser.parseInput( Command.VARIABLES, str );
        assertFalse( result.isValid() );
        List<String>        errors      = result.getErrors();
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
        InputParser         parser      = new InputParser();
        ValidationResult    result      = 
            parser.parseInput( Command.VARIABLES, str );
        assertFalse( result.isValid() );
        List<String>        errors      = result.getErrors();
        assertNotNull( errors );
        assertFalse( errors.isEmpty() );
        errors.forEach( System.out::println );
    }
    
    public void testParseVarsGoodAndBadSpecs()
    {
        String  strVals = "p=10,q=10,%,r=5 5";
        InputParser         parser      = new InputParser();
        ValidationResult    result      = 
            parser.parseInput( Command.VARIABLES, strVals );
        assertFalse( result.isValid() );
        List<String>        errors      = result.getErrors();
        assertNotNull( errors );
        assertFalse( errors.isEmpty() );
        errors.forEach( System.out::println );
        
        Equation    equation    = parser.getEquation();
        Double      pVal        = equation.getVar( "p" );
        Double      qVal        = equation.getVar( "q" );
        Double      rVal        = equation.getVar( "r" );
        
        // p and q should be stored...
        assertNotNull( pVal );
        assertEquals( 10, pVal );
        assertNotNull( qVal );
        assertEquals( 10, qVal );
        
        // ... but r should not
        assertNull( rVal );
    }
    
    @Test
    public void testIllegalArgumentException()
    {
        Class<IllegalArgumentException> clazz   =
            IllegalArgumentException.class;
        InputParser         parser      = new InputParser();
        
        assertThrows( clazz, () -> 
            parser.parseInput( Command.VARIABLES, null ) );
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
