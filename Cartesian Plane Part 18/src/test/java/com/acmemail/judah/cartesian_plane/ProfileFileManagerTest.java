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

public class ProfileFileManagerTest
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
        assertTrue( fileMgr.getLastResult() );
        assertEquals( adHocName, getCurrFileName() );
    }

    @Test
    public void testOpenGoRight()
    {
        Thread  thread  = executeChooserOp( 
            () -> adHocProfile = fileMgr.open()
        );
        enterPath( baseName );
        clickOn( openButton );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertEquals( baseProfile, adHocProfile );
        assertEquals( baseName, getCurrFileName() );
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
        assertNull( getCurrFileName() );
    }

    @Test
    public void testOpenCancel()
    {
        String  expName = getCurrFileName();
        Thread  thread  = executeChooserOp( 
            () -> adHocProfile = fileMgr.open()
        );
        enterPath( baseName );
        clickOn( cancelButton );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertNull( adHocProfile );
        String  actName = getCurrFileName();
        assertEquals( expName, actName );
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
        assertEquals( distinctName, getCurrFileName() );
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
        assertNull( getCurrFileName() );
    }

    @Test
    public void testOpenProfileCancel()
    {
        // Establish currFile name
        Thread  thread  = executeNonChooserOp( 
            () -> fileMgr.save( adHocFile )
        );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertEquals( adHocName, getCurrFileName() );

        Profile testProfile = new Profile();
        // sanity check
        assertEquals( baseProfile, testProfile );
        
        // Start to overwrite profile with properties from 
        // distinctFile, but cancel before completion
        thread  = executeChooserOp( 
            () -> fileMgr.open( testProfile )
        );
        enterPath( distinctName );
        clickOn( cancelButton );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertEquals( baseProfile, testProfile );
        assertEquals( adHocName, getCurrFileName() );
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
        assertEquals( baseName, getCurrFileName() );
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
        
        profile.setName( "testSaveProfileFileGoRight" );
        Thread  thread  = executeNonChooserOp( 
            () -> fileMgr.save( profile, adHocFile )
        );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertTrue( adHocFile.exists() );
        
        thread  = executeNonChooserOp( 
            () -> adHocProfile = fileMgr.open( adHocFile )
        );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertEquals( profile, adHocProfile );
        assertEquals( adHocFile, fileMgr.getCurrFile() );
        String  actFileName = getCurrFileName();
        assertEquals( adHocName, actFileName );
    }
    
    @Test
    public void testSaveProfileFileGoWrong()
    {
        Thread  thread  = executeNonChooserOp( 
            () -> fileMgr.save( new Profile(), noAccessFile )
        );
        assertTrue( dismissErrorDialog() );
        Utils.join( thread );
        assertNull( getCurrFileName() );
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
        
        thread  = executeChooserOp( 
            () -> adHocProfile = fileMgr.open( adHocFile )
        );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertEquals( profile, adHocProfile );
        assertEquals( adHocFile, fileMgr.getCurrFile() );
        String  actName = getCurrFileName();
        assertEquals( adHocName, actName );
    }

    @Test
    public void testSaveProfileToCurrFile()
    {
        // Establish currFile = adHocFile
        Thread  thread  = executeNonChooserOp( 
            () -> fileMgr.save( adHocFile )
        );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertTrue( adHocFile.exists() );
        assertEquals( adHocName, getCurrFileName() );
        
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
        String  actName = getCurrFileName();
        assertEquals( adHocName, actName );
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
        assertNull( getCurrFileName() );
    }

    @Test
    public void testSaveProfileCancel()
    {
        String  expName = getCurrFileName();
        adHocFile.delete();
        // sanity check
        assertFalse( adHocFile.exists() );
        fileMgr.close();
        
        Profile profile = new Profile();
        Thread  thread  = executeChooserOp( 
            () -> fileMgr.save( profile )
        );
        enterPath( adHocName );
        clickOn( cancelButton );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertFalse( adHocFile.exists() );
        assertNull( fileMgr.getCurrFile() );
        String  actName = getCurrFileName();
        assertEquals( expName, actName );
    }

    @Test
    public void testSaveAsGoRight()
    {
        adHocFile.delete();
        // sanity check
        assertFalse( adHocFile.exists() );
        
        // Write distinct properties to ad hoc file
        Thread  thread  = executeChooserOp( 
            () -> fileMgr.saveAs()
        );
        enterPath( adHocName );
        clickOn( saveButton );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        
        // Verify ad hoc file exists and contains base properties
        assertTrue( adHocFile.exists() );
        thread  = executeNonChooserOp( 
            () -> adHocProfile = fileMgr.open( adHocFile )
        );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertEquals( baseProfile, adHocProfile );
        String  currFileName    = getCurrFileName();
        assertEquals( adHocName, currFileName );
    }

    @Test
    public void testSaveAsCancel()
    {
        String  expName = getCurrFileName();
        adHocFile.delete();
        // sanity check
        assertFalse( adHocFile.exists() );
        
        // Write distinct properties to ad hoc file
        Thread  thread  = executeChooserOp( 
            () -> fileMgr.saveAs()
        );
        enterPath( adHocName );
        clickOn( cancelButton );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        
        // Verify ad hoc file not written
        assertFalse( adHocFile.exists() );
        String  actName = getCurrFileName();
        assertEquals( expName, actName );
    }

    @Test
    public void testSaveAsProfileGoRight()
    {
        adHocFile.delete();
        // sanity check
        assertFalse( adHocFile.exists() );
        
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
        assertTrue( adHocFile.exists() );
        thread  = executeChooserOp( 
            () -> adHocProfile = fileMgr.open( adHocFile )
        );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertEquals( profile, adHocProfile );
    }

    @Test
    public void testSaveAsProfileCancel()
    {
        adHocFile.delete();
        // sanity check
        assertFalse( adHocFile.exists() );
        
        // Write distinct properties to ad hoc file
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
        
        // Verify ad hoc file exists and contains modified properties
        assertTrue( adHocFile.exists() );
        thread  = executeNonChooserOp( 
            () -> adHocProfile = fileMgr.open( adHocFile )
        );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        assertEquals( profile, adHocProfile );
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

    private void enterPath( String fileName )
    {
        assertNotNull( chooserName );
        File    file    = new File( testDataDir, fileName );
        String  path    = file.getAbsolutePath();
        GUIUtils.schedEDTAndWait( () -> chooserName.setText( path ) );
    }
    
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
        getDialogAndOKButton();
        if ( errorDialogOKButton != null )
        {
            dismissed = true;
            GUIUtils.schedEDTAndWait( errorDialogOKButton::doClick );
            Utils.pause( 250 );
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
    private void getDialogAndOKButton()
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
    
    private String getCurrFileName()
    {
        File    currFile        = fileMgr.getCurrFile();
        String  currFileName    = currFile == null ?
            null : currFile.getName();
        return currFileName;
   
    }
    
    private void initFileManagerState()
    {
        Thread  thread  = executeChooserOp( () -> fileMgr.open() );
        enterPath( baseName );
        clickOn( openButton );
        assertFalse( dismissErrorDialog() );
        Utils.join( thread );
        validateState( baseName, true, ProfileFileManager.APPROVE );
        assertEquals( baseProfile, adHocProfile );
        assertEquals( baseName, getCurrFileName() );
    }
    
    private void 
    validateState( String expFile, boolean expResult, int expAction )
    {
        assertEquals( expFile, getCurrFileName() );
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
