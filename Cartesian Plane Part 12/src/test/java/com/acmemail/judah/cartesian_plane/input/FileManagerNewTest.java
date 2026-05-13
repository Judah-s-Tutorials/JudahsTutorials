package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import util.EquationTestUtil;

public class FileManagerNewTest
{
    private static final Class<NullPointerException>    NPE_CLASS   =
        NullPointerException.class;
    private static final String testFileID          = "CartesianPlaneTest";
    private static final String testInputFileName1  = 
        testFileID + "_InputFile1";
    private static final String testOutputFileName1 = 
        testFileID + "_OutputFile1";
    private static final String adHocOutputFileName = 
        testFileID + "_AdHocOutputFile1";
    
    private static final List<String>   inputData1  = List.of(
        Command.EQUATION + " lissajous",
        Command.SET      + " a=2, b=3",
        Command.XEQUALS  + " 3 * cos(a * t)",
        Command.YEQUALS  + " 3 * sin(b * t)",
        Command.START    + " 0",
        Command.END      + " 2 * pi",
        Command.STEP     + " pi / 200"
    );
        
    private static File   testInputFile1            = null;
    private static File   testOutputFile1           = null;
    private static File   adHocOutputFile           = null;
        
    @BeforeAll
    public static void beforeAll() throws IOException
    {
        testInputFile1 = File.createTempFile( testInputFileName1, null );
        testOutputFile1 = File.createTempFile( testOutputFileName1, null );
        adHocOutputFile = File.createTempFile( adHocOutputFileName, null );
        if ( testOutputFile1.exists() )
            testOutputFile1.delete();
        
        writeFile( testInputFile1, inputData1 );
    }
    
    @AfterAll
    public static void afterAll()
    {
        if ( testInputFile1.exists() )
            testInputFile1.delete();
        if ( testOutputFile1.exists() )
            testOutputFile1.delete();
    }
        
    @BeforeEach
    public void setUp() throws Exception
    {
        if ( testOutputFile1.exists() )
            testOutputFile1.delete();
        if ( adHocOutputFile.exists() )
            adHocOutputFile.delete();
    }

    @AfterEach
    public void tearDown() throws Exception
    {
    }

    @Test
    public void testSaveNPE()
    {
        Equation    equation    = new Exp4jEquation();
        File        file        = new File( "temp" );
        assertThrows( NPE_CLASS, () -> 
            FileManagerNew.save( null, equation )
        );
        assertThrows( NPE_CLASS, () -> 
            FileManagerNew.save( file, null )
        );
    }

    @Test
    public void testSaveDefault() throws IOException
    {
        // Save and reload an equation with default values.
        // Verify that the reloaded equation matches the original
        Equation    defEquation = new Exp4jEquation();
        FileManagerNew.save( adHocOutputFile, defEquation );
        Equation    newEquation = new Exp4jEquation();
        FileManagerNew.load( adHocOutputFile, newEquation );
        EquationTestUtil.verifyEquation( defEquation, newEquation );
    }

    @Test
    public void testSaveExpressions() throws IOException
    {
        // Save and reload an equation with modified expressions.
        // Verify that the reloaded equation matches the original
        Equation    defEquation = new Exp4jEquation();
        CommandProcessor    proc        = new CommandProcessor( defEquation );
        int                 baseExpr    = 100;
        for ( Command command : EquationTestUtil.EXPR_COMMANDS )
        {
            String          newExpr = String.valueOf( baseExpr++ );
            ParsedCommand   parsed  = 
                new ParsedCommand( command, "", newExpr );
            proc.processCommand( parsed );
        }

        FileManagerNew.save( adHocOutputFile, defEquation );
        Equation    newEquation = new Exp4jEquation();
        FileManagerNew.load( adHocOutputFile, newEquation );
        EquationTestUtil.verifyEquation( defEquation, newEquation );
    }

    @Test
    public void testSaveRangeProperties() throws IOException
    {
        // Save and reload an equation with a modified range.
        // Verify that the reloaded equation matches the original
        Equation    defEquation = new Exp4jEquation();
        defEquation.setRange( 100, 200,10 );

        FileManagerNew.save( adHocOutputFile, defEquation );
        Equation    newEquation = new Exp4jEquation();
        FileManagerNew.load( adHocOutputFile, newEquation );
        EquationTestUtil.verifyEquation( defEquation, newEquation );
    }

    @Test
    public void testSaveNames() throws IOException
    {
        // Save and reload an equation with modified names.
        // Verify that the reloaded equation matches the original
        // Save and reload an equation with modified expressions.
        // Verify that the reloaded equation matches the original
        Equation            defEquation = new Exp4jEquation();
        CommandProcessor    proc        = new CommandProcessor( defEquation );
        char                baseSuffix  = 'a';
        for ( Command command : EquationTestUtil.NAME_COMMANDS )
        {
            String          newName = "x" + baseSuffix++;
            ParsedCommand   parsed  = 
                new ParsedCommand( command, "", newName );
            proc.processCommand( parsed );
        }

        FileManagerNew.save( adHocOutputFile, defEquation );
        Equation    newEquation = new Exp4jEquation();
        FileManagerNew.load( adHocOutputFile, newEquation );
        EquationTestUtil.verifyEquation( defEquation, newEquation );
    }

    @Test
    public void testSaveVarMap() throws IOException
    {
        // Save and reload an equation with a modified variable map.
        // Verify that the reloaded equation matches the original
        Equation    defEquation = new Exp4jEquation();
        int         baseVal     = 100;
        for ( String name : Equation.INTRINSIC_VARIABLES.keySet() )
        {
            String  newName = name + "a";
            defEquation.setVar( name, baseVal++ );
            defEquation.setVar( newName, baseVal++ );
        }
        FileManagerNew.save( adHocOutputFile, defEquation );
        Equation    newEquation = new Exp4jEquation();
        FileManagerNew.load( adHocOutputFile, newEquation );
        EquationTestUtil.verifyEquation( defEquation, newEquation );
    }

    @Test
    public void testLoadFileEquationNPE()
    {
        Equation    equation    = new Exp4jEquation();
        File        file        = new File( "temp" );
        assertThrows( NPE_CLASS, () -> 
            FileManagerNew.load( (File)null, equation )
        );
        assertThrows( NPE_CLASS, () -> 
            FileManagerNew.load( file, null )
        );
    }

    @Test
    public void testLoadFileEquation() throws IOException
    {
        Map<String,Double>  expVarMap   = new HashMap<>();
        expVarMap.putAll( Equation.INTRINSIC_VARIABLES );
        double              baseVal     = 100;
        StringBuilder       argBldr     = new StringBuilder();
        for ( char nameChar = 'a' ; nameChar < 'f' ; ++nameChar )
        {
            String  name    = "" + nameChar;
            double  val     = baseVal++;
            argBldr.append( name ).append( "=" ).append( val ).append( "," );
            expVarMap.put( name, val );
        }
        // strip the last comma from setArg
        int                 argLen      = argBldr.length() - 1;
        String              setArg      = argBldr.substring( 0, argLen ); 
        String              expYExpr    = "100";
        String              expName     = "this equation";
        List<String>        lines       = List.of(
            Command.EQUATION + " " + expName,
            Command.YEQUALS + " " + expYExpr,
            Command.SET + " " + setArg
        );
        writeFile( adHocOutputFile, lines );
        
        Equation    equation    = new Exp4jEquation();
        FileManagerNew.load( adHocOutputFile, equation );
        assertEquals( expName, equation.getName() );
        assertEquals( expYExpr, equation.getYExpression() );
        assertEquals( expVarMap, equation.getVars() );
    }

    @Test
    public void testBufferedReaderEquationNPE()
        throws IOException
    {
        Equation        equation    = new Exp4jEquation();
        try (
            BufferedReader  bReader     = 
                new BufferedReader( new StringReader( "" ) );
        )
        {
            assertThrows( NPE_CLASS, () -> 
                FileManagerNew.load( (BufferedReader)null, equation )
            );
            assertThrows( NPE_CLASS, () -> 
                FileManagerNew.load( bReader, null )
            );
        }
    }

    @Test
    public void testLoadBufferedReaderEquation()
    {
        fail("Not yet implemented");
    }
    
    /**
     * Add a string to a list, after adding empty and comment lines.
     * The purpose is create a list of lines
     * with blank lines and comments liberally interleaved.
     * 
     * @param lines the list to add to
     * @param line  the line to add
     */
    private static void addPadding( List<String> lines, String line )
    {
        lines.add( "     " );
        lines.add( "" );
        lines.add( "#this is a comment" );
        lines.add( line );
    }

    /**
     * Write a list of strings to a given output file.
     * If the file exists it will be replaced.
     * If the file does not exist it will be created.
     *  
     * @param outFile       the given output file
     * @param list          the strings to write
     * @throws IOException  if an IO error occurs
     */
    private static void writeFile( File outFile, List<String> list )
        throws IOException
    {
        try (
            FileWriter  fWriter  = new FileWriter( outFile );
            PrintWriter pWriter = new PrintWriter( fWriter );
        )
        {
            list.stream().forEach( pWriter::println );
        }
        
        try (
            FileReader fReader = new FileReader( outFile );
            BufferedReader bReader = new BufferedReader( fReader );
        )
        {
            bReader.lines().forEach( System.out::println );
        }
    }
    
    private static void verify( Equation expEquation, Equation actEquation )
    {
        
    }
}
