package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.swing.JFrame;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.io.TempDir;

import com.acmemail.judah.cartesian_plane.test_utils.RobotAssistant;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

import util.EquationTestUtil;

/**
 * This class implements a JUnit test for the EquationFileChooser.
 * Because many tests of the chooser require it to be visible,
 * we address the following issues:
 * 
 * <ol>
 *     <li>
 *     </li>
 * </ol>
 */
@TestMethodOrder( MethodOrderer.OrderAnnotation.class )
public class EquationFileChooserTest
{
    /** Error message used when an operation times-out. */
    private static final String DIALOG_TIMEOUT  =
        "timeout waiting for dialog operation to complete";
    /** The maximum time to wait for the chooser dialog to be posted. */
    private static final long   MAX_FOCUS_WAIT  = 2000; // milliseconds
    /** 
     * The time to wait between checks 
     * for chooser dialog has acquire focus. 
     */
    private static final long   FOCUS_WAIT      = 250;
    /**  
     * The maximum time to wait for a thread encapsulating
     * the chooser dialog to complete.
     */
    private static final long   MAX_DIALOG_WAIT = 2000;

    @TempDir
    private static Path    tempRoot;
    
    private static final RobotAssistant robot               = 
        newRobotAssistant();
    
    private static final String         simpleEqName        = "SimpleEq.txt";
    private static final List<String>   simpleEqCommands    =
        List.of(
            "EQUATION " + "SimpleEq",
            "XEQUALS " + "5",
            "YEQUALS " + "10",
            "PARAM " + "simpleParam"
        );
    private static final Equation   simpleEq    =
        getEquation( simpleEqCommands );
    
    private static final String         saveEqName      = "SaveEq.txt";
    private static final List<String>   saveEqCommands  =
        List.of(
            "EQUATION " + "SaveEq",
            "XEQUALS " + "10",
            "YEQUALS " + "20",
            "RADIUS " + "saveRadius"
        );
    private static final Equation   saveEq    =
        getEquation( saveEqCommands );
    
    private static final String     noSuchFileName      = "noSuchFile.txt";
    private static final String     binaryFileName      = "binaryFile.bin";
    private static final String     readOnlyFileName    = "readOnly.txt";

    private static Path     simpleEqPath;
    private static Path     saveEqPath;
    private static Path     noSuchFilePath;
    private static Path     binaryFilePath;
    private static Path     readOnlyFilePath;
    

    private JFrame  parent;
    
    private EquationFileChooser chooser;
    private volatile Equation   testEq;
    private volatile boolean    saveResult;
    
    @BeforeAll
    public static void beforeAll() throws IOException
    {
        assertNotNull( robot );
        simpleEqPath  = tempRoot.resolve( simpleEqName );
        saveEqPath  = tempRoot.resolve( saveEqName );
        noSuchFilePath  = tempRoot.resolve( noSuchFileName );
        binaryFilePath  = tempRoot.resolve( binaryFileName );
        readOnlyFilePath  = tempRoot.resolve( readOnlyFileName );
        writeFile( simpleEqPath, simpleEqCommands );
        writeBinaryFile( binaryFilePath );
        writeFile( readOnlyFilePath, simpleEqCommands );
        readOnlyFilePath.toFile().setReadOnly();
    }
    
    @BeforeEach
    public void setUp() throws Exception
    {
        invokeAndWait( () -> parent = makeChooserFramework() );
        invokeAndWait( () -> chooser = new EquationFileChooser( parent ) );
        testEq = null;
    }

    @AfterEach
    public void tearDown() throws Exception
    {
        if ( parent != null )
            invokeAndWait( () -> {
                parent.setVisible( false );
                parent.dispose();
            });
        // assume we always want to start with no default
        // file to save to
        Files.deleteIfExists( saveEqPath );
    }

    @Test
    public void testEquationFileChooser()
    {
        invokeAndWait( () -> {
           new EquationFileChooser(); 
        });
    }

    @Test
    public void testEquationFileChooserComponent()
    {
        invokeAndWait( () -> {
            EquationFileChooser test    = new EquationFileChooser( null ); 
            assertNotNull( test );
         });
         invokeAndWait( () -> {
             JFrame  frame   = new JFrame();
             EquationFileChooser test    = new EquationFileChooser( frame ); 
             assertNotNull( test );
             frame.dispose();
          });
    }
    
    @Test
    @Order( 1 )
    public void testOpenDialogTimeoutSanityCheck()
    {
        Thread  thread  = startChooserThread( this::openOperation );
        join( thread, MAX_DIALOG_WAIT );
        
        if ( !thread.isAlive() )
        {
            String  message =
                "This test is supposed to confirm that the test "
                + "will not be compromised if a chooser "
                + "dialog never completes, leaving the worker thread "
                + "running, but the worker thread has terminated.";
            fail( message );
        }
    }

    @Test
    public void testOpenDialogApprove()
    {
        // Start a dialog, enter a path to a valid equation file,
        // and approve the operation. Verify that the equation was
        // successfully loaded.
        String          testPath        = simpleEqPath.toString();
        // sanity check
        assertNull( testEq );
        openDismiss( testPath, KeyEvent.VK_ENTER );
        assertNotNull( testEq );
        EquationTestUtil.verifyEquation( simpleEq, testEq );
    }

    @Test
    public void testOpenDialogCancel()
    {
        // Start a dialog, enter a path to a valid equation file,
        // and cancel the operation. Verify a null equation is returned.
        String          testPath        = simpleEqPath.toString();
        // Start with testEq non-null; if test succeeds, it will
        // change to null.
        testEq = new Exp4jEquation();
        openDismiss( testPath, KeyEvent.VK_ESCAPE );
        assertNull( testEq );
    }
    
    /**
     * Post the EquationFileChooser open dialog;
     * save the result in global variable testEq.
     */
    private void openOperation()
    {
        Optional<Equation>  optEquation = chooser.openDialog();
        testEq = optEquation.orElse( null );
    }

    @Test
    public void testSaveDialogApprove()
    {
        // Start a save dialog, enter a path to a valid file, 
        // and approve the operation. Verify that the equation was
        // successfully saved.
        String          testPath        = saveEqPath.toString();
        // Start with saveResult = false. If test succeeds
        // it will change to true.
        saveResult = false;
        saveDismiss( saveEq, testPath, KeyEvent.VK_ENTER );
        assertTrue( saveResult );
        
        // Re-read saved file to verify save.
        openDismiss( testPath, KeyEvent.VK_ENTER );
        assertNotNull( testEq );
        EquationTestUtil.verifyEquation( saveEq, testEq );
    }

    @Test
    public void testSaveDialogCancel()
    {
        // Start a save dialog, enter a path to a valid file, 
        // and cancel the operation. Verify that the operation
        // was canceled.
        String          testPath        = saveEqPath.toString();
        // Start with saveResult = true. If test succeeds
        // it will change to false.
        saveResult = true;
        saveDismiss( saveEq, testPath, KeyEvent.VK_ESCAPE );
        assertFalse( saveResult );
        assertFalse( Files.exists( saveEqPath ) );
    }

    @Test
    public void testSaveDialogInvalid()
    {
        // Start a save dialog, enter a path to a read-only file, 
        // and approve the operation. Verify that the operation
        // fails.
        String          testPath        = readOnlyFilePath.toString();
        // Start with saveResult = true. If test succeeds
        // it will change to false.
        saveResult = true;
        saveDismiss( saveEq, testPath, KeyEvent.VK_ENTER );
        assertFalse( saveResult );
    }
    
    /**
     * Post the EquationFileChooser save dialog;
     * save the result in global variable testEq.
     */
    private void saveOperation( Equation equation )
    {
        saveResult = chooser.saveDialog( equation );
    }

    private void openDismiss( String testPath, int lastKey )
    {
        Thread          thread          = 
            startChooserThread( this::openOperation );
        robot.type( testPath, lastKey );
        join( thread, MAX_DIALOG_WAIT );
        
        // The worker thread should be done by now. If not,
        // the test fails.
        if ( thread.isAlive() )
        {
            thread.interrupt();
            fail( DIALOG_TIMEOUT );
        }
    }

    private void 
    saveDismiss( Equation equation, String testPath, int lastKey )
    {
        Thread          thread          = 
            startChooserThread( () -> saveOperation( equation ) );
        robot.type( testPath, lastKey );
        join( thread, MAX_DIALOG_WAIT );
        
        // The worker thread should be done by now. If not,
        // the test fails.
        if ( thread.isAlive() )
        {
            thread.interrupt();
            fail( DIALOG_TIMEOUT );
        }
    }

    /**
     * Ensures that an operation executes in the context
     * of the event dispatch thread.
     * 
     * @param runner
     */
    private static void invokeAndWait( Runnable runner )
    {
        if ( EventQueue.isDispatchThread() )
            runner.run();
        else
        {
            try
            {
                EventQueue.invokeAndWait( runner );
            }
            catch ( InterruptedException | InvocationTargetException exc )
            {
                fail( exc );
            }
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

    private static void writeFile( Path path, List<String> lines )
        throws IOException
    {
        try (
            BufferedWriter bWriter = Files.newBufferedWriter( path );
            PrintWriter pWriter = new PrintWriter( bWriter );
        )
        {
            lines.forEach( pWriter::println );
        }
    }
    
    /**
     * Via CommandProcessor,
     * convert a list of lines to an equation.
     * 
     * @param lines the list to convert
     * 
     * @return  the converted equation
     */
    private static Equation getEquation( List<String> lines )
    {
        String              data        = String.join( "\n", lines );
        StringReader        sReader     = new StringReader( data );
        BufferedReader      bReader     = new BufferedReader( sReader );
        Equation            equation    = new Exp4jEquation();
        CommandProcessor    proc        = new CommandProcessor( equation );
        CommandReader       cmdReader   = new CommandReader( bReader );
        cmdReader.stream().forEach( proc::processCommand );

        return equation;
    }
    
    /**
     * Start a new thread with a Runnable
     * that will start the EquationFileChooser dialog.
     * Wait for the chooser dialog to become visible
     * and to acquire the keyboard focus,
     * then return the thread to the caller,
     * leaving the thread running.
     * <p>
     * If the chooser does not become acquire focus
     * after {@linkplain #MAX_FOCUS_WAIT} milliseconds,
     * a JUnit failure will be asserted.
     * 
     * @param runner    the Runnable to be exceuted in a new thread
     * 
     * @return  the thread encapsulating runner
     */
    private Thread startChooserThread( Runnable runner )
    {
        Thread  thread  = new Thread( runner );
        thread.start();
        waitForFocus( parent );
        return thread;
    }
    
    /**
     * Wait for the Window owned by the given parent
     * to acquire the keyboard focus.
     * If focus is not acquired after {@linkplain #MAX_FOCUS_WAIT}
     * a failure will be asserted.
     * 
     * @param parent    the given parent
     */
    private static void waitForFocus( Component parent )
    {
        boolean     focused     = false;
        long        startTime   = System.currentTimeMillis();
        long        elapsedTime = 0;
        while ( !focused && elapsedTime < MAX_FOCUS_WAIT )
        {
            System.out.println( "checking" );
            focused = Arrays.stream( Window.getWindows() )
                .filter( w -> parent.equals( w.getOwner() ) )
                .map( w -> w.getFocusOwner() )
                .filter( Objects::nonNull)
                .findFirst().isPresent();
            if ( !focused )
                Utils.pause( FOCUS_WAIT );
            elapsedTime = System.currentTimeMillis() - startTime;
        }
        if ( !focused )
            fail( "Chooser dialog failed to acquire focus" );
    }
    
    /**
     * Instantiate a very small JFrame and make it visible. 
     * 
     * @return  instantiated JFrame
     */
    private static JFrame makeChooserFramework()
    {
        JFrame      frame   = new JFrame( "test component" );
        Dimension   size    = new Dimension( 10, 10 );
        frame.getContentPane().setPreferredSize( size );
        frame.pack();
        frame.setVisible( true );
        return frame;
    }
    
    /**
     * Create a new RobotAssistant, 
     * catching the AWTException if necessary.
     * If an AWTException is caught, 
     * null will be returned.
     * The expectation is that the @BeforeAll method
     * will assert failure if null is returned,
     * causing execution of the JUnit test class
     * to be aborted.
     * 
     * @return  a new RobotAssistant, or null if failed
     */
    private static RobotAssistant newRobotAssistant()
    {
        RobotAssistant  robot   = null;
        try
        {
            robot = new RobotAssistant();
        }
        catch ( AWTException exc )
        {
            // 
        }
        return robot;
    }
    
    /**
     * Join a given thread, 
     * waiting a maximum of milliseconds
     * for the thread to complete.
     * If the timeout expires
     * a failure is asserted.
     * 
     * @param thread    the given thread
     * @param timeout   the maximum wait time
     */
    private void join( Thread thread, long timeout )
    {
        try
        {
            thread.join( timeout );
        }
        catch ( InterruptedException exc )
        {
            fail( "timeout waiting for thread to complete" );
        }
    }
}
