package com.acmemail.judah.cartesian_plane.input;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

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
    
    /** Test file name **/
    private static final String testFileName    = "FileManagerTest.tmp";
    private static final String testFilePath;
    private static final File   testFile;
    
    private static final String testName        = "test noma";
    
    private static final double testStart       = 10.1;
    private static final String testStartExpr   = "" + testStart;
    private static final double testEnd         = testStart * 5;
    private static final String testEndExpr     = "" + testEnd;
    private static final double testStep        = 1.1;
    private static final String testStepExpr    = "" + testStep;

    
    private static final String testXEq         = "100";
    private static final String testYEq         = "200";
    private static final String testTEq         = "300";
    private static final String testREq         = "400";
    
    private static final String testParam       = "pTest";
    private static final String testRadius      = "rTest";
    private static final String testTheta       = "yTest";
    
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
        testEquation.setRangeStart( testStartExpr );
        testEquation.setRangeEnd( testEndExpr );
        testEquation.setRangeStep( testStepExpr );
        testEquation.setXExpression( testXEq );
        testEquation.setYExpression( testYEq );
        testEquation.setRExpression( testREq );
        testEquation.setTExpression( testTEq );
        testEquation.setParamName( testParam );
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
    }
    
    @AfterAll
    public static void afterAll()
    {
        File    tempFile    = new File( testFilePath );
        if ( tempFile.exists() )
            tempFile.delete();
    }
    
    @Test
    public void testOpenEquationApprove() 
    {
        FileManager.save( testFile, testEquation );
        assertTrue( testFile.exists() );        
        Thread  thread  = startDialog( () -> execOpenCommand() );
        SwingUtilities.invokeLater( 
            () -> pathTextField.setText( testFilePath ) );
        SwingUtilities.invokeLater( () -> openButton.doClick() );
        Utils.join( thread );
        assertEqualsDefault( openEquation );
    }
    
    @Test
    public void testSaveEquationApprove() 
    {
        assertFalse( testFile.exists() );
        Thread  thread  = startDialog( () -> execSaveCommand() );
        SwingUtilities.invokeLater(
            () -> pathTextField.setText( testFilePath ) );
        SwingUtilities.invokeLater( () -> saveButton.doClick() );
        Utils.join( thread );
        assertTrue( testFile.exists() );
    }
    
    @Test
    public void testSaveEquationCancel() 
    {
        Thread  thread  = startDialog( () -> execSaveCommand() );
        SwingUtilities.invokeLater( 
            () -> pathTextField.setText( testFilePath ) );
        SwingUtilities.invokeLater( () -> cancelButton.doClick() );
        Utils.join( thread );
        assertFalse( testFile.exists() );
    }
    
    @Test
    public void testOpenEquationCancel() 
    {
        // put equation out there...
        // start to open the equation...
        // cancel open dialog...
        // verify equation is not read.
        FileManager.save( testFilePath, testEquation );
        Thread  thread  = startDialog( () -> execOpenCommand() );
        SwingUtilities.invokeLater( 
            () -> pathTextField.setText( testFilePath ) );
        SwingUtilities.invokeLater( () -> cancelButton.doClick() );
        Utils.join( thread );
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
    public void testGetLastFile()
    {
        Thread  thread  = startDialog( () -> execSaveCommand() );
        SwingUtilities.invokeLater(
            () -> pathTextField.setText( testFilePath ) );
        SwingUtilities.invokeLater( () -> saveButton.doClick() );
        Utils.join( thread );
        
        File    expFile = new File( testFilePath );
        assertEquals( expFile, FileManager.getLastFile() );
    }
    
    @Test
    public void testGetLastResultApprove()
    {
        // Save file via dialog w/approve;
        // verify last result is true
        Thread  thread  = startDialog( () -> execSaveCommand() );
        SwingUtilities.invokeLater(
            () -> pathTextField.setText( testFilePath ) );
        SwingUtilities.invokeLater( () -> saveButton.doClick() );
        Utils.join( thread );
        assertTrue( FileManager.getLastResult() );
    }
    
    @Test
    public void testGetLastResultCancel()
    {
        // Save file via dialog w/cancel;
        // verify last result is false
        Thread  thread  = startDialog( () -> execSaveCommand() );
        SwingUtilities.invokeLater(
            () -> pathTextField.setText( testFilePath ) );
        SwingUtilities.invokeLater( () -> cancelButton.doClick() );
        Utils.join( thread );
        assertFalse( FileManager.getLastResult() );
    }
    
    @Test
    public void testSaveWithIOError()
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
        Utils.join( thread );
        
        // verify save failed
        assertFalse( FileManager.getLastResult() );
        newEquation = FileManager.open( testFilePath );
        assertNotNull( newEquation );
        assertEquals( oldName, newEquation.getName() );
    }
    
    @Test
    public void testOpenWithIOError()
    {
        Thread      thread      = new Thread( () -> expectErrorDialog() );
        thread.start();
        Equation    equation    = FileManager.open( "nobodyhome" );
        Utils.join( thread );
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
        
        // Local  variables in anonymous classes must be final
        // or effectively final. This is not true of okButton,
        // so copy it to a new variable that is effectively final.
        JButton button  = okButton;
        SwingUtilities.invokeLater( () -> button.doClick() );
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
    
    /**
     * Gets the text field for entering a file path
     * in the file chooser dialog.
     * 
     * @return  
     *      text field for entering a file path
     *      in the file chooser dialog
     */
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
        assertEquals( testStartExpr, actVal.getRangeStartExpr() );
        assertEquals( testEnd, actVal.getRangeEnd() );
        assertEquals( testEndExpr, actVal.getRangeEndExpr() );
        assertEquals( testStep, actVal.getRangeStep() );
        assertEquals( testStepExpr, actVal.getRangeStepExpr() );
        assertEquals( testXEq, actVal.getXExpression() );
        assertEquals( testYEq, actVal.getYExpression() );
        assertEquals( testREq, actVal.getRExpression() );
        assertEquals( testTEq, actVal.getTExpression() );
        assertEquals( testParam, actVal.getParamName() );
        assertEquals( testRadius, actVal.getRadiusName() );
        assertEquals( testTheta, actVal.getThetaName() );
    }
}
