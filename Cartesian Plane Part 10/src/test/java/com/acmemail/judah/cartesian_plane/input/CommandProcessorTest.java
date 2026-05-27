package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Optional;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Execution(ExecutionMode.SAME_THREAD)
public class CommandProcessorTest
{
    private CommandProcessor proc;
    
    @BeforeEach
    public void beforeEach()
    {
        proc = new CommandProcessor();
    }
    
    @Test
    public void testCommandProcessor()
    {
        assertNotNull( proc.getEquation() );
    }

    @Test
    public void testCommandProcessorEquation()
    {
        Equation            equation    = new Exp4jEquation();
        CommandProcessor    processor   = new CommandProcessor( equation );
        assertSame( equation, processor.getEquation() );
        
        processor = new CommandProcessor( null );
        assertNotNull( processor.getEquation() );
    }
    
    @Test
    public void testParseExceptions()
    {
        ParsedCommand   command = getParsedCommand( Command.EQUATION );
        assertThrows( IllegalArgumentException.class, () -> 
            proc.processCommand( command )
        );
        assertThrows( NullPointerException.class, () -> 
            proc.processCommand( null )
        );
    }
    
    @Test
    public void testNewEquationWithoutName()
    {
        Equation            oldEquation = proc.getEquation();
        ParsedCommand       equationCmd = 
            getParsedCommand( Command.EQUATION );
        CommandProcessor    newProc     = proc.newEquation( equationCmd );
        Equation            newEquation = newProc.getEquation();
        assertNotNull( oldEquation );
        assertNotNull( newEquation );
        assertNotSame( oldEquation, newEquation );
        assertEquals( oldEquation.getClass(), newEquation.getClass() );
        assertTrue( newEquation.getName().isEmpty() );
    }
    
    @Test
    public void testNewEquationWithName()
    {
        Equation            oldEquation = proc.getEquation();
        String              newName     = "New Equation";
        ParsedCommand       equationCmd = 
            getParsedCommand( Command.EQUATION, newName );
        CommandProcessor    newProc     = proc.newEquation( equationCmd );
        Equation            newEquation = newProc.getEquation();
        assertNotNull( oldEquation );
        assertNotNull( newEquation );
        assertNotSame( oldEquation, newEquation );
        assertEquals( oldEquation.getClass(), newEquation.getClass() );
        assertEquals( newName, newEquation.getName() );
    }
    
    @Test
    public void testNewEquationExceptions()
    {
        ParsedCommand   command = getParsedCommand( Command.NONE );
        assertThrows( IllegalArgumentException.class, () -> 
            proc.newEquation( command )
        );
        assertThrows( NullPointerException.class, () -> 
            proc.newEquation( null )
        );
    }

    @ParameterizedTest
    @ValueSource( strings= 
        {"EXIT","NONE","YPLOT","XYPLOT","RPLOT","TPLOT","SELECT","OPEN","SAVE" } 
    )
    public void testProcessCommandNOOP( String strCommand )
    {
        Command         command = Command.toCommand( strCommand );
        ParsedCommand   parsed  = getParsedCommand( command );
        Result          result  = proc.processCommand( parsed );
        assertTrue( result.success() );
    }

    @Test
    public void testProcessCommandINVALID()
    {
        Command         command         = Command.INVALID;
        ParsedCommand   parsedCommand   = getParsedCommand( command );
        String          commandStr      = command.toString();
        Result          result          = proc.processCommand( parsedCommand );
        assertFalse( result.success() );
        List<String>    errors          = result.messages();
        assertNotNull( errors );
        assertFalse( errors.isEmpty() );
        assertTrue( errors.get( 0 ).startsWith( commandStr ) );
    }

    @Test
    public void testProcessCommandSTART()
    {
        Equation    equation    = proc.getEquation();
        testSetDouble( Command.START, equation::getRangeStart );
    }

    @Test
    public void testProcessCommandEND()
    {
        Equation    equation    = proc.getEquation();
        testSetDouble( Command.END, equation::getRangeEnd );
    }

    @Test
    public void testProcessCommandSTEP()
    {
        Equation    equation    = proc.getEquation();
        testSetDouble( Command.STEP, equation::getRangeStep );
    }

    @Test
    public void testProcessCommandPARAM()
    {
        Equation        equation    = proc.getEquation();
        String          newVal      = "newParamName";
        testSetString( Command.PARAM, newVal, equation::getParamName );
    }

    @Test
    public void testProcessCommandRADIUS()
    {
        Equation        equation    = proc.getEquation();
        String          newVal      = "newRadiusName";
        testSetString( Command.RADIUS, newVal, equation::getRadiusName );
    }

    @Test
    public void testProcessCommandTHETA()
    {
        Equation        equation    = proc.getEquation();
        String          newVal      = "newThetaName";
        testSetString( Command.THETA, newVal, equation::getThetaName );
    }

    @Test
    public void testProcessCommandXEQUALS()
    {
        Equation    equation    = proc.getEquation();
        String      oldVal      = equation.getXExpression();
        String      newVal      = oldVal + "*4";
        testSetString( Command.XEQUALS, newVal, equation::getXExpression );
    }
    
    @Test
    public void testProcessCommandYEQUALS()
    {
        Equation    equation    = proc.getEquation();
        String      oldVal      = equation.getYExpression();
        String      newVal      = oldVal + "*4";
        testSetString( Command.YEQUALS, newVal, equation::getYExpression );
    }
    
    @Test
    public void testProcessCommandREQUALS()
    {
        Equation        equation    = proc.getEquation();
        String          newVal      = "a + a + a";
        testSetString( Command.REQUALS, newVal, equation::getRExpression );
    }
    
    @Test
    public void testProcessCommandTEQUALS()
    {
        Equation        equation    = proc.getEquation();
        String          newVal      = "a + a + a";
        testSetString( Command.TEQUALS, newVal, equation::getTExpression );
    }

    @ParameterizedTest
    @ValueSource( strings= 
        { "", "p", "p,q", " a, b , c  ,  d  ",
          "abc", "def"
        })
    public void testParseVarsWithoutValues( String argStr )
    {
        ParsedCommand   command = getParsedCommand( Command.SET, argStr );
        Result          result  = proc.processCommand( command );
        assertTrue( result.success() );
        
        String[]        names   = argStr.split( "," );
        for ( String name : names )
        {
            // If its input is the empty string, split( "," )
            // returns a 1-element array consisting of the empty string.
            // Don't try to test that.
            String  vName   = name.trim();
            if ( !vName.isEmpty() )
            {
                Equation            equation    = proc.getEquation();
                Optional<Double>    val         = equation.getVar( vName );
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
        ParsedCommand   command = getParsedCommand( Command.SET, str );
        Result          result  = proc.processCommand( command );
        assertTrue( result.success() );
        
        String[]    specs   = str.split( "," );
        for ( String spec : specs )
        {
            Equation    equation    = proc.getEquation();
            String[]    pair        = spec.trim().split( "=", -1 );
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
    public void testParseVarsWithBadNames( String str )
    {
        ParsedCommand   parsedCommand   = 
            getParsedCommand( Command.SET, str );
        Result          result          = 
            proc.processCommand( parsedCommand );
        assertFalse( result.success() );
        List<String>        errors      = result.messages();
        assertNotNull( errors );
        assertFalse( errors.isEmpty() );
    }

    @ParameterizedTest
    @ValueSource( strings= 
        { "p=.", "p=.,q=%", " a = 5..0 , b = ..6 , c  =  %7  ,  d = 8$  ",
          "abc = 55.x , def = x 6, ghi = 5 5, jkl = 5 6"
        })
    public void testParseVarsWithBadValues( String argString )
    {
        ParsedCommand   parsedCommand   = 
            getParsedCommand( Command.SET, argString );
        Result          result          = 
            proc.processCommand( parsedCommand );
        assertFalse( result.success() );
        List<String>    errors          = result.messages();
        assertNotNull( errors );
        assertFalse( errors.isEmpty() );
    }
    
    @Test
    public void testNoCommandString()
    {
        // If we pass an invalid command in a ParsedCommand object
        // with no command string, does the error message correctly
        // reflect the stringified ParsedCommand.Command field?
        Command         invCommand      = Command.INVALID;
        String          invCommandStr   = invCommand.toString();
        ParsedCommand   parsedCommand   = 
            new ParsedCommand( invCommand, "", "" );
        Result          result          = 
            proc.processCommand( parsedCommand );
        assertFalse( result.success() );
        List<String>    errors          = result.messages();
        assertNotNull( errors );
        assertFalse( errors.isEmpty() );
        String          error           =
            errors.stream()
                .filter( s -> s.startsWith( invCommandStr ) )
                .findFirst()
                .orElse( null );
        assertNotNull( error );
    }
    
    @Test
    public void testParseVarsGoodAndBadSpecs()
    {
        // p and q are assigned from valid expressions;
        // s is not
        String          strVals         = "p=10,q=10,%,s=5 5";
        ParsedCommand   parsedCommand   = 
            getParsedCommand( Command.SET, strVals );
        Result          result          = 
            proc.processCommand( parsedCommand );
        assertFalse( result.success() );
        List<String>        errors      = result.messages();
        assertNotNull( errors );
        assertFalse( errors.isEmpty() );
        
        Equation            equation    = proc.getEquation();
        Optional<Double>    pVal        = equation.getVar( "p" );
        Optional<Double>    qVal        = equation.getVar( "q" );
        Optional<Double>    sVal        = equation.getVar( "s" );
        
        // p and q should be stored...
        assertTrue( pVal.isPresent() );
        assertEquals( 10, pVal.get() );
        assertTrue( qVal.isPresent() );
        assertEquals( 10, qVal.get() );
        
        // ... but s should not
        assertFalse( sVal.isPresent() );
        assertFalse( result.success() );
        assertTrue( result.messages().size() > 1 );
    }
    
    private void testSetDouble( Command cmd, DoubleSupplier getter  )
    {
        double          oldVal          = getter.getAsDouble();
        double          newVal          = oldVal + 1;
        String          newValStr       = String.valueOf( newVal );
        ParsedCommand   parsedCommand   = getParsedCommand( cmd, newValStr );
        Result          result          = 
            proc.processCommand( parsedCommand );
        assertTrue( result.success() );
        assertEquals( newVal, getter.getAsDouble() );        
        
        // test invalid value
        parsedCommand = getParsedCommand( cmd, "invalidvalue" );
        result = proc.processCommand( parsedCommand );
        assertFalse( result.success() );
        
        // test no-arg option
        testEmptyArg( cmd, newValStr );
    }
    
    private void testSetString( 
        Command          cmd,
        String           newVal, 
        Supplier<String> getter
    )
    {
        String          oldVal          = getter.get();
        assertNotEquals( oldVal, newVal );
        
        ParsedCommand   parsedCommand   = getParsedCommand( cmd, newVal );
        Result          result          = 
            proc.processCommand( parsedCommand );
        assertTrue( result.success() );
        assertEquals( newVal, getter.get() );        
        
        // test invalid input
        parsedCommand = getParsedCommand( cmd, "%invalid%" );
        result = proc.processCommand( parsedCommand );
        assertFalse( result.success() );
        
        List<String>    errors  = result.messages();
        assertNotNull( errors );
        assertFalse( errors.isEmpty() );
        
        // test no-arg option
        testEmptyArg( cmd, newVal );
    }
    
    private void 
    testEmptyArg( Command cmd, String expOutput )
    {
        String  actOutput   = getStdout( cmd, "" );
        assertEquals( expOutput, actOutput );
    }
    
    /**
     * Create a ParsedCommand 
     * from a given Command.
     * The command string for the parsed command
     * will be the stringified Command,
     * and the argument string will be the empty string.
     * 
     * @param command   the given Command
     * 
     * @return  the generated ParsedCommand
     */
    private static ParsedCommand getParsedCommand( Command command )
    {
        ParsedCommand   parsedCommand   = getParsedCommand( command, "" );
        return parsedCommand;
    }

    /**
     * Create a ParsedCommand 
     * from a given Command and argument string.
     * The command string for the parsed command
     * will be the stringified Command.
     * 
     * @param command   the given Command
     * @param argString the given argument string
     * 
     * @return  the generated ParsedCommand
     */
    private static ParsedCommand 
    getParsedCommand( Command command, String argString )
    {
        String          commandString   = command.toString();
        ParsedCommand   parsedCommand   = 
            new ParsedCommand( command, commandString, argString );
        return parsedCommand;
    }
    
    private String getStdout( Command cmd, String arg )
    {
        ByteArrayOutputStream   baoStream   = new ByteArrayOutputStream();
        PrintStream             printStream = new PrintStream( baoStream );
        PrintStream             stdOut      = System.out;
        try
        {
            System.setOut( printStream );
            ParsedCommand   parsedCommand   = getParsedCommand( cmd, arg );
            proc.processCommand( parsedCommand );
        }
        finally
        {
            System.setOut( stdOut );
        }
        
        String  str = baoStream.toString().trim();
        return str;
    }
}
