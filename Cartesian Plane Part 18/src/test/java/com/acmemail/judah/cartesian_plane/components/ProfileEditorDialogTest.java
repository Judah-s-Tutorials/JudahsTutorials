package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import javax.swing.JOptionPane;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.test_utils.ProfileEditorDialogTestGUI;
import com.acmemail.judah.cartesian_plane.test_utils.ProfileUtils;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

public class ProfileEditorDialogTest
{
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
     * After initialization the reference must not be changed,
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
     * Verify the reset operation.
     * <ol>
     * <li>Apply distinct properties to all components.</li>
     * <li>Verify distinct properties applied.</li>
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
    public void testReset()
    {
        Thread  thread  = testGUI.postDialog();
        Profile startProps          = testGUI.getComponentValues();
        assertEquals( baseProfile, startProps );
        applyDistinctProperties();
        Profile testDistinctProps   = testGUI.getComponentValues();
        assertEquals( testDistinctProps, distinctProfile );
        testGUI.pushResetButton();
        Profile testResetProps      = testGUI.getComponentValues();
        assertEquals( baseProfile, testResetProps );
        
        // Dialog should still be deployed after apply
        assertTrue( testGUI.isVisible() );
        
        testGUI.pushCancelButton();
        Utils.join( thread );
    }

    /**
     * Verify the apply operation.
     * <ol>
     * <li>Apply distinct properties to all components.</li>
     * <li>Verify distinct properties applied.</li>
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
        Thread  thread  = testGUI.postDialog();
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
     * <li>Apply distinct properties to all components.</li>
     * <li>Verify distinct properties applied.</li>
     * <li>Push OK button.</li>
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
    public void testOK()
    {
        Thread  thread  = testGUI.postDialog();
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
     * <li>Apply distinct properties to all components.</li>
     * <li>Verify distinct properties applied.</li>
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
        assertEquals( baseProfile, testProps );
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
