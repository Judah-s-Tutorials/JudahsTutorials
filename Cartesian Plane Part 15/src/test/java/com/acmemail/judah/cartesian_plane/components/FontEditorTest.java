package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.Color;
import java.awt.Font;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
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
    public void testGetPanel()
    {
        GUIUtils.schedEDTAndWait( () -> testGetPanelEDT() );
    }
    
    private void testGetPanelEDT()
    {
        // Make sure getPanel returns a JPanel containing all
        // the expected components.
        JPanel  panel   = defaultEditor.getPanel();
        assertFound( panel, nameCombo );
        assertFound( panel, boldToggle );
        assertFound( panel, italicToggle );
        assertFound( panel, sizeEditor );
        assertFound( panel, nameCombo );
        assertFound( panel, feedback );
        assertFound( panel, colorEditor.getColorButton() );
        assertFound( panel, colorEditor.getTextEditor() );
    }

    @Test
    public void testGetSelectedFont()
    {
        GUIUtils.schedEDTAndWait( () -> testGetSelectedFontEDT() );
    }
    
    private void testGetSelectedFontEDT()
    {
        // Select new property values for selected font,
        // then validate against getSelectedFont()
        int     numItems    = nameCombo.getItemCount();
        int     currItem    = nameCombo.getSelectedIndex();
        int     selectItem  = (currItem + 1) % numItems;
        nameCombo.setSelectedIndex( selectItem );
        String  expFontName = nameCombo.getSelectedItem().toString();
        
        int     expSize     = (int)sizeEditor.getNextValue();
        sizeEditor.setValue( expSize );
        
        boolean expBold     = !boldToggle.isSelected();
        boldToggle.setSelected( expBold );
        boolean expItalic   = !italicToggle.isSelected();
        italicToggle.setSelected( expItalic );
        
        Optional<Font>  optFont = defaultEditor.getSelectedFont();
        Font            font    = optFont.orElse( null );
        assertNotNull( font );
        assertEquals( expFontName, font.getName() );
        assertEquals( expBold, font.isBold() );
        assertEquals( expItalic, font.isItalic() );
        assertEquals( expSize, font.getSize() );
    }

    @Test
    public void testGetName()
    {
        GUIUtils.schedEDTAndWait( () -> testGetNameEDT() );
    }
    
    private void testGetNameEDT()
    {
        String  currName    = nameCombo.getSelectedItem().toString();
        assertEquals( currName, defaultEditor.getName() );
        
        int     numItems    = nameCombo.getItemCount();
        int     currItem    = nameCombo.getSelectedIndex();
        int     selectItem  = (currItem + 1) % numItems;
        nameCombo.setSelectedIndex( selectItem );
        String  expFontName = nameCombo.getSelectedItem().toString();
        assertEquals( expFontName, defaultEditor.getName() );
    }

    @Test
    public void testGetSize()
    {
        GUIUtils.schedEDTAndWait( () -> testGetSizeEDT() );
    }
    
    private void testGetSizeEDT()
    {
        int         currSize    = (int)sizeEditor.getValue();
        int         actSize     = defaultEditor.getSize().orElse( -1 );
        assertEquals( currSize, actSize );
        
        int     expSize     = (int)sizeEditor.getNextValue();
        sizeEditor.setValue( expSize );
        assertEquals( expSize, defaultEditor.getSize().orElse( -1 ) );
    }

    @Test
    public void testIsBold()
    {
        GUIUtils.schedEDTAndWait( () -> testIsBoldEDT() );
    }
    
    private void testIsBoldEDT()
    {
        boolean currBold    = boldToggle.isSelected();
        assertEquals( currBold, defaultEditor.isBold() );
        
        boolean expBold     = !currBold;
        boldToggle.setSelected( expBold );
        assertEquals( expBold, defaultEditor.isBold() );
    }

    @Test
    public void testIsItalic()
    {
        GUIUtils.schedEDTAndWait( () -> testIsItalicEDT() );
    }
    
    private void testIsItalicEDT()
    {
        boolean currItalic  = italicToggle.isSelected();
        assertEquals( currItalic, defaultEditor.isItalic() );
        
        boolean expItalic   = !currItalic;
        italicToggle.setSelected( expItalic );
        assertEquals( expItalic, defaultEditor.isItalic() );
    }

    @Test
    public void testGetColor()
    {
        GUIUtils.schedEDTAndWait( () -> testGetColorEDT() );
    }
    
    private void testGetColorEDT()
    {
        int     iExpColor   = Integer.decode( colorText.getText() );
        Color   currColor   = defaultEditor.getColor().orElse( null );
        assertNotNull( currColor );
        int     iCurrColor  = currColor.getRGB() & 0x00FFFFFF;
        assertEquals( iExpColor, iCurrColor );
        
        int     iNewColor   = iExpColor ^ 0xFF;
        String  sNewColor   = "" + iNewColor;
        colorText.setText( sNewColor );
        colorText.postActionEvent();
        Color   actColor    = defaultEditor.getColor().orElse( null );
        assertNotNull( actColor );
        int     iActColor   = actColor.getRGB() & 0x00FFFFFF;
        assertEquals( iNewColor, iActColor );
    }

    @Test
    public void testGetColorGoWrong()
    {
        GUIUtils.schedEDTAndWait( () -> testGetColorGoWrongEDT() );
    }
    
    private void testGetColorGoWrongEDT()
    {
        // set color to an invalid value; verify FontEditor
        // responds accordingly.
        colorText.setText( "Q" );
        colorText.postActionEvent();
        assertFalse( defaultEditor.getColor().isPresent() );
    }

    @Test
    void testGetNameCombo()
    {
        GUIUtils.schedEDTAndWait( () ->
            assertNotNull( defaultEditor.getNameCombo() )
        );
    }

    @Test
    void testGetBoldToggle()
    {
        GUIUtils.schedEDTAndWait( () ->
            assertNotNull( defaultEditor.getBoldToggle() )
        );
    }

    @Test
    void testGetItalicToggle()
    {
        GUIUtils.schedEDTAndWait( () ->
            assertNotNull( defaultEditor.getItalicToggle() )
        );
    }

    @Test
    void testGetSizeEditor()
    {
        GUIUtils.schedEDTAndWait( () ->
            assertNotNull( defaultEditor.getSizeEditor() )
        );
    }

    @Test
    void testGetColorEditor()
    {
        GUIUtils.schedEDTAndWait( () ->
            assertNotNull( defaultEditor.getColorEditor() )
        );
    }

    @Test
    void testGetFeedback()
    {
        GUIUtils.schedEDTAndWait( () ->
            assertNotNull( defaultEditor.getFeedback() )
        );
    }
    
    private void assertFound( JPanel panel, JComponent comp )
    {
        assertNotNull(
            ComponentFinder.find( panel, c -> c == comp )
        );
    }
}
