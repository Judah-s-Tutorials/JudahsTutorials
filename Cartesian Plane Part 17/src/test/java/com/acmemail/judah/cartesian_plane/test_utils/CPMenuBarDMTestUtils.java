package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.components.CPFrame;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.input.Equation;

/**
 * This class contains utilities
 * for the support of testing
 * the data-model aspects of the CPMenuBar
 * (file/new, file/open, file/save, etc.).
 * It simplifies access to components
 * of the CPMenuBar,
 * and is responsible for managing operations
 * that must be performed on the Event Dispatch Thread (EDT).
 * 
 * @author Jack Straub
 */
public class CPMenuBarDMTestUtils
{
    /** The application window that contains the menu bar. */
    private final CPFrame           cpFrame;
    
    /** The object that encapsulates the File menu on the menu bar. */
    private final JMenu             fileMenu;
    /** The menu item that launches a "new" operation. */
    private final JMenuItem         newItem;
    private final JMenuItem         openItem;
    private final JMenuItem         saveItem;
    private final JMenuItem         saveAsItem;
    private final JMenuItem         deleteItem;
    
    /** This CPMenuBarDMTestUtils object. */
    private static CPMenuBarDMTestUtils utils;
    
    /**
     * Returns this class's singleton,
     * creating it if necessary.
     * @return  this class's singleton
     */
    public static CPMenuBarDMTestUtils getUtils()
    {
        if ( utils == null )
            GUIUtils.schedEDTAndWait( () -> 
                utils = new CPMenuBarDMTestUtils() );
        return utils;
    }
    
    /**
     * Tests the enabled state of all the data management
     * menu items. 
     * The user provides the expected state
     * of the "Save," "Save As" and "Delete" items;
     * all other items are
     * expected to be enabled.
     * 
     * @param expSave   the expected state of the "Save" item
     * @param expSaveAs the expected state of the "Save As" item
     * @param expDelete the expected state of the "Delete" item
     */
    public void 
    testEnablement( boolean expSave, boolean expSaveAs, boolean expDelete )
    {
        assertTrue( newItem.isEnabled() );
        assertTrue( openItem.isEnabled() );
        assertEquals( expSave, saveItem.isEnabled() );
        assertEquals( expSaveAs, saveAsItem.isEnabled() );
        assertEquals( expDelete, deleteItem.isEnabled() );
    }
    
    /**
     * Dispose this object, 
     * and free all resources.
     */
    public void dispose()
    {
        utils = null;
        cpFrame.setVisible( false );
        cpFrame.dispose();
    }
    
    /**
     * Constructor.
     * Creates a CPFrame object,
     * and fully initializes this object.
     */
    private CPMenuBarDMTestUtils()
    {
        cpFrame = new CPFrame();
        fileMenu = getFileMenu();
        newItem = getMenuItem( "New" );
        openItem = getMenuItem( "Open" );
        saveItem = getMenuItem( "Save" );
        saveAsItem = getMenuItem( "Save As" );
        deleteItem = getMenuItem( "Delete" );
        utils = this;
    }
    
    /**
     * Gets the Equation object from the CPFrame.
     * 
     * @return  the Equation object from the CPFrame
     */
    public Equation getEquation()
    {
        return cpFrame.getEquation();
    }
    
    /**
     * Sets the Equation object in the CPFrame
     * to the given value.
     * 
     * param  equation the given value
     */
    public void setEquation( Equation equation )
    {
        cpFrame.loadEquation( equation );
    }
    
    /**
     * Activates the file/new function.
     */
    public void newEquation()
    {
        doClick( newItem );
    }
    
    /**
     * Clicks the given button.
     * This work is always delegated to the EDT.
     * 
     * @param button    the given button
     */
    public void doClick( AbstractButton button )
    {
        SwingUtilities.invokeLater(() -> button.doClick() );
        Utils.pause( 500 );
    }

    /**
     * Gets the File menu from the menu bar.
     * <p>
     * Precondition: The CPFrame has been created.
     * <p>
     * Precondition: This method must be called from the EDT.
     * 
     * @return the File menu object
     */
    private JMenu getFileMenu()
    {
        Predicate<JComponent>   isMenu  = c -> (c instanceof JMenu);
        Predicate<JComponent>   hasText = 
            c -> "File".equals( ((JMenu)c).getText() );
        Predicate<JComponent>   pred    = isMenu.and( hasText );
        JComponent              comp    =
            ComponentFinder.find( cpFrame, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JMenu );
        return (JMenu)comp;
    }
    
    /**
     * Finds the item within the File JMenu
     * with the given text.
     * <p>
     * Precondition: The fileMenu variable is initialized
     * <p>
     * Precondition: This method must be called from the EDT.
     * 
     * @param text  the given text
     * 
     * @return  
     *      the item within the File menu with the given text
     */
    private JMenuItem getMenuItem( String text )
    {
        JMenuItem   item    =
            Stream.of( fileMenu.getMenuComponents() )
                .filter( c -> (c instanceof JMenuItem) )
                .map( c -> (JMenuItem)c )
                .filter( i -> text.equalsIgnoreCase( i.getText() ) )
                .findFirst()
                .orElse( null );
        return item;
    }
}

