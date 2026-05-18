package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import util.EquationTestUtil;

public class FileManagerTest
{
    private static final Class<NullPointerException>    NPE_CLASS   =
        NullPointerException.class;
    private static final String testFileID          = "CartesianPlaneTest";
    private static final String adHocOutputFileName = 
        testFileID + "_AdHocOutputFile1";
    private static final String binaryFileName      =
        testFileID + "_NotATextFile";
    private static final String noSuchFileName      =
        testFileID + "_NoSuchFile";
    /** 
     * See discussion of read-onlyness at 
     * {@linkplain #writeReadOnlyFile(File, List)}
     */
    private static final String readOnlyFileName      =
        testFileID + "_ReadOnlyFile";
        
    private static File     adHocOutputFile     = null;
    private static File     binaryFile          = null;
    private static File     noSuchFile          = null;
    private static File     readOnlyFile        = null;
        
    @BeforeAll
    public static void beforeAll() throws IOException
    {
        adHocOutputFile = createTempFile( adHocOutputFileName );
        binaryFile = createTempFile( binaryFileName );
        noSuchFile = createTempFile( noSuchFileName );
        readOnlyFile = createTempFile( readOnlyFileName );
        
        writeBinaryFile( binaryFile );
        writeReadOnlyFile( readOnlyFile, List.of( "line1" ) );
    }
    
    private static File createTempFile( String name ) throws IOException
    {
        File    file    = File.createTempFile( name, null );
        file.deleteOnExit();
        if ( file.exists() )
        {
            file.setWritable( true );
            file.delete();
        }
        return file;
    }
    
    @AfterAll
    public static void afterAll()
    {
        if ( binaryFile.exists() )
            binaryFile.delete();
        if ( adHocOutputFile.exists() )
            adHocOutputFile.delete();
        if ( noSuchFile.exists() )
            noSuchFile.delete();
        if ( readOnlyFile.exists() )
        {
            readOnlyFile.setWritable( true );
            readOnlyFile.delete();
        }
    }
        
    @BeforeEach
    public void setUp() throws Exception
    {
        if ( adHocOutputFile.exists() )
            adHocOutputFile.delete();
    }

    @Test
    public void testSaveNPE()
    {
        Equation    equation    = new Exp4jEquation();
        File        file        = new File( "temp" );
        assertThrows( NPE_CLASS, () -> 
            FileManager.save( null, equation )
        );
        assertThrows( NPE_CLASS, () -> 
            FileManager.save( file, null )
        );
    }

    @Test
    public void testSaveLoadDefault() throws IOException
    {
        // Save and reload an equation with default values.
        // Verify that the reloaded equation matches the original
        Equation    defEquation = new Exp4jEquation();
        FileManager.save( adHocOutputFile, defEquation );
        Equation    newEquation = new Exp4jEquation();
        Result      result      = 
            FileManager.load( adHocOutputFile, newEquation );
        assertTrue( result.isSuccess() );
        assertTrue( result.getMessages().isEmpty() );
        EquationTestUtil.verifyEquation( defEquation, newEquation );
    }   

    @Test
    public void testSaveLoadExpressions() throws IOException
    {
        // Save and reload an equation with modified expressions.
        // Verify that the reloaded equation matches the original
        Equation            defEquation = new Exp4jEquation();
        CommandProcessor    proc        = new CommandProcessor( defEquation );
        int                 baseExpr    = 100;
        for ( Command command : EquationTestUtil.EXPR_COMMANDS )
        {
            String          newExpr = String.valueOf( baseExpr++ );
            ParsedCommand   parsed  = 
                new ParsedCommand( command, "", newExpr );
            proc.processCommand( parsed );
        }

        FileManager.save( adHocOutputFile, defEquation );
        Equation    newEquation = new Exp4jEquation();
        Result      result      = 
            FileManager.load( adHocOutputFile, newEquation );
        assertTrue( result.isSuccess() );
        assertTrue( result.getMessages().isEmpty() );
        EquationTestUtil.verifyEquation( defEquation, newEquation );
    }

    @Test
    public void testSaveLoadRangeProperties() throws IOException
    {
        // Save and reload an equation with a modified range.
        // Verify that the reloaded equation matches the original
        Equation    defEquation = new Exp4jEquation();
        defEquation.setRange( 100, 200,10 );

        FileManager.save( adHocOutputFile, defEquation );
        Equation    newEquation = new Exp4jEquation();
        Result      result      = 
            FileManager.load( adHocOutputFile, newEquation );
        assertTrue( result.isSuccess() );
        assertTrue( result.getMessages().isEmpty() );
        EquationTestUtil.verifyEquation( defEquation, newEquation );
    }

    @Test
    public void testSaveLoadNames() throws IOException
    {
        // Save and reload an equation with modified names.
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

        FileManager.save( adHocOutputFile, defEquation );
        Equation    newEquation = new Exp4jEquation();
        Result      result      = 
            FileManager.load( adHocOutputFile, newEquation );
        assertTrue( result.isSuccess() );
        assertTrue( result.getMessages().isEmpty() );
        EquationTestUtil.verifyEquation( defEquation, newEquation );
    }

    @Test
    public void testSaveLoadVarMap() throws IOException
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
        FileManager.save( adHocOutputFile, defEquation );
        Equation    newEquation = new Exp4jEquation();
        Result      result      =
            FileManager.load( adHocOutputFile, newEquation );
        assertTrue( result.isSuccess() );
        assertTrue( result.getMessages().isEmpty() );
        EquationTestUtil.verifyEquation( defEquation, newEquation );
    }

    @Test
    public void testSaveToInvalidFile() throws IOException
    {
        // Try to save to a read-only file.
        Equation    defEquation = new Exp4jEquation();
        assertThrows( IOException.class, () -> 
            FileManager.save( readOnlyFile, defEquation )
        );
    }

    @Test
    public void testLoadFileNPE()
    {
        // Verify that FileManager.load... throws NPE when expected
        Equation    equation    = new Exp4jEquation();
        File        file        = new File( "temp" );
        assertThrows( NPE_CLASS, () -> 
            FileManager.load( (File)null, equation )
        );
        assertThrows( NPE_CLASS, () -> 
            FileManager.load( file, null )
        );
    }

    @Test
    public void testLoadFile() throws IOException
    {
        // Create an output file containing valid commands.
        // Load it, and verify it loaded correctly.
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
        Result      result      =
            FileManager.load( adHocOutputFile, equation );
        assertTrue( result.isSuccess() );
        assertTrue( result.getMessages().isEmpty() );
        assertEquals( expName, equation.getName() );
        assertEquals( expYExpr, equation.getYExpression() );
        assertEquals( expVarMap, equation.getVars() );
    }

    @Test
    public void testLoadFileWithBlanksComments() throws IOException
    {
        // Create an input file with valid commands and lots blank
        // lines and comments. Verify that it can be loaded.
        List<String>    lines           = new ArrayList<>();
        String          equationName    = "blank line tester";
        Equation        expEquation     = new Exp4jEquation();
        
        expEquation.setName( equationName );
        addPadding( lines, Command.EQUATION + " " + equationName );
        
        double      start           = 10;
        double      end             = 20;
        double      step            = 5;
        expEquation.setRange( start, end, step );
        addPadding( lines, Command.START + " " + start );
        addPadding( lines, Command.END + " " + end );
        addPadding( lines, Command.STEP + " " + step );
        
        String      varName         = "varName";
        double      varValue        = .5;
        expEquation.setVar( varName, varValue );
        addPadding( lines, Command.SET + " " + varName + "=" + varValue );
        writeFile( adHocOutputFile, lines );
        
        Equation    actEquation     = new Exp4jEquation();
        Result      result          = 
            FileManager.load( adHocOutputFile, actEquation );
        assertTrue( result.isSuccess() );
        assertTrue( result.getMessages().isEmpty() );
        EquationTestUtil.verifyEquation( expEquation, actEquation );
    }
    
    @Test
    public void testLoadFileValidEquationOnly() throws IOException
    {
        // Create an input file with a first line that is a valid 
        // EQUATION command, but all other lines are invalid;
        // All Command enum names will be valid.
        List<String>    lines           = new ArrayList<>();
        String          equationName    = "invalid args tester";
        Equation        expEquation     = new Exp4jEquation();
        
        expEquation.setName( equationName );
        lines.add( Command.EQUATION + " " + equationName );
        lines.add( Command.INVALID.toString() );
        lines.add( Command.REQUALS + " badVar + 5" );
        lines.add( Command.RADIUS + " %invalidName" );
        lines.add( Command.SET + " x=..35" );
        writeFile( adHocOutputFile, lines );
        
        Equation    actEquation     = new Exp4jEquation();
        Result      result          = 
            FileManager.load( adHocOutputFile, actEquation );
        assertFalse( result.isSuccess() );
        assertFalse( result.getMessages().isEmpty() );
        EquationTestUtil.verifyEquation( expEquation, actEquation );
    }
    
    @Test
    public void testLoadFileInvalidEnum() throws IOException
    {
        // Create an input file with a command that cannot
        // be converted to an enum. Valid commands in the file
        // should be processed as usual.
        List<String>    lines           = new ArrayList<>();
        Equation        expEquation     = new Exp4jEquation();
        String          varName         = "x";
        double          varValue        = .25;
        double          step            = .05;
        lines.add( Command.SET + " " + varName + "=" + varValue );
        expEquation.setVar( varName, varValue );
        lines.add( "NOT_AN_ENUM" );
        lines.add( Command.STEP + " " + step );
        expEquation.setRangeStep( step );
        writeFile( adHocOutputFile, lines );
        
        Equation    actEquation     = new Exp4jEquation();
        Result      result          = 
            FileManager.load( adHocOutputFile, actEquation );
        assertFalse( result.isSuccess() );
        assertFalse( result.getMessages().isEmpty() );
        EquationTestUtil.verifyEquation( expEquation, actEquation );
    }
    
    @Test
    public void testLoadFileAlternatingInvalid() throws IOException
    {
        // Create an input file with all alternating valid
        // and invalid commands
        List<String>    lines           = new ArrayList<>();
        Equation        expEquation     = new Exp4jEquation();
        String          equationName    = "Alternating";
        String          varName         = "z";
        double          varValue        = 31;
        String          radiusName      = "q";
        double          step            = .1;
        
        expEquation.setName( equationName );
        lines.add( Command.EQUATION + " " + equationName );
      
        lines.add( Command.INVALID.toString() );
        
        expEquation.setVar( varName, varValue );
        lines.add( Command.SET + " " + varName + "=" + varValue );
        
        lines.add( Command.PARAM + " $" );
        
        expEquation.setRadiusName( radiusName );
        lines.add( Command.RADIUS + " " + radiusName );
        
        lines.add( "NOT_AN_ENUM" );
        
        expEquation.setRangeStep( step );
        lines.add( Command.STEP + " " + step );
        
        writeFile( adHocOutputFile, lines );
        
        Equation    actEquation     = new Exp4jEquation();
        Result      result          = 
            FileManager.load( adHocOutputFile, actEquation );
        assertFalse( result.isSuccess() );
        assertFalse( result.getMessages().isEmpty() );
        EquationTestUtil.verifyEquation( expEquation, actEquation );
    }
    
    @Test 
    public void testLoadNotATextFile() throws IOException
    {
        // Try loading a file that's not text.
        Equation    expEquation     = new Exp4jEquation();
        Equation    actEquation     = new Exp4jEquation();
        Result      result          = 
            FileManager.load( binaryFile, actEquation );
        assertFalse( result.isSuccess() );
        assertFalse( result.getMessages().isEmpty() );
        EquationTestUtil.verifyEquation( expEquation, actEquation );
    }
    
    @Test 
    public void testLoadNoSuchFile() throws IOException
    {
        // Try loading a file that doesn't exist.
        Equation    expEquation     = new Exp4jEquation();
        Equation    actEquation     = new Exp4jEquation();
        assertThrows( IOException.class, () -> 
            FileManager.load( noSuchFile, actEquation )
        );
        EquationTestUtil.verifyEquation( expEquation, actEquation );
    }

    @Test
    public void testLoadBufferedReaderEquationNPE()
        throws IOException
    {
        Equation        equation    = new Exp4jEquation();
        try (
            BufferedReader  bReader     = 
                new BufferedReader( new StringReader( "" ) );
        )
        {
            assertThrows( NPE_CLASS, () -> 
                FileManager.load( (BufferedReader)null, equation )
            );
            assertThrows( NPE_CLASS, () -> 
                FileManager.load( bReader, null )
            );
        }
    }

    @Test
    public void testLoadBufferedReaderEquation() throws IOException
    {
        // This is one of only a few test methods that 
        // apply directly to Load(BufferedReader,Equation).
        // Everything else is verified through the Load(File,Equation)
        // test methods.
        
        // Save and reload an equation with default values.
        // Verify that the reloaded equation matches the original
        Equation    defEquation = new Exp4jEquation();
        FileManager.save( adHocOutputFile, defEquation );
        try( 
            FileReader fReader = new FileReader( adHocOutputFile );
            BufferedReader bReader = new BufferedReader( fReader );
        )
        {
            Equation    newEquation = new Exp4jEquation();
            Result      result      = 
                FileManager.load( bReader, newEquation );
            EquationTestUtil.verifyEquation( defEquation, newEquation );
            assertTrue( result.isSuccess() );
            assertTrue( result.getMessages().isEmpty() );
        }
    }

    @Test
    public void testLoadBufferedReaderEquationMisc() throws IOException
    {
        // This is one of only a few test methods that 
        // apply directly to Load(BufferedReader,Equation).
        // Everything else is verified through the Load(File,Equation)
        // test methods.
        
        // Test direct call to load(BufferedReader,Equation) that is
        // partly invalid.
        String              varName     = "var";
        double              varValue    = 5.1;
        String              toParse     = 
            "# comment\nINVALID\nSET " + varName + "=" + varValue;
        StringReader        sReader     = new StringReader( toParse );
        Equation            equation    = new Exp4jEquation();
        try ( BufferedReader bReader = new BufferedReader( sReader ) )
        {
            Result  result  = FileManager.load( bReader, equation );
            assertFalse( result.isSuccess() );
            assertFalse( result.getMessages().isEmpty() );
        }
        Optional<Double>    expVarValue = Optional.of( varValue );
        assertEquals( expVarValue, equation.getVar( varName ) );
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
    }
    
    /**
     * Create a file containing binary data.
     * The created file will be non-empty;
     * assumptions about the specific content should not be made.
     * 
     * @param file          file to create
     * @throws IOException  if an IO error occurs
     */
    private static void writeBinaryFile( File file ) throws IOException
    {
        try (
            FileOutputStream fStream = new FileOutputStream( file );
            DataOutputStream dStream = new DataOutputStream( fStream );
        )
        {
            for ( int inx = 1 ; inx < 101 ; ++inx )
                dStream.write( inx );
        }
    }
    
    /**
     * Write a list of strings to a given output file,
     * then make it read-only.
     *  
     * @param outFile       the given output file
     * @param list          the strings to write
     * @throws IOException  if an IO error occurs
     */
    private static void writeReadOnlyFile( File outFile, List<String> list )
        throws IOException
    {
        try (
            FileWriter  fWriter  = new FileWriter( outFile );
            PrintWriter pWriter = new PrintWriter( fWriter );
        )
        {
            list.stream().forEach( pWriter::println );
        }
        
        // Note: This means of making a file unwritable is problematic;
        // it may be sensitive to operating environment.
        outFile.setReadOnly();
    }
}
