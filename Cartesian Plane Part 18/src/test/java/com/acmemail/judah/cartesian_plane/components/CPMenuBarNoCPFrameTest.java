package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;

/**
 * Gets coverage of CPMenuBar logic dependent on the presence
 * of a CPFrame. This entails starting a JFrame (other than a CPFrame)
 * with a menu bar; then, for each File/menuItem:
 * <ul>
 * <li>Enable the menuItem;</li>
 * <li>Click the menuItem; and</li>
 * <li>Restore the menuItem's original state.</li>
 * </ul>
 * 
 * @author Jack Straub
 */
public class CPMenuBarNoCPFrameTest
{
    private static final PropertyManager    pMgr    = 
        PropertyManager.INSTANCE;
    private static TestFrame                frame;
    
    @BeforeAll
    static void beforeAll() 
    {
        GUIUtils.schedEDTAndWait( () -> frame = new TestFrame() );
    }

    @Test
    void test()
    {
        // Set DM properties to ensure all menu items are enabled
        pMgr.setProperty( CPConstants.DM_MODIFIED_PN, true );
        pMgr.setProperty( CPConstants.DM_OPEN_EQUATION_PN, true );
        pMgr.setProperty( CPConstants.DM_OPEN_FILE_PN, true );
        frame.activateItems();
    }

    @SuppressWarnings("serial")
    private static class TestFrame extends JFrame
    {
        private String[]                allLabels   =
        { "New", "Open", "Save As", "Save", "Delete", "Close" };
        private List<AbstractButton>    allItems    = new ArrayList<>();
        
        public TestFrame()
        {
            super( "CPMenuBar Negative Testing" );
            setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            
            JPanel  contentPane = new JPanel( new BorderLayout() );
            contentPane.add( new CPMenuBar( this ), BorderLayout.NORTH );
            
            setContentPane( contentPane );
            pack();
            setVisible( true );
            
            JMenu   menu    = getFileMenu();
            Stream.of( allLabels )
                .forEach( s -> allItems.add( getMenuItem( menu, s ) ) );
        }
        
        public void activateItems()
        {
            allItems.forEach( this::clickButton );
        }
        
        private void clickButton( AbstractButton button )
        {
            GUIUtils.schedEDTAndWait( () -> {
                assertTrue( button.isEnabled() );
                button.doClick();
            });
        }
        
        private JMenu getFileMenu()
        {
            Predicate<JComponent>   isMenu      = 
                c -> (c instanceof JMenu);
            Predicate<JComponent>   isFileMenu  =
                c -> "File".equals( ((JMenu)c).getText() );
            Predicate<JComponent>   pred        =
                isMenu.and( isFileMenu );
            JComponent              comp    =
                ComponentFinder.find( this, pred );
            assertNotNull( comp );
            assertTrue( comp instanceof JMenu );
            JMenu   fileMenu    = (JMenu)comp;
            return fileMenu;
        }
        
        private JMenuItem getMenuItem( JMenu fileMenu, String text )
        {
            JMenuItem   item    =
                Stream.of( fileMenu.getMenuComponents() )
                    .filter( c -> (c instanceof JMenuItem) )
                    .map( c -> (JMenuItem)c )
                    .filter( i -> text.equalsIgnoreCase( i.getText() ) )
                    .findFirst()
                    .orElse( null );
            assertNotNull( item );
            return item;
        }
    }
}
