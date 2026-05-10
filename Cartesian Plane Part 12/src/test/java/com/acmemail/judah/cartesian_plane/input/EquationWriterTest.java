package com.acmemail.judah.cartesian_plane.input;

import static com.acmemail.judah.cartesian_plane.input.Command.END;
import static com.acmemail.judah.cartesian_plane.input.Command.EQUATION;
import static com.acmemail.judah.cartesian_plane.input.Command.PARAM;
import static com.acmemail.judah.cartesian_plane.input.Command.RADIUS;
import static com.acmemail.judah.cartesian_plane.input.Command.REQUALS;
import static com.acmemail.judah.cartesian_plane.input.Command.SET;
import static com.acmemail.judah.cartesian_plane.input.Command.START;
import static com.acmemail.judah.cartesian_plane.input.Command.STEP;
import static com.acmemail.judah.cartesian_plane.input.Command.TEQUALS;
import static com.acmemail.judah.cartesian_plane.input.Command.THETA;
import static com.acmemail.judah.cartesian_plane.input.Command.XEQUALS;
import static com.acmemail.judah.cartesian_plane.input.Command.YEQUALS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class EquationWriterTest
{
    /** List of Commands represent expressions. */
    private static final List<Command>  exprList    =
        List.of( XEQUALS, YEQUALS, REQUALS, TEQUALS );
    /** Hash of Commands represent expressions. */
    private static final Set<Command>   expressionCommands  =
        new HashSet<>( exprList );

    /** List of commands representing "special" variable names. */
    private static final List<Command>  nameList =
        List.of( PARAM, RADIUS, THETA );
    /** Hash of commands representing "special" variable names. */
    private static final Set<Command>   nameCommands =
        new HashSet<>( nameList );

    /** List of commands representing range properties. */
    private static final List<Command>  rangeList =
        List.of( START, END, STEP );
    /** Hash of commands representing range properties. */
    private static final Set<Command>   rangeCommands =
        new HashSet<>( rangeList );
    
    /** 
     * Intrinsic variable map, declared here for convenience.
     * @see Equation#INTRINSIC_VARIABLES
     */
    private static final Map<String,Double> intrinsicVars   =
        Equation.INTRINSIC_VARIABLES;
    
    private static final String equationName    = "This Equation";
        
    private ByteArrayOutputStream   outData;
    private PrintWriter             writer;
    private Equation                equation;
    
    @BeforeEach
    public void setUp() throws Exception
    {
        outData = new ByteArrayOutputStream();
        writer = new PrintWriter( outData );
        equation = new Exp4jEquation();
        equation.setName( equationName );
    }

    @AfterEach
    public void tearDown() throws Exception
    {
        writer.close();
        outData.close();
    }

    @Test
    public void testWriteThrowsNPE()
    {
        // Verify that the write method throws NPE for null arguments
        Class<NullPointerException> clazz   = NullPointerException.class;
        assertThrows( clazz, () -> EquationWriter.write( null, writer ) );
        assertThrows( clazz, () -> EquationWriter.write( equation, null ) );
    }
    
    @Test
    public void testEquationNameWritten()
    {
        List<String>    output  = getOutput( equation );
        String          actName = getEquationName( output );
        assertEquals( equationName, actName );
    }
    
    @Test
    public void testDefaultVariablesNotPresent()
    {
        List<String>        output  = getOutput( equation );
        Map<String,Double>  actMap  = getVarMap( output );
        assertTrue( actMap.isEmpty() );
    }
    
    @ParameterizedTest
    @MethodSource( "varNameSource" )
    public void testIntrinsicVariableOverride( String name )
    {
        Optional<Double>    optVar  = equation.getVar( name );
        // intrinsic variable must be present
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
        // should contain one statement, "Set name=newVar."
        assertEquals( 1, map.size(), name );
        assertTrue( map.containsKey( name ), name );
        assertEquals( newVal, map.get( name ), name );
    }
    
    @Test
    public void testAllIntrinsicVariablesOverride()
    {
        Map<String,Double>  expMap  = new HashMap<>();
        intrinsicVars.keySet().stream().forEach( s -> {
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
    public void testDefaultExpressionsNotPresent()
    {
        List<String>        output  = getOutput( equation );
        Map<Command,String> actMap  = getExprMap( output );
        assertTrue( actMap.isEmpty() );
    }
    
    @ParameterizedTest
    @MethodSource( "expressionCommandSource" )
    public void testExpressionOverride( Command command )
    {
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
        CommandProcessor    proc    = new CommandProcessor( equation );
        int                 exprVal = 10;
        Map<Command,String> expMap  = new HashMap<>();
        for ( Command cmd : expressionCommands )
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
    public void testDefaultNamesNotPresent()
    {
        List<String>        output  = getOutput( equation );
        Map<Command,String> actMap  = getNameMap( output );
        assertTrue( actMap.isEmpty() );
    }
    
    @Test
    public void testAllNamesOverride()
    {
        CommandProcessor    proc        = new CommandProcessor( equation );
        String              baseName    = "base";
        char                suffix      = 'A';
        Map<Command,String> expMap  = new HashMap<>();
        for ( Command cmd : nameCommands )
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
    
    @ParameterizedTest
    @MethodSource( "nameCommandSource" )
    public void testNameOverride( Command command )
    {
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
    public void testRoundTrip()
    {
        String  expEquationName = "rose";
        String  expVarName      = "a";
        double  expVarValue     = 2;
        String  expYExpression  = "a * x";
        double  expRangeStart   = 0;
        double  expRangeEnd     = 10;
        double  expRangeStep    = 1;
        
        equation.setName( expEquationName );
        equation.setVar( expVarName, expVarValue );
        equation.setYExpression( expYExpression );
        equation.setRange( expRangeStart, expRangeEnd, expRangeStep );
        // Generate output via EquationWriter; take the output,
        // and feed it back into a BufferedReader.
        BufferedReader      bufReader   = pipeInput( equation );

        Equation            reloaded    = new Exp4jEquation();
        CommandProcessor    proc        = new CommandProcessor( reloaded );
        CommandReader       cmdReader   = new CommandReader( bufReader );
        cmdReader.stream().forEach( proc::processCommand );

        assertEquals( expEquationName, reloaded.getName() );
        assertEquals( expYExpression,  reloaded.getYExpression() );
        Optional<Double>    actVarValue = reloaded.getVar( expVarName );
        assertEquals( Optional.of( expVarValue ), actVarValue );
        assertEquals( expRangeStart, reloaded.getRangeStart() );
        assertEquals( expRangeEnd, reloaded.getRangeEnd() );
        assertEquals( expRangeStep, reloaded.getRangeStep() );
    }
    
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
    
    private static Stream<String> varNameSource()
    {
        Stream<String>  stream  = intrinsicVars.keySet().stream();
        return stream;
    }
    
    private static Stream<Command> expressionCommandSource()
    {
        Stream<Command> stream  = expressionCommands.stream();
        return stream;
    }
    
    private static Stream<Command> nameCommandSource()
    {
        Stream<Command> stream  = nameCommands.stream();
        return stream;
    }
    
    private BufferedReader pipeInput( Equation equation )
    {
        EquationWriter.write( equation, writer );
        writer.close();
        
        String          output  = outData.toString();
        StringReader    sReader = new StringReader( output );
        BufferedReader  bReader = new BufferedReader( sReader );
        return bReader;
    }
    
    private List<String> getOutput( Equation equation )
    {
        EquationWriter.write( equation, writer );
        writer.close();
        String          output  = outData.toString();
        List<String>    lines   = output.lines().toList();
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
                .map( l -> l.split( "[\\s=]+", 3 ) )
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
     * producing a map of variable name -> value pairs
     * for each SET command.
     * 
     * @param lines the list of Command strings
     * 
     * @return  the generated map
     */
    private static Map<Command,String> getNameMap( List<String> lines )
    {
        Map<Command,String>  map = new HashMap<Command, String>();
        for ( String line : lines )
        {
            String[]    parts       = line.split( "\\s+", 2 );
            int         partsLen    = parts.length;
            assertTrue( partsLen > 0 );
            Command     command = Command.toCommand( parts[0] );
            if (  nameCommands.contains( command ) )
            {
                assertEquals( 2, partsLen );
                map.put( command, parts[1] );
            }
        }
        
        return map;
    }
    
    /**
     * Filter a list of strings,
     * each beginning with a Command 
     * optionally followed by an argument
     * producing a map of Command -> value pairs.
     * The map will consist only of commands
     * that are registered in the expressionCommands map.
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
            if ( expressionCommands.contains( command ) )
            {
                assertTrue( parts.length == 2 );
                map.put( command, parts[1] );
            }
        }
        
        return map;
    }
    
    /**
     * Filter a given list of lines for range commands
     * (START, END, STEP)
     * and generate a map of command-> value.
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
                if ( rangeCommands.contains( cmd ) )
                {
                    double  val = Double.parseDouble( pair[1] );
                    map.put( cmd, val );
                }
            }
        }
        return map;
    }
}
