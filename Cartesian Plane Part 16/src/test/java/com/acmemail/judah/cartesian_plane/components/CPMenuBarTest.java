package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Component;
import java.util.stream.Stream;

import javax.swing.JCheckBoxMenuItem;
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
    
    @BeforeEach
    public void beforeEach()
    {
        tester = CPMenuBarTestDialog.getTestDialog();
    }

//    @AfterEach
    void tearDown() throws Exception
    {
        ComponentFinder.disposeAll();
        tester = null;
    }

    @Test
    public void test()
    {
        CPMenuBarTestDialog.getTestDialog();
        Utils.pause( 1000 );
    }

//    @Test
    public void testFileMenu()
    {
        JMenu       fileMenu    = tester.getMenu( "File" );
        assertNotNull( fileMenu );
        
        testFileItem( "Open" );
        testFileItem( "Save" );
        testFileItem( "Save As" );
        testFileItem( "Exit" );
    }

    @Test
    public void testWindowMenu()
    {
        JMenu       windowMenu  = tester.getMenu( "Window" );
        assertNotNull( windowMenu );
        
        assertTrue( windowMenu.getMenuComponentCount() >= 2 );
        Component   comp    = windowMenu.getMenuComponent( 0 );
        assertTrue( comp instanceof JCheckBoxMenuItem );
        JCheckBoxMenuItem   graphItem   = (JCheckBoxMenuItem)comp;
        comp = windowMenu.getMenuComponent( 1 );
        assertTrue( comp instanceof JCheckBoxMenuItem );
        JCheckBoxMenuItem   lineItem    = (JCheckBoxMenuItem)comp;
        
        assertFalse( graphItem.isSelected() );
        assertFalse( lineItem.isSelected() );
    }

//    @Test
    public void getMenuItemTest()
    {
        getMenuItemTest( "File" );
        getMenuItemTest( "File", "Open" );
        getMenuItemTest( "File", "Save" );
        getMenuItemTest( "File", "Save As" );
        getMenuItemTest( "File", "Exit" );

        getMenuItemTest( "Window" );
        getMenuItemTest( "Window", "Edit Line Properties" );
        getMenuItemTest( "Window", "Edit Graph Properties" );

        getMenuItemTest( "Help" );
        getMenuItemTest( "Help", "Math Help" );
        getMenuItemTest( "Help", "Math Help", "Math is Fun" );
        getMenuItemTest( "Help", "Math Help", "Wolfram Alpha" );
        getMenuItemTest( "Help", "Math Help", "Khan Academy" );

        getMenuItemTest( "Help", "Calculators" );
        getMenuItemTest( "Help", "Calculators", "web2.0calc" );
        getMenuItemTest( "Help", "Calculators", "Calculator.net" );

        getMenuItemTest( "Help", "About" );
    }
    
    private void testFileItem( String text )
    {
        String[]    args    = { "File", text };
        JMenuItem   item    = tester.getMenuItem( args );
        assertNotNull( item );
        assertTrue( text.equals( item.getText() ) );
        assertTrue( item.getActionListeners().length > 0 );
    }
    
    private void getMenuItemTest( String... labels )
    {
        StringBuilder   bldr    = new StringBuilder();
        Stream.of( labels )
            .peek( bldr::append )
            .forEach( l -> bldr.append( "/" ) );
        bldr.append( " ==> " );
        JMenuItem   item    = tester.getMenuItem( labels );
        bldr.append( item.getText() )
            .append( " (visible: " )
            .append( item.isVisible() )
            .append( ")" );
        System.out.println( bldr );
    }
}
