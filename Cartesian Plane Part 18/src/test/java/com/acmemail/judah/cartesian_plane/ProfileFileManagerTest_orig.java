package com.acmemail.judah.cartesian_plane;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

public class ProfileFileManagerTest_orig
{
    private static final String     testDataDirName = 
        "ProfileFileManagerTest";
    private static final  File      testDataDir     = 
        Utils.getTestDataDir( testDataDirName );
    private static final  String    baseName        = 
        "BaseProfile.txt";
    private static final  String    distinctName    = 
        "DistinctProfile.txt";
    private static final  String    adHocName       = 
        "AdHoc.txt";
    private static final  String    noAccessName    = 
        "NoAccessFile.txt";
    private static final  String    noSuchFileName  = 
        "NoSuchFile.txt";
    private static final File       baseFile        = 
        new File( testDataDir, baseName );
    private static final File       distinctFile    = 
        new File( testDataDir, distinctName );
    private static final File       adHocFile       = 
        new File( testDataDir, adHocName );
    private static final File       noAccessFile    = 
        new File( testDataDir, noAccessName );
    private static final File       noSuchFile      = 
        new File( testDataDir, noSuchFileName );
    
    /** 
     * Profile containing original property values; 
     * modified in {@link #setUpBeforeClass()} and never changed.
     * Used in {@link #beforeEach()} to restore the PropertyManager
     * to the base properties before each test.
     */
    private static final Profile    baseProfile     = new Profile();
    /** Profile containing unique property values; never modified. */
    private static final Profile    distinctProfile = 
        ProfileUtils.getDistinctProfile( baseProfile );
    /** 
     * Profile for ad hoc use during testing. Contents should be
     * considered volatile outside of very narrow usage, such:
     * <span style="font-family: monospace;">
     * <br>
     * &nbsp;&nbsp;&nbsp;&nbsp;profile = getProfile();<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;validate( profile );
     * </span>
     */
    private Profile                 adHocProfile    = new Profile();
    /** 
     * Temporary integer for temporary use in lambdas, for example:<br>
     * <pre>    adHocResult = executeChooserOp( 
     *         () -> adHocResult = fileMgr.saveAs( profile )
     */
    private boolean                 adHocResult     = false;
    /** The file manager under test. */
    private final ProfileFileManager    fileMgr     = 
        new ProfileFileManager();
    
    /** The FileChooser from the fileMgr. */
    private final JFileChooser      fileChooser     = 
        fileMgr.getFileChooser();
    /** The OK button from the dialog for displaying I/O errors. */
    private AbstractButton errorDialogOKButton;
    
    // The components we get from the FileChooser aren't predictable.
    // For example, sometimes there's an open button, and sometimes
    // there's not. References to FileChooser components should be
    // calculated every time they're needed, after making sure the
    // FileChooser is visible.
    /** The dialog encapsulating the JFileChooser. */
    private JDialog         chooserDialog   = null;
    /** The field to enter a file name in the FileChooserDialog. */
    private JTextField      chooserName     = null;
    /** The Open button in the FileChooser dialog, if present. */
    private JButton         openButton      = null;
    /** The Save button in the FileChooser dialog, if present. */
    private JButton         saveButton      = null;
    /** The Cancel button in the FileChooser dialog, if present. */
    private JButton         cancelButton    = null;

    @BeforeAll
    static public void beforeAll() throws Exception
    {
        System.out.println( Utils.getTempDir().getAbsolutePath() );
        
        baseFile.delete();
        baseProfile.setName( "Unique-Test-Name" );
        saveProfile( baseProfile, baseFile );
        
        distinctFile.delete();
        saveProfile( distinctProfile, distinctFile );
        
        noSuchFile.delete();
        
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
    }
    
    @BeforeEach
    public void beforeEach() throws Exception
    {
        baseProfile.apply();
        chooserName = null;
        openButton = null;
        saveButton = null;
        cancelButton = null;
        initFileManagerState();
    }
    
    @Test
    public void testMisc()
    {
        File    inError = new File( testDataDir, noSuchFileName );
        try ( FileInputStream inStr = new FileInputStream( inError ) )
        {
            System.out.println( "no input test failed" );
        }
        catch ( IOException exc )
        {
            System.out.println( exc.getMessage() );
        }

        File    outError = new File( testDataDir, noAccessName );
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
    public void testProfileFileManager()
    {
        ProfileFileManager  test    = new ProfileFileManager();
        test.getLastResult();
        assertNull( test.getCurrFile() );
    }

    @Test
    public void testGetCurrFile()
    {
        Thread  thread  = executeNonChooserOp( 
            () -> adHocProfile = fileMgr.open( baseFile )
        );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertEquals( baseFile, fileMgr.getCurrFile() );

        thread  = executeNonChooserOp( 
            () -> fileMgr.save( adHocFile )
        );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertEquals( adHocFile, fileMgr.getCurrFile() );
    }

    @Test
    public void testGetLastResultTrue()
    {
        Thread  thread  = executeNonChooserOp( 
            () -> adHocProfile = fileMgr.open( baseFile )
        );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertTrue( fileMgr.getLastResult() );
    }

    @Test
    public void testGetLastResultFalse()
    {
        Thread  thread  = executeNonChooserOp( 
            () -> adHocProfile = fileMgr.open( noSuchFile )
        );
        assertTrue( dismissErrorDialog() );
        Utils.join( thread );
        assertFalse( fileMgr.getLastResult() );
    }

    @Test
    public void testClose()
    {
        Thread  thread  = executeNonChooserOp( 
            () -> adHocProfile = fileMgr.open( baseFile )
        );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertNotNull( fileMgr.getCurrFile() );
        
        fileMgr.close();
        thread  = executeNonChooserOp( fileMgr::close );
        Utils.join( thread );
        assertNull( fileMgr.getCurrFile() );
    }

    @Test
    public void testNewFileGoRight()
    {
        adHocFile.delete();
        // Sanity check
        assertFalse( adHocFile.exists() );
        Thread  thread  = executeChooserOp( 
            () -> fileMgr.newFile()
        );
        enterPath( adHocName );
        clickOn( saveButton );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        validateState( adHocFile, true, ProfileFileManager.APPROVE );
    }

    @Test
    public void testOpenGoRight()
    {
        int expAction   = fileMgr.getLastAction();
        Thread  thread  = executeChooserOp( 
            () -> adHocProfile = fileMgr.open()
        );
        enterPath( baseName );
        clickOn( openButton );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertEquals( baseProfile, adHocProfile );
        validateState( baseFile, true, expAction );
    }

    @Test
    public void testOpenGoWrong()
    {
        Thread  thread  = executeChooserOp( 
            () -> adHocProfile = fileMgr.open()
        );
        enterPath( noSuchFileName );
        clickOn( openButton );
        assertTrue( dismissErrorDialog() );
        Utils.join( thread );
        assertNull( adHocProfile );
        assertNull( fileMgr.getCurrFile() );
        validateState( null, false, ProfileFileManager.APPROVE );
    }

    @Test
    public void testOpenCancel()
    {
        File    expFile = fileMgr.getCurrFile();
        Thread  thread  = executeChooserOp( 
            () -> adHocProfile = fileMgr.open()
        );
        enterPath( baseName );
        clickOn( cancelButton );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertNull( adHocProfile );
        validateState( expFile, true, ProfileFileManager.CANCEL );
    }

    @Test
    public void testOpenProfileGoRight()
    {
        Profile testProfile = new Profile();
        // sanity check
        assertEquals( baseProfile, testProfile );
        
        // Overwrite profile with properties from distinctFile
        Thread  thread  = executeChooserOp( 
            () -> adHocProfile = fileMgr.open( testProfile )
        );
        enterPath( distinctName );
        clickOn( openButton );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertEquals( distinctProfile, testProfile );
        validateState( distinctFile, true, ProfileFileManager.APPROVE );
    }

    @Test
    public void testOpenProfileGoWrong()
    {
        Profile testProfile = new Profile();
        // sanity check
        assertEquals( baseProfile, testProfile );
        
        // Try/fail to overwrite profile with properties from noSuchFile
        Thread  thread  = executeChooserOp( 
            () -> adHocProfile = fileMgr.open( testProfile )
        );
        enterPath( noSuchFileName );
        clickOn( openButton );
        assertTrue( dismissErrorDialog() );
        Utils.join( thread );
        assertEquals( baseProfile, testProfile );
        validateState( null, false, ProfileFileManager.APPROVE );
    }

    @Test
    public void testOpenProfileCancel()
    {
        Profile testProfile = new Profile();
        // sanity check
        assertEquals( baseProfile, testProfile );
        
        // Start to overwrite profile with properties from 
        // distinctFile, but cancel before completion
        Thread  thread  = executeChooserOp( 
            () -> fileMgr.open( testProfile )
        );
        enterPath( distinctName );
        clickOn( cancelButton );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertEquals( baseProfile, testProfile );
        validateState( baseFile, true, ProfileFileManager.CANCEL );
    }

    @Test
    public void testOpenFile()
    {
        Thread  thread  = executeNonChooserOp( 
            () -> adHocProfile = fileMgr.open( baseFile )
        );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertEquals( baseProfile, adHocProfile );
        validateState( baseFile, true, ProfileFileManager.APPROVE );
    }
    
    @Test
    public void testSaveProfileFileGoRight()
    {
        adHocFile.delete();
        // sanity check
        assertFalse( adHocFile.exists() );
        
        Profile profile = new Profile();
        // sanity check
        assertEquals( baseProfile, profile );
        
        int expAction   = fileMgr.getLastAction();
        profile.setName( "testSaveProfileFileGoRight" );
        Thread  thread  = executeNonChooserOp( 
            () -> fileMgr.save( profile, adHocFile )
        );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertTrue( adHocFile.exists() );
        validateState( adHocFile, true, expAction );

        thread  = executeNonChooserOp( 
            () -> adHocProfile = fileMgr.open( adHocFile )
        );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertEquals( profile, adHocProfile );
        assertEquals( adHocFile, fileMgr.getCurrFile() );
        validateState( adHocFile, true, expAction );
    }
    
    @Test
    public void testSaveProfileFileGoRightTemp()
    {
        adHocFile.delete();
        // sanity check
        assertFalse( adHocFile.exists() );

        int     expAction   = fileMgr.getLastAction();
        Profile profile     = new Profile();
        profile.setName( "testSaveProfileFileGoRight" );
        
        Thread  thread  = executeNonChooserOp( 
            () -> adHocResult = fileMgr.save( profile, adHocFile )
        );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertTrue( adHocResult );
        assertTrue( adHocFile.exists() );
        File    currFile    = fileMgr.getCurrFile();
        int     lastAction  = fileMgr.getLastAction();
        assertTrue( compareFiles( adHocFile, currFile ) );
        assertTrue( fileMgr.getLastResult() );
        assertEquals( expAction, lastAction );

        thread  = executeNonChooserOp( 
            () -> adHocProfile = fileMgr.open( adHocFile )
        );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertEquals( profile, adHocProfile );
        assertTrue( compareFiles( adHocFile, currFile ) );
        assertTrue( fileMgr.getLastResult() );
        assertEquals( expAction, lastAction );
    }
    
    @Test
    public void testSaveAsProfileGoRightTemp()
    {
        adHocFile.delete();
        // sanity check
        assertFalse( adHocFile.exists() );

        Profile profile     = new Profile();
        profile.setName( "testSaveProfileFileGoRight" );
        
        Thread  thread  = executeChooserOp( 
            () -> adHocResult = fileMgr.saveAs( profile )
        );
        enterPath( adHocName );
        clickOn( saveButton );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertTrue( adHocResult );
        assertTrue( adHocFile.exists() );
        File    currFile    = fileMgr.getCurrFile();
        int     lastAction  = fileMgr.getLastAction();
        assertTrue( compareFiles( adHocFile, currFile ) );
        assertTrue( fileMgr.getLastResult() );
        assertEquals( ProfileFileManager.APPROVE, lastAction );

        thread  = executeNonChooserOp( 
            () -> adHocProfile = fileMgr.open( adHocFile )
        );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertEquals( profile, adHocProfile );
        assertTrue( compareFiles( adHocFile, currFile ) );
        assertTrue( fileMgr.getLastResult() );
        assertEquals( ProfileFileManager.APPROVE, lastAction );
    }
    
    @Test
    public void testSaveProfileFileGoWrong()
    {
        int expAction   = fileMgr.getLastAction();
        Thread  thread  = executeNonChooserOp( 
            () -> fileMgr.save( new Profile(), noAccessFile )
        );
        assertTrue( dismissErrorDialog() );
        Utils.join( thread );
        validateState( null, false, expAction );
    }

    @Test
    public void testSaveProfileNoCurrFile()
    {
        adHocFile.delete();
        // sanity check
        assertFalse( adHocFile.exists() );
        fileMgr.close();
        
        Profile profile = new Profile();
        // sanity check
        assertEquals( baseProfile, profile );
        
        profile.setName( "testSaveProfileGoRight" );
        Thread  thread  = executeChooserOp( 
            () -> fileMgr.save( profile )
        );
        enterPath( adHocName );
        clickOn( saveButton );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertTrue( adHocFile.exists() );
        validateState( adHocFile, true, ProfileFileManager.APPROVE );
        
        thread  = executeChooserOp( 
            () -> adHocProfile = fileMgr.open( adHocFile )
        );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertEquals( profile, adHocProfile );
        validateState( adHocFile, true, ProfileFileManager.APPROVE );
    }

    @Test
    public void testSaveProfileToCurrFile()
    {
        int expAction   = fileMgr.getLastAction();
        // Establish currFile = adHocFile
        Thread  thread  = executeNonChooserOp( 
            () -> fileMgr.save( adHocFile )
        );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertTrue( adHocFile.exists() );
        validateState( adHocFile, true, expAction );
        
        // Write distinct properties to currFile (adHocFile)
        thread  = executeNonChooserOp( 
            () -> fileMgr.save( distinctProfile )
        );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        
        // Verify contents of adHocFile
        thread  = executeNonChooserOp( 
            () -> adHocProfile = fileMgr.open( adHocFile )
        );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertEquals( distinctProfile, adHocProfile );
        assertEquals( adHocFile, fileMgr.getCurrFile() );
        validateState( adHocFile, true, expAction );
    }

    @Test
    public void testSaveProfileGoWrong()
    {
        assertTrue( noAccessFile.exists() );
        assertFalse( noAccessFile.canWrite() );
        fileMgr.close();
        
        Profile profile = new Profile();
        // sanity check
        assertEquals( baseProfile, profile );
        
        profile.setName( "testSaveProfileGoWrong" );
        Thread  thread  = executeChooserOp( 
            () -> fileMgr.save( profile )
        );
        enterPath( noAccessName );
        clickOn( saveButton );
        assertTrue( dismissErrorDialog() );
        Utils.join( thread );
        validateState( null, false, ProfileFileManager.APPROVE );
    }

    @Test
    public void testSaveProfileCancel()
    {
        // Make sure currFile is null
        fileMgr.close();
        // sanity check
        assertNull( fileMgr.getCurrFile() );
        
        // Make sure adHoc file doesn't exist
        adHocFile.delete();
        // sanity check
        assertFalse( adHocFile.exists() );
        
        boolean expResult   = fileMgr.getLastResult();
        Profile profile     = new Profile();
        Thread  thread      = executeChooserOp( 
            () -> fileMgr.save( profile )
        );
        enterPath( adHocName );
        clickOn( cancelButton );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertFalse( adHocFile.exists() );
        validateState( null, expResult, ProfileFileManager.CANCEL );
    }

    @Test
    public void testSaveAsGoRight()
    {
        // Write distinct properties to ad hoc file
        Thread  thread  = executeChooserOp( 
            () -> adHocResult = fileMgr.saveAs()
        );
        enterPath( adHocName );
        clickOn( saveButton );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        validateState( adHocFile, true, ProfileFileManager.APPROVE );
        
        // Verify ad hoc file exists and contains base properties
        assertTrue( adHocFile.exists() );
        thread  = executeNonChooserOp( 
            () -> adHocProfile = fileMgr.open( adHocFile )
        );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertEquals( baseProfile, adHocProfile );
        validateState( adHocFile, true, ProfileFileManager.APPROVE );
    }

    @Test
    public void testSaveAsCancel()
    {
        // Start to write adHocfile then cancel
        Thread  thread  = executeChooserOp( 
            () -> fileMgr.saveAs()
        );
        enterPath( adHocName );
        clickOn( cancelButton );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        
        // Verify ad hoc file not written
        assertFalse( adHocFile.exists() );
        validateState( baseFile, true, ProfileFileManager.CANCEL );
    }

    @Test
    public void testSaveAsProfileGoRight()
    {
        // Write distinct properties to ad hoc file
        Profile profile = new Profile();
        profile.setName( "testSaveAsProfile" );
        Thread  thread  = executeChooserOp( 
            () -> fileMgr.saveAs( profile )
        );
        enterPath( adHocName );
        clickOn( saveButton );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        
        // Verify ad hoc file exists and contains modified properties
        validateFile( profile, adHocFile );
    }

    @Test
    public void testSaveAsProfileCancel()
    {
        // Start to write adHoc file then cancel
        Profile profile = new Profile();
        profile.setName( "testSaveAsProfile" );
        Thread  thread  = executeChooserOp( 
            () -> fileMgr.saveAs( profile )
        );
        enterPath( adHocName );
        clickOn( cancelButton );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        
        // Verify ad hoc file exists and contains modified properties
        assertFalse( adHocFile.exists() );
        validateState( baseFile, true, ProfileFileManager.CANCEL );
    }

    @Test
    public void testSaveProfileFile()
    {
        adHocFile.delete();
        // sanity check
        assertFalse( adHocFile.exists() );

        // Write distinct properties to ad hoc file
        Profile profile = new Profile();
        profile.setName( "testSaveAsProfile" );
        Thread  thread  = executeNonChooserOp( 
            () -> fileMgr.save( profile, adHocFile )
        );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        validateState( adHocFile, true, ProfileFileManager.APPROVE );
        
        // Verify ad hoc file exists and contains modified properties
        assertTrue( adHocFile.exists() );
        validateFile( profile, adHocFile );
    }
    
    private void validateFile( Profile profile, File file )
    {
        assertTrue( file.exists() );
        Thread  thread  = executeNonChooserOp( 
            () -> adHocProfile = fileMgr.open( file )
        );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertEquals( profile, adHocProfile );
        validateState( file, true, ProfileFileManager.APPROVE );
    }
    
    private static void saveProfile( Profile profile, File file ) 
        throws Exception
    {
        ProfileParser   parser      = new ProfileParser( profile );
        try ( 
            FileOutputStream fileStr = new FileOutputStream( file );
            PrintWriter writer = new PrintWriter( fileStr );
        )
        {
            parser.getProperties()
                .forEach( writer::println );
        }
    }

    /**
     * In the context of the EDT,
     * enters the given file name
     * into the JFileChooser's text field.
     * 
     * @param fileName  the given file name
     */
    private void enterPath( String fileName )
    {
        assertNotNull( chooserName );
        File    file    = new File( testDataDir, fileName );
        String  path    = file.getAbsolutePath();
        GUIUtils.schedEDTAndWait( () -> chooserName.setText( path ) );
    }
    
    /**
     * In the context of the EDT,
     * clicks on the given button.
     * 
     * @param button    the given button
     */
    private static void clickOn( AbstractButton button )
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
    private Thread executeChooserOp( Runnable runner )
    {
        Thread  thread  = new Thread( runner );
        thread.start();
        Utils.pause( 250 );
        getFileChooserComponents();
        assertTrue( fileChooser.isVisible() );
        return thread;
    }
    
    private Thread executeNonChooserOp( Runnable runner )
    {
        Thread  thread  = new Thread( runner );
        thread.start();
        Utils.pause( 250 );
        return thread;
    }
    
    private boolean dismissErrorDialog()
    {
        boolean dismissed   = false;
        Utils.pause( 250 );
        getErrorDialogAndOKButton();
        if ( errorDialogOKButton != null )
        {
            dismissed = true;
            clickOn( errorDialogOKButton );
        }
        return dismissed;
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
    private void getErrorDialogAndOKButton()
    {
        final boolean canBeFrame    = false;
        final boolean canBeDialog   = true;
        final boolean mustBeVis     = true;
        GUIUtils.schedEDTAndWait( () ->  {
            errorDialogOKButton = null;
            ComponentFinder finder  = 
                new ComponentFinder( canBeDialog, canBeFrame, mustBeVis );
            Window          window  = finder.findWindow( c -> true );
            if ( window != null )
            {
                assertTrue( window instanceof JDialog );
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
     * Delete the adHocFile;
     * save the baseProfile to the baseFile
     * via the file chooser.
     * <p>
     * Postconditions:
     * <ul>
     * <li>baseFile exists</li>
     * <li>adHocFile doesn't exist</li>
     * <li>currFile = baseFile</li>
     * <li>lastResult = true</li>
     * <li>lastAction = APPROVE</li>
     * <ul>
     */
    private void initFileManagerState()
    {
        adHocFile.delete();
        
        Thread  thread  = executeChooserOp( () -> fileMgr.open() );
        enterPath( baseName );
        clickOn( openButton );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        adHocProfile.reset();
        validateState( baseFile, true, ProfileFileManager.APPROVE );
        assertEquals( baseProfile, adHocProfile );
        assertFalse( adHocFile.exists() );
    };
    
    /**
     * Compare the names of two given File objects;
     * if both objects are null
     * the result is true.
     * Otherwise, the names are converted to uppercase
     * and compared for equality.
     * 
     * @param file1 the first given File object
     * @param file2 the second given File object
     * 
     * @return  true if the names of the given Files are equal
     */
    private static boolean compareFiles( File file1, File file2 )
    {
        boolean result  = false;
        if ( file1 == file2 )
            result = true;
        else if ( file1 == null )
            result = false;
        else
        {
            String  name1   = file1.getName().toUpperCase();
            String  name2   = file2.getName().toUpperCase();
            result  = name1.equals( name2 );
        }
        return result;
    }

    /**
     * Asserts that the given file, result, and action
     * match the return values of ProfileFileManager methods
     * getCurrFile, getLastResult, and getLastAction.
     * 
     * @param expFile   the given file
     * @param expResult the given result
     * @param expAction the given action
     */
    private void 
    validateState( File expFile, boolean expResult, int expAction )
    {
        File    currFile    = fileMgr.getCurrFile();
        assertTrue( compareFiles( expFile, currFile ) );
        assertEquals( expResult, fileMgr.getLastResult() );
        assertEquals( expAction, fileMgr.getLastAction() );
    }
    
    /**
     * This method gets the dialog
     * that encapsulates the JFileChooser (if necessary)
     * and the required constituent components.
     * It must only be called 
     * after we expect the dialog 
     * to be visible (see {@link #getChooserDialog()}). 
     * Additionally, the state of the dialog 
     * is generally unpredictable; 
     * for example, sometimes the Save button exists, 
     * and sometimes it doesn't. 
     * Therefore, this method must be called 
     * every time a reference 
     * to a component is needed. 
     * <p>
     * Precondition: The FileChooser dialog is visible.
     * 
     * @see #getChooserDialog()
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
        
        // Get chooser dialog if necessary
        if ( chooserDialog == null )
            chooserDialog = getChooserDialog();
        
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
    
    /**
     * Obtains a reference
     * to the dialog that
     * encapsulates the JFileChooser.
     * Since we can't be sure
     * when the dialog is created
     * we must not call this method
     * until we've executed an operation
     * that requires it.
     * <p>
     * Precondition:
     * The JFileChooser dialog must be visible.
     * 
     * @return  the dialog that encapsulates the JFileChooser
     * 
     * @see #getFileChooserComponents()
     */
    private JDialog getChooserDialog()
    {
        final boolean           canBeFrame  = false;
        final boolean           canBeDialog = true;
        final boolean           mustBeVis   = true;
        final String            title       = fileChooser.getDialogTitle();
        final Predicate<Window> isDialog    = w -> (w instanceof JDialog);
        final Predicate<Window> hasTitle    = 
            w -> title.equals( ((JDialog)w).getTitle() );
        final Predicate<Window> pred        = isDialog.and( hasTitle );
        
        ComponentFinder     finder      = 
            new ComponentFinder( canBeDialog, canBeFrame, mustBeVis );
        Window          window  = finder.findWindow( pred );
        assertNotNull( window );
        assertTrue( window instanceof JDialog );
        return (JDialog)window;
    }
}
