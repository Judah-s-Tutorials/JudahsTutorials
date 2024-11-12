package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Component;
import java.awt.Window;
import java.io.File;
import java.util.function.Predicate;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.ProfileFileManager;
import com.acmemail.judah.cartesian_plane.components.ProfileEditorDialog;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;

/**
 * An instance of this class
 * is used to display and manage a ProfileEditor.
 * Interaction with the ProfileEditor
 * is conducted via operations
 * performed on the EDT.
 * 
 * @author Jack Straub
 * 
 * @see ProfileEditorTestBase
 */
public class ProfileEditorDialogTestGUI extends ProfileEditorTestBase
{
    /** How long to wait for dialog to post. */
    private static final long                   pauseInterval   = 125;
    /** The singleton for this GUI test object. */
    private static ProfileEditorDialogTestGUI   testGUI;
    /** Directory containing test data. */
    private static final  File      testDataDir     = 
        ProfileFileManagerTestData.getTestDataDir();

    /** The dialog under test. */
    private final ProfileEditorDialog  testDialog;
    /** The file manager used by testDialog. */
    private final ProfileFileManager    fileMgr;
    
    /** The dialog's OK button. */
    private final JButton               okButton;
    /** The dialog's Apply button. */
    private final JButton               applyButton;
    /** The dialog's Reset button. */
    private final JButton               resetButton;
    /** The dialog's Cancel button. */
    private final JButton               cancelButton;
    /** The dialog's Open button. */
    private final JButton               openButton;
    /** The dialog's Save As button. */
    private final JButton               saveAsButton;
    /** The dialog's Save button. */
    private final JButton               saveButton;
    /** The dialog's Close button. */
    private final JButton               closeButton;

    /** The OK button from the dialog for displaying I/O errors. */
    private AbstractButton errorDialogOKButton;
    
    /** The FileChooser from the fileMgr. */
    private JFileChooser      fileChooser;
    
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
    private JButton         openFileButton  = null;
    /** The Save button in the FileChooser dialog, if present. */
    private JButton         saveFileButton  = null;
    /** The Cancel button in the FileChooser dialog, if present. */
    private JButton         cancelFileButton    = null;

    /** The last result returned by the dialog. */
    private int     lastDialogResult;
    
    /** The last result returned in an I/O operation. */
    private Object  lastIOResult;
    
    /**
     * Instantiates and returns a ProfileEditorDialogTestGUI.
     * May be invoked from within the EDT.
     * If not invoked from the EDIT,
     * instantiation is scheduled via a task on the EDT.
     * 
     * @param profile   
     *      the profile to be encapsulated in the dialog
     *      
     * @return the instantiated ProfileEditorTestGUI_old
     */
    public static ProfileEditorDialogTestGUI getTestGUI( Profile profile )
    {
        if ( testGUI != null )
            ;
        else if ( SwingUtilities.isEventDispatchThread() )
            initGUI( profile );
        else
            GUIUtils.schedEDTAndWait( () -> initGUI( profile ) );
        return testGUI;
    }
    
    /**
     * Constructor.
     * Fully initializes this ProfileEditorTestGUI_old.
     * Must be invoked from the EDT.
     * 
     * @param profile   Profile to install in the ProfileEditor
     */
    private ProfileEditorDialogTestGUI( ProfileEditorDialog dialog )
    {
        super( dialog.getProfileEditor() );
        testDialog = dialog;
        fileMgr = testDialog.getFileManager();
        fileChooser = fileMgr.getFileChooser();
        okButton = getButton( "OK" );
        applyButton = getButton( "Apply" );
        resetButton = getButton( "Reset" );
        cancelButton = getButton( "Cancel" );
        openButton = getButton( "Open File" );
        saveButton = getButton( "Save" );
        saveAsButton = getButton( "Save As" );
        closeButton = getButton( "Close File" );
    }
    
    /**
     * Starts the test dialog in a dedicated thread.
     * Returns after the dialog becomes visible.
     * 
     * @return  the ID of the thread running the dialog
     */
    public Thread postDialog()
    {
        Thread  thread  = new Thread( 
            () -> lastDialogResult = testDialog.showDialog()
        );
        GUIUtils.schedEDTAndWait( () -> thread.start() );
        while ( !testDialog.isVisible() )
            Utils.pause( 1 );
        Utils.pause( 10 );
        return thread;
    }
    
    /**
     * This is not the method to invoke
     * to push the test dialog's cancel button.
     * It is a cleanup method to be called
     * in the event that a test aborts
     * with the test dialog still displayed.
     * It cancels the dialog
     * and waits for it to become invisible.
     * 
     * @see #pushCancelButton()
     */
    public void cancelDialog()
    {
        cancelFontDialog();
        if ( testDialog.isVisible() )
        {
            pushCancelButton();
            while ( testDialog.isVisible() )
                Utils.pause( 2 );
        }
    }
    
    /**
     * Indicates whether or not the dialog under test
     * is visible.
     * 
     * @return  true if the dialog under test is visible
     */
    public boolean isVisible()
    {
        return testDialog.isVisible();
    }
    
    /**
     * Gets the last result returned when the dialog
     * was dismissed.

     * @return
     *  the last result returned when the dialog was dismissed.
     */
    public int getLastDialogResult()
    {
        return lastDialogResult;
    }
    
    /**
     * Pushes the dialog's OK button.
     */
    public void pushOKButton()
    {
        pushButton( okButton );
    }
    
    /**
     * Pushes the dialog's Apply button.
     */
    public void pushApplyButton()
    {
        pushButton( applyButton );
    }
    
    /**
     * Pushes the dialog's Reset button.
     */
    public void pushResetButton()
    {
        pushButton( resetButton );
    }
    
    /**
     * Pushes the dialog's Cancel button.
     */
    public void pushCancelButton()
    {
        pushButton( cancelButton );
    }
    
    /**
     * Pushes the dialog's Open File button.
     */
    public void pushOpenButton()
    {
        pushButton( openButton );
    }
    
    /**
     * Pushes the dialog's Save button.
     */
    public void pushSaveButton()
    {
        pushButton( saveButton );
    }
    
    /**
     * Pushes the dialog's Save As button.
     */
    public void pushSaveAsButton()
    {
        pushButton( saveAsButton );
    }
    
    /**
     * Pushes the dialog's Close File button.
     */
    public void pushCloseButton()
    {
        pushButton( closeButton );
    }
    
    public void openFile( File file )
    {
        execFileOp( 
            this::pushOpenButton, 
            file, 
            true, // expectChooser
            true, // approve
            false // expectError
        );
    }
    
    public void saveAs( File file )
    {
        execFileOp( 
            this::pushSaveAsButton, 
            file, 
            true, // expectChooser
            true, // approve
            false // expectError
        );
    }
    
    public Profile getProfile( File file )
    {
        Profile profile = fileMgr.open( file );
        return profile;
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
    public void execFileOp(
        Runnable runner, 
        File file, 
        boolean expectChooser,
        boolean approve,
        boolean expectError
    )
    {
        Thread      thread      = new Thread( runner );
        thread.start();

        if ( expectChooser )
        {
            Utils.pause( pauseInterval );
            String      name        = file.getName();
            getFileChooserComponents();
            assertTrue( fileChooser.isVisible() );
            enterPath( name );
            AbstractButton  targetButton    = openFileButton != null ?
                openFileButton : saveFileButton;
            assertNotNull( targetButton );
            pushButton( targetButton );
        }
        
        boolean dismissStatus   = dismissErrorDialog();
        assertEquals( expectError, dismissStatus );
        Utils.join( thread );
    }
    
    public File getCurrFile()
    {
        return fileMgr.getCurrFile();
    }

    /**
     * Instantiates a ProfileEditorDialogTestGUI.
     * Must be invoked from within the EDT.
     * 
     * @param profile
     *      the profile to be encapsulated in the dialog
     */
    private static void initGUI( Profile profile )
    {
        ProfileEditorDialog dialog = 
            new ProfileEditorDialog( null, profile );
        testGUI = new ProfileEditorDialogTestGUI( dialog );
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
            pushButton( errorDialogOKButton );
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
            Predicate<Window>   wPred   = 
                w -> !(w instanceof ProfileEditorDialog);
            Window              window  = 
                finder.findWindow( wPred );
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
     * Activates the given button
     * in the context of the EDT.
     * 
     * @param button    the given button
     */
    private static void pushButton( AbstractButton button )
    {
        GUIUtils.schedEDTAndWait( () -> button.doClick() );
    }
    
    /**
     * From the test dialog
     * get the JButton with the given text.
     * 
     * @param text  the given text
     * 
     * @return  the JButton with the given text
     */
    private JButton getButton( String text )
    {
        Predicate<JComponent>   pred    = 
            ComponentFinder.getButtonPredicate( text );
        JComponent              comp    = 
            ComponentFinder.find( testDialog, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JButton );
        return (JButton)comp;
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
        cancelFileButton = (JButton)comp;
        
        // The Open and Save buttons may not both be present, but
        // at least one of them must be.
        openFileButton = null;
        comp = ComponentFinder.find( fileChooser, openPred );
        if ( comp != null )
        {
            assertTrue( comp instanceof JButton );
            openFileButton = (JButton)comp;
        }
        saveFileButton = null;
        comp = ComponentFinder.find( fileChooser, savePred );
        if ( comp != null )
        {
            assertTrue( comp instanceof JButton );
            saveFileButton = (JButton)comp;
        }
        assertTrue( openFileButton != null || saveFileButton != null );
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
}
