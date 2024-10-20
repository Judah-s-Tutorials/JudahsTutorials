package com.acmemail.judah.cartesian_plane;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Component;
import java.awt.Window;
import java.util.function.Predicate;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

class ProfileFileManagerTest
{
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
    static void setUpBeforeClass() throws Exception
    {
    }

    @BeforeEach
    void setUp() throws Exception
    {
    }

    @Test
    void testGetCurrFile()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetLastResult()
    {
        fail("Not yet implemented");
    }

    @Test
    void testClose()
    {
        fail("Not yet implemented");
    }

    @Test
    void testOpen()
    {
        fail("Not yet implemented");
    }

    @Test
    void testOpenFile()
    {
        fail("Not yet implemented");
    }

    @Test
    void testSaveProfile()
    {
        fail("Not yet implemented");
    }

    @Test
    void testSaveAs()
    {
        fail("Not yet implemented");
    }

    @Test
    void testSaveAsProfile()
    {
        fail("Not yet implemented");
    }

    @Test
    void testSaveProfileFile()
    {
        fail("Not yet implemented");
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
    private int expectDialog( Runnable runner )
    {
        Thread  thread          = new Thread( runner );
        int     dialogCounter   = 0;
        thread.start();
        while ( thread.isAlive() )
        {
            Utils.pause( 250 );
            getDialogAndOKButton();
            if ( errorDialogOKButton != null )
            {
                ++dialogCounter;
                okAndWait();
            }
        }
        return dialogCounter;
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
