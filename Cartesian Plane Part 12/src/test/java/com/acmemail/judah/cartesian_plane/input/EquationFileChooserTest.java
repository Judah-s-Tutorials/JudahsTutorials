package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Component;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.io.TempDir;

import com.acmemail.judah.cartesian_plane.test_util.EquationTestUtil;
import com.acmemail.judah.cartesian_plane.test_utils.MessageArchive;

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
    /**  
     * Root reference for all file created in/for this test.
     * Automatically destroyed by JUnit on test completion.
     * @see #beforeAll()
     */
    @TempDir
    private static Path    tempRoot;
    
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
    

    private static JFrame       parent;
    
    private MockJFileChooser    mockJFileChooser;
    private IEquationFileChooser    chooser;
    
    @BeforeAll
    public static void beforeAll() throws IOException
    {
        invokeAndWait( () -> parent = new JFrame() );
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
        messageArchive.clear();
        invokeAndWait( () -> {
            mockJFileChooser = new MockJFileChooser();
            chooser = new EquationFileChooser( parent, mockJFileChooser );
            chooser.setMessageConsumer( messageArchive );
        });
    }

    @AfterEach
    public void tearDown() throws Exception
    {
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
             EquationFileChooser test    = new EquationFileChooser( parent ); 
             assertEquals( parent, test.getParent() );
          });
    }

    @Test
    public void testOpenDialogApprove()
    {
        // Start a dialog, enter a path to a valid equation file,
        // and approve the operation. Verify that the equation was
        // successfully loaded.
        mockJFileChooser.setSelectedFile( simpleEqPath.toFile() );
        mockJFileChooser.approve( true );
        Optional<Equation>  optEquation = chooser.openDialog();
        assertTrue( optEquation.isPresent() );
        assertTrue( messageArchive.isEmpty() );
        Equation    testEq  = optEquation.get();
        EquationTestUtil.verifyEquation( simpleEq, testEq );
    }

    @Test
    public void testOpenDialogCancel()
    {
        // Start a dialog, enter a path to a valid equation file,
        // and cancel the operation. Verify a null equation is returned.
        mockJFileChooser.setSelectedFile( simpleEqPath.toFile() );
        mockJFileChooser.approve( false );
        Optional<Equation>  optEquation = chooser.openDialog();
        assertTrue( messageArchive.isEmpty() );
        assertFalse( optEquation.isPresent() );
    }

    @Test
    public void testOpenDialogNoSuchFile()
    {
        // Start a dialog, enter a path to a non-existent file,
        // and approve the operation. Verify that no equation 
        // is loaded.
        mockJFileChooser.setSelectedFile( noSuchFilePath.toFile() );
        mockJFileChooser.approve( true );
        Optional<Equation>  optEquation = chooser.openDialog();
        assertFalse( optEquation.isPresent() );
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
        mockJFileChooser.setSelectedFile( binaryFilePath.toFile() );
        mockJFileChooser.approve( true );
        Optional<Equation>  optEquation = chooser.openDialog();
        assertFalse( optEquation.isPresent() );
        assertFalse( messageArchive.isEmpty() );
        String  expMessageFragment  = "INVALID";
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
        mockJFileChooser.setSelectedFile( parseErrorPath.toFile() );
        mockJFileChooser.approve( true );
        Optional<Equation>  optEquation = chooser.openDialog();
        assertFalse( optEquation.isPresent() );
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
        mockJFileChooser.setSelectedFile( saveEqPath.toFile() );
        mockJFileChooser.approve( true );
        boolean         result          = chooser.saveDialog( saveEq );
        assertTrue( result );
        assertTrue( messageArchive.isEmpty() );
        
        // Re-read saved file to verify save.
        Optional<Equation>  optEquation = chooser.openDialog();
        assertTrue( optEquation.isPresent() );
        assertTrue( messageArchive.isEmpty() );
        Equation        testEq          = optEquation.get();
        EquationTestUtil.verifyEquation( saveEq, testEq );
    }

    @Test
    public void testSaveDialogCancel()
    {
        // Start a save dialog, enter a path to a valid file, 
        // and cancel the operation. Verify that the operation
        // was canceled.
        mockJFileChooser.approve( false );
        boolean result  = chooser.saveDialog( saveEq );
        assertFalse( result );
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
        mockJFileChooser.setSelectedFile( invalidFilePath.toFile() );
        mockJFileChooser.approve( true );
        boolean         result          = chooser.saveDialog( saveEq );
        assertFalse( result );
        assertFalse( messageArchive.isEmpty() );
        String  expMessageFragment  = testPath;
        String  lastMessage         = messageArchive.getLastMessage();
        assertTrue( lastMessage.contains( expMessageFragment ) );
    }

    /**
     * Ensures that an operation executes in the context
     * of the event dispatch thread.
     * 
     * @param runner
     */
    private static void invokeAndWait( Runnable runner )
    {
        if ( SwingUtilities.isEventDispatchThread() )
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
    
    @SuppressWarnings("serial")
    private static class MockJFileChooser extends JFileChooser
    {
        private File    selectedFile    = null;
        private int     expectedResult  = 0;
        
        @Override
        public int showOpenDialog( Component parent )
        {
            return expectedResult;
        }
        
        @Override
        public int showSaveDialog( Component parent )
        {
            return expectedResult;
        }
        
        @Override
        public File getSelectedFile()
        {
            return selectedFile;
        }

        public void setSelectedFile(File selectedFile)
        {
            this.selectedFile = selectedFile;
        }
        
        public void approve( boolean result )
        {
            this.expectedResult = result ? 
                JFileChooser.APPROVE_OPTION : JFileChooser.CANCEL_OPTION;
        }
    }
}
