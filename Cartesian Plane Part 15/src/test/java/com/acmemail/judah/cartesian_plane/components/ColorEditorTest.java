package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.util.function.Predicate;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

class ColorEditorTest
{
    private ColorEditor defEditor;
    
    private JTextField  textEditor;
    private JButton     colorButton;
    private JComponent  feedbackWindow;
    
    /** The dialog containing the color chooser. */
    private JDialog         chooserDialog;
    /** The JColorChooser component of the chooserDialog. */
    private JColorChooser   chooser;
    /** The OK button on the color chooser dialog. */
    private JButton         chooserOKButton;
    /** The cancel button on the color chooser dialog. */
    private JButton         chooserCancelButton;
    
    private boolean     actionListener1Fired;
    private boolean     actionListener2Fired;
    
    @BeforeEach
    public void beforeEach()
    {
        defEditor = new ColorEditor();
        textEditor = defEditor.getTextEditor();
        colorButton = defEditor.getColorButton();
        feedbackWindow = defEditor.getFeedback();
    }

    @Test
    void testColorEditor()
    {
        assertNotNull( textEditor );
        assertNotNull( colorButton );
        assertNotNull( feedbackWindow );
    }

    @Test
    void testAddActionListener()
    {
        actionListener1Fired = false;
        actionListener2Fired = false;
        defEditor.addActionListener( e -> actionListener1Fired = true );
        commit();
        assertTrue( actionListener1Fired );
        assertFalse( actionListener2Fired );

        actionListener1Fired = false;
        actionListener2Fired = false;
        defEditor.addActionListener( e -> actionListener2Fired = true );
        commit();
        assertTrue( actionListener1Fired );
        assertTrue( actionListener2Fired );
    }

    @Test
    void testRemoveActionListener()
    {
        ActionListener  listener1   = e -> actionListener1Fired = true;
        ActionListener  listener2   = e -> actionListener2Fired = true;

        actionListener1Fired = false;
        actionListener2Fired = false;
        defEditor.addActionListener( listener1 );
        defEditor.addActionListener( listener2 );
        commit();
        assertTrue( actionListener1Fired );
        assertTrue( actionListener2Fired );

        actionListener1Fired = false;
        actionListener2Fired = false;
        defEditor.removeActionListener( listener2 );
        commit();
        assertTrue( actionListener1Fired );
        assertFalse( actionListener2Fired );

        actionListener1Fired = false;
        actionListener2Fired = false;
        defEditor.removeActionListener( listener1 );
        commit();
        assertFalse( actionListener1Fired );
        assertFalse( actionListener2Fired );
    }

    @Test
    void testCommit()
    {
        int     origRGB = getFeedbackRGB();
        int     testRGB = getUniqueRGB();
        textEditor.setText( "" + testRGB );        
        // edit hasn't been committed, so the feedback window 
        // should not have changed color
        assertEquals( origRGB, getFeedbackRGB() );
        
        commit();
        assertEquals( testRGB, getFeedbackRGB() );
    }

    @Test
    void testGetPanel()
    {
        // Just make sure we get back a JPanel containing all three 
        // ColorEditor components somewhere in the panel's window hierarchy.
        
        Predicate<JComponent>   isTextEditor    = c -> (c == textEditor);
        Predicate<JComponent>   isColorButton   = c -> (c == colorButton);
        Predicate<JComponent>   isFeedback      = c -> (c == feedbackWindow);
        JPanel  panel   = defEditor.getPanel();
        assertNotNull( ComponentFinder.find( panel, isTextEditor ) );
        assertNotNull( ComponentFinder.find( panel, isColorButton ) );
        assertNotNull( ComponentFinder.find( panel, isFeedback ) );
    }
    
    @Test
    public void testGetTextEditor()
    {
        int testRGB = getUniqueRGB();
        textEditor.setText( "" + testRGB );        
        commit();
        assertEquals( testRGB, getFeedbackRGB() );
    }

    @Test
    void testGetColorButton()
    {
        // startColorSelector pokes the colorButton which causes
        // the chooser dialog to be posted.
        Thread  thread  = startColorSelector();
        assertTrue( chooser.isVisible() );
        
        int     testRGB     = getUniqueRGB();
        Color   testColor   = new Color( testRGB );
        chooser.setColor( testColor );
        chooserOKButton.doClick();
        Utils.join( thread );
        assertEquals( testRGB, getFeedbackRGB() );
    }

    @Test
    void testSelectColorAndCancel()
    {
        // start color selector and select a color; poke the cancel
        // button, and verify that the selected color is not applied.
        // the chooser dialog to be posted.
        Thread  thread  = startColorSelector();
        assertTrue( chooser.isVisible() );
        
        int     origRGB     = getFeedbackRGB();
        int     testRGB     = getUniqueRGB();
        Color   testColor   = new Color( testRGB );
        chooser.setColor( testColor );
        chooserCancelButton.doClick();
        Utils.join( thread );
        assertEquals( origRGB, getFeedbackRGB() );
    }

    @Test
    void testGetColor()
    {
        int     testRGB     = getUniqueRGB();
        textEditor.setText( "" + testRGB );        
        commit();
        
        int     actRGB      = getFeedbackRGB();
        assertEquals( testRGB, actRGB );
    }
    
    @Test
    public void testEditTextNeg()
    {
        // Enter an invalid value into the text editor, and
        // verify that the GUI behaves accordingly.
        Color   origColor   = getFeedbackColor();
        GUIUtils.schedEDTAndWait( () ->  textEditor.setText( "invalid" ) );
        commit();
        GUIUtils.schedEDTAndWait( () -> {
            String  actText = textEditor.getText().toUpperCase();
            assertTrue( actText.contains( "ERROR" ) );
        });
        assertEquals( origColor, getFeedbackColor() );
    }
    
    private void commit()
    {
        // commit will precipitate activity on the EDT.
        // Give it a moment to do its thing, and settle down.
        defEditor.commit();
        Utils.pause( 250 );
    }
    
    private Color getFeedbackColor()
    {
        Color[] bgColor = new Color[1];
        GUIUtils.schedEDTAndWait( () -> 
            bgColor[0] = feedbackWindow.getBackground() 
        );
        return bgColor[0];
    }
    
    private int getUniqueRGB()
    {
        int fRGB    = getFeedbackRGB();
        int rgb     = fRGB ^ 0xff;
        assertNotEquals( fRGB, rgb );
        return rgb;
    }
    
    private int getFeedbackRGB()
    {
        int rgb = getRGB( getFeedbackColor() );
        return rgb;
    }
    
    private int getRGB( Color color )
    {
        int rgb = color.getRGB() & 0xffffff;
        return rgb;
    }
    
    private Thread startColorSelector()
    {
        Thread  thread  = 
            new Thread( () -> colorButton.doClick(), "ColorSelectorThread" );
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
