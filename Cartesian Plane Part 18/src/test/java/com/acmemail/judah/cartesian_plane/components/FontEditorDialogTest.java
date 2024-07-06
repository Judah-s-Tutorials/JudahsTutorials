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
import com.acmemail.judah.cartesian_plane.test_utils.FontEditorDialogTestUtil;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

class FontEditorDialogTest
{
    private final FontEditorDialogTestUtil  testUtil    =
        new FontEditorDialogTestUtil();
    
    @BeforeEach
    void setUp() throws Exception
    {
        testUtil.resetProfile();
    }

    @ParameterizedTest    
    @ValueSource( booleans= { true, false} )
    public void testBold( boolean okay )
    {
        testBoolean( 
            okay, 
            testUtil::setBold, 
            testUtil::isBold, 
            testUtil::isProfileBold
        );
    }

    @ParameterizedTest    
    @ValueSource( booleans= { true, false} )
    public void testItalic( boolean okay )
    {
        testBoolean( 
            okay, 
            testUtil::setItalic, 
            testUtil::isItalic, 
            testUtil::isProfileItalic
        );
    }

    @ParameterizedTest    
    @ValueSource( booleans= { true, false} )
    public void testName( boolean okay )
    {
        assertFalse( testUtil.isVisible() );
        Thread  guiThread   = testUtil.showDialog();
        
        String  origProfileName = testUtil.getProfileName();
        String  origGUIName     = testUtil.getName();
        String  newName         = origProfileName.equals( Font.DIALOG ) ? 
            Font.MONOSPACED : Font.DIALOG;
        assertEquals( origProfileName, origGUIName );
        assertNotEquals( origProfileName, newName );
        
        testUtil.setName( newName );
        assertEquals( newName, testUtil.getName() );
        assertEquals( origProfileName, testUtil.getProfileName() );
        
        testUtil.clickReset();
        assertEquals( origProfileName, testUtil.getName() );
        assertEquals( origProfileName, testUtil.getProfileName() );
        
        testUtil.setName( newName );
        assertEquals( newName, testUtil.getName() );
        assertEquals( origProfileName, testUtil.getProfileName() );
        
        if ( okay )
            testUtil.clickOK();
        else
            testUtil.clickCancel();
        Utils.join( guiThread );
        assertFalse( testUtil.isVisible() );
        if ( okay )
            assertEquals( newName, testUtil.getProfileName() );
        else
            assertEquals( origProfileName, testUtil.getProfileName() );
    }

    @ParameterizedTest    
    @ValueSource( booleans= { true, false} )
    public void testSize( boolean okay )
    {
        assertFalse( testUtil.isVisible() );
        Thread  guiThread   = testUtil.showDialog();
        
        float   origProfileSize = testUtil.getProfileSize();
        int     origGUISize     = testUtil.getSize();
        int     newSize         = (int)origProfileSize + 1;
        assertEquals( origProfileSize, origGUISize );
        assertNotEquals( origProfileSize, newSize );
        
        testUtil.setSize( newSize );
        assertEquals( newSize, testUtil.getSize() );
        assertEquals( origProfileSize, testUtil.getProfileSize() );
        
        testUtil.clickReset();
        assertEquals( origProfileSize, testUtil.getSize() );
        assertEquals( origProfileSize, testUtil.getProfileSize() );
        
        testUtil.setSize( newSize );
        assertEquals( newSize, testUtil.getSize() );
        assertEquals( origProfileSize, testUtil.getProfileSize() );
        
        if ( okay )
            testUtil.clickOK();
        else
            testUtil.clickCancel();
        Utils.join( guiThread );
        assertFalse( testUtil.isVisible() );
        if ( okay )
            assertEquals( newSize, testUtil.getProfileSize() );
        else
            assertEquals( origProfileSize, testUtil.getProfileSize() );
    }

    @ParameterizedTest    
    @ValueSource( booleans= { true, false} )
    public void testColor( boolean okay )
    {
        assertFalse( testUtil.isVisible() );
        Thread  guiThread   = testUtil.showDialog();
        
        int     origProfileColor    = testUtil.getProfileColor();
        int     origGUIColor        = testUtil.getColor();
        int     newColor            = origProfileColor + 1;
        assertEquals( origProfileColor, origGUIColor );
        assertNotEquals( origProfileColor, newColor );
        
        testUtil.setColor( newColor );
        assertEquals( newColor, testUtil.getColor() );
        assertEquals( origProfileColor, testUtil.getProfileColor() );
        
        testUtil.clickReset();
        assertEquals( origProfileColor, testUtil.getColor() );
        assertEquals( origProfileColor, testUtil.getProfileColor() );
        
        testUtil.setColor( newColor );
        assertEquals( newColor, testUtil.getColor() );
        assertEquals( origProfileColor, testUtil.getProfileColor() );
        
        if ( okay )
            testUtil.clickOK();
        else
            testUtil.clickCancel();
        Utils.join( guiThread );
        assertFalse( testUtil.isVisible() );
        if ( okay )
            assertEquals( newColor, testUtil.getProfileColor() );
        else
            assertEquals( origProfileColor, testUtil.getProfileColor() );
    }
    
    @Test
    public void testGetPropertySet()
    {
        Profile             testProfile     = testUtil.getProfile();
        GraphPropertySet    expPropertySet  = testProfile.getMainWindow();
        GraphPropertySet    actPropertySet  = testUtil.getPropertySet();
        assertEquals( expPropertySet, actPropertySet );
    }
    
    private void testBoolean( 
        boolean           okay,
        Consumer<Boolean> guiSetter, 
        Supplier<Boolean> guiGetter,
        Supplier<Boolean> profileGetter
     )
    {
        assertFalse( testUtil.isVisible() );
        Thread  guiThread   = testUtil.showDialog();
        
        boolean origProfileVal  = profileGetter.get();
        boolean origGUIVal      = guiGetter.get();
        boolean newVal          = !origProfileVal;
        assertEquals( origProfileVal, origGUIVal );
        assertNotEquals( origProfileVal, newVal );
        
        guiSetter.accept( newVal );
        assertEquals( newVal, guiGetter.get() );
        assertEquals( origProfileVal, profileGetter.get() );
        
        testUtil.clickReset();
        assertEquals( origProfileVal, profileGetter.get() );
        assertEquals( origProfileVal, guiGetter.get() );
        
        guiSetter.accept( newVal );
        assertEquals( newVal, guiGetter.get() );
        assertEquals( origProfileVal, profileGetter.get() );
        
        if ( okay )
            testUtil.clickOK();
        else
            testUtil.clickCancel();
        Utils.join( guiThread );
        assertFalse( testUtil.isVisible() );
        if ( okay )
            assertEquals( newVal, profileGetter.get() );
        else
            assertEquals( origProfileVal, profileGetter.get() );
    }
}
