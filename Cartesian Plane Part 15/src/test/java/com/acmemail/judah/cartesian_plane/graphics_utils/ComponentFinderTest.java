package com.acmemail.judah.cartesian_plane.graphics_utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.GridLayout;
import java.awt.Window;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.BeforeAll;
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
    private static final String     frameID             = "Frame";
    private static final String     dialogID            = "Dialog";
    private static final String     disposedPrefix      = "Disposed";
    private static final String     notVisiblePrefix    = "NotVisible";
    private static final String     visiblePrefix       = "Visible";
    
    private static final String     titleLabel          = "Title";
    private static final String     abortLabel          = "Abort";
    private static final String     cancelLabel         = "Cancel";
    private static final String     exitLabel           = "Exit";
    private static final String     okLabel             = "OK";
    
    private static TestDialog       visibleDialog;
    private static TestDialog       notVisibleDialog;
    private static TestDialog       disposedDialog;
    
    private static TestFrame        visibleFrame;
    private static TestFrame        notVisibleFrame;
    private static TestFrame        disposedFrame;

    @BeforeAll
    public static void beforeAll() throws Exception
    {
        visibleDialog = new TestDialog( visiblePrefix );
        visibleDialog.show();
        notVisibleDialog = new TestDialog( notVisiblePrefix );
        disposedDialog = new TestDialog( disposedPrefix );
        disposedDialog.dispose();
        
        visibleFrame = new TestFrame( visiblePrefix );
        visibleFrame.start( true );
        notVisibleFrame = new TestFrame( notVisiblePrefix );
        notVisibleFrame.start( false );
        disposedFrame = new TestFrame( disposedPrefix );
        disposedFrame.start( false );
        disposedFrame.dispose();
    }
    
    @Test
    public void testFindPredicateTFFT()
    {
        testFindPredicateOfJComponent( true, false, false, true );
    }
    
    @Test
    public void testFindPredicateFTFT()
    {
        testFindPredicateOfJComponent( false, true, false, true );
    }
    
    @Test
    public void testFindPredicateTTFT()
    {
        testFindPredicateOfJComponent( true, true, false, true );
    }
    
    @Test
    public void testFindPredicateTTTT()
    {
        testFindPredicateOfJComponent( true, true, true, true );
    }
    
    @Test
    public void testFindPredicateTFTT()
    {
        testFindPredicateOfJComponent( true, false, true, true );
    }
    
    @Test
    public void testFindPredicateTTFF()
    {
        testFindPredicateOfJComponent( true, true, false, false );
    }
    
    @Test
    public void testFindWindowTFFT()
    {
        testFindWindowPredicateOfWindow( true, false, false, true );
    }
    
    @Test
    public void testFindWindowFTFT()
    {
        testFindWindowPredicateOfWindow( false, true, false, true );
    }
    
    @Test
    public void testFindWindowTTFT()
    {
        testFindWindowPredicateOfWindow( true, true, false, true );
    }
    
    @Test
    public void testFindWindowTTTT()
    {
        testFindWindowPredicateOfWindow( true, true, true, true );
    }
    
    @Test
    public void testFindWindowTFTT()
    {
        testFindWindowPredicateOfWindow( true, false, true, true );
    }
    
    @Test
    public void testFindWindowTTFF()
    {
        testFindWindowPredicateOfWindow( true, true, false, false );
    }

    private void testFindPredicateOfJComponent( 
        boolean canBeDialog, 
        boolean canBeFrame, 
        boolean mustBeVisible,
        boolean mustBeDisplayable
    )
    {
        List<TestWindow>    successWindows  = 
            getExpSuccessWindows( 
                canBeDialog, 
                canBeFrame, 
                mustBeVisible, 
                mustBeDisplayable
            );
        List<TestWindow>    failWindows     = 
            getExpFailWindows( 
                canBeDialog, 
                canBeFrame, 
                mustBeVisible, 
                mustBeDisplayable
            );        
        ComponentFinder finder  = 
            new ComponentFinder( canBeDialog, canBeFrame, mustBeVisible );
        finder.setMustBeDisplayable( mustBeDisplayable );
        testFindPredicateOfJComponent( 
            successWindows, 
            failWindows, 
            finder
        );
    }

    private void testFindWindowPredicateOfWindow( 
        boolean canBeDialog, 
        boolean canBeFrame, 
        boolean mustBeVisible,
        boolean mustBeDisplayable
    )
    {
        List<TestWindow>    successWindows  = 
            getExpSuccessWindows( 
                canBeDialog, 
                canBeFrame, 
                mustBeVisible, 
                mustBeDisplayable
            );
        List<TestWindow>    failWindows     = 
            getExpFailWindows( 
                canBeDialog, 
                canBeFrame, 
                mustBeVisible, 
                mustBeDisplayable
            );        
        ComponentFinder finder  = 
            new ComponentFinder( canBeDialog, canBeFrame, mustBeVisible );
        finder.setMustBeDisplayable( mustBeDisplayable );
        testFindWindowPredicateOfWindow( 
            successWindows, 
            failWindows, 
            finder
        );
    }

    private List<TestWindow> getExpSuccessWindows( 
        boolean canBeDialog, 
        boolean canBeFrame, 
        boolean mustBeVisible,
        boolean mustBeDisplayable
    )
    {
        List<TestWindow>    list    = new ArrayList<>();
        if ( canBeDialog )
        {
            list.add( visibleDialog );
            if ( !mustBeVisible )
                list.add( notVisibleDialog );
            if ( !mustBeDisplayable )
                list.add( disposedDialog );
        }
        if ( canBeFrame )
        {
            list.add( visibleFrame );
            if ( !mustBeVisible )
                list.add( notVisibleFrame );
            if ( !mustBeDisplayable )
                list.add( disposedFrame );
        }
        return list;
    }

    private List<TestWindow> getExpFailWindows( 
        boolean canBeDialog, 
        boolean canBeFrame, 
        boolean mustBeVisible,
        boolean mustBeDisplayable
    )
    {
        List<TestWindow>    list    = new ArrayList<>();
        if ( !canBeDialog )
        {
            list.add( visibleDialog );
            list.add( notVisibleDialog );
            list.add( disposedDialog );
        }
        else
        {
            if ( mustBeVisible )
                list.add( notVisibleDialog );
            if ( mustBeDisplayable )
                list.add( disposedDialog );
        }

        if ( !canBeFrame )
        {
            list.add( visibleFrame );
            list.add( notVisibleFrame );
            list.add( disposedFrame );
        }
        else
        {
            if ( mustBeVisible )
                list.add( notVisibleFrame );
            if ( mustBeDisplayable )
                list.add( disposedDialog );
        }
        return list;
    }
    
    @Test
    public void testFindWindowPredicate()
    {
        TestWindow      testWindowOne   = visibleDialog;
        List<String>    labelsOne       = getLabels( testWindowOne );
        Window          windowOne       = testWindowOne.getWindow();
        JComponent      pane1           = testWindowOne.getContentPane();

        TestWindow      testWindow2     = notVisibleFrame;
        List<String>    labelsTwo       = getLabels( testWindow2 );
        
        for ( String testLabel : labelsOne )
        {
            Predicate<JComponent>   pred    = 
                ComponentFinder.getButtonPredicate( testLabel );
            
            JComponent              comp1   = 
                ComponentFinder.find( windowOne, pred );
            assertNotNull( comp1 );
            assertTrue( comp1 instanceof JButton );
            assertEquals( testLabel, ((JButton)comp1).getText() );
        }
        
        for ( String testLabel : labelsTwo )
        {
            Predicate<JComponent>   pred    = 
                ComponentFinder.getButtonPredicate( testLabel );
            JComponent              comp1   =
                ComponentFinder.find( pane1, pred );
            assertNull( comp1 );
        }
    }
    
    @Test
    public void testFindJComponentPredicate()
    {
        TestWindow      testWindowOne   = visibleDialog;
        List<String>    labelsOne       = getLabels( testWindowOne );
        JComponent      pane1           = testWindowOne.getContentPane();

        TestWindow      testWindow2     = notVisibleFrame;
        List<String>    labelsTwo       = getLabels( testWindow2 );
        
        for ( String testLabel : labelsOne )
        {
            Predicate<JComponent>   pred    = 
                ComponentFinder.getButtonPredicate( testLabel );
            JComponent              comp1   =
                ComponentFinder.find( pane1, pred );
            assertNotNull( comp1 );
            assertTrue( comp1 instanceof JButton );
            assertEquals( testLabel, ((JButton)comp1).getText() );
        }
        
        for ( String testLabel : labelsTwo )
        {
            Predicate<JComponent>   pred    = 
                ComponentFinder.getButtonPredicate( testLabel );
            JComponent              comp1   =
                ComponentFinder.find( pane1, pred );
            assertNull( comp1 );
        }
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

    @Test
    public void testGetWindowPredicate()
    {
        String  dialogTitle         = "Dialog Title";
        String  notDialogTitle      = "Not Dialog Title";
        JDialog dialogSuccess       = new JDialog( (Window)null, dialogTitle );
        JDialog dialogFail          = new JDialog( (Window)null, notDialogTitle );
        String  frameTitle          = "Frame Title";
        String  notFrameTitle       = "Not Frame Title";
        JFrame  frameSuccess        = new JFrame( frameTitle );
        JFrame  frameFail           = new JFrame( notFrameTitle );
        Window  notDialogOrFrame    = new Window( (Window)null );
        
        Predicate<Window>   dialogPred      = 
            ComponentFinder.getWindowPredicate( dialogTitle );
        Predicate<Window>   framePred       = 
            ComponentFinder.getWindowPredicate( frameTitle );
        
        assertFalse( dialogPred.test( dialogFail ) );
        assertFalse( dialogPred.test( frameSuccess ) );
        assertFalse( dialogPred.test( frameFail ) );
        assertFalse( dialogPred.test( notDialogOrFrame ) );
        assertTrue( dialogPred.test( dialogSuccess ) );
        
        assertFalse( framePred.test( frameFail ) );
        assertFalse( framePred.test( dialogSuccess ) );
        assertFalse( framePred.test( dialogFail ) );
        assertFalse( framePred.test( notDialogOrFrame ) );
        assertTrue( framePred.test( frameSuccess ) );
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
    
    private static List<String> getLabels( TestWindow... windows )
    {
        List<String>    list    =
            Arrays.stream( windows )
                .flatMap( w -> w.getLabels().stream() )
                .collect( Collectors.toList() );
        return list;
    }

    private interface TestWindow
    {
        String  getTitle();
        List<String> getLabels();
        JPanel getContentPane();
        Window getWindow();
    }
    
    private void testFindPredicateOfJComponent( 
        List<TestWindow>  expSuccessWindows,
        List<TestWindow>  expFailWindows,
        ComponentFinder   finder
    )
    {
        TestWindow[]    successArray    =
            expSuccessWindows.toArray( new TestWindow[0] );
        TestWindow[]    failArray       =
            expFailWindows.toArray( new TestWindow[0] );
        List<String>    successLabels   = getLabels( successArray );
        List<String>    failLabels      = getLabels( failArray );
        
        for ( String str : successLabels )
        {
            Predicate<JComponent>   pred    =
                ComponentFinder.getButtonPredicate( str );
            JComponent              jComp   = finder.find( pred );
            assertNotNull( jComp, str );
            assertTrue( jComp instanceof JButton, str );
            assertEquals( str, ((JButton)jComp).getText() );
        }
        
        for ( String str : failLabels )
        {
            Predicate<JComponent>   pred    = 
                ComponentFinder.getButtonPredicate( str );
            JComponent              jComp   = finder.find( pred );
            assertNull( jComp, str );
        }
    }
    
    private void testFindWindowPredicateOfWindow( 
        List<TestWindow>  expSuccessWindows,
        List<TestWindow>  expFailWindows,
        ComponentFinder   finder
    )
    {        
        for ( TestWindow testWindow : expSuccessWindows )
        {
            String              expTitle    = testWindow.getTitle();
            Predicate<Window>   pred        =
                ComponentFinder.getWindowPredicate( expTitle );
            Window              window        = finder.findWindow( pred );
            assertNotNull( window, expTitle );
            assertEquals( testWindow.getWindow(), window );
        }
        
        for ( TestWindow window : expFailWindows )
        {
            String              expTitle    = window.getTitle();
            Predicate<Window>   pred        =
                ComponentFinder.getWindowPredicate( expTitle );
            Window              wind        = finder.findWindow( pred );
            assertNull( wind, expTitle );
        }
    }

    @SuppressWarnings("serial")
    public static class TestContentPane extends JPanel
    {
        private final JPanel            panel1;
        private final JPanel            panel2;
        private final JButton           okButton;
        private final JButton           cancelButton;
        private final JButton           exitButton;
        private final JButton           abortButton;
        
        public TestContentPane( String prefix )
        {
            super( new GridLayout( 2, 1 ) );
            
            panel1 = new JPanel( new GridLayout( 1, 2 ) );
            this.add( panel1 );
            okButton = new JButton( prefix + okLabel );
            cancelButton = new JButton( prefix + cancelLabel );
            panel1.add( okButton );
            panel1.add( cancelButton );
            
            panel2 = new JPanel( new GridLayout( 1, 2 ) );
            this.add( panel2 );
            exitButton = new JButton( prefix + exitLabel );
            abortButton = new JButton( prefix + abortLabel);
            panel2.add( exitButton );
            panel2.add( abortButton );
        }
        
        public List<String> getLabels()
        {
            List<String>    list    = List.of( 
                okButton.getText(),
                cancelButton.getText(),
                exitButton.getText(),
                abortButton.getText()
            );
            return list;
        }
    }

    private static class TestDialog implements TestWindow
    {
        private final JDialog           dialog;
        private final TestContentPane   contentPane;
        private final String            title;
        
        public TestDialog( String prefix )
        {
            String  thisID  = prefix + dialogID;
            title = thisID + titleLabel;
            dialog = new JDialog( (Window)null, title );
            dialog.setModal( false );
            contentPane = new TestContentPane( thisID );
            dialog.setContentPane( contentPane );
            
            dialog.pack();
        }
    
        public void show()
        {
            dialog.setVisible( true );
        }
        
        public void dispose()
        {
            dialog.dispose();
        }
        
        @Override
        public JPanel getContentPane()
        {
            return contentPane;
        }
        
        @Override
        public String getTitle()
        {
            return title;
        }
        
        @Override
        public Window getWindow()
        {
            return dialog;
        }
        
        @Override
        public List<String> getLabels()
        {
            return contentPane.getLabels();
        }
    }
    
    private static class TestFrame implements TestWindow
    {
        private final JFrame            frame;
        private final TestContentPane   contentPane;
        private final String            title;
        
        public TestFrame( String prefix )
        {
            String  thisID  = prefix + frameID;
            title = thisID + titleLabel;
            frame = new JFrame( title );
            contentPane = new TestContentPane( thisID );
            frame.setContentPane( contentPane );
        }
        
        public void start( boolean makeVisible )
        {
            try
            {
                SwingUtilities.invokeAndWait( () -> {
                    frame.pack();
                });
                frame.setVisible( makeVisible );
            }
            catch ( InterruptedException | InvocationTargetException exc )
            {
                exc.printStackTrace();
                System.exit( 1 );
            }
        }

        @Override
        public JPanel getContentPane()
        {
            return contentPane;
        }
        
        public void dispose()
        {
            frame.dispose();
        }
        
        @Override
        public String getTitle()
        {
            return title;
        }
        
        @Override
        public Window getWindow()
        {
            return frame;
        }
        
        @Override
        public List<String> getLabels()
        {
            return contentPane.getLabels();
        }
    }
}
