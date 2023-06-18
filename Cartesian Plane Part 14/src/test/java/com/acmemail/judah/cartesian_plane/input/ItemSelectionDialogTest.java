package com.acmemail.judah.cartesian_plane.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.AWTException;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListModel;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.test_utils.RobotAssistant;
import com.acmemail.judah.cartesian_plane.test_utils.Utils;

class ItemSelectionDialogTest
{
    private static JList<?> jList;
    private static List<?>  actListItems;
    private static JButton  okButton;
    private static JButton  cancelButton;
    
    private static final String[]   names   =
    { "Sally", "Manny", "Jane", "Moe", "Anapurna", 
      "Jack", "Alice", "Tweedledee", "Elizabeth", "Tweedledum",
    };
    
    private static final ItemSelectionDialog   mainDialog   =
        new ItemSelectionDialog( "JUnit Test", names );
    
    /** This variable set by show() method. */
    private int selection   = 0;
    
    @BeforeAll
    public static void beforeAll()
    {
        ComponentFinder finder  =
            new ComponentFinder( true, false, false );
        
        okButton = getButton( finder, "OK" );
        cancelButton = getButton( finder, "Cancel" );
        JComponent comp = finder.find( c -> c instanceof JList );
        assertNotNull( comp );
        assertTrue( comp instanceof JList );
        jList = (JList<?>)comp;
        
        ListModel<?>    model   = jList.getModel();
        int             size    = model.getSize();
        actListItems = IntStream.range( 0, size )
            .mapToObj( model::getElementAt )
            .collect( Collectors.toList() );
    }
    
    private static JButton getButton( ComponentFinder finder, String text )
    {
        Predicate<JComponent>   pred    = 
            ComponentFinder.getButtonPredicate( text );
        JComponent  comp    = finder.find( pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JButton );
        assertEquals( text, ((JButton)comp).getText() );
        return (JButton)comp;
    }
    
    @Test
    public void testListItems()
    {
        assertEquals( names.length, actListItems.size() );
        Arrays.stream( names )
            .forEach( n -> assertTrue( actListItems.contains( n ) ) );
    }
    
    @Test
    void testShowOK() 
        throws InterruptedException
    {
        Thread          thread          = startDialog();
        int             expSelection    = 2;
        jList.setSelectedIndex( expSelection );
        okButton.doClick();
        thread.join();
        
        assertEquals( expSelection, selection );
    }

    @Test
    void testShowCancel() 
        throws InterruptedException
    {
        Thread          thread  = startDialog();
        cancelButton.doClick();
        thread.join();
        
        assertTrue( selection < 0 );
    }
    
    @Test
    public void testEnterKeyboardAction() 
        throws InterruptedException, AWTException
    {
        Thread          thread          = startDialog();
        RobotAssistant  robot           = new RobotAssistant();
        int             expSelection    = 2;
        jList.setSelectedIndex( expSelection );
        robot.type( "", KeyEvent.VK_ENTER );
        thread.join();
        
        assertEquals( expSelection, selection );
    }
    
    @Test
    public void testEscapeKeyboardAction() 
        throws InterruptedException, AWTException
    {
        Thread          thread          = startDialog();
        RobotAssistant  robot           = new RobotAssistant();
                robot.type( "", KeyEvent.VK_ESCAPE );
        thread.join();
        
        assertTrue( selection < 0 );
    }
    
    @Test
    public void testDialogContent()
    {
        assertEquals( names.length, actListItems.size() );
        Arrays.stream( names )
            .forEach( n -> assertTrue( actListItems.contains( n ) ) );
    }
    
    @Test
    public void testEmptyList()
        throws InterruptedException
    {
        String              title   = "Empty String Tester";
        ItemSelectionDialog dialog  = 
            new ItemSelectionDialog( title, new String[0] );
        Thread              thread          = startDialog( dialog );
        ComponentFinder     finder          = 
            new ComponentFinder( true, false, true );
        JButton             okButton        = getButton( finder, "OK" );
        JButton             cancelButton    = getButton( finder, "Cancel" );
        assertFalse( okButton.isEnabled() );
        cancelButton.doClick();
        thread.join();
        assertTrue( selection < 0 );

        Predicate<Window>   pred            = 
            ComponentFinder.getWindowPredicate( title );
        finder.setMustBeVisible(false);
        Window              dialogWindow    = finder.findWindow( pred );
        assertNotNull( dialogWindow );
        dialogWindow.dispose();
    }

    private Thread startDialog()
    {
        Thread  thread  = startDialog( mainDialog );
        return thread;
    }

    private Thread startDialog( ItemSelectionDialog dialog )
    {
        Thread  thread  = new Thread( () -> show( dialog ) );
        thread.start();
        Utils.pause( 500 );
        return thread;
    }
    
    private void show( ItemSelectionDialog dialog )
    {
        selection = dialog.show();
    }
}
