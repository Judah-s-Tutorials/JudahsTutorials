package com.acmemail.judah.cartesian_plane.input;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.AWTException;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.test_utils.RobotAssistant;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

class EquationMapTest
{
    private static String[]     testVars        = { "p", "q", "r" };
    private static String[]     testNames       = 
        { "eName1", "eName2", "eName3", "eName4" };
    private static Equation[]   testEquations   = 
        new Equation[testNames.length];
    private static File[]       testFiles       = 
        new File[testEquations.length];
    
    private static File         tempDir         = Utils.getTempDir();
    
    private static String       eqStr1NoName    = "noName.txt";
    private static File         eqFile1NoName   = new File( eqStr1NoName );
    private static String       eqStr2NotEq     = "noName.txt";
    private static File         eqFile2NotEq    = new File( eqStr2NotEq );
    
    private static Map<String,Equation> testMap = new HashMap<>();
    
    // dialog selection, set by startDialog
    private Equation    selectedEquation;

    
    @BeforeAll
    public static void setUpBeforeClass() throws Exception
    {
        System.out.println( tempDir.getAbsolutePath() );
        for ( int inx = 0 ; inx < testNames.length ; ++inx  )
        {
            String      name        = testNames[inx];
            Equation    equation    = new Exp4jEquation();
            equation.setName( name );
            
            Arrays.stream( testVars )
                .forEach( v -> equation.setVar( v, v.charAt(0) ) );
            File    file    = Utils.getTempFile( name + ".txt" );
            FileManager.save( file, equation );
            testEquations[inx] = equation;
            testFiles[inx] = file;
            testMap.put( name, equation );
            
            // make a file that contains an Equation with no name;
            // should be recognized as "not an equation file"
            FileManager.save( eqFile1NoName, new Exp4jEquation() );
            
            // make a file that clearly does not contain an equation
            try ( PrintWriter pWriter = new PrintWriter( eqFile2NotEq ) )
            {
                pWriter.println( "not an equation file" );
            }  
            catch (IOException exc )
            {
                exc.printStackTrace();
                System.exit( 1 );
            }
        }
    }

    @AfterAll
    public static void tearDownAfterClass() throws Exception
    {
        Arrays.stream( testFiles )
            .filter( f -> f.exists() )
            .forEach( f -> f.delete() );
        if ( eqFile1NoName.exists() )
            eqFile1NoName.delete();
        if ( eqFile2NotEq.exists() )
            eqFile2NotEq.delete();
    }

    @AfterEach
    public void afterEach() throws Exception
    {
        EquationMap.init();
    }
    
    @Test
    public void testInit()
    {
        // make sure it's empty...
        // fill it up...
        // make sure it's non-empty
        // init it...
        // make sure it's empty again
        Map<String,Equation>    map = EquationMap.getEquationMap();
        assertTrue( map.isEmpty() );
        EquationMap.parseEquationFiles( tempDir );
        map = EquationMap.getEquationMap();
        assertFalse( map.isEmpty() );
        EquationMap.init();
        map = EquationMap.getEquationMap();
        assertTrue( map.isEmpty() );
    }

    @Test
    void testParseEquationFilesDir()
    {
        Map<String,Equation>    map = EquationMap.getEquationMap();
        assertTrue( map.isEmpty() );
        EquationMap.parseEquationFiles( tempDir );
        map = EquationMap.getEquationMap();
        
        Arrays.stream( testNames )
            .forEach( n -> verifyEquation( 
                testMap.get( n ), EquationMap.getEquation( n )
            ));
    }

    // Call parseEquationFiles passing a directory.
    // Verify that all equations are loaded.
    @Test
    void testParseEquationFilesFile()
    {
        EquationMap.parseEquationFiles( tempDir );
        Map<String,Equation>    map = EquationMap.getEquationMap();
        assertEquals( testFiles.length, map.size() );
    }

    // Call parseEquationFiles with no arguments.
    // This will cause a dialog to open; select a directory,
    // and verify that all equations are loaded.
    @Test
    void testParseEquationFilesSelectDirApprove() throws AWTException
    {
        Map<String,Equation>    map = EquationMap.getEquationMap();
        assertTrue( map.isEmpty() );
        
        RobotAssistant  robot   = new RobotAssistant();
        Thread          thread  = startDialog( () -> 
            EquationMap.parseEquationFiles()
        );
        robot.type( testFiles[0].getAbsolutePath(), KeyEvent.VK_ENTER );
        Utils.join( thread );
        map = EquationMap.getEquationMap();
        assertEquals( 1, map.size() );
        verifyEquation( testEquations[0], map.get( testNames[0] ) );
    }


    // Call parseEquationFiles with no arguments.
    // This will cause a dialog to open; select a file,
    // and verify that only the selected file loaded.
    @Test
    void testParseEquationFilesSelectFileApprove() throws AWTException
    {
        Map<String,Equation>    map = EquationMap.getEquationMap();
        assertTrue( map.isEmpty() );
        
        RobotAssistant  robot   = new RobotAssistant();
        Thread          thread  = startDialog( () -> 
            EquationMap.parseEquationFiles()
        );
        robot.type( tempDir.getAbsolutePath(), KeyEvent.VK_ENTER );
        Utils.join( thread );
        map = EquationMap.getEquationMap();
        
        Arrays.stream( testNames )
            .forEach( n -> verifyEquation( 
                testMap.get( n ), EquationMap.getEquation( n )
            ));
    }


    // Call parseEquationFiles with no arguments.
    // This will cause a dialog to open; cancel the dialog,
    // and verify that no equations are loaded.
    @Test
    void testParseEquationFilesSelectDirCancel() throws AWTException
    {
        RobotAssistant  robot   = new RobotAssistant();
        Thread          thread  = startDialog( () -> 
            EquationMap.parseEquationFiles()
        );
        robot.type( tempDir.getAbsolutePath(), KeyEvent.VK_ESCAPE );
        Utils.join( thread );
        Map<String,Equation>    map = EquationMap.getEquationMap();
        assertTrue( map.isEmpty() );
    }

    // Call parseEquationFile passing a file name as an argument.
    // Verify that just the given file is loaded.
    @Test
    void testParseEquationFile()
    {
        EquationMap.parseEquationFile( testFiles[0] );
        Map<String,Equation>    map = EquationMap.getEquationMap();
        assertEquals( 1, map.size() );
        
        Equation    expEquation = testEquations[0];
        String      testName    = testNames[0];
        Equation    actEquation = EquationMap.getEquation( testName );
        verifyEquation( expEquation, actEquation );
    }

    @Test
    void testIsEquationFile()
    {
        assertFalse( EquationMap.isEquationFile( eqFile1NoName ) );
        assertFalse( EquationMap.isEquationFile( eqFile2NotEq ) );
    }

    // Call getEquation passing an equation name as an argument.
    // Verify that the correct equation is returned.
    @Test
    void testGetEquationString()
    {
        EquationMap.parseEquationFiles( tempDir );
        Equation    expEquation     = testEquations[0];
        Equation    actEquation     = EquationMap.getEquation( testNames[0] );
        verifyEquation( expEquation, actEquation );
    }

    // Call getEquation passing no arguments.
    // This will cause a selection dialog to pop up;
    // select the third equation in the list.
    // Verify that the correct equation is returned.
    //
    // Note: EquationMap displays the selection dialog with 
    // equation names in alphabetical order. To figure out
    // which one is third, we have to make and sort our
    // own list of equation names.
    @Test
    void testGetEquationEnter() 
        throws AWTException
    {
        EquationMap.parseEquationFiles( tempDir );
        
        // Figure out which equation will be third in the list
        Map<String,Equation>    map     = EquationMap.getEquationMap();
        List<String>            names   = new ArrayList<>( map.keySet() );
        Collections.sort( names );
        String      name        = names.get( 2 );
        Equation    expEquation = map.get( name );
        
        Thread          thread  = null;
        RobotAssistant  robot   = new RobotAssistant();
        
        thread  = startDialog( () ->
            selectedEquation = EquationMap.getEquation() 
        );
        robot.downArrow(); // select second item in list
        robot.downArrow(); // select third item in list
        robot.type( "", KeyEvent.VK_ENTER );
        Utils.join( thread );
        verifyEquation( expEquation, selectedEquation );
    }

    // Call getEquation passing no arguments.
    // This will cause a selection dialog to pop up;
    // cancel the dialog and verify that null is returned.
    @Test
    void testGetEquationCancel() 
        throws AWTException
    {
        EquationMap.parseEquationFiles( tempDir );
        Thread          thread  = null;
        RobotAssistant  robot   = new RobotAssistant();
        
        thread  = startDialog( () ->
            selectedEquation = EquationMap.getEquation() 
        );
        robot.type( "", KeyEvent.VK_ESCAPE );
        Utils.join( thread );
        assertNull( selectedEquation );
    }

    @Test
    void testGetEquationMap()
    {
        EquationMap.parseEquationFiles( tempDir );
        Map<String,Equation>    map = EquationMap.getEquationMap();
        assertEquals( testEquations.length, map.size() );
    }
    
    /**
     * Verify that two given Equations encapsulate the same value.
     * 
     * @param expEquation   expected value
     * @param actEquation   actual value.
     */
    private void verifyEquation( Equation expEquation, Equation actEquation )
    {
        assertNotNull( actEquation );
        assertEquals( expEquation.getName(), actEquation.getName() );
        assertEquals( expEquation.getVars(), actEquation.getVars() );
    }
    
    /**
     * Start a new thread using a given Runnable.
     * The expectation is that the Runnable will cause
     * a dialog to open, 
     * which will then be manipulated
     * by the this thread.
     * 
     * @param runner    the given Runnable
     * 
     * @return  the new Thread
     */
    private Thread startDialog( Runnable runner )
    {
        Thread  thread  = new Thread( runner );
        thread.start();
        Utils.pause( 500 );
        return thread;
    }
}
