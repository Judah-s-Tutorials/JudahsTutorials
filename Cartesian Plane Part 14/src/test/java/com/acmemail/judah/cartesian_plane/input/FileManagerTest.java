package com.acmemail.judah.cartesian_plane.input;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.AWTException;
import java.awt.Window;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

class FileManagerTest
{
    /** 
     * How long in milliseconds,
     * the {@linkplain #expectErrorDialog()} pauses before
     * looking for a visible message dialog. Note that the method
     * may wait multiple times.
     * @see #expDialogMaxWaitMillis
     */
    private static final long   expDialogPauseMillis    = 250;
    /**
     * The maximum amount of time, in milliseconds,
     * that the {@linkplain #expectErrorDialog()}
     * for a message to become visible.
     * @see #expDialogMaxWaitMillis
     */
    private static final long   expDialogMaxWaitMillis  = 2000;
    
    /** Unmodifiable map of test variables to values */
    private static final Map<String,Double> testVarMap;

    /** Path to temporary directory as a string.  */
    private static final String tempDirStr  = 
        System.getProperty( "java.io.tmpdir" );
    /** Path to temporary directory as a File.  */
    private static final File   tempDir     = new File( tempDirStr );
    
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
    private static Equation    openEquation;
    
    /////////////////////////////////////////
    //
    // JFileChooser components.
    // Reset to null in beforeEach method.
    // Set in startDialog method.
    //
    /////////////////////////////////////////
    private JDialog     jDialog;
    private JButton     saveButton;
    @SuppressWarnings("unused")
    private JButton     openButton;
    private JButton     cancelButton;
    private JTextField  pathTextField;
    
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
        
        testFilePath = tempDirStr + File.separator + testFileName;
        testFile = new File( testFilePath );
    }

    private static boolean done = false;
    
    /**
     * Problem: in Windows (at least)
     * setting a path that begins with root (\)
     * then selecting open button with doClick
     * causes a null pointer exception
     * in Swing code. The workaround is...
     * <p>
     * ... set the current directory in the JFileChooser
     * to the system temp directory before it becomes visible.
     * Note that:
     * <ul>
     * <li>
     *      I tried doing this in a @BeforeAll method
     *      and I got strange reflection errors,
     *      that's why I put it in the @BeforeEach method
     *      in an if(!done) wrapper.
     * </li>
     * <li>
     *     Setting the current directory to temp
     *     after the dialog becomes visible
     *     doesn't help.
     * </li>
     *     Once the current directory is set
     *     it doesn't matter whether the path
     *     that gets set in the text field
     *     starts with the root or not.
     * </li>
     * </ul>
     * <p>
     * This procedure must be executed before
     * the JFileChooser becomes visible.
     */
    @BeforeEach
    public void beforeEach()
    {
        testEquation = new Exp4jEquation();
        
        // remove default variables before replacing them
        // with explicit test variables
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
        
        // Make sure the test file is writable, in case it was
        // changed in the course of executing a test.
        testFile.setWritable( true );
        
        // Destroy pre-existing data
        if ( testFile.exists() )
            testFile.delete();
        
        // Reset the JFileChooser component ids.
        jDialog = null;
        saveButton = null;
        openButton = null;
        cancelButton = null;
        pathTextField = null;

        if ( !done )
        {
            Thread  thread  = startDialog( () -> execOpenCommand() );
            Utils.pause( 250 );
            JComponent  comp    = 
                ComponentFinder.find( jDialog, c -> (c instanceof JFileChooser) );
            assertNotNull( comp );
            assertTrue( comp instanceof JFileChooser );
            Utils.pause( 2000 );
            cancelButton.doClick();
            Utils.join( thread );
            ((JFileChooser)comp).setCurrentDirectory( tempDir );
            done = true;
        }
    }
    
    @AfterAll
    public static void afterAll()
    {
        File    tempFile    = new File( testFilePath );
        if ( tempFile.exists() )
            tempFile.delete();
    }
    
    /**
     * Maintenance note:
     * Attempted to remove use of robot from this code.
     * Substituted textField.setText() and openButton.doClick().
     * The doClick() results in a null-pointer exception
     * deep in the JList code.
     * Problem persists when executing this test alone.
     * Can't figure out why,
     * can't find a way around it 
     * by shaking up the GUI,
     * can't reproduce the problem
     * in a stand-alone program.
     * Workaround is to eliminate openButton.doClick(),
     * and substitute "enter" key-press using robot
     * to activate the open button.
     * 
     * @throws AWTException
     * @throws InterruptedException
     */
    @Test
    public void testOpenEquationApprove() 
        throws AWTException, InterruptedException
    {
        Thread          thread  = null;
        
        FileManager.save( testFile, testEquation );
        assertTrue( testFile.exists() );        
        thread  = startDialog( () -> execOpenCommand() );
        pathTextField.setText( testFilePath );
        openButton.doClick();
        thread.join();
        assertEqualsDefault( openEquation );
    }
    
    @Test
    public void testSaveEquationApprove() 
        throws AWTException, InterruptedException
        
    {
        Thread          thread  = null;
        
        assertFalse( testFile.exists() );
        thread  = startDialog( () -> execSaveCommand() );
        pathTextField.setText( testFilePath );
        saveButton.doClick();
        thread.join();
        assertTrue( testFile.exists() );
    }
    
    @Test
    public void testSaveEquationCancel() 
        throws AWTException, InterruptedException
    {
        Thread          thread  = null;
        
        thread  = startDialog( () -> execSaveCommand() );
        pathTextField.setText( testFilePath );
        cancelButton.doClick();
        thread.join();
        assertFalse( testFile.exists() );
    }
    
    @Test
    public void testOpenEquationCancel() 
        throws AWTException, InterruptedException
    {
        Thread          thread  = null;
        
        // put equation out there...
        // start to open the equation...
        // cancel open dialog...
        // verify equation is not read.
        FileManager.save( testFilePath, testEquation );
        thread  = startDialog( () -> execOpenCommand() );
        pathTextField.setText( testFilePath );
        cancelButton.doClick();
        thread.join();
        assertNull( openEquation );
    }

    @Test
    public void testSaveOpenStringEquation()
    {
        FileManager.save( testFilePath, testEquation );
        Equation    equation    = FileManager.open( testFilePath );
        assertEqualsDefault( equation );
    }

    @Test
    void testSaveOpenFileEquation()
    {
        FileManager.save( testFile, testEquation );
        Equation    equation    = FileManager.open( testFile );
        assertEqualsDefault( equation );
    }

    @Test
    public void testSaveOpenStreamEquation()
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
            fail( "I/O error", exc );
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
            fail( "I/O error", exc );
        }
        
    }
    
    @Test
    public void testSaveWithIOError() throws InterruptedException
    {
        FileManager.save( testFilePath, testEquation );
        assertTrue( testFile.exists() );
        
        testFile.setReadOnly();
        String  oldName = testEquation.getName();
        Equation    newEquation = new Exp4jEquation();
        newEquation.setName( oldName + "_test" );
        
        // start new thread to wait for error message dialog
        Thread      thread      = new Thread( () -> expectErrorDialog() );
        thread.start();
        
        // try to save new equation
        FileManager.save( testFilePath, newEquation );
        
        // wait for error dialog to be dismissed
        thread.join();
        
        // verify save failed
        newEquation = FileManager.open( testFilePath );
        assertNotNull( newEquation );
        assertEquals( oldName, newEquation.getName() );
    }
    
    @Test
    public void testOpenWithIOError() throws InterruptedException
    {
        Thread      thread      = new Thread( () -> expectErrorDialog() );
        thread.start();
        Equation    equation    = FileManager.open( "nobodyhome" );
        thread.join();
        assertNull( equation );
    }
    
    private Thread startDialog( Runnable runner )
    {
        Thread  thread  = new Thread( runner );
        thread.start();
        Utils.pause( 500 );
        
        jDialog = getJDialog();
        saveButton = getButton( "Save" );
        openButton = getButton( "Open" );
        cancelButton = getButton( "Cancel" );
        pathTextField = getTextField();
        return thread;
    }
    
    private static void execOpenCommand()
    {
        openEquation = FileManager.open();
    }
    
    private void execSaveCommand()
    {
        FileManager.save( testEquation );
    }
    
    /**
     * Waits for a message dialog to become visible, 
     * then dismisses it.
     * Waits in a loop for {@linkplain #expDialogPauseMillis}
     * milliseconds at a time,
     * but not for longer than an aggregate
     * of {@linkplain #expDialogMaxWaitMillis} milliseconds.
     */
    private void expectErrorDialog()
    {
        long    startMillis = System.currentTimeMillis();
        long    totalMillis = 0;
        JButton okButton    = null;
        do
        {
            Utils.pause( expDialogPauseMillis );
            okButton = getErrorDialogOKButton();
            totalMillis = System.currentTimeMillis() - startMillis;
        } while ( okButton == null && totalMillis < expDialogMaxWaitMillis );
        assertNotNull( okButton,"timeout waiting for error dialog" );
        
        okButton.doClick();
    }
    
    private JButton getErrorDialogOKButton()
    {
        ComponentFinder         finder  = 
            new ComponentFinder( true, false, true );
        Predicate<JComponent>   pred    =
            ComponentFinder.getButtonPredicate( "OK" );
        JComponent              comp    = finder.find( pred );
        JButton                 button  = null;
        if ( comp instanceof JButton )
            button = (JButton)comp;
        return button;
    }
    
    private JDialog getJDialog()
    {
        ComponentFinder finder  = new ComponentFinder( true, false, true );
        Window          window  = finder.findWindow( w -> true );
        JDialog         dialog  = null;
        if ( window != null && window instanceof JDialog )
            dialog = (JDialog)window;
        return dialog;
    }
    
    private JButton getButton( String text )
    {
        JButton                 button  = null;
        Predicate<JComponent>   pred    = 
            ComponentFinder.getButtonPredicate( text );
        if ( jDialog != null )
        {
            JComponent  comp    = ComponentFinder.find( jDialog, pred );
            if ( comp != null && comp instanceof JButton )
                button = (JButton)comp;
        }
        return button;
    }
    
    private JTextField getTextField()
    {
        JTextField              textField   = null;
        Predicate<JComponent>   pred        = c -> (c instanceof JTextField);
        if ( jDialog != null )
        {
            JComponent  comp    = ComponentFinder.find( jDialog, pred );
            if ( comp != null && comp instanceof JTextField )
                textField = (JTextField)comp;
        }
        return textField;
    }

    private static void assertEqualsDefault( Equation actVal )
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
