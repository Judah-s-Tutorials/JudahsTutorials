package com.acmemail.judah.cartesian_plane.components;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Window;
import java.util.function.Predicate;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

class ColorSelectorTest
{
    private ColorSelector   colorSelector;
    private JDialog         chooserDialog;
    private JColorChooser   chooser;
    private JButton         chooserOKButton;
    private JButton         chooserCancelButton;
    
    private Color           selectedColor;
    
    @BeforeEach
    public void beforeEach()
    {
        GUIUtils.schedEDTAndWait( () -> 
            colorSelector = new ColorSelector()
        );
    }
    
    @AfterEach
    public void afterEach()
    {
        ComponentFinder.disposeAll();
    }
    
    @Test
    public void testColorSelector()
    {
        Thread  thread  = showColorSelector();
        GUIUtils.schedEDTAndWait( () -> chooserOKButton.doClick() );
        Utils.join( thread );
    }

    /**
     * @param iColor
     */
    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5})
    public void testColorSelectorColor( int iColor )
    {
        Color   color   = new Color( iColor );
        GUIUtils.schedEDTAndWait( () -> 
            colorSelector   = new ColorSelector( color )
        );
        Thread  thread  = showColorSelector();
        GUIUtils.schedEDTAndWait( () -> chooserOKButton.doClick() );
        Utils.join( thread );
        assertEquals( color, selectedColor );
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5})
    void testColorSelectorWindowStringColor( int iColor )
    {
        Color   color   = new Color( iColor );
        String  title   = "Test Title" + iColor;
        GUIUtils.schedEDTAndWait( () -> 
            colorSelector   = new ColorSelector( null, title, color )
        );
        Thread  thread  = showColorSelector();
        GUIUtils.schedEDTAndWait( () -> {
            assertEquals( title, chooserDialog.getTitle() );
            chooserOKButton.doClick();
        });
        Utils.join( thread );
        assertEquals( color, selectedColor );
    }

    @Test
    void testShowDialogOK()
    {
        Thread  thread  = showColorSelector();
        assertTrue( chooserDialog.isVisible() );
        Color   color   = getUniqueColor( chooser.getColor() );
        chooser.setColor( color );
        chooserOKButton.doClick();
        Utils.join( thread );
        assertEquals( color, selectedColor );
    }

    @Test
    void testShowDialogCancel()
    {
        Thread  thread  = showColorSelector();
        assertTrue( chooserDialog.isVisible() );
        chooserCancelButton.doClick();
        Utils.join( thread );
        assertNull( selectedColor );
    }
    
    private void clickButton( JButton button )
    {
        GUIUtils.schedEDTAndWait( () -> chooserOKButton.doClick() );
    }
    
    private Color getUniqueColor( Color base )
    {
        int     iBase   = base.getRGB();
        int     iUnique = iBase ^ 0xFFFFFF;
        Color   color   = new Color( iUnique );
        return color;
    }
    
    private Thread showColorSelector()
    {
        assertNotNull( colorSelector );
        Thread  thread  = 
            new Thread( () -> selectedColor = colorSelector.showDialog() );
        thread.start();
        Utils.pause( 250 );
        GUIUtils.schedEDTAndWait( () -> {
            getChooserDialog();
            getChooser();
            getChooserOKButton();
            getChooserCancelButton();
        });
        return thread;
    }
    
    private void getChooserDialog()
    {
        boolean canBeDialog     = true;
        boolean canBeFrame      = false;
        boolean mustBeVisible   = true;
        ComponentFinder finder  = 
            new ComponentFinder( canBeDialog, canBeFrame, mustBeVisible );
        Window  comp    = finder.findWindow( c -> true );
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
    
    private void getChooserCancelButton()
    {
        Predicate<JComponent>   pred    = 
            ComponentFinder.getButtonPredicate( "Cancel" );
        JComponent              comp    =
            ComponentFinder.find( chooserDialog, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JButton );
        chooserCancelButton = (JButton)comp;
    }
}
