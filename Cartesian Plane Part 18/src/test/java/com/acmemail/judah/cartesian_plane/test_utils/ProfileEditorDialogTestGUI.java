package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Predicate;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.Profile;
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
    /** The singleton for this GUI test object. */
    private static ProfileEditorDialogTestGUI testGUI;
    
    /** The dialog under test. */
    private final ProfileEditorDialog  testDialog;
    
    /** The dialog's OK button. */
    private final JButton               okButton;
    /** The dialog's Apply button. */
    private final JButton               applyButton;
    /** The dialog's Reset button. */
    private final JButton               resetButton;
    /** The dialog's Cancel button. */
    private final JButton               cancelButton;
    
    /** The last result returned by the dialog. */
    private int     lastDialogResult;
    
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
        okButton = getButton( "OK" );
        applyButton = getButton( "Apply" );
        resetButton = getButton( "Reset" );
        cancelButton = getButton( "Cancel" );
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
     * Activates the given button
     * in the context of the EDT.
     * 
     * @param button    the given button
     */
    private static void pushButton( JButton button )
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
}
