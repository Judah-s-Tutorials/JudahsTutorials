package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.acmemail.judah.cartesian_plane.test_util.EquationTestUtil;

public class FileManagerTest
{
    private static final String adHocOutputPathName = "AdHocFile";
    private static final String binaryPathName      = "NotATextFile";
    private static final String noSuchPathName      = "NoSuchFile";
    /** 
     * See discussion of read-onlyness at 
     * {@linkplain #writeReadOnlyFile(File, List)}
     */
    private static final String readOnlyPathName    = "ReadOnlyFile";

    /**  
     * Root reference for all file created in/for this test.
     * Automatically destroyed by JUnit on test completion.
     * @see #beforeAll()
     */
    @TempDir
    private static Path    tempRoot;

    private static Path     adHocOutputPath     = null;
    private static Path     binaryPath          = null;
    private static Path     noSuchPath          = null;
    private static Path     readOnlyPath        = null;
        
    @BeforeAll
    public static void beforeAll() throws IOException
    {
        adHocOutputPath = tempRoot.resolve( adHocOutputPathName );
        binaryPath =  tempRoot.resolve( binaryPathName );
        noSuchPath = tempRoot.resolve( noSuchPathName );
        readOnlyPath = tempRoot.resolve( readOnlyPathName );
        
        writeBinaryFile( binaryPath );
        writeReadOnlyFile( readOnlyPath, List.of( "line1" ) );
    }
        
    @BeforeEach
    public void beforeEach() throws IOException
    {
        Files.deleteIfExists( adHocOutputPath );
    }

    @Test
    public void testSaveNPE()
    {
        Equation    equation    = new Exp4jEquation();
        File        file        = new File( "temp" );
        assertThrows( NullPointerException.class, () -> 
            FileManager.save( null, equation )
        );
        assertThrows( NullPointerException.class, () -> 
            FileManager.save( file, null )
        );
    }

    @Test
    public void testSaveLoadDefault() throws IOException
    {
        // Save and reload an equation with default values.
        // Verify that the reloaded equation matches the original
        Equation    defEquation     = new Exp4jEquation();
        File        adHocOutputFile = adHocOutputPath.toFile();
        FileManager.save( adHocOutputFile, defEquation );
        Equation    newEquation = new Exp4jEquation();
        Result      result      = 
            FileManager.load( adHocOutputFile, newEquation );
        assertTrue( result.success() );
        assertTrue( result.messages().isEmpty() );
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

        File        adHocOutputFile = adHocOutputPath.toFile();
        FileManager.save( adHocOutputFile, defEquation );
        Equation    newEquation = new Exp4jEquation();
        Result      result      = 
            FileManager.load( adHocOutputFile, newEquation );
        assertTrue( result.success() );
        assertTrue( result.messages().isEmpty() );
        EquationTestUtil.verifyEquation( defEquation, newEquation );
    }

    @Test
    public void testSaveLoadRangeProperties() throws IOException
    {
        // Save and reload an equation with a modified range.
        // Verify that the reloaded equation matches the original
        Equation    defEquation = new Exp4jEquation();
        defEquation.setRange( 100, 200,10 );

        File        adHocOutputFile = adHocOutputPath.toFile();
        FileManager.save( adHocOutputFile, defEquation );
        Equation    newEquation = new Exp4jEquation();
        Result      result      = 
            FileManager.load( adHocOutputFile, newEquation );
        assertTrue( result.success() );
        assertTrue( result.messages().isEmpty() );
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

        File        adHocOutputFile = adHocOutputPath.toFile();
        FileManager.save( adHocOutputFile, defEquation );
        Equation    newEquation = new Exp4jEquation();
        Result      result      = 
            FileManager.load( adHocOutputFile, newEquation );
        assertTrue( result.success() );
        assertTrue( result.messages().isEmpty() );
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
        File        adHocOutputFile = adHocOutputPath.toFile();
        FileManager.save( adHocOutputFile, defEquation );
        Equation    newEquation = new Exp4jEquation();
        Result      result      =
            FileManager.load( adHocOutputFile, newEquation );
        assertTrue( result.success() );
        assertTrue( result.messages().isEmpty() );
        EquationTestUtil.verifyEquation( defEquation, newEquation );
    }

    @Test
    public void testSaveToInvalidFile()
    {
        // Try to save to a read-only file.
        Equation    defEquation = new Exp4jEquation();
        assertThrows( IOException.class, () -> 
            FileManager.save( readOnlyPath.toFile(), defEquation )
        );
    }

    @Test
    public void testLoadFileNPE()
    {
        // Verify that FileManager.load... throws NPE when expected
        Equation    equation    = new Exp4jEquation();
        File        file        = new File( "temp" );
        assertThrows( NullPointerException.class, () -> 
            FileManager.load( (File)null, equation )
        );
        assertThrows( NullPointerException.class, () -> 
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
        File        adHocOutputFile = adHocOutputPath.toFile();
        writeFile( adHocOutputFile, lines );
        
        Equation    equation    = new Exp4jEquation();
        Result      result      =
            FileManager.load( adHocOutputFile, equation );
        assertTrue( result.success() );
        assertTrue( result.messages().isEmpty() );
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
        File        adHocOutputFile = adHocOutputPath.toFile();
        writeFile( adHocOutputFile, lines );
        
        Equation    actEquation     = new Exp4jEquation();
        Result      result          = 
            FileManager.load( adHocOutputFile, actEquation );
        assertTrue( result.success() );
        assertTrue( result.messages().isEmpty() );
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
        File        adHocOutputFile = adHocOutputPath.toFile();
        writeFile( adHocOutputFile, lines );
        
        Equation    actEquation     = new Exp4jEquation();
        Result      result          = 
            FileManager.load( adHocOutputFile, actEquation );
        assertFalse( result.success() );
        assertFalse( result.messages().isEmpty() );
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
        File        adHocOutputFile = adHocOutputPath.toFile();
        writeFile( adHocOutputFile, lines );
        
        Equation    actEquation     = new Exp4jEquation();
        Result      result          = 
            FileManager.load( adHocOutputFile, actEquation );
        assertFalse( result.success() );
        assertFalse( result.messages().isEmpty() );
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
        
        File        adHocOutputFile = adHocOutputPath.toFile();
        writeFile( adHocOutputFile, lines );
        
        Equation    actEquation     = new Exp4jEquation();
        Result      result          = 
            FileManager.load( adHocOutputFile, actEquation );
        assertFalse( result.success() );
        assertFalse( result.messages().isEmpty() );
        EquationTestUtil.verifyEquation( expEquation, actEquation );
    }
    
    @Test 
    public void testLoadNotATextFile() throws IOException
    {
        // Try loading a file that's not text.
        Equation    expEquation     = new Exp4jEquation();
        Equation    actEquation     = new Exp4jEquation();
        Result      result          = 
            FileManager.load( binaryPath.toFile(), actEquation );
        assertFalse( result.success() );
        assertFalse( result.messages().isEmpty() );
        EquationTestUtil.verifyEquation( expEquation, actEquation );
    }
    
    @Test 
    public void testLoadNoSuchFile() throws IOException
    {
        // Try loading a file that doesn't exist.
        Equation    expEquation     = new Exp4jEquation();
        Equation    actEquation     = new Exp4jEquation();
        assertThrows( IOException.class, () -> 
            FileManager.load( noSuchPath.toFile(), actEquation )
        );
        EquationTestUtil.verifyEquation( expEquation, actEquation );
    }

//    @Test
//    public void testLoadBufferedReaderEquationMisc() throws IOException
//    {
//        // This is one of only a few test methods that 
//        // apply directly to Load(BufferedReader,Equation).
//        // Everything else is verified through the Load(File,Equation)
//        // test methods.
//        
//        // Test direct call to load(BufferedReader,Equation) that is
//        // partly invalid.
//        String              varName     = "var";
//        double              varValue    = 5.1;
//        String              toParse     = 
//            "# comment\nINVALID\nSET " + varName + "=" + varValue;
//        StringReader        sReader     = new StringReader( toParse );
//        Equation            equation    = new Exp4jEquation();
//        try ( BufferedReader bReader = new BufferedReader( sReader ) )
//        {
//            Result  result  = FileManager.load( bReader, equation );
//            assertFalse( result.success() );
//            assertFalse( result.messages().isEmpty() );
//        }
//        Optional<Double>    expVarValue = Optional.of( varValue );
//        assertEquals( expVarValue, equation.getVar( varName ) );
//    }    
    
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
            FileWriter  fWriter = 
                new FileWriter( outFile, StandardCharsets.UTF_8 );
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
    private static void writeBinaryFile( Path path ) throws IOException
    {
        File    file    = path.toFile();
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
    private static void writeReadOnlyFile( Path outPath, List<String> list )
        throws IOException
    {
        File    outFile = outPath.toFile();
        writeFile( outFile, list );

        // Note: This means of making a file unwritable is problematic;
        // it may be sensitive to operating environment.
        outFile.setReadOnly();
    }
}
