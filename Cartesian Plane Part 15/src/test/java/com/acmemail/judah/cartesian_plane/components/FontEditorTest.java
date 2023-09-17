package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

class FontEditorTest
{
    private FontEditor          defaultEditor;
    
    private JComboBox<String>   nameCombo;
    private JCheckBox           boldToggle;
    private JCheckBox           italicToggle;
    private JSpinner            sizeEditor;
    private JLabel              feedback;
    private ColorEditor         colorEditor;
    private JTextField          colorText;
    private JButton             colorButton;
    
    private JColorChooser       chooser;
    private JDialog             chooserDialog;
    private JButton             chooserOKButton;
    private Color               selectedColor;

    @BeforeEach
    public void beforeEach() throws Exception
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
    public void afterEach() throws Exception
    {
        ComponentFinder.disposeAll();
    }

    @Test
    public void testFontEditor()
    {
        assertNotNull( nameCombo );
        assertNotNull( boldToggle );
        assertNotNull( italicToggle );
        assertNotNull( sizeEditor );
        assertNotNull( colorEditor );
        assertNotNull( feedback );
        
        FontProfile currFont    = new FontProfile();
        assertTrue( currFont.matches( defaultEditor ) );
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
        boldToggle.doClick();
        boolean expItalic   = !italicToggle.isSelected();
        italicToggle.doClick();
        
        Optional<Font>  optFont = defaultEditor.getSelectedFont();
        Font            font    = optFont.orElse( null );
        assertNotNull( font );
        assertEquals( expFontName, font.getName() );
        assertEquals( expBold, font.isBold() );
        assertEquals( expItalic, font.isItalic() );
        assertEquals( expSize, font.getSize() );
    }
    
    @Test
    public void testBoldToggle()
    {
        GUIUtils.schedEDTAndWait( () -> testBoldToggleEDT() );
    }
    
    private void testBoldToggleEDT()
    {
        boolean currBold    = boldToggle.isSelected();
        testBoldToggleEDT( currBold );
        
        currBold = !currBold;
        boldToggle.doClick();
        testBoldToggleEDT( currBold );
        
        currBold = !currBold;
        boldToggle.doClick();
        testBoldToggleEDT( currBold );
    }
    
    private void testBoldToggleEDT( boolean expBold )
    {
        Font    selFont     = defaultEditor.getSelectedFont().orElse( null );
        Font    fbFont      = feedback.getFont();
        assertNotNull( selFont );
        assertNotNull( fbFont );
        boolean currBold    = boldToggle.isSelected();
        assertEquals( expBold, currBold );
        assertEquals( expBold, selFont.isBold() );
        assertEquals( expBold, fbFont.isBold() );
    }
    
    @Test
    public void testItalicToggle()
    {
        GUIUtils.schedEDTAndWait( () -> testItalicToggleEDT() );
    }
    
    private void testItalicToggleEDT()
    {
        boolean currItalic  = italicToggle.isSelected();
        testItalicToggleEDT( currItalic );
        
        currItalic = !currItalic;
        italicToggle.setSelected( currItalic );
        testItalicToggleEDT( currItalic );
        
        currItalic = !currItalic;
        italicToggle.setSelected( currItalic );
        testItalicToggleEDT( currItalic );
    }
    
    private void testItalicToggleEDT( boolean expItalic )
    {
        Font    font        = defaultEditor.getSelectedFont().orElse( null );
        assertNotNull( font );
        boolean currItalic  = italicToggle.isSelected();
        assertEquals( expItalic, currItalic );
        assertEquals( expItalic, font.isItalic() );
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
    public void testColorButton()
    {
        showColorSelector();
        GUIUtils.schedEDTAndWait( () -> testColorButtonEDT() );
        GUIUtils.schedEDTAndWait( () -> {
            Color   expColor    = chooser.getColor();
            Color   actColor    = defaultEditor.getColor().orElse( null );
            assertEquals( expColor, actColor );
        });
    }
    
    private void testColorButtonEDT()
    {
        assertTrue( chooserDialog.isVisible() );
        Color   currColor   = defaultEditor.getColor().orElse( null );
        assertNotNull( currColor );
        int     currRGB     = currColor.getRGB() & 0xffffff;
        int     uniqueRGB   = currRGB ^ 0xff;
        Color   uniqueColor = new Color( uniqueRGB );
        chooser.setColor( uniqueColor );
        chooserOKButton.doClick();
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
    public void testInvalidColor()
    {
        GUIUtils.schedEDTAndWait( () -> testInvalidColorEDT() );
    }
    
    @Test
    public void testInvalidColorEDT()
    {
        colorText.setText( "q" );
        colorText.postActionEvent();
        Optional<Color> optColor        = defaultEditor.getColor();
        assertFalse( optColor.isPresent() );
        
        String          feedbackText    = 
            feedback.getText().toUpperCase();
        assertTrue( feedbackText.contains( "ERROR" ) );
    }

    @Test
    public void testGetNameCombo()
    {
        GUIUtils.schedEDTAndWait( () ->
            assertNotNull( defaultEditor.getNameCombo() )
        );
    }

    @Test
    public void testGetBoldToggle()
    {
        GUIUtils.schedEDTAndWait( () ->
            assertNotNull( defaultEditor.getBoldToggle() )
        );
    }

    @Test
    public void testGetItalicToggle()
    {
        GUIUtils.schedEDTAndWait( () ->
            assertNotNull( defaultEditor.getItalicToggle() )
        );
    }

    @Test
    public void testGetSizeEditor()
    {
        GUIUtils.schedEDTAndWait( () ->
            assertNotNull( defaultEditor.getSizeEditor() )
        );
    }

    @Test
    public void testGetColorEditor()
    {
        GUIUtils.schedEDTAndWait( () ->
            assertNotNull( defaultEditor.getColorEditor() )
        );
    }

    @Test
    public void testGetFeedback()
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
    
    private void showColorSelector()
    {
        assertFalse( SwingUtilities.isEventDispatchThread() );
        SwingUtilities.invokeLater( () -> colorButton.doClick() );
        Utils.pause( 250 );
        GUIUtils.schedEDTAndWait( () -> {
            getChooserDialog();
            getChooser();
            getChooserOKButton();
        });
    }
    
    private void getChooserDialog()
    {
        boolean canBeDialog     = true;
        boolean canBeFrame      = false;
        boolean mustBeVisible   = true;
        ComponentFinder finder  = 
            new ComponentFinder( canBeDialog, canBeFrame, mustBeVisible );
        Window  comp    = finder.findWindow( w -> true );
        assertNotNull( comp );
        assertTrue( comp instanceof JDialog );
        chooserDialog = (JDialog)comp;
    }
    
    private void getChooser()
    {
        // Assume getColorDialog called first
        JComponent  comp    = 
             ComponentFinder.find( 
                 chooserDialog, 
                 c -> (c instanceof JColorChooser) 
             );
        assertNotNull( comp );
        assertTrue( comp instanceof JColorChooser );
        chooser = (JColorChooser)comp;
    }
    
    private void getChooserOKButton()
    {
        Predicate<JComponent>   pred    = 
            ComponentFinder.getButtonPredicate( "OK" );
        JComponent              comp    =
            ComponentFinder.find( chooserDialog, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JButton );
        chooserOKButton = (JButton)comp;
    }
    
    private class FontProfile
    {
        public final Font   font;
        public final Color  textColor;
        
        public FontProfile()
        {
            textColor = colorEditor.getColor().orElse( null );
            
            String  name        = (String)nameCombo.getSelectedItem();
            int     size        = (int)sizeEditor.getValue();
            int     bold        = 
                boldToggle.isSelected() ? Font.BOLD : 0;
            int     italic      = 
                italicToggle.isSelected() ? Font.ITALIC : 0;
            int     style       = bold | italic;
            font = new Font( name, style, size );
        }
        
        @Override
        public int hashCode()
        {
            int hash    = Objects.hash( font, textColor );
            return hash;
        }
        
        @Override
        public boolean equals( Object obj )
        {
            boolean result  = false;
            if ( this == obj )
                result = true;
            else if ( obj == null )
                result = false;
            else if ( !(obj instanceof FontProfile ) )
                result = false;
            else
            {
                FontProfile that    = (FontProfile)obj;
                result = 
                    Objects.equals( this.font, that.font )
                    && Objects.equals( this.textColor, that.textColor );
            }
            return result;
        }
        
        public boolean matches( FontEditor that )
        {
            boolean result  = that == null;
            if ( !result )
            {
                Font    thatFont    = that.getSelectedFont().orElse( null );
                Color   thatColor   = that.getColor().orElse( null );
                result = 
                    Objects.equals( this.font, thatFont )
                    && Objects.equals( this.textColor, thatColor );
            }
            return result;
        }
    }
}
