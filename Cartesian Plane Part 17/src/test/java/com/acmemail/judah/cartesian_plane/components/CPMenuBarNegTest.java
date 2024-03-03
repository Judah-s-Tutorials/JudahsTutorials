package com.acmemail.judah.cartesian_plane.components;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;

/**
 * Gets coverage of CPMenuBar logic dependent on the presence
 * of a CPFrame. This entails starting JFrame (other than a CPFrame)
 * with a menu bar; then, for each File/menuItem:
 * <ul>
 * <li>Enable the menuItem;</li>
 * <li>Click the menuItem; and</li>
 * <li>Restore the menuItem's original state.</li>
 * </ul>
 * 
 * @author Jack Straub
 */
class CPMenuBarNoCPFrameTest
{
    private static TestFrame   frame;
    
    @BeforeAll
    static void beforeAll() 
    {
        GUIUtils.schedEDTAndWait( () -> frame = new TestFrame() );
    }

    @Test
    void test()
    {
        frame.clickButton( frame.newItem );
        frame.clickButton( frame.openItem );
        frame.clickButton( frame.saveItem );        
        frame.clickButton( frame.saveAsItem );
        frame.clickButton( frame.deleteItem );
    }

    @SuppressWarnings("serial")
    private static class TestFrame extends JFrame
    {
        public final JMenu      fileMenu;
        public final JMenuItem  newItem;
        public final JMenuItem  openItem;
        public final JMenuItem  saveItem;
        public final JMenuItem  saveAsItem;
        public final JMenuItem  deleteItem;
        
        public TestFrame()
        {
            super( "CPMenuBar Negative Testing" );
            setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            
            JPanel  contentPane = new JPanel( new BorderLayout() );
            JPanel  placeHolder = new JPanel();
            placeHolder.setPreferredSize( new Dimension( 200, 100 ));
            placeHolder.setBackground( Color.ORANGE );
            contentPane.add( placeHolder, BorderLayout.CENTER );
            contentPane.add( new CPMenuBar( this ), BorderLayout.NORTH );
            
            setContentPane( contentPane );
            pack();
            setVisible( true );
            
            fileMenu = getFileMenu();
            newItem = getMenuItem( "New" );
            openItem = getMenuItem( "Open" );
            saveItem = getMenuItem( "Save" );
            saveAsItem = getMenuItem( "Save As" );
            deleteItem = getMenuItem( "Delete" );
        }
        
        public void clickButton( AbstractButton button )
        {
            GUIUtils.schedEDTAndWait( () -> {
                boolean enabledOrig = button.isEnabled();
                button.setEnabled( true );
                button.doClick();
                button.setEnabled( enabledOrig );
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
}
