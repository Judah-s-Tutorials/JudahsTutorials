package com.acmemail.judah.cartesian_plane;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Component;
import java.awt.Window;
import java.io.File;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
import com.acmemail.judah.cartesian_plane.test_utils.ProfileFileManagerTestData;
import com.acmemail.judah.cartesian_plane.test_utils.ProfileUtils;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

public class ProfileFileManagerTest
{
    /** How long to wait for dialog to post. */
    private static final long       pauseInterval   = 125;
    /** Directory containing test data. */
    private static final  File      testDataDir     = 
        ProfileFileManagerTestData.getTestDataDir();
    /** File containing base test data; should never be modified. */
    private static final File       baseFile        = 
        ProfileFileManagerTestData.getBaseFile();
    /** 
     * File containing test data distinct from baseFile;
     *  should never be modified. 
     */
    private static final File       distinctFile    = 
        ProfileFileManagerTestData.getDistinctFile();
    /** File for use by test methods as needed. */
    private static final File       adHocFile       = 
        ProfileFileManagerTestData.getAdhocFile();
    /** 
     * File that can be read but not written; 
     * for error testing output operations.
     */
    private static final File       readOnlyFile    = 
        ProfileFileManagerTestData.getReadonlyFile();
    /** File that never exists; for error testing input operations. */
    private static final File       noSuchFile      = 
        ProfileFileManagerTestData.getNosuchFile();
    
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
     * <pre>    adHocResult = supplier.getAsBoolean()</pre>
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
    public static void beforeAll() throws Exception
    {
    }
    
    @AfterAll
    public static void afterAll() throws Exception
    {
        ProfileFileManagerTestData.shutDown();
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
    public void testProfileFileManager()
    {
        ProfileFileManager  test    = new ProfileFileManager();
        test.getLastResult();
        assertNull( test.getCurrFile() );
    }

    @Test
    public void testGetCurrFile()
    {
        assertNull( fileMgr.getCurrFile() );
        Supplier<Profile>   supplier    = () -> fileMgr.open( baseFile );
        openGoRight( supplier, baseFile, false );
        File    currFile    = fileMgr.getCurrFile();
        assertTrue( 
            ProfileFileManagerTestData.compareFiles( baseFile, currFile )
        );
    }

    @Test
    public void testGetLastResult()
    {
        assertFalse( fileMgr.getLastResult() );
        
        setLastResult( true );
        assertTrue( fileMgr.getLastResult() );
        
        setLastResult( false );
        assertFalse( fileMgr.getLastResult() );
    }

    @Test
    public void testClose()
    {
        Supplier<Profile>   supplier    = () -> fileMgr.open( baseFile );
        openGoRight( supplier, baseFile, false );
        assertNotNull( fileMgr.getCurrFile() );
        
        fileMgr.close();
        assertNull( fileMgr.getCurrFile() );
    }
    
    @Test
    public void testGetFileChooser()
    {
        assertNotNull( fileMgr.getFileChooser() );
    }

    @Test
    public void testOpenGoRight()
    {
        Supplier<Profile>   supplier    = () -> fileMgr.open();
        openGoRight( supplier, distinctFile, true );
        assertEquals( distinctProfile, adHocProfile );
    }

    @Test
    public void testOpenGoWrong()
    {
        Supplier<Profile>   supplier    = () -> fileMgr.open();
        openGoWrong( supplier, noSuchFile, true );
    }

    @Test
    public void testOpenCancel()
    {
        Supplier<?> supplier    = () -> fileMgr.open();
        cancelOperation( supplier, adHocFile );
        assertFalse( adHocFile.exists() );
    }

    @Test
    public void testOpenProfileGoRight()
    {
        Profile profile = new Profile();
        Supplier<Profile>   supplier    = () -> fileMgr.open( profile );
        openGoRight( supplier, distinctFile, true );
        assertEquals( distinctProfile, profile );
    }

    @Test
    public void testOpenProfileGoWrong()
    {
        Profile profile = new Profile();
        Supplier<Profile>   supplier    = () -> fileMgr.open( profile );
        openGoWrong( supplier, noSuchFile, true );
        assertEquals( baseProfile, profile );
    }

    @Test
    public void testOpenProfileCancel()
    {
        Profile profile = new Profile();
        Supplier<?> supplier    = () -> fileMgr.open( profile );
        cancelOperation( supplier, distinctFile);
        assertEquals( baseProfile, profile );
    }

    @Test
    public void testOpenFileGoRight()
    {
        Supplier<Profile>   supplier    = 
            () -> fileMgr.open( distinctFile );
        openGoRight( supplier, distinctFile, false );
        assertEquals( distinctProfile, adHocProfile );
    }

    @Test
    public void testOpenFileGoWrong()
    {
        Supplier<Profile>   supplier    = 
            () -> fileMgr.open( noSuchFile );
        openGoWrong( supplier, distinctFile, false );
    }

    @Test
    public void testOpenFileProfileGoRight()
    {
        Profile profile = new Profile();
        Supplier<Profile>   supplier    = 
            () -> fileMgr.open( distinctFile, profile );
        openGoRight( supplier, distinctFile, false );
        assertEquals( distinctProfile, profile );
    }

    @Test
    public void testOpenFileProfileGoWrong()
    {
        Profile profile = new Profile();
        Supplier<Profile>   supplier    = 
            () -> fileMgr.open( noSuchFile, profile );
        openGoWrong( supplier, distinctFile, false );
        assertEquals( baseProfile, profile );
    }

    @Test
    public void testNewFileGoRight()
    {
        BooleanSupplier supplier    = () -> fileMgr.newFile();
        saveGoRight( supplier, adHocFile, true );
        ProfileFileManagerTestData.validateFile( baseProfile, adHocFile );
    }

    @Test
    public void testNewFileGoWrong()
    {
        BooleanSupplier supplier    = () -> fileMgr.newFile();
        saveGoWrong( supplier, readOnlyFile, true );
    }
    
    @Test
    public void testNewFileCancel()
    {
        Supplier<?> supplier    = () -> fileMgr.newFile();
        cancelOperation( supplier, adHocFile );
        assertFalse( adHocFile.exists() );
    }
    
    @Test
    public void testSaveFileGoRight()
    {
        BooleanSupplier supplier    = () -> fileMgr.save( adHocFile );
        saveGoRight( supplier, adHocFile, false );
        ProfileFileManagerTestData.validateFile( baseProfile, adHocFile );
    }

    @Test
    public void testSaveFileGoWrong()
    {
        BooleanSupplier supplier    = () -> fileMgr.save( readOnlyFile );
        saveGoWrong( supplier, readOnlyFile, false );
    }

    @Test
    public void testSaveProfile_CurrFileGoRight()
    {
        // make sure there's a file open; write distinct data to it
        BooleanSupplier  supplier    = 
            () -> fileMgr.save( distinctProfile, adHocFile );
        saveGoRight( supplier, adHocFile, false );
        // sanity check
        ProfileFileManagerTestData.validateFile( 
            distinctProfile, 
            adHocFile 
        );

        // save base data to currently open file (adHocFile)
        supplier = () -> fileMgr.save( baseProfile );
        saveGoRight( supplier, adHocFile, false );
        // sanity check
        ProfileFileManagerTestData.validateFile( baseProfile, adHocFile );
    }

    @Test
    public void testSaveProfile_CurrFileGoWrong()
    {
        // read the no-write file
        Supplier<Profile>   readSupplier    = 
            () -> fileMgr.open( readOnlyFile );
        openGoRight( readSupplier, readOnlyFile, false );

        // try to save to currently open file (no-write file)
        BooleanSupplier supplier = () -> fileMgr.save( baseProfile );
        saveGoWrong( supplier, readOnlyFile, false );
    }
    
    @Test
    public void testSaveProfile_NoCurrFileGoRight()
    {
        // Preconditions: adHocFile doesn't exist, getCurrFile()a
        // returns null.
        assertFalse( adHocFile.exists() );
        assertNull( fileMgr.getCurrFile() );
        
        BooleanSupplier supplier = () -> fileMgr.save( distinctProfile );
        saveGoRight( supplier, adHocFile, true );
        // sanity check
        ProfileFileManagerTestData.validateFile( 
            distinctProfile, 
            adHocFile
        );
    }

    @Test
    public void testSaveProfile_NoCurrFileGoWrong()
    {
        // Preconditions: adHocFile doesn't exist, getCurrFile()a
        // returns null.
        assertFalse( adHocFile.exists() );
        assertNull( fileMgr.getCurrFile() );
        
        // try to save to currently open file (no-write file)
        BooleanSupplier supplier = () -> fileMgr.save( baseProfile );
        saveGoWrong( supplier, readOnlyFile, true );
    }

    @Test
    public void testSaveProfile_NoCurrFileCancel()
    {
        // Preconditions: adHocFile doesn't exist, getCurrFile()a
        // returns null.
        assertFalse( adHocFile.exists() );
        assertNull( fileMgr.getCurrFile() );
        
        Supplier<?> supplier = () -> fileMgr.save( baseProfile );
        cancelOperation( supplier, adHocFile );
        assertFalse( adHocFile.exists() );
    }

    @Test
    public void testSaveAsGoRight()
    {
        BooleanSupplier  supplier    = () -> fileMgr.saveAs();
        saveGoRight( supplier, adHocFile, true );
        ProfileFileManagerTestData.validateFile( baseProfile, adHocFile );
    }

    @Test
    public void testSaveAsGoWrong()
    {
        BooleanSupplier  supplier    = () -> fileMgr.saveAs();
        saveGoWrong( supplier, readOnlyFile, true );
    }

    @Test
    public void testSaveAsCancel()
    {
        Supplier<?> supplier    = () -> fileMgr.saveAs();
        cancelOperation( supplier, adHocFile);
        assertFalse( adHocFile.exists() );
    }

    @Test
    public void testSaveAsProfileGoRight()
    {
        BooleanSupplier supplier    = 
            () -> fileMgr.saveAs( distinctProfile );
        saveGoRight( supplier, adHocFile, true );
        ProfileFileManagerTestData.validateFile( 
            distinctProfile, 
            adHocFile 
        );
    }

    @Test
    public void testSaveAsProfileGoWrong()
    {
        BooleanSupplier  supplier    = () -> fileMgr.saveAs();
        saveGoWrong( supplier, readOnlyFile, true );
    }

    @Test
    public void testSaveAsProfileCancel()
    {
        Supplier<?> supplier    = () -> fileMgr.saveAs( distinctProfile );
        cancelOperation( supplier, adHocFile);
        assertFalse( adHocFile.exists() );
    }

    @Test
    public void testSaveProfileFileGoRight()
    {
        BooleanSupplier  supplier    = 
            () -> fileMgr.save( distinctProfile, adHocFile );
        saveGoRight( supplier, adHocFile, false );
        ProfileFileManagerTestData.validateFile( 
            distinctProfile, 
            adHocFile
        );
    }
    
    @Test
    public void testSaveProfileFileGoWrong()
    {
        BooleanSupplier  supplier    = 
            () -> fileMgr.save( distinctProfile, readOnlyFile );
        saveGoWrong( supplier, readOnlyFile, false );
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
     * Execute the given operation
     * against the given file
     * in a dedicated thread;
     * does not return
     * until the dedicated thread dies.
     * The given operation is assumed to be a save operation.
     * The given file is deleted at the start of the operation,
     * and its existence is confirmed at the end of the operation.
     * If a file chooser dialog is expected,
     * the file name is entered into its file name field,
     * and its Save button is pushed.
     * If the file chooser is expected
     * but is not posted,
     * an assertion is triggered.
     * The operation is expected to complete normally;
     * if an error dialog is posted
     * an assertion is triggered.
     * 
     * @param supplier      the given operation
     * @param file          the given file
     * @param expectChooser 
     *      true if the operation requires interaction
     *      with the file chooser
     */
    private void saveGoRight( 
        BooleanSupplier supplier, 
        File file, 
        boolean expectChooser
    )
    {
        file.delete();
        assertFalse( file.exists() );
        
        Runnable    edtRunner   = 
            () -> adHocResult = supplier.getAsBoolean();
        Runnable    runner      = 
            () -> GUIUtils.schedEDTAndWait( edtRunner );
        String      name        = file.getName();
        int         expAction   = fileMgr.getLastAction();
        Thread      thread  = new Thread( runner );
        thread.start();
        
        if ( expectChooser )
        {
            Utils.pause( pauseInterval );
            getFileChooserComponents();
            assertTrue( fileChooser.isVisible() );
            enterPath( name );
            clickOn( saveButton );
            expAction = ProfileFileManager.APPROVE;
        }
        
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertTrue( adHocResult );
        assertTrue( file.exists() );
        validateState( file, true, expAction );
    }
    
    
    /**
     * Execute the given operation
     * against the given file
     * in a dedicated thread
     * with the expectation
     * that the operation will fail
     * and post an error dialog.
     * This method does not return
     * until the dedicated thread dies.
     * If a file chooser dialog is expected,
     * the file name is entered into its file name field,
     * and its Save button is pushed.
     * If the file chooser is expected
     * but is not posted,
     * an assertion is triggered.
     * The operation is expected to fail with an I/O error
     * and post an error dialog;
     * If an error dialog is not posted
     * an assertion is triggered.
     * 
     * @param supplier      the given operation
     * @param file          the given file
     * @param expectChooser 
     *      true if the operation requires interaction
     *      with the file chooser
     */
    private void saveGoWrong( 
        BooleanSupplier supplier, 
        File file, 
        boolean expectChooser
    )
    {
        Runnable    edtRunner   = 
            () -> adHocResult = supplier.getAsBoolean();
        Runnable    runner      = 
            () -> GUIUtils.schedEDTAndWait( edtRunner );
        String      name        = file.getName();
        int         expAction   = fileMgr.getLastAction();
        
        Thread      thread  = new Thread( runner );
        thread.start();
        
        if ( expectChooser )
        {
            Utils.pause( pauseInterval );
            getFileChooserComponents();
            assertTrue( fileChooser.isVisible() );
            enterPath( name );
            clickOn( saveButton );
            expAction = ProfileFileManager.APPROVE;
        }

        assertTrue( dismissErrorDialog() );
        Utils.join( thread );
        assertFalse( adHocResult );
        validateState( null, false, expAction );
    }
    
    /**
     * Executes the given operation
     * against the given file
     * in a dedicated thread;
     * does not return
     * until the dedicated thread dies.
     * The given operation is assumed to be an open operation.
     * The existence of the given file 
     * is confirmed at the start of the operation.
     * If a file chooser dialog is expected,
     * the file name is entered into its file name field,
     * and its Open button is pushed.
     * If the file chooser is expected
     * but is not posted,
     * an assertion is triggered.
     * The operation is expected to complete normally;
     * if an error dialog is posted
     * an assertion is triggered.
     * 
     * @param supplier      the given operation
     * @param file          the given file
     * @param expectChooser 
     *      true if the operation requires interaction
     *      with the file chooser
     */
    private void openGoRight(
        Supplier<Profile> supplier, 
        File file, 
        boolean expectChooser
    )
    {
        assertTrue( file.exists() );
        
        Runnable    edtRunner   = 
            () -> adHocProfile = supplier.get();
        Runnable    runner      = 
            () -> GUIUtils.schedEDTAndWait( edtRunner );
        String      name        = file.getName();
        int         expAction   = fileMgr.getLastAction();
        Thread      thread      = new Thread( runner );
        thread.start();

        if ( expectChooser )
        {
            Utils.pause( pauseInterval );
            getFileChooserComponents();
            assertTrue( fileChooser.isVisible() );
            enterPath( name );
            clickOn( openButton );
            expAction = ProfileFileManager.APPROVE;
        }
        
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        validateState( file, true, expAction );
    }
    
    /**
     * Execute the given operation
     * against the given file
     * in a dedicated thread
     * with the expectation
     * that the operation will fail
     * and post an error dialog.
     * This method does not return
     * until the dedicated thread dies.
     * If a file chooser dialog is expected,
     * the file name is entered into its file name field,
     * and its Open button is pushed.
     * If the file chooser is expected
     * but is not posted,
     * an assertion is triggered.
     * The operation is expected to fail with an I/O error
     * and post an error dialog;
     * If an error dialog is not posted
     * an assertion is triggered.
     * 
     * @param supplier      the given operation
     * @param file          the given file
     * @param expectChooser 
     *      true if the operation requires interaction
     *      with the file chooser
     */
    private void openGoWrong(
        Supplier<Profile> supplier, 
        File file, 
        boolean expectChooser
    )
    {
        Runnable    edtRunner   = () -> adHocProfile = supplier.get();
        Runnable    runner      = 
            () -> GUIUtils.schedEDTAndWait( edtRunner );
        String      name        = file.getName();
        int         expAction   = fileMgr.getLastAction();
        Thread      thread      = new Thread( runner );
        thread.start();

        if ( expectChooser )
        {
            Utils.pause( pauseInterval );
            getFileChooserComponents();
            assertTrue( fileChooser.isVisible() );
            enterPath( name );
            clickOn( openButton );
            expAction = ProfileFileManager.APPROVE;
        }
        
        assertTrue( dismissErrorDialog() );
        Utils.join( thread );
        assertNull( adHocProfile );
        validateState( null, false, expAction );
    }
    
    /**
     * Begin a given operation
     * against a given file
     * in a dedicated thread
     * then cancel it via the file chooser.
     * This method does not return
     * until the dedicated thread dies.
     * When the file chooser is posted
     * the name of the given file
     * is entered into its name field
     * and its Cancel button is pushed.
     * If the file chooser is not posted
     * an assertion is triggered.
     * 
     * @param supplier  the given operation
     * @param file      the given file
     */
    private void cancelOperation(
        Supplier<?> supplier, 
        File file
    )
    {
        Runnable    edtRunner   = () -> supplier.get();
        Runnable    runner      = 
            () -> GUIUtils.schedEDTAndWait( edtRunner );
        String      name        = file.getName();
        File        expFile     = fileMgr.getCurrFile();
        boolean     expResult   = fileMgr.getLastResult();
        Thread      thread      = new Thread( runner );
        thread.start();
        
        Utils.pause( pauseInterval );
        getFileChooserComponents();
        assertTrue( fileChooser.isVisible() );
        enterPath( name );
        clickOn( cancelButton );

        Utils.join( thread );
        validateState( expFile, expResult, ProfileFileManager.CANCEL );
    }
    
    /**
     * Search for an error dialog;
     * if found,
     * dismiss it by pushing its OK button.
     * 
     * @return  true if the error dialog is found
     */
    private boolean dismissErrorDialog()
    {
        boolean dismissed   = false;
        Utils.pause( pauseInterval );
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
     * <li>adHocFile doesn't exist</li>
     * <li>currFile = null</li>
     * <li>lastResult = false</li>
     * <li>lastAction = CANCEL</li>
     * <ul>
     */
    private void initFileManagerState()
    {
        adHocFile.delete();
        assertFalse( adHocFile.exists() );
        setLastAction( ProfileFileManager.CANCEL );
        setLastResult( false );
        fileMgr.close();
        validateState( null, false, ProfileFileManager.CANCEL );
    };
    
    /**
     * Set the lastAction property of the file manager 
     * to the given action.
     * The property is set by initiating an operation
     * that requires interaction with the file chooser,
     * then pushing the file chooser button
     * that will set the lastAction property
     * to the given value, APPROVE or CANCEL.
     * If the given action is APPROVE
     * the client can expect the file manager's
     * lastResult and currFile properties to change.
     * 
     * @param action    the given action
     */
    private void setLastAction( int action )
    {
        Supplier<Profile>   supplier    = () -> fileMgr.open();
        if ( action == ProfileFileManager.APPROVE )
            openGoRight( supplier, baseFile, true );
        else
            cancelOperation( (Supplier<?>)supplier, adHocFile );
        assertEquals( action, fileMgr.getLastAction() );
    }
    
    /**
     * Set the lastResult property of the file manager 
     * to the given value.
     * The property is set by initiating an operation
     * that will either complete successfully
     * (if expResult is true)
     * or fail (if expResult is false).
     * The client can expect the file manager's
     * currFile property to change.
     * 
     * @param expResult    the given value
     */
    private void setLastResult( boolean expResult )
    {
        int expAction   = fileMgr.getLastAction();
        if ( expResult )
        {
            Supplier<Profile> supplier  = () -> fileMgr.open( baseFile );
            openGoRight( supplier, baseFile, false );
            validateState( baseFile, true, expAction );
        }
        else
        {
            Supplier<Profile>   supplier  = 
                () -> fileMgr.open( noSuchFile );
            openGoWrong( supplier, noSuchFile, false );
            validateState( null, false, expAction );
        }
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
        assertTrue( 
            ProfileFileManagerTestData.compareFiles( expFile, currFile )
        );
        assertEquals( expResult, fileMgr.getLastResult() );
        assertEquals( expAction, fileMgr.getLastAction() );
    }
    
    /**
     * This method gets the components of the dialog
     * that are required for testing;
     * If necessary,
     * the dialog itself is located.
     * This method must only be called 
     * after we expect the dialog 
     * to be visible (see {@link #getChooserDialog()}). 
     * Additionally, the state of the dialog 
     * should be considered unpredictable; 
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
