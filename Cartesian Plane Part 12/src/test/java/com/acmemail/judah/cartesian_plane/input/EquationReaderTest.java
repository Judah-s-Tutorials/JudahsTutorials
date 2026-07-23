package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.acmemail.judah.cartesian_plane.test_util.EquationTestUtil;

class EquationReaderTest
{
    private static final String     defName     = "this equation";
    private static final String     eqCommand   = "Equation " + defName;
    private static final Equation   defEquation = new Exp4jEquation();
    
    private static final List<Closeable>    closeables  = new ArrayList<>();
    
    @BeforeAll
    public static void beforeAll()
    {
        defEquation.setName( defName );
    }
    
    @AfterEach
    public void afterEach() throws IOException
    {
        for ( Closeable closeable : closeables )
            closeable.close();
        closeables.clear();
    }
    
    @Test
    public void testIntrinsicVariableDefaults()
    {
        List<String>    commands    = List.of( eqCommand );
        Equation        actEquation = loadGoRight( commands );
        assertEquals( defEquation.getVars(), actEquation.getVars() );
    }
    
    @ParameterizedTest
    @ValueSource( strings= {"=", " = "} )
    public void testIntrinsicVarOverrideOneSetEach( String equals )
    {
        List<String>    commands    = new ArrayList<String>();
        commands.add( eqCommand );
        Equation.INTRINSIC_VARIABLES.keySet().stream()
            .map( k -> "SET " + k + equals + k.hashCode() )
            .forEach( commands::add );
        testIntrinsicVarOverride( commands );
    }
    
    @ParameterizedTest
    @ValueSource( strings= {"=", " = "} )
    public void testIntrinsicVarOverrideOneSet( String equals )
    {
        String          separator   = 
            equals.charAt( 0 ) == ' ' ? " , " : ",";
        List<String>    allVars     = new ArrayList<>();
        Equation.INTRINSIC_VARIABLES.keySet().stream()
            .map( k -> k + equals + k.hashCode() )
            .forEach( allVars::add );
        String          setCommand  = 
            "SET " + String.join( separator, allVars );
        List<String>    commands    = List.of( eqCommand, setCommand );
        testIntrinsicVarOverride( commands );
    }
    
    @Test
    public void testMiscVars()
    {
        Equation        expEquation = new Exp4jEquation();
        List<String>    commands    = new ArrayList<String>();
        commands.add( eqCommand );
        for ( char name = 'a' ; name < 'l' ; ++name )
        {
            String  expName = "" + name + name + name;
            double  expVal  = expName.hashCode();
            String  line    = "SET " + expName + "=" + expVal;
            commands.add( line );
            expEquation.setVar( expName, expVal );
        }
        
        Equation        actEquation = loadGoRight( commands );
        assertEquals( expEquation.getVars(), actEquation.getVars() );
    }
    
    @Test
    public void testExpressionDefaults()
    {
        List<String>        commands    = List.of( eqCommand );
        Equation            actEquation = loadGoRight( commands );
        Map<Command,String> expExprMap  = 
            EquationTestUtil.getExprMap( defEquation );
        Map<Command,String> actExprMap  = 
            EquationTestUtil.getExprMap( actEquation );
        assertEquals( expExprMap, actExprMap );
    }
    
    @ParameterizedTest
    @ValueSource( strings= {"+3+", " + 3 + "} )
    public void testExpressionOverride( String modVal )
    {
        List<String>    commands    = new ArrayList<>();
        commands.add( eqCommand );
        
        Map<Command,String> expExpr = new HashMap<Command, String>();
        int                 base    = 100;
        for ( Command command : EquationTestUtil.EXPR_COMMANDS )
        {
            int     nextBase    = base++;
            // Depending on the value of modVal, the following will
            // produce something like "a + 3 + 100" or "a+3+100".
            String  nextExpr    = "a" + modVal + nextBase;
            String  nextCommand = command + " " + nextExpr;
            commands.add( nextCommand );
            expExpr.put( command, nextExpr );
        }
        
        Equation        actEquation = loadGoRight( commands );
        Map<Command,String> actExpr = 
            EquationTestUtil.getExprMap( actEquation );
        assertEquals( expExpr, actExpr );
    }
    
    @Test
    public void testRangeDefaults()
    {
        List<String>        commands    = List.of( eqCommand );
        Equation            actEquation = loadGoRight( commands );
        Map<Command,Double> expRangeMap = 
            EquationTestUtil.getRangeMap( defEquation );
        Map<Command,Double> actRangeMap = 
            EquationTestUtil.getRangeMap( actEquation );
        assertEquals( expRangeMap, actRangeMap );
    }
    
    @Test
    public void testRangeOverride()
    {
        double  expStart    = 211;
        double  expEnd      = expStart + 111;
        double  expStep     = 5;
        List<String>    commands    = 
            List.of( 
                eqCommand,
                Command.START + " " + expStart,
                Command.END + " " + expEnd,
                Command.STEP + " " + expStep
            );
        
        Equation        actEquation = loadGoRight( commands );
        assertEquals( expStart, actEquation.getRangeStart() );
        assertEquals( expEnd, actEquation.getRangeEnd() );
        assertEquals( expStep, actEquation.getRangeStep() );
    }
    
    @Test
    public void testNameDefaults()
    {
        List<String>        commands    = List.of( eqCommand );
        Equation            actEquation = loadGoRight( commands );
        Map<Command,String> expNameMap = 
            EquationTestUtil.getNameMap( defEquation );
        Map<Command,String> actNameMap = 
            EquationTestUtil.getNameMap( actEquation );
        assertEquals( expNameMap, actNameMap );
    }
    
    @Test
    public void testNameOverride()
    {
        Map<Command,String> expNames    = new HashMap<>();
        List<String>        commands    = new ArrayList<>();
        commands.add( eqCommand );
        for ( Command cmd : EquationTestUtil.NAME_COMMANDS )
        {
            String  expName = cmd + "___";
            String  line    = cmd + " " + expName;
            expNames.put( cmd, expName );
            commands.add( line );
        }
        
        Equation        actEquation = loadGoRight( commands );
        Map<Command,String> actNames    = 
            EquationTestUtil.getNameMap( actEquation );
        assertEquals( expNames, actNames );
    }
    
    @Test
    public void testEmpty()
    {
        List<String>    commands    = new ArrayList<>();
        Equation        expEquation = new Exp4jEquation();
        Equation        actEquation = loadGoRight( commands );
        EquationTestUtil.verifyEquation( expEquation, actEquation );
    }
    
    @Test
    public void testBlank()
    {
        List<String>    commands    = List.of( "   " );
        Equation        expEquation = new Exp4jEquation();
        Equation        actEquation = loadGoRight( commands );
        EquationTestUtil.verifyEquation( expEquation, actEquation );
    }
    
    @Test
    public void testComment()
    {
        List<String>    commands    = List.of( "#comment" );
        Equation        expEquation = new Exp4jEquation();
        Equation        actEquation = loadGoRight( commands );
        EquationTestUtil.verifyEquation( expEquation, actEquation );
    }
    
    @Test
    public void testNothingButErrors()
    {
        List<String>    commands    = List.of( "SETT X=5", "STEP 3.$" );
        Equation        expEquation = new Exp4jEquation();
        
        BufferedReader  reader      = getBufferedReader( commands );
        Equation        actEquation = new Exp4jEquation();
        Result          result      = 
            EquationReader.load( actEquation, reader );
        assertFalse( result.success() );
        assertTrue( result.messages().size() > 1 );

        EquationTestUtil.verifyEquation( expEquation, actEquation );
    }
    
    @Test
    public void testMingledErrors()
    {
        List<String>    commands    = 
            List.of( 
                "SETT X=5", 
                "set a=3",
                "STEP 3.$",
                "start 32"
            );
        Equation        expEquation = new Exp4jEquation();
        expEquation.setVar( "a", 3 );
        expEquation.setRangeStart( 32 );
        
        BufferedReader  reader      = getBufferedReader( commands );
        Equation        actEquation = new Exp4jEquation();
        Result          result      = 
            EquationReader.load( actEquation, reader );
        assertFalse( result.success() );
        assertTrue( result.messages().size() > 1 );

        EquationTestUtil.verifyEquation( expEquation, actEquation );
    }
    
    private static void testIntrinsicVarOverride( List<String> commands )
    {
        Equation            actEquation = loadGoRight( commands );
        Set<String>         expKeys     = defEquation.getVars().keySet();
        Map<String, Double> actMap      = actEquation.getVars();
        Set<String>         actKeys     = actMap.keySet();
        assertEquals( expKeys, actKeys );
        for ( String key : actKeys )
        {
            double  actVal  = actMap.get( key );
            assertEquals( actVal, key.hashCode() );
        }
    }
    
    private static void verify( Result result )
    {
        List<String>    diags   = result.messages();
        String          diag    = 
            diags.isEmpty() ? "no diagnostic" : diags.get( 0 );
        assertTrue( result.success(), diag );
    }

    private static Equation loadGoRight( List<String> commands )
    {
        BufferedReader  reader      = getBufferedReader( commands );
        Equation        actEquation = new Exp4jEquation();
        Result          result      = 
            EquationReader.load( actEquation, reader );
        verify( result );
        return actEquation;
    }
    
    private static BufferedReader getBufferedReader( List<String> list )
    {
        String          concat          = String.join( "\n", list );
        StringReader    stringReader    = new StringReader( concat );
        BufferedReader  reader          = new BufferedReader( stringReader );
        closeables.add( reader );
        
        return reader;
    }
}
