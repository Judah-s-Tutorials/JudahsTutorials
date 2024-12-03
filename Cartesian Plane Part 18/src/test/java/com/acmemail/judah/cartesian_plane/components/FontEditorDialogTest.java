package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.awt.Font;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.acmemail.judah.cartesian_plane.Profile;
import com.acmemail.judah.cartesian_plane.test_utils.FontEditorDialogTestGUI;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

public class FontEditorDialogTest
{
    private final FontEditorDialogTestGUI  testGUI     =
        new FontEditorDialogTestGUI();
    
    @BeforeEach
    void beforeEach() throws Exception
    {
        testGUI.resetProfile();
    }

    @ParameterizedTest    
    @ValueSource( booleans= { true, false} )
    public void testBold( boolean okay )
    {
        testBoolean( 
            okay, 
            testGUI::setBold, 
            testGUI::isBold, 
            testGUI::isProfileBold
        );
    }

    @ParameterizedTest    
    @ValueSource( booleans= { true, false} )
    public void testItalic( boolean okay )
    {
        testBoolean( 
            okay, 
            testGUI::setItalic, 
            testGUI::isItalic, 
            testGUI::isProfileItalic
        );
    }

    @ParameterizedTest    
    @ValueSource( booleans= { true, false} )
    public void testName( boolean okay )
    {
        assertFalse( testGUI.isVisible() );
        Thread  guiThread   = testGUI.showDialog();
        
        String  origProfileName = testGUI.getProfileName();
        String  origGUIName     = testGUI.getName();
        String  newName         = origProfileName.equals( Font.DIALOG ) ? 
            Font.MONOSPACED : Font.DIALOG;
        assertEquals( origProfileName, origGUIName );
        assertNotEquals( origProfileName, newName );
        
        testGUI.setName( newName );
        assertEquals( newName, testGUI.getName() );
        assertEquals( origProfileName, testGUI.getProfileName() );
        
        testGUI.clickReset();
        assertEquals( origProfileName, testGUI.getName() );
        assertEquals( origProfileName, testGUI.getProfileName() );
        
        testGUI.setName( newName );
        assertEquals( newName, testGUI.getName() );
        assertEquals( origProfileName, testGUI.getProfileName() );
        
        if ( okay )
            testGUI.clickOK();
        else
            testGUI.clickCancel();
        Utils.join( guiThread );
        assertFalse( testGUI.isVisible() );
        if ( okay )
            assertEquals( newName, testGUI.getProfileName() );
        else
            assertEquals( origProfileName, testGUI.getProfileName() );
    }

    @ParameterizedTest    
    @ValueSource( booleans= { true, false} )
    public void testSize( boolean okay )
    {
        assertFalse( testGUI.isVisible() );
        Thread  guiThread   = testGUI.showDialog();
        
        float   origProfileSize = testGUI.getProfileSize();
        int     origGUISize     = testGUI.getSize();
        int     newSize         = (int)origProfileSize + 1;
        assertEquals( origProfileSize, origGUISize );
        assertNotEquals( origProfileSize, newSize );
        
        testGUI.setSize( newSize );
        assertEquals( newSize, testGUI.getSize() );
        assertEquals( origProfileSize, testGUI.getProfileSize() );
        
        testGUI.clickReset();
        assertEquals( origProfileSize, testGUI.getSize() );
        assertEquals( origProfileSize, testGUI.getProfileSize() );
        
        testGUI.setSize( newSize );
        assertEquals( newSize, testGUI.getSize() );
        assertEquals( origProfileSize, testGUI.getProfileSize() );
        
        if ( okay )
            testGUI.clickOK();
        else
            testGUI.clickCancel();
        Utils.join( guiThread );
        assertFalse( testGUI.isVisible() );
        if ( okay )
            assertEquals( newSize, testGUI.getProfileSize() );
        else
            assertEquals( origProfileSize, testGUI.getProfileSize() );
    }

    @ParameterizedTest    
    @ValueSource( booleans= { true, false} )
    public void testColor( boolean okay )
    {
        assertFalse( testGUI.isVisible() );
        Thread  guiThread   = testGUI.showDialog();
        
        int     origProfileColor    = testGUI.getProfileColor();
        int     origGUIColor        = testGUI.getColor();
        int     newColor            = origProfileColor + 1;
        assertEquals( origProfileColor, origGUIColor );
        assertNotEquals( origProfileColor, newColor );
        
        testGUI.setColor( newColor );
        assertEquals( newColor, testGUI.getColor() );
        assertEquals( origProfileColor, testGUI.getProfileColor() );
        
        testGUI.clickReset();
        assertEquals( origProfileColor, testGUI.getColor() );
        assertEquals( origProfileColor, testGUI.getProfileColor() );
        
        testGUI.setColor( newColor );
        assertEquals( newColor, testGUI.getColor() );
        assertEquals( origProfileColor, testGUI.getProfileColor() );
        
        if ( okay )
            testGUI.clickOK();
        else
            testGUI.clickCancel();
        Utils.join( guiThread );
        assertFalse( testGUI.isVisible() );
        if ( okay )
            assertEquals( newColor, testGUI.getProfileColor() );
        else
            assertEquals( origProfileColor, testGUI.getProfileColor() );
    }
    
    @Test
    public void testGetPropertySet()
    {
        Profile             testProfile     = testGUI.getProfile();
        GraphPropertySet    expPropertySet  = testProfile.getMainWindow();
        GraphPropertySet    actPropertySet  = testGUI.getPropertySet();
        assertEquals( expPropertySet, actPropertySet );
    }
    
    private void testBoolean( 
        boolean           okay,
        Consumer<Boolean> guiSetter, 
        Supplier<Boolean> guiGetter,
        Supplier<Boolean> profileGetter
     )
    {
        assertFalse( testGUI.isVisible() );
        Thread  guiThread   = testGUI.showDialog();
        
        boolean origProfileVal  = profileGetter.get();
        boolean origGUIVal      = guiGetter.get();
        boolean newVal          = !origProfileVal;
        assertEquals( origProfileVal, origGUIVal );
        assertNotEquals( origProfileVal, newVal );
        
        guiSetter.accept( newVal );
        assertEquals( newVal, guiGetter.get() );
        assertEquals( origProfileVal, profileGetter.get() );
        
        testGUI.clickReset();
        assertEquals( origProfileVal, profileGetter.get() );
        assertEquals( origProfileVal, guiGetter.get() );
        
        guiSetter.accept( newVal );
        assertEquals( newVal, guiGetter.get() );
        assertEquals( origProfileVal, profileGetter.get() );
        
        if ( okay )
            testGUI.clickOK();
        else
            testGUI.clickCancel();
        Utils.join( guiThread );
        assertFalse( testGUI.isVisible() );
        if ( okay )
            assertEquals( newVal, profileGetter.get() );
        else
            assertEquals( origProfileVal, profileGetter.get() );
    }
}
