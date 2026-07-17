package com.acmemail.judah.cartesian_plane.input;

import static com.acmemail.judah.cartesian_plane.input.Command.END;
import static com.acmemail.judah.cartesian_plane.input.Command.EQUATION;
import static com.acmemail.judah.cartesian_plane.input.Command.REQUALS;
import static com.acmemail.judah.cartesian_plane.input.Command.SET;
import static com.acmemail.judah.cartesian_plane.input.Command.START;
import static com.acmemail.judah.cartesian_plane.input.Command.STEP;
import static com.acmemail.judah.cartesian_plane.input.Command.TEQUALS;
import static com.acmemail.judah.cartesian_plane.input.Command.XEQUALS;
import static com.acmemail.judah.cartesian_plane.input.Command.YEQUALS;
import static com.acmemail.judah.cartesian_plane.input.Equation.INTRINSIC_VARIABLES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.acmemail.judah.cartesian_plane.test_util.EquationTestUtil;

public class EquationWriterTest
{
    /** Name of the equation for use during testing. */
    private static final String equationName    = "This Equation";
    /** Equation for testing; initialized in the @BeforeEach method. */
    private Equation                equation;
    
    @BeforeEach
    public void setUp()
    {
        equation = new Exp4jEquation();
        equation.setName( equationName );
    }

    @Test
    public void testWriteThrowsNPE()
    {
        // Verify that the write method throws NPE for null arguments
        PrintWriter                 writer  = new PrintWriter( System.out );
        Class<NullPointerException> clazz   = NullPointerException.class;
        assertThrows( clazz, () -> EquationWriter.write( null, writer ) );
        assertThrows( clazz, () -> EquationWriter.write( equation, null ) );
    }
    
    @Test
    public void testEquationFirst()
    {
        // Verify that the first line containing a command
        // in the EquationWriter output contains the EQUATION command.
        List<String>    output  = getOutput( equation );
        // make sure we've got the first line containing a command
        String  first   =
            output.stream()
                // defensive: skip blank lines and comment lines in case
                // the writer is extended to emit them
                .map( String::trim )
                .filter( s -> !s.isEmpty() )
                .filter( s -> !s.startsWith( "#" ) )
                .findFirst().orElse( null );
        assertNotNull( first );
        assertTrue( first.startsWith( EQUATION.toString() ) );
    }
    
    @Test
    public void testEquationNameWritten()
    {
        // Verify that the equation name is written to
        // the EquationWriter output.
        List<String>    output  = getOutput( equation );
        String          actName = getEquationName( output );
        assertEquals( equationName, actName );
    }
    
    @Test
    public void testDefaultVariablesNotWritten()
    {
        // Verify that intrinsic variables with default values
        // are not written to the EquationWriter output.
        List<String>        output  = getOutput( equation );
        Map<String,Double>  actMap  = getVarMap( output );
        assertTrue( actMap.isEmpty() );
    }
    
    @ParameterizedTest
    @MethodSource( "varNameSource" )
    public void testIntrinsicVariableOverride( String name )
    {
        // Override the value of one of the intrinsic variables and
        // verify that it is written to the EquationWriter output.
        Optional<Double>    optVar  = equation.getVar( name );
        // sanity check; intrinsic variable must be present
        assertTrue( optVar.isPresent(), name );
        double  origVal = optVar.get();
        double  newVal  = origVal + 1;
        equation.setVar( name, newVal );
        
        // sanity check
        Optional<Double>    newOpt  = equation.getVar( name );
        assertTrue( newOpt.isPresent(), name );
        assertEquals( newVal, newOpt.get() );
        
        List<String>        lines   = getOutput( equation );
        Map<String,Double>  map     = getVarMap( lines );
        // We changed the value of 1 intrinsic variable; we set no
        // additional variables; the output from Equation.write should
        // contain one statement, "Set name=newVar."
        assertEquals( 1, map.size(), name );
        assertTrue( map.containsKey( name ), name );
        assertEquals( newVal, map.get( name ), name );
    }
    
    @Test
    public void testAllIntrinsicVariablesOverride()
    {
        // Override the values of the all of the intrinsic variables and
        // verify that they are written to the EquationWriter output.
        Map<String,Double>  expMap  = new HashMap<>();
        INTRINSIC_VARIABLES.keySet().stream().forEach( s -> {
            Optional<Double>    optVar  = equation.getVar( s );
            // intrinsic variable must be present
            assertTrue( optVar.isPresent(), s );
            double  origVal = optVar.get();
            double  newVal  = origVal + 1;
            equation.setVar( s, newVal );
            expMap.put( s, newVal );
        });
        
        List<String>        lines   = getOutput( equation );
        Map<String,Double>  actMap  = getVarMap( lines );
        assertEquals( expMap, actMap );
    }
    
    @Test
    public void testSetVariables()
    {
        // Verify that various variables added to the equation
        // variable map are written to the EquationWriter output.
        Map<String,Double>  expMap      = new HashMap<>();
        double              nextVal     = -100;
        String[]            testNames   =
        { "a", "abc", "u", "uvw", "x", "xyz" };
        for ( String name : testNames )
        {
            equation.setVar( name, nextVal );
            expMap.put( name, nextVal );
            ++nextVal;
        }
        
        List<String>        lines   = getOutput( equation );
        Map<String,Double>  actMap  = getVarMap( lines );
        assertEquals( expMap, actMap );
    }
    
    @Test
    public void testDefaultRangeValues()
    {
        // Verify that range property default values are written to the
        // EquationWriter output.
        Map<Command,Double> expMap  = Map.of(
            START, equation.getRangeStart(),
            END, equation.getRangeEnd(),
            STEP, equation.getRangeStep()
        );
        List<String>        output  = getOutput( equation );
        Map<Command,Double> actMap  = getRangeMap( output );
        assertEquals( expMap, actMap );
    }
    
    @Test
    public void testNewRangeValues()
    {
        // Verify that range properties with overridden values are 
        // written to the EquationWriter output.
        double              testStart   = 105;
        double              testEnd     = 125;
        double              testStep    = 5;
        Map<Command,Double> expMap  = Map.of(
            START, testStart,
            END, testEnd,
            STEP, testStep
        );
        equation.setRange( testStart, testEnd, testStep );
        List<String>        output  = getOutput( equation );
        Map<Command,Double> actMap  = getRangeMap( output );
        assertEquals( expMap, actMap );
    }
    
    @Test
    public void testDefaultExpressionsNotWritten()
    {
        // Verify that expression commands with default values
        // are not written to the EquationWriter output.
        List<String>        output  = getOutput( equation );
        Map<Command,String> actMap  = getExprMap( output );
        assertTrue( actMap.isEmpty() );
    }
    
    @ParameterizedTest
    @MethodSource( "expressionCommandSource" )
    public void testExpressionOverride( Command command )
    {
        // Verify that an expression command with an overridden value
        // is written to the EquationWriter output.
        CommandProcessor    proc    = new CommandProcessor( equation );
        String              newExpr = "1234";
        ParsedCommand       parsed  = 
            new ParsedCommand( command, "", newExpr );
        proc.processCommand( parsed );
        List<String>        output  = getOutput( equation );
        Map<Command,String> expMap  = Map.of( command, newExpr );
        Map<Command,String> actMap  = getExprMap( output );
        assertEquals( expMap, actMap, command.toString() );
    }
    
    @Test
    public void testAllExpressionsOverride()
    {
        // Verify that all expressions commands with overridden values
        // are written to the EquationWriter output. Use CommandProcessor
        // to control which setter to use with a given command.
        CommandProcessor    proc    = new CommandProcessor( equation );
        int                 exprVal = 10;
        Map<Command,String> expMap  = new HashMap<>();
        for ( Command cmd : EquationTestUtil.EXPR_COMMANDS )
        {
            String  exprStr = String.valueOf( exprVal++ );
            ParsedCommand       parsed  = 
                new ParsedCommand( cmd, "", exprStr );
            proc.processCommand( parsed );
            expMap.put( cmd, exprStr );
        }
        List<String>        output  = getOutput( equation );
        Map<Command,String> actMap  = getExprMap( output );
        assertEquals( expMap, actMap );
    }
    
    @Test
    public void testExpressionsWithSpaces()
    {
        // Verify that expressions containing spaces are
        // written to the EquationWriter output.
        final String    testXExpression = "3 * 4";
        final String    testYExpression = "5 * 6";
        final String    testTExpression = "7 * 8";
        final String    testRExpression = "9 * 10";
        Map<Command,String> expMap  = Map.of(
            XEQUALS, testXExpression,
            YEQUALS, testYExpression,
            TEQUALS, testTExpression,
            REQUALS, testRExpression
        );
        equation.setXExpression( testXExpression );
        equation.setYExpression( testYExpression );
        equation.setTExpression( testTExpression );
        equation.setRExpression( testRExpression );

        List<String>        output  = getOutput( equation );
        Map<Command,String> actMap  = getExprMap( output );
        assertEquals( expMap, actMap );
    }
    
    @Test
    public void testDefaultNamesNotWritten()
    {
        // Verify that special names (PARAM, etc.) with default values
        // are not written to the EquationWriter output.
        List<String>        output  = getOutput( equation );
        Map<Command,String> actMap  = getNameMap( output );
        assertTrue( actMap.isEmpty() );
    }
    
    @ParameterizedTest
    @MethodSource( "nameCommandSource" )
    public void testNameOverride( Command command )
    {
        // Verify that a special name (PARAM, etc.) with an overridden
        // value is written to the EquationWriter output.
        CommandProcessor    proc    = new CommandProcessor( equation );
        String              newName = "test";
        ParsedCommand       parsed  = new 
            ParsedCommand( command, "", newName );
        proc.processCommand( parsed );
        List<String>        output  = getOutput( equation );
        Map<Command,String> expMap  = Map.of( command, newName );
        Map<Command,String> actMap  = getNameMap( output );
        assertEquals( expMap, actMap, command.toString() );
    }
    
    @Test
    public void testAllNamesOverride()
    {
        // Verify that special names (PARAM, etc.) with overridden values
        // are written to the EquationWriter output. Use CommandProcessor 
        // to control which setter to use with a given command.
        CommandProcessor    proc        = new CommandProcessor( equation );
        String              baseName    = "base";
        char                suffix      = 'A';
        Map<Command,String> expMap  = new HashMap<>();
        for ( Command cmd : EquationTestUtil.NAME_COMMANDS )
        {
            String          newName = baseName + suffix++;
            ParsedCommand   parsed  = new ParsedCommand( cmd, "", newName );
            proc.processCommand( parsed );
            expMap.put( cmd, newName );
        }
        List<String>        output  = getOutput( equation );
        Map<Command,String> actMap  = getNameMap( output );
        assertEquals( expMap, actMap );
    }
    
    /**
     * Generate a stream of the intrinsic variable names
     * for use in a ParameterizedTest.
     * 
     * @return  the generated stream
     */
    private static Stream<String> varNameSource()
    {
        Stream<String>  stream  = INTRINSIC_VARIABLES.keySet().stream();
        return stream;
    }
    
    /**
     * Generate a stream of the expression commands (XEQUALS, etc.)
     * for use in a ParameterizedTest.
     * 
     * @return  the generated stream
     */
    private static Stream<Command> expressionCommandSource()
    {
        Stream<Command> stream  = 
            EquationTestUtil.EXPR_COMMANDS.stream();
        return stream;
    }
    
    /**
     * Generate a stream of the name commands (PARAM, etc.)
     * for use in a ParameterizedTest.
     * 
     * @return  the generated stream
     */
    private static Stream<Command> nameCommandSource()
    {
        Stream<Command> stream  = EquationTestUtil.NAME_COMMANDS.stream();
        return stream;
    }
    
    /**
     * Given the output from EquationWriter.write,
     * find the EQUATION line and parse the equation name.
     * If not found, the empty string is returned.
     * 
     * @param output    the given output
     * 
     * @return  The EQUATION name, or the empty string if not found
     */
    private static String getEquationName( List<String> output )
    {
        String  name    = "";
        String  line    = 
            output.stream()
                .filter( s -> s.startsWith( EQUATION.toString() ) )
                .findFirst().orElse( null );
        if ( line != null )
        {
            String[]    parts   = line.split( "\\s+", 2 );
            if ( parts.length > 1 )
                name = parts[1];
        }
        return name;
    }
    
    /**
     * Write the given equation,
     * capturing its output.
     * Transform the output into a list of individual lines
     * and return the list.
     * 
     * @param equation  the given equation
     * 
     * @return  a list of lines captured from EquationWriter.write
     */
    private List<String> getOutput( Equation equation )
    {
        List<String>    lines   = null;
        try ( ByteArrayOutputStream outData = new ByteArrayOutputStream();
            PrintWriter writer = new PrintWriter( outData );
        )
        {
            EquationWriter.write( equation, writer );
            writer.close();
            String  output  = outData.toString();
            lines = output.lines().toList();
        }
        catch ( IOException exc )
        {
            fail( "Unexpected I/O failure", exc );
        }
        return lines;
    }
    
    /**
     * Filter a list of strings,
     * each beginning with a Command 
     * optionally followed by an argument,
     * producing a map of variable name -> value pairs
     * for each SET command.
     * 
     * @param lines the list of Command strings
     * 
     * @return  the generated map
     */
    private static Map<String,Double> getVarMap( List<String> lines )
    {
        Map<String,Double>  map = 
            lines.stream()
                .filter( l -> l.startsWith( SET.toString() ) )
                // note: the following expression splits
                // SET a=1 into [SET][a][1]
                .map( l -> l.split( "[\\s=]+", 3 ) )
                .peek( a -> assertEquals( 3, a.length ) )
                .collect( 
                    Collectors.toMap( 
                        a -> a[1], 
                        a -> Double.parseDouble( a[2] )
                    )
                );
        
        return map;
    }
    
    /**
     * Filter a list of strings,
     * each beginning with a Command 
     * optionally followed by an argument,
     * producing a map of Command -> name pairs
     * for each of the special name commands (PARAM, etc.).
     * 
     * @param lines the list of Command strings
     * 
     * @return  the generated map
     */
    private static Map<Command,String> getNameMap( List<String> lines )
    {
        Map<Command,String>  map = new HashMap<>();
        for ( String line : lines )
        {
            String[]    parts       = line.split( "\\s+", 2 );
            Command     command = Command.toCommand( parts[0] );
            if (  EquationTestUtil.NAME_COMMANDS.contains( command ) )
            {
                assertEquals( 2, parts.length );
                map.put( command, parts[1] );
            }
        }
        
        return map;
    }

    /**
     * Filter a list of strings,
     * each beginning with a Command 
     * optionally followed by an argument,
     * producing a map of Command -> value pairs
     * for each expression command (YEQUALS, etc.).
     * 
     * @param lines the list of Command strings
     * 
     * @return  the generated map
     */
    private static Map<Command,String> getExprMap( List<String> lines )
    {
        Map<Command,String>  map = new HashMap<>();
        for ( String line : lines )
        {
            String[]    parts   = line.split( "\\s+", 2 );
            Command     command = Command.toCommand( parts[0] );
            if ( EquationTestUtil.EXPR_COMMANDS.contains( command ) )
            {
                assertEquals( 2, parts.length );
                map.put( command, parts[1] );
            }
        }
        
        return map;
    }

    /**
     * Filter a given list of lines for range commands
     * (START, END, STEP)
     * and generate a map of command-> value pairs.
     * 
     * @param lines the given list
     * 
     * @return  the generated map
     */
    private static Map<Command,Double> getRangeMap( List<String> lines )
    {
        Map<Command,Double>  map = new HashMap<>();
        for ( String line : lines )
        {
            String[]    pair    = line.split( "\\s+", 2 );
            if ( pair.length == 2 )
            {
                Command cmd = Command.toCommand( pair[0] );
                if ( EquationTestUtil.RANGE_COMMANDS.contains( cmd ) )
                {
                    double  val = Double.parseDouble( pair[1] );
                    map.put( cmd, val );
                }
            }
        }
        return map;
    }
}
