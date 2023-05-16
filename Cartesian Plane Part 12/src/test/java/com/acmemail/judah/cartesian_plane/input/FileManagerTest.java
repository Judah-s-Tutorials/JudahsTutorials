package com.acmemail.judah.cartesian_plane.input;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.AWTException;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.test_utils.RobotAssistant;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

class FileManagerTest
{
    /** Unmodifiable map of test variables to values */
    private static final Map<String,Double> testVarMap;
    
    /** Test file name **/
    private static final String testFileName    = "FileManagerTest.tmp";
    private static final String testFilePath;
    private static final File   testFile;
    
    private static final String testName    = "test noma";
    
    private static final double testStart   = 10.1;
    private static final double testEnd     = testStart * 5;
    private static final double testStep    = 1.1;
    
    private static final String testXEq     = "100";
    private static final String testYEq     = "200";
    private static final String testTEq     = "300";
    private static final String testREq     = "400";
    
    private static final String testParam   = "pTest";
    private static final String testRadius  = "rTest";
    private static final String testTheta   = "yTest";
    
    // initialized in beforeEach
    private Equation    testEquation;
    // set by execOpenCommand
    private Equation    openEquation;
    
    static
    {
        Map<String,Double>  temp    = new HashMap<>();
        
        // Add the default declarations to the var map
        Equation    equation    = new Exp4jEquation();
        temp.putAll( equation.getVars() );
        
        // Add a few non-default variables; use a double value
        // that can be perfectly represented
        IntStream.range( 'a', 'm' )
            .forEach( c -> temp.put( "" + (char)c, c + 3.1 ) );
        testVarMap = Collections.unmodifiableMap( temp );
        printVars( testVarMap );
        
        String  tempDir = System.getProperty( "java.io.tmpdir" );
        testFilePath = tempDir + File.separator + testFileName;
        testFile = new File( testFilePath );
    }
    
    @BeforeEach
    public void beforeEach()
    {
        testEquation = new Exp4jEquation();
        
        // remove default variables before replacing them
        // with explicit test variables
//        testEquation.getVars().entrySet()
//            .forEach( e -> testEquation.removeVar( e.getKey() ));
        testVarMap.entrySet()
            .forEach( e -> testEquation.setVar( e.getKey(), e.getValue() ));
        
        // set everything else that makes sense
        testEquation.setName( testName );
        testEquation.setRange( testStart, testEnd, testStep );
        testEquation.setXExpression( testXEq );
        testEquation.setYExpression( testYEq );
        testEquation.setRExpression( testREq );
        testEquation.setTExpression( testTEq );
        testEquation.setParam( testParam );
        testEquation.setRadiusName( testRadius );
        testEquation.setThetaName( testTheta );
        
        // This variable starts each test equal to null.
        // In some tests, it will be replaced by a valid equation
        // if the test is successful.
        openEquation = null;
        
        testFile.setWritable( true );
        
        // Destroy pre-existing data
        if ( testFile.exists() )
            testFile.delete();
    }
    
    @AfterAll
    public static void afterAll()
    {
        File    tempFile    = new File( testFilePath );
        if ( tempFile.exists() )
            tempFile.delete();
    }
    
    private static void printVars( Map<String,Double> map )
    {
        map.entrySet().stream()
            .sorted( (e1,e2) -> e1.getKey().compareTo( e2.getKey() ) )
            .forEach( e -> System.out.print( e + ",") );
        System.out.println();
    }
    
    @Test
    void testSaveOpenEquationApprove() 
        throws AWTException, InterruptedException
    {
        Thread          thread  = null;
        RobotAssistant  robot   = new RobotAssistant();
        
        thread  = startDialog( () -> execSaveCommand() );
        robot.type( testFilePath, KeyEvent.VK_ENTER );
        thread.join();
        assertTrue( testFile.exists() );
        
        thread  = startDialog( () -> execOpenCommand() );
        robot.type( testFilePath, KeyEvent.VK_ENTER );
        thread.join();
        assertEqualsDefault( openEquation );
    }
    
    @Test
    void testSaveOpenEquationCancel() 
        throws AWTException, InterruptedException
    {
        Thread          thread  = null;
        RobotAssistant  robot   = new RobotAssistant();
        
        thread  = startDialog( () -> execSaveCommand() );
        robot.type( testFilePath, KeyEvent.VK_ESCAPE );
        thread.join();
        assertFalse( testFile.exists() );
        
        // put equation out there...
        // start to open the equation...
        // cancel open dialog...
        // verify equation is not read.
        FileManager.save( testFilePath, testEquation );
        thread  = startDialog( () -> execOpenCommand() );
        robot.type( testFilePath, KeyEvent.VK_ESCAPE );
        thread.join();
        assertNull( openEquation );
    }

    @Test
    void testSaveOpenStringEquation()
    {
        FileManager.save( testFilePath, testEquation );
        Equation    equation    = FileManager.open( testFilePath );
        printVars( equation.getVars() );
        assertEqualsDefault( equation );
    }

    @Test
    void testSaveOpenFileEquation()
    {
        File    testFile    = new File( testFilePath );
        FileManager.save( testFile, testEquation );
        Equation    equation    = FileManager.open( testFile );
        assertEqualsDefault( equation );
    }

    @Test
    void testSaveOpenStreamEquation()
    {
        try (
            PrintWriter printer = new PrintWriter( testFilePath );
        )
        {
            FileManager.save( printer, testEquation );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            fail( "I/O error" );
        }
        
        try (
            FileReader fReader = new FileReader( testFilePath );
            BufferedReader bReader = new BufferedReader( fReader );
        )
        {
            Equation    equation    = FileManager.open( bReader );
            assertEqualsDefault( equation );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            fail( "I/O error" );
        }
        
    }
    
    @Test
    public void saveWithIOError()
    {
        FileManager.save( testFilePath, testEquation );
        assertTrue( testFile.exists() );
        
        testFile.setReadOnly();
        String  oldName = testEquation.getName();
        Equation    newEquation = new Exp4jEquation();
        newEquation.setName( oldName + "_test" );
        
        // try to save new equation
        FileManager.save( testFilePath, newEquation );
        // verify save failed
        newEquation = FileManager.open( testFilePath );
        assertNotNull( newEquation );
        assertEquals( oldName, newEquation.getName() );
    }
    
    @Test
    public void openWithIOError()
    {
        Equation    equation    = FileManager.open( "nobodyhome" );
        assertNull( equation );
    }
    
    private Thread startDialog( Runnable runner )
    {
        Thread  thread  = new Thread( runner );
        thread.start();
        Utils.pause( 500 );
        return thread;
    }
    
    private void execOpenCommand()
    {
        openEquation = FileManager.open();
    }
    
    private void execSaveCommand()
    {
        FileManager.save( testEquation );
    }

    private void assertEqualsDefault( Equation actVal )
    {
        assertNotNull( actVal );
        assertEquals( testVarMap, actVal.getVars() );
        assertEquals( testName, actVal.getName() );
        assertEquals( testStart, actVal.getRangeStart() );
        assertEquals( testEnd, actVal.getRangeEnd() );
        assertEquals( testStep, actVal.getRangeStep() );
        assertEquals( testXEq, actVal.getXExpression() );
        assertEquals( testYEq, actVal.getYExpression() );
        assertEquals( testREq, actVal.getRExpression() );
        assertEquals( testTEq, actVal.getTExpression() );
        assertEquals( testParam, actVal.getParam() );
        assertEquals( testRadius, actVal.getRadiusName() );
        assertEquals( testTheta, actVal.getThetaName() );
    }
}
