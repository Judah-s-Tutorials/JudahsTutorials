package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
import java.io.IOException;
import java.io.OutputStream;
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

import com.acmemail.judah.cartesian_plane.test_utils.MessageArchive;
import com.acmemail.judah.cartesian_plane.test_utils.RobotAssistant;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

import util.EquationTestUtil;

/**
 * This class implements a JUnit test for the EquationFileChooser.
 * Many tests of the chooser require its dialog to be visible.
 * Because the dialog is modal the thread that starts it will be blocked.
 * To handle this, 
 * most tests employ a subsidiary thread to post the dialog
 * and wait for it to become visible.
 * Then the main test thread
 * completes and dismisses the dialog,
 * and waits for the subsidiary thread to terminate.
 * 
 * @see #startChooserThread(Runnable)
 * @see #waitForFocus(Component)
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
     * for the chooser dialog to acquire focus. 
     */
    private static final long   FOCUS_WAIT      = 250;
    /**  
     * The maximum time to wait for a thread encapsulating
     * the chooser dialog to complete.
     */
    private static final long   MAX_DIALOG_WAIT = 3000;

    /**  
     * Root reference for all file created in/for this test.
     * Automatically destroyed by JUnit on test completion.
     * @see #beforeAll()
     */
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
    
    private static final MessageArchive messageArchive  = 
        new MessageArchive();
    
    private static final String     noSuchFileName      = "noSuchFile.txt";
    private static final String     binaryFileName      = "binaryFile.bin";
    private static final String     invalidFileName     = 
        "no/such/dir/invalid.txt";
    
    /** 
     * This file is used to create an equation file with two parse errors.
     * It contains an invalid RADIUS name, and an invalid variable
     * declaration.
     */
    private static final String     parseErrorFileName  = "parseError.txt";
    private static final List<String> parseErrorCommands  = List.of(
        Command.EQUATION + " parse errors",
        "# parse error number 1",
        Command.SET + " x=%",
        Command.PARAM + " param",
        "# parse error number 2",
        Command.RADIUS + " ^r",
        Command.START + " -1"
    );


    private static Path     simpleEqPath;
    private static Path     saveEqPath;
    private static Path     noSuchFilePath;
    private static Path     binaryFilePath;
    private static Path     invalidFilePath;
    private static Path     parseErrorPath;
    

    private JFrame  parent;
    
    private IEquationFileChooser    chooser;
    private volatile Equation       testEq;
    private volatile boolean        saveResult;
    
    @BeforeAll
    public static void beforeAll() throws IOException
    {
        assertNotNull( robot );
        simpleEqPath  = tempRoot.resolve( simpleEqName );
        saveEqPath  = tempRoot.resolve( saveEqName );
        noSuchFilePath  = tempRoot.resolve( noSuchFileName );
        binaryFilePath  = tempRoot.resolve( binaryFileName );
        invalidFilePath  = tempRoot.resolve( invalidFileName );
        parseErrorPath  = tempRoot.resolve( parseErrorFileName );
        writeFile( simpleEqPath, simpleEqCommands );
        writeFile( parseErrorPath, parseErrorCommands );
        writeBinaryFile( binaryFilePath );
    }
    
    @BeforeEach
    public void setUp() throws Exception
    {
        invokeAndWait( () -> parent = makeChooserFramework() );
        invokeAndWait( () -> {
            chooser = new EquationFileChooser( parent );
            chooser.setMessageConsumer( messageArchive );
        });
        messageArchive.clear();
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
    public void testMessageConsumer()
    {
        // get the current message consumer
        EquationFileChooser chooser         = new EquationFileChooser();
        MessageConsumer     currConsumer    = chooser.getMessageConsumer();
        
        // set a new consumer
        MessageConsumer newConsumer         = new MessageArchive();
        chooser.setMessageConsumer( newConsumer );
        
        // test the getter; sanity check against current consumer
        MessageConsumer testConsumer        = chooser.getMessageConsumer();
        assertEquals( newConsumer, testConsumer );
        assertNotEquals( newConsumer, currConsumer );
        
        // pass null to the setter, restoring the default consumer
        chooser.setMessageConsumer( null );
        testConsumer = chooser.getMessageConsumer();
        assertNotEquals( newConsumer, testConsumer );
    }

    @Test
    public void testEquationFileChooser()
    {
        invokeAndWait( () -> {
            EquationFileChooser test    = new EquationFileChooser(); 
            assertNull( test.getParent() );
        });
    }

    @Test
    public void testEquationFileChooserComponent()
    {
        invokeAndWait( () -> {
            EquationFileChooser test    = new EquationFileChooser( null ); 
            assertNull( test.getParent() );
         });
         invokeAndWait( () -> {
             JFrame  frame   = new JFrame();
             EquationFileChooser test    = new EquationFileChooser( frame ); 
             assertEquals( frame, test.getParent() );
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
        assertTrue( messageArchive.isEmpty() );
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
        assertTrue( messageArchive.isEmpty() );
        assertNull( testEq );
    }

    @Test
    public void testOpenDialogNoSuchFile()
    {
        // Start a dialog, enter a path to a non-existent file,
        // and approve the operation. Verify that no equation 
        // is loaded.
        String          testPath        = noSuchFilePath.toString();
        // this will change if the test succeeds
        testEq = new Exp4jEquation();
        openDismiss( testPath, KeyEvent.VK_ENTER );
        assertNull( testEq );
        assertFalse( messageArchive.isEmpty() );
        String  expMessageFragment  = "cannot find the file";
        String  lastMessage         = messageArchive.getLastMessage();
        assertTrue( lastMessage.contains( expMessageFragment ) );
    }

    @Test
    public void testOpenDialogInvalidFile()
    {
        // Start a dialog, enter a path to an invalid file,
        // and approve the operation. Verify that no equation 
        // is loaded.
        String          testPath        = binaryFilePath.toString();
        // this will change if the test succeeds
        testEq = new Exp4jEquation();
        openDismiss( testPath, KeyEvent.VK_ENTER );
        assertNull( testEq );
        assertFalse( messageArchive.isEmpty() );
        String  expMessageFragment  = "valid command";
        String  lastMessage         = messageArchive.getLastMessage();
        assertTrue( lastMessage.contains( expMessageFragment ) );
    }

    @Test
    public void testOpenDialogParseErrors()
    {
        // Start a dialog, enter a path to a readable file
        // that contains parse errors, and approve the operation.
        // Verify that no equation is created, and error
        // messages are posted.
        String          testPath        = parseErrorPath.toString();
        // this will change if the test succeeds
        testEq = new Exp4jEquation();
        openDismiss( testPath, KeyEvent.VK_ENTER );
        assertNull( testEq );
        assertFalse( messageArchive.isEmpty() );
        String  expMessageFragment  = "not a valid name";
        String  lastMessage         = messageArchive.getLastMessage();
        assertTrue( lastMessage.contains( expMessageFragment ) );
    }

    @Test
    public void testSaveAndReload()
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
        assertTrue( messageArchive.isEmpty() );
        
        // Re-read saved file to verify save.
        openDismiss( testPath, KeyEvent.VK_ENTER );
        assertNotNull( testEq );
        assertTrue( messageArchive.isEmpty() );
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
        assertTrue( messageArchive.isEmpty() );
        assertFalse( Files.exists( saveEqPath ) );
    }

    @Test
    public void testSaveDialogInvalid()
    {
        // Start a save dialog, enter a path to a file location, 
        // and approve the operation. Verify that the operation
        // fails.
        String          testPath        = invalidFilePath.toString();
        // Start with saveResult = true. If test succeeds
        // it will change to false.
        saveResult = true;
        saveDismiss( saveEq, testPath, KeyEvent.VK_ENTER );
        assertFalse( saveResult );
        assertFalse( messageArchive.isEmpty() );
        String  expMessageFragment  = testPath;
        String  lastMessage         = messageArchive.getLastMessage();
        assertTrue( lastMessage.contains( expMessageFragment ) );
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

    private void saveDismiss( Equation equation, String testPath, int lastKey )
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
     * @param path          file to create
     * @throws IOException  if an IO error occurs
     */
    private static void writeBinaryFile( Path path ) throws IOException
    {
        try ( OutputStream oStream = Files.newOutputStream( path )  )
        {
            for ( int inx = 1 ; inx < 101 ; ++inx )
                oStream.write( inx );
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
     * If the chooser does not acquire focus
     * after {@linkplain #MAX_FOCUS_WAIT} milliseconds,
     * a JUnit failure will be asserted.
     * 
     * @param runner    the Runnable to be executed in a new thread
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
     * <p>
     * Important note:
     * this method terminates successfully
     * when some component in the target JFileChooser
     * object receives focus,
     * but we don't know <em>which</em> component.
     * Most clients of this method
     * are going to assume
     * that the chooser's text field
     * will have focus, 
     * which, while likely true, is not guaranteed.
     * If this test ever starts acting flaky
     * this would be one of the first places
     * to look.
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
            Thread.currentThread().interrupt();
        }
    }
}
