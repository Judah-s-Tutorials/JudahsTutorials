package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;

class FontEditorTest
{
    private FontEditor          defaultEditor;
    private JComboBox<String>   nameCombo;
    private JCheckBox           boldToggle;
    private JCheckBox           italicToggle;
    private JSpinner            sizeEditor;
    private JComponent          feedback;
    private ColorEditor         colorEditor;
    private JTextField          colorText;
    private JButton             colorButton;
    
    @BeforeEach
    void setUp() throws Exception
    {
        GUIUtils.schedEDTAndWait( () -> {    
            defaultEditor = new FontEditor();
            nameCombo = defaultEditor.getNameCombo();
            boldToggle = defaultEditor.getBoldToggle();
            italicToggle = defaultEditor.getItalicToggle();
            sizeEditor = defaultEditor.getSizeEditor();
            feedback = defaultEditor.getFeedback();
            colorEditor = defaultEditor.getColorEditor();
            colorText = colorEditor.getTextEditor();
            colorButton = colorEditor.getColorButton();
        });
    }

    @AfterEach
    void tearDown() throws Exception
    {
        ComponentFinder.disposeAll();
    }

    @Test
    void testFontEditor()
    {
        assertNotNull( nameCombo );
        assertNotNull( boldToggle );
        assertNotNull( italicToggle );
        assertNotNull( sizeEditor );
        assertNotNull( colorEditor );
        assertNotNull( feedback );
    }

    @Test
    void testGetPanel()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetSelectedFont()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetName()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetSize()
    {
        fail("Not yet implemented");
    }

    @Test
    void testIsBold()
    {
        fail("Not yet implemented");
    }

    @Test
    void testIsItalic()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetColor()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetNameCombo()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetBoldToggle()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetItalicToggle()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetSizeEditor()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetColorEditor()
    {
        fail("Not yet implemented");
    }

    @Test
    void testGetFeedback()
    {
        fail("Not yet implemented");
    }

}
