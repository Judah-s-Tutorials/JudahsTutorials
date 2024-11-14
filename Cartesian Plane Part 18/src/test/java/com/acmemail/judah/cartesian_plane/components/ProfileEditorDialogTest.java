package com.acmemail.judah.cartesian_plane.components;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.stream.Stream;

import javax.swing.JOptionPane;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.test_utils.ProfileEditorDialogTestGUI;
import com.acmemail.judah.cartesian_plane.test_utils.ProfileFileManagerTestData;
import com.acmemail.judah.cartesian_plane.test_utils.ProfileUtils;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

public class ProfileEditorDialogTest
{
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
     * Represents the properties of a Profile as determined by the
     * PropertyManager. Used as needed to return the PropertyManager
     * to its original state (see for example, {@link #beforeEach()}.
     * Never modified after initialization.
     */
    private static final Profile    baseProfile     = new Profile();
    /** 
     * Contains property values guaranteed to be different from those
     * stored in the BaseProfile. Never modified after initialization.
     */
    private static final Profile    distinctProfile = 
        ProfileUtils.getDistinctProfile( baseProfile );
    /** 
     * Profile used to initialize the test GUI/ProfileEditor.
     * After initialization the reference to the object
     * must not be changed,
     * but the contents of the object may be changed as needed.
     * The contents are restored to their original values before
     * each test (see {@link #beforeEach()}).
     */
    private static final Profile    profile         = new Profile();
    /** 
     * The object that displays and manager the ProfileEditor.
     * Guarantees that all interaction with the ProfileEditor
     * components is conducted via the EDT.
     */
    private static final ProfileEditorDialogTestGUI testGUI = 
        ProfileEditorDialogTestGUI.getTestGUI( profile );

    @BeforeEach
    public void beforeEach() throws Exception
    {
        // Restore the properties in the PropertyManager
        // to their original values.
        baseProfile.apply();
        profile.reset();
        
        // Return the working profile to its original state;
        // reset the components of the ProfileEditor to their
        // original values.
        testGUI.reset();
        
        // Verify that the working profile has been returned
        // to its original state.
        assertEquals( baseProfile, profile );
        
        // Verify that the components of the ProfileEditor GUI
        // have been returned to their original states.
        Profile testProfile = testGUI.getComponentValues();
        assertEquals( profile, testProfile );
    }
    
    @AfterEach
    public void afterEach()
    {
        // In the event that a test failed while the FontEditorDialog
        // is posted, this will dismiss it. If the dialog is not posted 
        // it has no effect.
        testGUI.cancelDialog();
        
    }
    
    @AfterAll
    public static void afterAll()
    {
        // For the sake of tests run in suites, make sure
        // the original profile is restored at the end of the test.
        // Also make sure all GUI windows are disposed.
        baseProfile.apply();
        ComponentFinder.disposeAll();
    }

    @Test
    public void testProfileEditorDialog()
    {
        // If it doesn't crash, it passes.
        new ProfileEditorDialog( null,new Profile() );
    }

    @Test
    public void testGetProfileEditor()
    {
        assertNotNull( testGUI.getProfileEditor() );
    }
    
    /**
     * Verify the reset operation
     * when there is no open file.
     * The reset operation
     * should restore the GUI component values
     * to Profile properties
     * stored in the PropertyManager.
     * <ol>
     * <li>Apply distinct properties to all components.</li>
     * <li>Verify distinct properties applied.</li>
     * <li>Push the close button.</li>
     * <li>Push reset button.</li>
     * <li>Verify all properties returned to original values.</li>
     * </ol>
     * <p>
     * Note:
     * To avoid interrupting the process
     * before the dialog under test is dismissed,
     * all verification takes place
     * after the dialog's cancel button
     * has been pushed.
     */
    @Test
    public void testResetFromBase()
    {
        Thread  thread  = testGUI.postDialog();
        Profile startProps          = testGUI.getComponentValues();
        assertEquals( baseProfile, startProps );
        applyDistinctProperties();
        Profile testDistinctProps   = testGUI.getComponentValues();
        assertEquals( testDistinctProps, distinctProfile );
        
        testGUI.pushCloseButton();
        testGUI.pushResetButton();
        Profile testResetProps      = testGUI.getComponentValues();
        
        // Dialog should still be deployed after reset
        assertTrue( testGUI.isVisible() );
        
        // Cancel the editor dialog and wait for it to quiesce.
        testGUI.pushCancelButton();
        Utils.join( thread );
        
        // Dialog should no longer be deployed
        assertFalse( testGUI.isVisible() );
        assertEquals( baseProfile, testResetProps );
    }
    
    /**
     * Verify the reset operation
     * when resetting from an open file.
     * The reset operation
     * should restore the GUI component values
     * to Profile properties
     * stored in the open file.
     * <ol>
     * <li>Apply distinct properties to all components.</li>
     * <li>Verify distinct properties applied.</li>
     * <li>Save toAdHocFile</li>
     * <li>Apply base properties to all components.</li>
     * <li>Verify base properties applied.</li>
     * <li>Push reset button.</li>
     * <li>Verify all properties returned to distinct values.</li>
     * </ol>
     * <p>
     * Note:
     * To avoid interrupting the process
     * before the dialog under test is dismissed,
     * all verification takes place
     * after the dialog's cancel button
     * has been pushed.
     */
    @Test
    public void testResetFromFile()
    {
        Thread  thread              = testGUI.postDialog();
        // Give all editor components distinct values.
        applyDistinctProperties();
        // Sanity check; verify the new values of the editor components
        Profile testDistinctProps   = testGUI.getComponentValues();
        assertEquals( testDistinctProps, distinctProfile );
        
        // Save distinct properties to adHocFile
        testGUI.saveAs( adHocFile );
        // Sanity check; currFile should now be adHocFile.
        File    currFile            = testGUI.getCurrFile();
        assertTrue( 
            ProfileFileManagerTestData.compareFileNames( adHocFile, currFile )
        );
        
        // Sanity check; verify contents of adHocFile
        assertTrue( 
            ProfileFileManagerTestData.validateFile(
                distinctProfile, 
                adHocFile
            )
        );
        
        // Push the reset button and get the reset values of the editor 
        // components; verify that they match the distinct profile.
        testGUI.pushResetButton();
        Profile testResetProps      = testGUI.getComponentValues();
        assertEquals( distinctProfile, testResetProps );
        
        // Dialog should still be deployed after reset
        assertTrue( testGUI.isVisible() );
        
        // Cancel the editor dialog and wait for it to quiesce.
        testGUI.pushCancelButton();
        Utils.join( thread );
        
        // Dialog should no longer be deployed
        assertFalse( testGUI.isVisible() );
    }

    /**
     * Verify the apply operation.
     * <ol>
     * <li>Set all components to distinct values.</li>
     * <li>Verify distinct properties set.</li>
     * <li>Push apply button.</li>
     * <li>Verify all properties written to PropertyManager.</li>
     * </ol>
     * <p>
     * Note:
     * To avoid interrupting the process
     * before the dialog under test is dismissed,
     * all verification takes place
     * after the dialog's cancel button
     * has been pushed.
     */
    @Test
    public void testApply()
    {
        Thread  thread              = testGUI.postDialog();
        Profile startProps          = testGUI.getComponentValues();
        assertEquals( baseProfile, startProps );
        applyDistinctProperties();
        Profile testDistinctProps   = testGUI.getComponentValues();
        testGUI.pushApplyButton();
        Profile testCommittedProps  = new Profile();
        
        // Dialog should still be deployed after apply
        assertTrue( testGUI.isVisible() );
        
        testGUI.pushCancelButton();
        Utils.join( thread );
        assertEquals( testDistinctProps, distinctProfile );
        assertEquals( testDistinctProps, testCommittedProps );
    }

    /**
     * Verify the OK operation.
     * <ol>
     * <li>Set all components to distinct values.</li>
     * <li>Verify distinct properties set.</li>
     * <li>Push apply button.</li>
     * <li>Verify all properties written to PropertyManager.</li>
     * <li>Verify dialog closed.</li>
     * </ol>
     * <p>
     * Note:
     * To avoid interrupting the process
     * before the dialog under test is dismissed,
     * all verification takes place
     * after the dialog's cancel button
     * has been pushed.
     */
    @Test
    public void testOK()
    {
        Thread  thread              = testGUI.postDialog();
        Profile startProps          = testGUI.getComponentValues();
        assertEquals( baseProfile, startProps );
        applyDistinctProperties();
        Profile testDistinctProps   = testGUI.getComponentValues();
        testGUI.pushOKButton();
        Utils.join( thread );
        
        // Dialog not be shown after the OK button is pushed.
        assertFalse( testGUI.isVisible() );
        int     lastResult  = testGUI.getLastDialogResult();
        assertEquals( JOptionPane.OK_OPTION, lastResult );
        
        Profile testCommittedProps  = new Profile();
        assertEquals( testDistinctProps, distinctProfile );
        assertEquals( testDistinctProps, testCommittedProps );
    }

    /**
     * Verify the Cancel operation.
     * <ol>
     * <li>Set all components to distinct values.</li>
     * <li>Verify distinct properties set.</li>
     * <li>Push Cancel button.</li>
     * <li>Verify NO properties written to PropertyManager.</li>
     * <li>Restart the dialog.</li>
     * <li>
     *      Verify all component reflect the unmodified properties
     *      as stored in the PropertyManager.
     * </ol>
     * <p>
     * Note:
     * To avoid interrupting the process
     * before the dialog under test is dismissed,
     * all verification takes place
     * after the dialog's cancel button
     * has been pushed.
     */
    @Test
    public void testCancel()
    {
        Thread  thread  = testGUI.postDialog();
        Profile startProps          = testGUI.getComponentValues();
        assertEquals( baseProfile, startProps );
        applyDistinctProperties();
        Profile testDistinctProps   = testGUI.getComponentValues();
        testGUI.pushCancelButton();
        Utils.join( thread );
        
        // Dialog not be shown after the OK button is pushed.
        assertFalse( testGUI.isVisible() );
        int     lastResult  = testGUI.getLastDialogResult();
        assertEquals( JOptionPane.CANCEL_OPTION, lastResult );
        
        Profile testCommittedProps  = new Profile();
        assertEquals( distinctProfile, testDistinctProps );
        assertEquals( baseProfile, testCommittedProps );
        
        thread = testGUI.postDialog();
        Profile testProps   = testGUI.getComponentValues();
        testGUI.pushCancelButton();
        Utils.join( thread );
        assertEquals( baseProfile, testProps );
    }
    
    /**
     * Verify the Open File operation.
     * <ol>
     * <li>Verify test GUI contains base property values.</li>
     * <li>Open distinctFile</li>
     * <li>Verify distinct properties set.</li>
     * <li>Push Cancel button.</li>
     * </ol>
     * <p>
     * Note:
     * To avoid interrupting the process
     * before the dialog under test is dismissed,
     * all verification takes place
     * after the dialog's cancel button
     * has been pushed.
     */
    @Test
    public void testOpen()
    {
        Thread  thread      = testGUI.postDialog();
        Profile startProps  = testGUI.getComponentValues();
        assertEquals( baseProfile, startProps );
        testGUI.openFile( distinctFile );
        Profile currProps   = testGUI.getComponentValues();
        testGUI.pushCancelButton();
        Utils.join( thread );
        assertFalse( testGUI.isVisible() );
        assertEquals( distinctProfile, currProps );
    }
    
    /**
     * Verify behavior when the Open File operation fails.
     * <ol>
     * <li>Set distinct properties in GUI.</li>
     * <li>Try to open noSuchFile.</li>
     * <li>Verify distinct properties still set.</li>
     * <li>Push Cancel button.</li>
     * </ol>
     * <p>
     * Note:
     * To avoid interrupting the process
     * before the dialog under test is dismissed,
     * all verification takes place
     * after the dialog's cancel button
     * has been pushed.
     */
    @Test
    public void testOpenGoWrong()
    {
        Thread  thread      = testGUI.postDialog();
        applyDistinctProperties();
        Profile startProps  = testGUI.getComponentValues();
        assertEquals( distinctProfile, startProps );
        testGUI.openFileGoWrong( noSuchFile );
        Profile currProps   = testGUI.getComponentValues();
        testGUI.pushCancelButton();
        Utils.join( thread );
        assertFalse( testGUI.isVisible() );
        assertEquals( distinctProfile, currProps );
    }
    
    /**
     * Verify the Save operation when no file
     * is currently open.
     * <ol>
     * <li>Verify base properties set in test GUI.</li>
     * <li>Close open file, if necessary.</li>
     * <li>Execute Save specifying adHocFile.</li>
     * <li>Verify adHocFile contains base property values.</li>
     * </ol>
     * <p>
     * Note:
     * To avoid interrupting the process
     * before the dialog under test is dismissed,
     * all verification takes place
     * after the dialog's cancel button
     * has been pushed.
     */
    @Test
    public void testSaveNoOpenFile()
    {
        Thread  thread      = testGUI.postDialog();
        Profile startProps  = testGUI.getComponentValues();
        assertEquals( baseProfile, startProps );
        
        adHocFile.delete();
        assertFalse( adHocFile.exists() );
        testGUI.pushCloseButton();
        testGUI.save( adHocFile );
        testGUI.pushCancelButton();
        Utils.join( thread );
        assertTrue( adHocFile.exists() );
        assertTrue(
            ProfileFileManagerTestData
                .validateFile( baseProfile, adHocFile )
        );
    }
    
    /**
     * Verify the Save operation when a file
     * is currently open.
     * <ol>
     * <li>Verify base properties set in test GUI.</li>
     * <li>SaveAs to adHocFile.</li>
     * <li>Load distinct properties into GUI.<li>
     * <li>Execute Save specifying adHocFile.</li>
     * <li>Verify adHocFile contains distinct property values.</li>
     * </ol>
     * <p>
     * Note:
     * To avoid interrupting the process
     * before the dialog under test is dismissed,
     * all verification takes place
     * after the dialog's cancel button
     * has been pushed.
     */
    @Test
    public void testSaveOpenFile()
    {
        Thread  thread      = testGUI.postDialog();
        Profile startProps  = testGUI.getComponentValues();
        assertEquals( baseProfile, startProps );
        
        adHocFile.delete();
        assertFalse( adHocFile.exists() );
        testGUI.saveAs( adHocFile );

        applyDistinctProperties();
        testGUI.save( adHocFile );
        testGUI.pushCancelButton();
        Utils.join( thread );
        assertTrue(
            ProfileFileManagerTestData
                .validateFile( distinctProfile, adHocFile )
        );
    }

    /**
     * Verify the Save As operation.
     * <ol>
     * <li>Verify base properties set in test GUI.</li>
     * <li>Execute SaveAs specifying adHocFile.</li>
     * <li>Verify adHocFile contains base property values.</li>
     * </ol>
     * <p>
     * Note:
     * To avoid interrupting the process
     * before the dialog under test is dismissed,
     * all verification takes place
     * after the dialog's cancel button
     * has been pushed.
     */
    @Test
    public void testSaveAs()
    {
        Thread  thread      = testGUI.postDialog();
        Profile startProps  = testGUI.getComponentValues();
        assertEquals( baseProfile, startProps );
        
        adHocFile.delete();
        assertFalse( adHocFile.exists() );
        testGUI.saveAs( adHocFile );
        testGUI.pushCancelButton();
        Utils.join( thread );
        assertTrue( adHocFile.exists() );
        assertTrue(
            ProfileFileManagerTestData
                .validateFile( baseProfile, adHocFile )
        );
    }
    
    /**
     * Verify the Save As operation.
     * <ol>
     * <li>Verify base properties set in test GUI.</li>
     * <li>Execute SaveAs specifying adHocFile.</li>
     * <li>Verify adHocFile contains base property values.</li>
     * </ol>
     */
    @Test
    public void testCloseFile()
    {
        Thread  thread      = testGUI.postDialog();
        testGUI.openFile( distinctFile );
        File    currFile    = testGUI.getCurrFile();
        assertTrue(
            ProfileFileManagerTestData.compareFileNames( distinctFile, currFile )
        );
        testGUI.pushCloseButton();
        assertNull( testGUI.getCurrFile() );
        testGUI.pushCancelButton();
        Utils.join( thread );
    }
    
    /**
     * Change the values of all GUI components
     * related to Profile properties
     * to distinct values.
     */
    private void applyDistinctProperties()
    {
        testGUI.setGridUnit( distinctProfile.getGridUnit() );
        testGUI.setName( distinctProfile.getName() );
        applyDistinctGraphProperties();
        Stream.of( 
            "LinePropertySetGridLines",
            "LinePropertySetAxes",
            "LinePropertySetTicMajor",
            "LinePropertySetTicMinor"
        )
            .forEach( this::applyDistinctLineProperties );
    }
    
    /**
     * Change the values of all GUI components
     * related to GraphPropertySetMW properties
     * to distinct values.
     */
    private void applyDistinctGraphProperties()
    {
        GraphPropertySet    props   = distinctProfile.getMainWindow();
        int                 rgb     =
            props.getBGColor().getRGB() & 0xFFFFFF;
        testGUI.setBGColor( rgb );
        testGUI.setFontDraw( props.isFontDraw() );
        testGUI.setGridWidth( props.getWidth() );
        applyDistinctFontProperties();
//        waitOp();
    }
    
    /**
     * Change the values of all GUI components
     * related to font properties
     * to distinct values.
     */
    private void applyDistinctFontProperties()
    {
        Thread              thread  = testGUI.editFont();
        GraphPropertySet    props   = distinctProfile.getMainWindow();
        int                 rgb     =
            props.getFGColor().getRGB() & 0xFFFFFF;
        testGUI.setFGColor( rgb );
        testGUI.setFontBold( props.isBold() );
        testGUI.setFontItalic( props.isItalic() );
        testGUI.setFontDraw( props.isFontDraw() );
        testGUI.setFontName( props.getFontName() );
        testGUI.setFontSize( props.getFontSize() );
//        waitOp();
        testGUI.selectFDOK();
        Utils.join( thread );
    }
    
    /**
     * Change the values of the properties
     * in the given LinePropertySet of the working profile
     * to distinct values.
     * 
     * @param name  the given LinePropertySet
     */
    private void applyDistinctLineProperties( String name )
    {
        LinePropertySet props   = 
            distinctProfile.getLinePropertySet( name );
        if ( props.hasColor() )
        {
            int rgb = props.getColor().getRGB() & 0xffffff;
            testGUI.setColor( name, rgb );
        }
        if ( props.hasDraw() )
            testGUI.setDraw( name, props.getDraw() );
        if ( props.hasLength() )
            testGUI.setLength( name, props.getLength() );
        if ( props.hasSpacing() )
            testGUI.setSpacing( name, props.getSpacing() );
        if ( props.hasStroke() )
            testGUI.setStroke( name, props.getStroke() );
    }
    
    /**
     * This method is for debugging purposes only.
     * It posts a modal dialog
     * which will suspend the test
     * and freeze the GUI
     * until it is dismissed.
     */
    @SuppressWarnings("unused")
    private void waitOp()
    {
        JOptionPane.showMessageDialog( null, "Waiting" );
    }
}
