package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Component;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import com.acmemail.judah.cartesian_plane.components.ColorEditor;
import com.acmemail.judah.cartesian_plane.components.FontEditor;
import com.acmemail.judah.cartesian_plane.components.FontEditorDialog;
import com.acmemail.judah.cartesian_plane.components.GraphPropertySet;
import com.acmemail.judah.cartesian_plane.components.Profile;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;

/**
 * @author Jack Straub
 */
public class FontEditorDialogTestUtil
{
    /** 
     * The font properties when the application is started;
     * initialized when this class is loaded and never modified.
     * Can be used to reset the PropertyManager properties
     * to their original values.
     */
    private static final Profile    baseProfile = new Profile();
    
    /** The Profile associated with the FontEditorDialog. */
    private final Profile           profile = new Profile();
    /** 
     * The profile's main window properties, including the
     * font properties under test..
     */
    private final GraphPropertySet  mainWindow  = profile.getMainWindow();
    /** The dialog under test. */
    private final FontEditorDialog  dialog      =
        new FontEditorDialog( null, profile.getMainWindow() );
    /** The dialog's FontEditor. */
    private final FontEditor        fontEditor  = dialog.getFontEditor();
    /** The name editor from the FontEditor */
    private final JComboBox<String> nameEditor  = 
        fontEditor.getNameCombo();
    /** Bold style check box from FontEditor. */
    private final JCheckBox         boldBox     = 
        fontEditor.getBoldToggle();
    /** Bold style check box from FontEditor. */
    private final JCheckBox         italicBox   = 
        fontEditor.getItalicToggle();
    /** The ColorEditor from the FontEditor. */
    private final ColorEditor       colorEditor =
        fontEditor.getColorEditor();
    /** The text field from the ColorEditor. */
    private final JTextField        colorText   =
        colorEditor.getTextEditor();
    /** The dialog's OK button. */
    private final JButton   okButton        = getButton( "OK" );
    /** The dialog's Cancel button. */
    private final JButton   cancelButton    = getButton( "Cancel" );
    /** The dialog's Reset button. */
    private final JButton   resetButton     = getButton( "Reset" );
    
    /** Object for transient use in lambdas. */
    private Object  adHocObj   = null;
    
    /**
     * In the context of the EDT
     * clicks the given button.
     * 
     * @param button    the given button
     */
    private void doClick( JButton button )
    {
        GUIUtils.schedEDTAndWait( () -> button.doClick() );
    }
    
    /**
     * In the context of the EDT,
     * executes the given getter
     * and returns the resulting String value.
     * 
     * @param getter    the given getter
     * 
     * @return the object obtained from the given getter
     */
    private String getStringProperty( Supplier<Object> getter )
    {
        Object  objVal  = getProperty( getter );
        assertTrue( objVal instanceof String );
        return (String)objVal;
    }
    
    /**
     * In the context of the EDT,
     * executes the given getter
     * and returns the resulting float value.
     * 
     * @param getter    the given getter
     * 
     * @return the object obtained from the given getter
     */
    private float getFloatProperty( Supplier<Object> getter )
    {
        Object  objVal  = getProperty( getter );
        assertTrue( objVal instanceof Float );
        return (float)objVal;
    }
    
    /**
     * In the context of the EDT,
     * executes the given getter
     * and returns the resulting Boolean value.
     * 
     * @param getter    the given getter
     * 
     * @return the object obtained from the given getter
     */
    private boolean getBooleanProperty( Supplier<Object> getter )
    {
        Object  objVal  = getProperty( getter );
        assertTrue( objVal instanceof Boolean );
        return (boolean)objVal;
    }
    
    /**
     * In the context of the EDT,
     * executes the given getter
     * and returns the resulting value.
     * 
     * @param getter    the given getter
     * 
     * @return the object obtained from the given getter
     */
    private Object getProperty( Supplier<Object> getter )
    {
        GUIUtils.schedEDTAndWait( () -> adHocObj = getter.get() );
        return adHocObj;
    }
    
    /**
     * In the context of the EDT,
     * sets the value of the property
     * referenced by the given setter.
     * 
     * @param setter    the given setter
     * @param val       the given property value
     * 
     * @return the object obtained from the given getter
     */
    private void setProperty( Consumer<Object> setter, Object val )
    {
        GUIUtils.schedEDTAndWait( () -> setter.accept( val ) );
    }
    
    /**
     * Finds the JButton with the given text.
     * 
     * @param text  the given text
     * 
     * @return  the JButton with the given text
     */
    private JButton getButton( String text )
    {
        Predicate<JComponent>   pred        =
            ComponentFinder.getButtonPredicate( text );
        Component               contentPane = dialog.getContentPane();
        assertTrue( contentPane instanceof JComponent );
        JComponent              comp        =
            ComponentFinder.find( (JComponent)contentPane, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JButton );
        return (JButton)comp;
    }
}
