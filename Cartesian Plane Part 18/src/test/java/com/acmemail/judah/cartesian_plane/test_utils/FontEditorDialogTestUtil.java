package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
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
     * Returns the main window properties.
     * 
     * @return  the main window properties
     */
    public GraphPropertySet getMainWindow()
    {
        return mainWindow;
    }
    
    /**
     * Restores the working profile properties
     * to their original values.
     */
    public void resetProfile()
    {
        baseProfile.apply();
        profile.reset();
    }
    
    /**
     * Returns the state of the dialog's
     * visible property.
     *  
     * @return  true if the dialog is visible
     */
    public boolean isVisible()
    {
        return dialog.isVisible();
    }
    
    /**
     * Click on the OK button.
     */
    public void clickOK()
    {
        doClick( okButton );
    }
    
    /**
     * Click on the Cancel button.
     */
    public void clickCancel()
    {
        doClick( cancelButton );
    }
    
    /**
     * Click on the Reset button.
     */
    public void clickReset()
    {
        doClick( resetButton );
    }
    
    /**
     * Gets the font name from the current profile.
     * 
     * @return  the font name from the current profile
     */
    public String getProfileName()
    {
        String  name    = 
            getStringProperty( () -> mainWindow.getFontName() );
        return name;
    }
    
    /**
     * Gets the font size from the current profile.
     * 
     * @return  the font size from the current profile
     */
    public float getProfileSize()
    {
        float   size    =
            getFloatProperty( () -> mainWindow.getFontSize() );
        return size;
    }
    
    /**
     * Gets the font style from the current profile.
     * 
     * @return  the font style from the current profile
     */
    public int getProfileStyle()
    {
        int style   =
            getIntProperty( () -> mainWindow.getFontStyle() );
        return style;
    }
    
    /**
     * Select the given name in the 
     * name combo box.
     * 
     * @param name  
     *      the given name; 
     *      must be spelled exactly as appears in the combo box
     */
    public void setName( String name )
    {
        setProperty( o -> nameEditor.setSelectedItem( o ), name );
    }
    
    /**
     * Returns the selection
     * from the font name combo box.
     * 
     * @return  the selection from the font name combo box
     */
    public String getName()
    {
        String  name    = 
            getStringProperty( () -> fontEditor.getName() );
        return name;
    }
    
    public void setSize( int size )
    {
        setProperty( o -> fontEditor.setSize( (int)o ), size );
    }
    
    /**
     * Returns the font size.
     * 
     * @return  the font size
     */
    public int getSize()
    {
        int   size    = 
            getIntProperty( () -> fontEditor.getSize().orElse( -1 ) );
        return size;
    }
    
    /**
     * Set the color editor text field
     * to the given RGB value.
     *
     * @param rgbVal  
     *      the given Color value
     */
    public void setColor( int rgbVal )
    {
        String  strVal  = String.valueOf( rgbVal );
        setProperty( o -> colorText.setText( (String)o ), strVal );
    }
    
    /**
     * Returns the RGB value
     * corresponding to the text
     * of the Color editor's text field.
     * If the text can't be parsed
     * -1 is returned.
     * 
     * @return  
     *      the RGB value parsed from the text 
     *      of the color editor's text field
     */
    public int getColor()
    {
        int     rgb     = -1;
        String  text    = 
            getStringProperty( () -> colorText.getText() );
        try
        {
            rgb = Integer.decode( text );
        }
        catch ( NumberFormatException exc )
        {
            // deliberately ignored
        }
        return rgb;
    }
    
    /**
     * Returns the RGB value
     * corresponding to the foreground color
     * of the Color editor's text field
     * in the current profile.
     * 
     * @return  
     *      the RGB value of the Profiles foreground color property
     */
    public int getProfileColor()
    {
        int     rgb     = getIntProperty( () -> {
            Color   color   = mainWindow.getFGColor();
            int     rgb2    = color.getRGB() & 0xFFFFFF;
            return rgb2;
        });
        return rgb;
    }
    
    /**
     * Gets the value of the Bold check box.
     * 
     * @return  true if the Bold check box is selected
     */
    public boolean isBold()
    {
        boolean bold    = getBooleanProperty( () -> boldBox.isSelected() );
        return bold;
    }
    
    /**
     * Sets the value of the Bold check box
     * to the given value.
     * 
     * @param selected  the given value
     */
    public void setBold( boolean selected )
    {
        setProperty( o -> boldBox.setSelected( (boolean)o ), selected );
    }
    
    /**
     * Gets the value of the Bold profile property.
     * 
     * @return  the value of the Bold profile property
     */
    public boolean isProfileBold()
    {
        boolean bold    = getBooleanProperty( () -> mainWindow.isBold() );
        return bold;
    }
    
    /**
     * Gets the value of the Italic profile property.
     * 
     * @return  the value of the Italic profile property
     */
    public boolean isProfileItalic()
    {
        boolean italic  = 
            getBooleanProperty( () -> mainWindow.isItalic() );
        return italic;
    }
    
    /**
     * Gets the value of the Italic check box.
     * 
     * @return  true if the Italic check box is selected
     */
    public boolean isItalic()
    {
        boolean italic  = 
            getBooleanProperty( () -> italicBox.isSelected() );
        return italic;
    }
    
    /**
     * Sets the value of the Italic check box
     * to the given value.
     * 
     * @param selected  the given value
     */
    public void setItalic( boolean selected )
    {
        setProperty( o -> italicBox.setSelected( (boolean)o ), selected );
    }
    
    /**
     * Starts the editor dialog in a new thread
     * and returns the ID of the thread.
     * 
     * @return  the ID of the thread that is running the editor dialog
     */
    public Thread showDialog()
    {
        Runnable    runner  = 
            () -> GUIUtils.schedEDTAndWait( dialog::showDialog );
        Thread  thread  = new Thread( runner );
        thread.start();
        Utils.pause( 125 );
        return thread;
    }
    
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
     * and returns the resulting int value.
     * 
     * @param getter    the given getter
     * 
     * @return the object obtained from the given getter
     */
    private int getIntProperty( Supplier<Object> getter )
    {
        Object  objVal  = getProperty( getter );
        assertTrue( objVal instanceof Integer );
        return (int)objVal;
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
