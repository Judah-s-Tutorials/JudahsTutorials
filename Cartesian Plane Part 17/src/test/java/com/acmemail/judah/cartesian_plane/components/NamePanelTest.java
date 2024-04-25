package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.event.KeyEvent;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.Exp4jEquation;
import com.acmemail.judah.cartesian_plane.test_utils.NamePanelTestGUI;

class NamePanelTest
{
    private static NamePanelTestGUI testGUI;
    private String  defaultName = "Default Equation Name";
    
    @BeforeAll
    static void setUpBeforeClass() throws Exception
    {
        GUIUtils.schedEDTAndWait( () -> testGUI = new NamePanelTestGUI() );
    }

    @BeforeEach
    public void setUp() throws Exception
    {
        Equation    equation    = new Exp4jEquation();
        equation.setName( defaultName );
        testGUI.newEquation( equation );
    }

    @Test
    public void testInit()
    {
        assertTrue( testGUI.isEnabled() );
        assertTrue( testGUI.isCommitted() );
        assertFalse( testGUI.isChangedFont() );
        assertEquals( testGUI.getValue(), testGUI.getEqValue() );
        assertEquals( defaultName, testGUI.getText() );
    }

    @Test
    public void testClose()
    {
        testGUI.newEquation( null );
        assertFalse( testGUI.isEnabled() );
    }

    @Test
    public void testChangeAndCommitEnter()
    {
        testChangeAndCommit( KeyEvent.VK_ENTER );
    }

    @Test
    public void testChangeAndCommitTab()
    {
        testChangeAndCommit( KeyEvent.VK_TAB );
    }
    
    /**
     * Change the name in the text field,
     * then type the given key code.
     * The key code is expected to be
     * either VK_ENTER or VK_TAB.
     * 
     * @param keyCode   the given key code
     */
    private void testChangeAndCommit( int keyCode )
    {
        String  newName = "Not " + defaultName;
        assertFalse( testGUI.isModified() );
        assertFalse( testGUI.isChangedFont() );
        assertTrue( testGUI.isCommitted() );
        testGUI.paste( newName );
        assertFalse( testGUI.isModified() );
        assertTrue( testGUI.isChangedFont() );
        assertFalse( testGUI.isCommitted() );
        testGUI.type( keyCode );
        assertTrue( testGUI.isModified() );
        assertFalse( testGUI.isChangedFont() );
        assertTrue( testGUI.isCommitted() );
        assertEquals( newName, testGUI.getEqValue() );
    }
}
