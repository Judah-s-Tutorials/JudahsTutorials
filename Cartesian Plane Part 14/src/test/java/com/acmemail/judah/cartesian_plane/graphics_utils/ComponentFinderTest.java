package com.acmemail.judah.cartesian_plane.graphics_utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Window;
import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
    
/**
 * Note that testDisposeAll must be run last.
 * The order of other tests doesn't matter.
 * Set the method order to MethodOrderer.OrderAnnotation.class.
 * Set the order of testDisposeAll to 999.
 * Set the order of all other test methods to 0.
 * 
 * @author Jack Straub
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ComponentFinderTest
{
    @Test
    public void testFindWindow()
    {
        // public Window findWindow( Predicate<Window> pred )
    }

    @Test
    public void testFindPredicateOfJComponent()
    {
        // public JComponent find( Predicate<JComponent> pred )
        fail("Not yet implemented");
    }

    @Test
    public void testFindWindowPredicateOfJComponent()
    {
        // public static JComponent 
        // find( Window window, Predicate<JComponent> pred )
        fail("Not yet implemented");
    }

    @Test
    public void testFindJComponentPredicateOfJComponent()
    {
        // public static JComponent 
        // find( JComponent container, Predicate<JComponent> pred )
        fail("Not yet implemented");
    }

    @Test
    public void testTopWindowFilterAccessors()
    {
        ComponentFinder     finder  = new ComponentFinder();
        Predicate<Window>   filter  = w -> (w instanceof JDialog);
            finder.setTopWindowFilter( filter );
        assertEquals( filter, finder.getTopWindowFilter() );
    }
    
    @Test
    public void testBooleanAccessors()
    {
        ComponentFinder finder  = new ComponentFinder();
        testBooleanAccessors( 
            finder::setCanBeDialog, 
            finder::isCanBeDialog, 
            "can-be-dialog"
        );
        testBooleanAccessors( 
            finder::setCanBeFrame, 
            finder::isCanBeFrame, 
            "can-be-frame"
        );
        testBooleanAccessors( 
            finder::setMustBeVisible, 
            finder::isMustBeVisible, 
            "must-be-visible"
        );
        testBooleanAccessors( 
            finder::setMustBeDisplayable, 
            finder::isMustBeDisplayable, 
            "must-be-displayable"
        );
    }
    
    private void testBooleanAccessors( 
        Consumer<Boolean> setter, 
        BooleanSupplier   getter, 
        String            remark 
    )
    {
        setter.accept( false );
        assertFalse( getter.getAsBoolean(), remark );
        setter.accept( true );
        assertTrue( getter.getAsBoolean(), remark );
    }

    @Test
    public void testCtors()
    {
        ComponentFinder finder  = new ComponentFinder();
        assertTrue( finder.isCanBeDialog() );
        assertTrue( finder.isCanBeFrame() );
        assertTrue( finder.isMustBeVisible() );
        assertTrue( finder.isMustBeDisplayable() );
        
        testCtor( true, true, true );
        testCtor( false, true, true );
        testCtor( true, false, true );
        testCtor( true, true, false );
    }
    
    private void 
    testCtor( boolean cbDialog, boolean cbFrame, boolean mbVisible )
    {
        ComponentFinder finder  =
            new ComponentFinder( cbDialog, cbFrame, mbVisible );
        assertEquals( cbDialog, finder.isCanBeDialog() );
        assertEquals( cbFrame, finder.isCanBeFrame() );
        assertEquals( mbVisible, finder.isMustBeVisible() );
        assertTrue( finder.isMustBeDisplayable() );
    }
    

    @Test
    public void testGetButtonPredicate()
    {
        String  buttonLabel     = "Button Label";
        String  notButtonLabel  = "Not Button Label";
        JButton expButton       = new JButton( buttonLabel );
        JButton notExpButton    = new JButton( notButtonLabel );
        JLabel  notButton       = new JLabel( buttonLabel );
        
        Predicate<JComponent>   buttonPred  = 
            ComponentFinder.getButtonPredicate( buttonLabel );
        assertFalse( buttonPred.test( notExpButton ) );
        assertFalse( buttonPred.test( notButton ) );
        assertTrue( buttonPred.test( expButton ) );
    }

    /**
     * This test disposes all windows.
     * In doing so, it upsets the state
     * in which all other tests need to run.
     * Consequently, this test must be run last.
     */
    @Order( Integer.MAX_VALUE )
    @Test
    public void testDisposeAll()
    {
        // In case we're running this test stand-alone, make
        // sure that there are windows to dispose.
        new JDialog().pack();
        new JDialog().pack();
        
        // Sanity check: there must be at least two windows
        // that have not yet been disposed.
        long    count   = Arrays.stream( Window.getWindows() )
            .filter( Window::isDisplayable )
            .count();
        assertTrue( count > 1 );
        
        ComponentFinder.disposeAll();
        // Verify all windows have now been disposed
        count   = Arrays.stream( Window.getWindows() )
            .filter( Window::isDisplayable )
            .count();
        assertEquals( count, 0 );
    }
    
    private interface TestWindow
    {
        String  getTitle();
        List<String> getLabels();
        JPanel getContentPane();
        Window getWindow();
    }

}
