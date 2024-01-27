package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Window;
import java.util.function.Predicate;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

class AboutDialogTest
{
    private static final String testResDir  = "AboutDialogTest";
    private static final String expTestText = "AboutDialog test";
    
    private AboutDialog about;
    
    @AfterEach
    public void afterEach() throws Exception
    {
        ComponentFinder.disposeAll();
        about = null;
    }

    @Test
    public void testAboutDialog()
    {
        about = new AboutDialog();
        Thread  thread  = showDialog();
        assertTrue( about.getDialog().isVisible() );
        closeDialog();
        Utils.join( thread );
    }

    @Test
    public void testAboutDialogWindow()
    {
        JFrame  frame   = new JFrame();
        about = new AboutDialog( frame );
        Window  owner   = about.getDialog().getOwner();
        assertNotNull( owner );
        assertEquals( frame, owner );
    }

    @Test
    public void testAboutDialogWindowString()
    {
        JFrame  frame   = new JFrame();
        about = new AboutDialog( frame, testResDir );
        Window  owner   = about.getDialog().getOwner();
        assertNotNull( owner );
        assertEquals( frame, owner );
        validateText( expTestText );
    }

    @Test
    public void testShowDialog()
    {
        about = new AboutDialog();
        JDialog dialog  = about.getDialog();
        Thread  thread  = showDialog();
        assertTrue( dialog.isVisible() );
        closeDialog();
        Utils.join( thread );
        assertFalse( dialog.isVisible() );
        
        thread = showDialog();
        assertTrue( dialog.isVisible() );
        GUIUtils.schedEDTAndWait( () -> about.showDialog( false ) );
        Utils.join( thread );
        assertFalse( dialog.isVisible() );

    }

    @Test
    public void testGetDialog()
    {
        about = new AboutDialog();
        JDialog dialog  = about.getDialog();
        assertNotNull( dialog );
    }

    private Thread showDialog()
    {
        Thread  thread  = new Thread( () ->
            GUIUtils.schedEDTAndWait( () -> about.showDialog( true ) )
        );
        thread.start();
        Utils.pause( 125 );
        return thread;
    }
    
    private void closeDialog()
    {
        GUIUtils.schedEDTAndWait( () -> closeDialogEDT() );
    }

    private void closeDialogEDT()
    {
        Predicate<JComponent>   pred    =
            ComponentFinder.getButtonPredicate( "Close" );
        JDialog                 dialog  = about.getDialog();
        JComponent              comp    = 
            ComponentFinder.find( dialog, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof AbstractButton );
        ((AbstractButton)comp).doClick();
    }
    
    private void validateText( String expText )
    {
        GUIUtils.schedEDTAndWait( () -> validateTextEDT( expText ) );
    }
    
    private void validateTextEDT( String expText )
    {
        Predicate<JComponent>   pred    = c -> (c instanceof JEditorPane );
        JDialog                 dialog  = about.getDialog();
        JComponent              comp    = 
            ComponentFinder.find( dialog, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JEditorPane );
        
        String                  actText = ((JEditorPane)comp).getText();
        assertTrue( actText.contains( expText ) );
    }
}
