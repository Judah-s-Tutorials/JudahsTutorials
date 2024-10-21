package com.acmemail.judah.cartesian_plane;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Component;
import java.awt.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.function.Predicate;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.test_utils.ProfileUtils;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

public class ProfileFileManagerTest
{
    private static String   testDataDirName     = "ProfileFileManagerTest";
    private static File     testDataDir         = 
        Utils.getTestDataDir( testDataDirName );
    private static String   baseProfileName     = "BaseProfile.txt";
    private static String   distinctProfileName = "DistinctProfile.txt";
    private static String   adHocName           = "AdHoc.txt";
    private static String   noAccessProfileName = "NoAccessFile.txt";
    private static String   noSuchFile          = "NoSuchFile.txt";
    
    /** 
     * Profile containing original property values; 
     * modified in {@link #setUpBeforeClass()} and never changed.
     */
    private static Profile  baseProfile         = new Profile();
    /** Profile containing unique property values; never modified. */
    private Profile         distinctProfile     = 
        ProfileUtils.getDistinctProfile( baseProfile );
    
    /** The FileChooser from the ProfileFileManager. */
    private JFileChooser    fileChooser     = 
        ProfileFileManager.getFileChooser();
    /** The JOptionPane dialog used to display error messages. */
    private JDialog         errorDialog     = null;
    /** The OK button from {@link #errorDialog}. */
    private AbstractButton errorDialogOKButton;
    
    // The components we get from the FileChooser aren't predictable.
    // For example, sometimes there's an open button, and sometimes
    // there's not. References to FileChooser components should be
    // calculated every time they're needed, after making sure the
    // FileChooser is visible.
    /** The field to enter a file name in the FileChooserDialog. */
    private JTextField      chooserName     = null;
    /** The Open button in the FileChooser dialog, if present. */
    private JButton         openButton      = null;
    /** The Save button in the FileChooser dialog, if present. */
    private JButton         saveButton      = null;
    /** The Cancel button in the FileChooser dialog, if present. */
    private JButton         cancelButton    = null;

    @BeforeAll
    static public void setUpBeforeClass() throws Exception
    {
        System.out.println( Utils.getTempDir().getAbsolutePath() );
        
        File    baseFile    = new File( testDataDir, baseProfileName );
        if ( baseFile.exists() )
            baseFile.delete();
        
        baseProfile.setName( "Unique-Test-Name" );
        ProfileParser   parser      = new ProfileParser( baseProfile );
        try ( 
            FileOutputStream fileStr = new FileOutputStream( baseFile );
            PrintWriter writer = new PrintWriter( fileStr );
        )
        {
            parser.getProperties()
                .forEach( writer::println );
        }
        
        File    distinctFile    = 
            new File( testDataDir, distinctProfileName );
        if ( distinctFile.exists() )
            distinctFile.delete();
        
        
        File    noAccessFile   = 
            new File( testDataDir, noAccessProfileName );
        noAccessFile.createNewFile();
        if ( noAccessFile.canRead() )
            noAccessFile.setReadable( false );
        if ( noAccessFile.canWrite() )
            noAccessFile.setWritable( false );
    }
    
    @AfterAll
    public static void afterAll() throws Exception
    {
        if ( testDataDir.exists() )
            Utils.recursiveDelete( testDataDir );
        String  finalStatus = testDataDir.getName() + ": Delete data dir ";
        finalStatus += testDataDir.exists() ? "failed" : "succeeded";
        System.err.println( finalStatus );
    }
    
    @BeforeEach
    public void setUp() throws Exception
    {
    }
    
    @Test
    public void testMisc()
    {
        File    inError = new File( testDataDir, noSuchFile );
        try ( FileInputStream inStr = new FileInputStream( inError ) )
        {
            System.out.println( "no input test failed" );
        }
        catch ( IOException exc )
        {
            System.out.println( exc.getMessage() );
        }

        File    outError = new File( testDataDir, noAccessProfileName );
        try ( PrintStream inStr = new PrintStream( outError ) )
        {
            System.out.println( "no output test failed" );
        }
        catch ( IOException exc )
        {
            System.out.println( exc.getMessage() );
        }
        System.out.println( "done" );
    }

    @Test
    public void testGetCurrFile()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testGetLastResult()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testClose()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testOpenGoRight()
    {
        Thread  thread  = executeOp( ProfileFileManager::open );
        enterPath( baseProfileName );
        clickOn( openButton );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        Profile currProfile = new Profile();
        assertEquals( baseProfile, currProfile );
    }

    @Test
    public void testOpenGoWrong()
    {
        Thread  thread  = executeOp( ProfileFileManager::open );
        enterPath( noSuchFile );
        clickOn( openButton );
        assertTrue( dismissErrorDialog() );
        Utils.join( thread );
    }

    @Test
    public void testOpenFile()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testSaveProfile()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testSaveAs()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testSaveAsProfile()
    {
        fail("Not yet implemented");
    }

    @Test
    public void testSaveProfileFile()
    {
        fail("Not yet implemented");
    }
    
    private void enterPath( String fileName )
    {
        assertNotNull( chooserName );
        File    file    = new File( testDataDir, fileName );
        String  path    = file.getAbsolutePath();
        GUIUtils.schedEDTAndWait( () -> chooserName.setText( path ) );
    }
    
    private void clickOn( AbstractButton button )
    {
        GUIUtils.schedEDTAndWait( button::doClick );
    }
    
    /**
     * Execute a given operation,
     * wait for an error errorDialog to post,
     * then dismiss the error errorDialog
     * by selecting its OK button.
     * The given operation
     * is executed via a dedicated thread
     * and this method does not return
     * until the thread
     * has reached the terminated state.
     * The given operation may post many error dialogs;
     * the number of dialogs posted is returned.
     * 
     * 
     * @param runner    the given operation
     * 
     * @return  
     *      the number of dialogs posted 
     *      while executing the given operation
     */
    private Thread executeOp( Runnable runner )
    {
        Thread  thread          = new Thread( runner );
        thread.start();
        Utils.pause( 250 );
        assertTrue( fileChooser.isVisible() );
        getFileChooserComponents();
        return thread;
    }
    
    private boolean dismissErrorDialog()
    {
        boolean dismissed   = false;
        Utils.pause( 250 );
        getDialogAndOKButton();
        if ( errorDialogOKButton != null )
        {
            dismissed = true;
            GUIUtils.schedEDTAndWait( errorDialogOKButton::doClick );
            String  status  = "Error dialog visible: " + errorDialog.isVisible();
            System.out.println( status );
            Utils.pause( 250 );
        }
        return dismissed;
    }
    
    /**
     * Pushes the OK button
     * indicated by {@link #errorDialogOKButton}
     * and wait for its constituent dialog to terminate.
     * The {@link #errorDialogOKButton} is set to null
     * after being pushed.
     * <p>
     * Precondition:<br>
     * The {@link #errorDialogOKButton} is non-null.
     * Postcondition:<br>
     * The {@link #errorDialogOKButton} is null.
     * Postcondition:<br>
     * The error dialog is no longer visible.
     */
    private void okAndWait()
    {
        errorDialogOKButton.doClick();
        errorDialogOKButton = null;
        while ( errorDialog.isVisible() )
            Utils.pause( 125 );
    }
    
    /**
     * Searches for the first visible JDialog,
     * locates its constituent OK button,
     * and sets {@link #errorDialogOKButton}
     * to the ID of the button.
     * If no dialog is visible
     * {@link #errorDialogOKButton} is set to null.
     * <p>
     * Precondition: the FileChooser dialog is not posted.
     */
    private void getDialogAndOKButton()
    {
        final boolean canBeFrame    = false;
        final boolean canBeDialog   = true;
        final boolean mustBeVis     = true;
        GUIUtils.schedEDTAndWait( () ->  {
            errorDialogOKButton = null;
            errorDialog = null;
            ComponentFinder finder  = 
                new ComponentFinder( canBeDialog, canBeFrame, mustBeVis );
            Window          window  = finder.findWindow( c -> true );
            if ( window != null )
            {
                assertTrue( window instanceof JDialog );
                errorDialog = (JDialog)window;
                Predicate<JComponent>   pred    = 
                    ComponentFinder.getButtonPredicate( "OK" );
                JComponent              comp    = 
                    ComponentFinder.find( window, pred );
                assertNotNull( comp );
                assertTrue( comp instanceof AbstractButton );
                errorDialogOKButton = (AbstractButton)comp;
            }
        });
    }
    
    /**
     * 
     * <p>
     * Precondition: The FileChooser dialog is visible.
     */
    private void getFileChooserComponents()
    {
        final Predicate<JComponent> namePred    =
            c -> (c instanceof JTextField );
        final Predicate<JComponent> openPred    =
            ComponentFinder.getButtonPredicate( "Open" );
        final Predicate<JComponent> savePred    =
            ComponentFinder.getButtonPredicate( "Save" );
        final Predicate<JComponent> cancelPred  =
            ComponentFinder.getButtonPredicate( "Cancel" );
        
        // The text field component should always be present.
        Component   comp        = 
            ComponentFinder.find( fileChooser, namePred );
        assertNotNull( comp );
        assertTrue( comp instanceof JTextField );
        chooserName = (JTextField)comp;
        
        // The Cancel should always be present.
        comp = ComponentFinder.find( fileChooser, cancelPred );
        assertNotNull( comp );
        assertTrue( comp instanceof JButton );
        cancelButton = (JButton)comp;
        
        // The Open and Save buttons may not both be present, but
        // at least one of them must be.
        openButton = null;
        comp = ComponentFinder.find( fileChooser, openPred );
        if ( comp != null )
        {
            assertTrue( comp instanceof JButton );
            openButton = (JButton)comp;
        }
        saveButton = null;
        comp = ComponentFinder.find( fileChooser, savePred );
        if ( comp != null )
        {
            assertTrue( comp instanceof JButton );
            saveButton = (JButton)comp;
        }
        assertTrue( openButton != null || saveButton != null );
    }
}
