package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Component;
import java.util.stream.IntStream;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.test_utils.CPMenuBarTestDialog;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

class CPMenuBarTest
{
    private CPMenuBarTestDialog tester;
    private JDialog             lineDialog;
    private JDialog             graphDialog;
    private JDialog             aboutDialog;
    
    @BeforeEach
    public void beforeEach()
    {
        tester = CPMenuBarTestDialog.getTestDialog();
        lineDialog = tester.getLineDialog();
        graphDialog = tester.getGraphDialog();
        aboutDialog = tester.getAboutDialog();
    }

    @AfterEach
    void tearDown() throws Exception
    {
        ComponentFinder.disposeAll();
        tester = null;
        lineDialog = null;
        graphDialog = null;
        aboutDialog = null;
    }

    @Test
    public void testFileMenu()
    {
        JMenu       fileMenu    = tester.getMenu( "File" );
        assertNotNull( fileMenu );
        
        testFileItem( "Open" );
        testFileItem( "Save" );
        testFileItem( "Save As" );
        testFileItem( "Exit" );
        
        testFileDialog( "Open" );
        testFileDialog( "Save" );
        testFileDialog( "Save As" );
    }

    @Test
    public void testWindowMenu()
    {
        JMenu       windowMenu  = tester.getMenu( "Window" );
        assertNotNull( windowMenu );
        
        testWindowItem( "Edit Line Properties" );
        testWindowItem( "Edit Graph Properties" );
        
        assertTrue( windowMenu.getMenuComponentCount() >= 2 );
        Component   comp    = windowMenu.getMenuComponent( 0 );
        assertTrue( comp instanceof JCheckBoxMenuItem );
        JCheckBoxMenuItem   graphItem   = (JCheckBoxMenuItem)comp;
        comp = windowMenu.getMenuComponent( 1 );
        assertTrue( comp instanceof JCheckBoxMenuItem );
        JCheckBoxMenuItem   lineItem    = (JCheckBoxMenuItem)comp;
        
        testWindowDialog( lineItem, lineDialog );
        testWindowDialog( graphItem, graphDialog );
    }

    @Test
    public void testHelpMenu()
    {
        JMenu       helpMenu    = tester.getMenu( "Help" );
        assertNotNull( helpMenu );
        
        testHelpItem( "Math Help" );
        testHelpItem( "Math Help", "Math is Fun" );
        testHelpItem( "Math Help", "Wolfram Alpha" );
        testHelpItem( "Math Help", "Khan Academy" );
        testHelpItem( "Calculators" );
        testHelpItem( "Calculators", "web2.0calc" );
        testHelpItem( "Calculators", "Desmos" );
        testHelpItem( "Calculators", "Calculator.net" );
        testHelpItem( "About" );
        
        testHelpAbout();
    }
    
    @Test
    public void testActivateLink()
    {
        JMenuItem   mathIsFun   = 
            tester.getMenuItem( "Help", "Math Help", "Math is Fun" );
        assertNotNull( mathIsFun );
        Utils.pause( 500 );
        assertTrue( tester.hasFocus() );
        tester.doClick( mathIsFun );
        Utils.pause( 500 );
        assertFalse( tester.hasFocus() );
    }
    
    private void testFileItem( String text )
    {
        String[]    args    = { "File", text };
        JMenuItem   item    = tester.getMenuItem( args );
        assertNotNull( item );
        assertTrue( text.equals( item.getText() ) );
        assertTrue( item.getActionListeners().length > 0 );
    }
    
    private void testFileDialog( String text )
    {
        String[]    args    = { "File", text };
        JMenuItem   item    = tester.getMenuItem( args );
        assertNotNull( item );
        
        // Temporary logic, mainly to improve test coverage.
        // To be replaced by specific tests for "Open", "Save" and
        // "Save As", after those menu items are hooked up.
        tester.setEnabled( item, true );
        assertNotNull( item );
        tester.doClick( item );
        tester.setEnabled( item, false );
    }
    
    private void testWindowItem( String text )
    {
        String[]    args    = { "Window", text };
        JMenuItem   item    = tester.getMenuItem( args );
        assertNotNull( item );
        assertTrue( text.equals( item.getText() ) );
    }
    
    private void testWindowDialog( JCheckBoxMenuItem item, JDialog dialog )
    {
        // Start with dialog not visible
        assertFalse( item.isSelected() );
        assertFalse( tester.isVisible( dialog ) );

        // Make dialog visible by clicking check box item
        tester.doClick( item );
        assertTrue( item.isSelected() );
        assertTrue( tester.isVisible( dialog ) );

        // Make dialog non-visible by clicking check box item
        tester.doClick( item );
        assertFalse( item.isSelected() );
        assertFalse( tester.isVisible( dialog ) );

        // Make dialog visible by clicking check box item,
        // then make it non-visible by closing it directly;
        // make sure check box is made false.
        tester.doClick( item );
        assertTrue( item.isSelected() );
        assertTrue( tester.isVisible( dialog ) );
        tester.setVisible( dialog, false );
        Utils.pause( 500 );
        assertFalse( item.isSelected() );
        assertFalse( tester.isVisible( dialog ) );
    }
    
    private void testHelpItem( String... labels )
    {
        int         len     = labels.length;
        String      expText = labels[len - 1];
        String[]    args    = new String[len + 1];
        args[0] = "Help";
        IntStream.range( 0, len ).forEach( i -> args[i+1] = labels[i] );
        JMenuItem   item    = tester.getMenuItem( args );
        assertNotNull( item );
        assertTrue( expText.equals( item.getText() ) );
    }
    
    private void testHelpAbout()
    {
        JMenuItem   about   = tester.getMenuItem( "Help", "About" );
        assertNotNull( about );
        Thread      thread  = tester.doClickInThread( about );
        assertTrue( tester.isVisible( aboutDialog ) );
        tester.setVisible( aboutDialog, false );
        Utils.join( thread );
        assertFalse( tester.isVisible( aboutDialog ) );
    }
}
