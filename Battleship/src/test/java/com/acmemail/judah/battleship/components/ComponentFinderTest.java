package com.acmemail.judah.battleship.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Container;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ComponentFinderTest
{
    private static final Map<String,JComponent> jcMap   = new HashMap<>();
    private static final List<JDialog>          dialogs = new ArrayList<>();
    private static final List<JFrame>           frames  = new ArrayList<>();
    private static Window                       notAJWindow;
    private static JFrame                       disposedFrame;
    private static JDialog                      disposedDialog;
    private static JFrame                       namelessFrame;
    private static JDialog                      namelessDialog;
    private static int                          winNum  = 0 ;
    
    @BeforeAll
    public static void beforeAll()
    {
        ComponentUtils.invokeAndWait( () -> {
            Stream.of( "A", "B", "C" ).forEach( ComponentFinderTest::buildDialog );
            Stream.of( "X", "Y", "Z" ).forEach( ComponentFinderTest::buildFrame );
            disposedDialog = buildDialog( "disposed dialog" );
            disposedDialog.dispose();
            disposedFrame = buildFrame( "disposed frame" );
            disposedFrame.dispose();
            notAJWindow = new Window( null );
            namelessFrame = buildNamelessFrame();
            namelessDialog = buildNamelessDialog();
        });
    }
    
    @AfterAll
    public static void afterAll()
    {
        ComponentFinder.disposeAll();
        Arrays.stream( Window.getWindows() )
            .forEach( w -> assertFalse( w.isDisplayable() ) );
    }
    
    @Test
    public void testGetFramePredicateOfJFrameFalse()
    {
        String  notFound    = "nobody home";
        for ( JFrame frame : frames )
        {
            String              name        = frame.getName();
            Predicate<JFrame>   hasName     = ComponentFinder.jfHasName( name );
            String              title       = frame.getTitle();
            Predicate<JFrame>   hasTitle    = ComponentFinder.jfHasTitle( title );
            
            JFrame              actFrame = 
                ComponentFinder.getFrame( hasName, false );
            assertSame( frame, actFrame, "name=" + name );
            
            actFrame = ComponentFinder.getFrame( hasTitle, false );
            assertSame( frame, actFrame, title );
        }
        Predicate<JFrame>   hasName = ComponentFinder.jfHasName( notFound );
        Predicate<JFrame>   hasTitle = ComponentFinder.jfHasTitle( notFound );
        JFrame  actFrame    = ComponentFinder.getFrame( hasName, false );
        assertNull( actFrame );
        actFrame = ComponentFinder.getFrame( hasTitle, false );
        assertNull( actFrame );
    }
    
    @Test
    public void testGetFramePredicateOfJFrameTrue()
    {
        for ( JFrame frame : frames )
        {
            String              name    = frame.getName();
            Predicate<JFrame>   hasName = 
                ComponentFinder.jfHasName( name );
            
            JFrame              actFrame = 
                ComponentFinder.getFrame( hasName, true );
            if ( frame.isDisplayable() )
                assertEquals( frame, actFrame, name );
            else
                assertNull( actFrame );
        }
        
        Stream.of( "nobody home", disposedFrame.getName() )
            .forEach( s -> {
                Predicate<JFrame>   hasName     = 
                    ComponentFinder.jfHasName( s );
                JFrame              actFrame    = 
                    ComponentFinder.getFrame( hasName, true );
                assertNull( actFrame );
            });
    }

    @Test
    public void testGetFrameStringBoolean()
    {
        for ( JFrame frame : frames )
        {
            String              title    = frame.getTitle();
            
            JFrame              actFrame = 
                ComponentFinder.getFrame( title, true );
            if ( frame.isDisplayable() )
                assertSame( frame, actFrame, title );
            else
                assertNull( actFrame );
        }
        
        Stream.of( "nobody home", disposedFrame.getTitle() )
            .forEach( s -> {
                JFrame              actFrame    =
                    ComponentFinder.getFrame( s, true );
                assertNull( actFrame );
            });
    }

    @Test
    public void testGetFrameString()
    {
        for ( JFrame frame : frames )
        {
            String              title       = frame.getTitle();
            JFrame              actFrame    = ComponentFinder.getFrame( title );
            if ( frame.isDisplayable() )
                assertEquals( frame, actFrame );
            else
                assertNull( actFrame );
        }
        
        Stream.of( "nobody home", disposedFrame.getTitle() )
            .forEach( s -> {
                JFrame  actFrame    = ComponentFinder.getFrame( s, true );
                assertNull( actFrame );
            });
    }

    @Test
    public void testGetDialogStringBoolean()
    {
        for ( JDialog dialog : dialogs )
        {
            String  title    = dialog.getTitle();
            
            JDialog actDialog = 
                ComponentFinder.getDialog( title, true );
            if ( dialog.isDisplayable() )
                assertSame( dialog, actDialog, title );
            else
                assertNull( actDialog );
        }
        
        Stream.of( "nobody home", disposedDialog.getTitle() )
            .forEach( s -> {
                JDialog actDialog   =
                    ComponentFinder.getDialog( s, true );
                assertNull( actDialog );
            });
    }

    @Test
    public void testGetDialogPredicateOfJDialogFalse()
    {
        String  notFound    = "nobody home";
        for ( JDialog dialog : dialogs )
        {
            String              name            = dialog.getName();
            Predicate<JDialog>  hasName     = ComponentFinder.jdHasName( name );
            String              title       = dialog.getTitle();
            Predicate<JDialog>  hasTitle    = 
                ComponentFinder.jdHasTitle( title );
            
            JDialog             actDialog = 
                ComponentFinder.getDialog( hasName, false );
            assertSame( dialog, actDialog, "name=" + name );
            
            actDialog = ComponentFinder.getDialog( hasTitle, false );
            assertSame( dialog, actDialog, title );
        }
        Predicate<JDialog>  hasName     = ComponentFinder.jdHasName( notFound );
        Predicate<JDialog>  hasTitle    = ComponentFinder.jdHasTitle( notFound );
        JDialog             actdialog   = 
            ComponentFinder.getDialog( hasName, false );
        assertNull( actdialog );
        actdialog = ComponentFinder.getDialog( hasTitle, false );
        assertNull( actdialog );
    }
    
    @Test
    public void testGetDialogPredicateOfJDialogTrue()
    {
        for ( JDialog dialog : dialogs )
        {
            String              name        = dialog.getName();
            Predicate<JDialog>  hasName     = 
                ComponentFinder.jdHasName( name );
            
            JDialog             actDialog   = 
                ComponentFinder.getDialog( hasName, true );
            if ( dialog.isDisplayable() )
                assertEquals( dialog, actDialog );
            else 
                assertNull( actDialog );
        }
        
        Stream.of( "nobody home", disposedDialog.getName() )
            .forEach( s -> {
                Predicate<JDialog>  hasName     = 
                    ComponentFinder.jdHasName( s );
                JDialog             actDialog   = 
                    ComponentFinder.getDialog( hasName, true );
                assertNull( actDialog );
            });
    }

    @ParameterizedTest
    @MethodSource( "testAllWindows" )
    public void testGetDialogString( Window window )
    {
        if ( window instanceof JDialog dialog )
        {
            System.out.println( dialog.getName() );
            JDialog result  = ComponentFinder.getDialog( dialog.getTitle() );
            assertSame( dialog, result );
        }
        JDialog result  = ComponentFinder.getDialog( disposedDialog.getTitle() );
        assertNull( result );
    }    

    @ParameterizedTest
    @MethodSource( "testAllWindows" )
    public void testGetTopLevelWindow( Window window )
    {
        Window  found   = ComponentFinder.getTopLevelWindow( w -> w == window );
        assertSame( found, window );
        found   = ComponentFinder.getTopLevelWindow( w -> false );
        assertNull( found );
    }

    @ParameterizedTest
    @MethodSource( "testAllWindows" )
    public void testGetComponentWindowPredicateOfJComponent( Window window )
    {
        List<JComponent>    allComps    = null;
        if ( window instanceof JFrame frame )
            allComps = getAllDescendants( frame );
        else if ( window instanceof JDialog dialog )
            allComps = getAllDescendants( dialog );
        else
        {
            String  className = window.getClass().getSimpleName();
            fail( "unexpected window type: " + className );
        }
        
        allComps.stream()
            .forEach( jc -> {
                JComponent  comp    = 
                    ComponentFinder.getComponent( window, c -> c == jc );
                assertSame( jc, comp, jc.getName() );
            });
        JComponent  comp    = 
            ComponentFinder.getComponent( window, c -> false );
        assertNull( comp );
    }

    @ParameterizedTest
    @MethodSource( "testAllWindows" )
    public void testGetComponentWindowString( Window window )
    {
        List<JComponent>    allComps    = null;
        if ( window instanceof JFrame frame )
            allComps = getAllDescendants( frame );
        else if ( window instanceof JDialog dialog )
            allComps = getAllDescendants( dialog );
        else
        {
            String  className = window.getClass().getSimpleName();
            fail( "unexpected window type: " + className );
        }
        
        allComps.stream()
            .forEach( jc -> {
                String      expName = jc.getName();
                JComponent  comp    = 
                    ComponentFinder.getComponent( window, expName );
                String      actName = comp == null ? null : comp.getName();
                assertEquals( expName, actName );
            });
    }

    @ParameterizedTest
    @MethodSource( "testAllJPanels" )
    public void testGetComponentContainerPredicateOfJComponent( JPanel panel )
    {
        List<JComponent>    allComps    = getAllDescendants( panel );
        allComps.stream()
            .forEach( jc -> {
                Predicate<JComponent>   pred    = c -> c == jc;
                JComponent  comp    = 
                    ComponentFinder.getComponent( panel, pred );
                assertSame( jc, comp );
            });

        JComponent  notComp = ComponentFinder.getComponent( panel, c -> false );
        assertNull( notComp );
    }
    
    @Test
    public void testNotASwingWindow()
    {
        Window  result  = 
            ComponentFinder.getTopLevelWindow( w -> w == notAJWindow );
        assertNull( result );
    }
    
    @Test
    public void testPotentialNPEs()
    {
        String                  testString  = "nobody home";
        
        Predicate<JComponent>   jcNamePred  = 
            ComponentFinder.jcHasName( testString );
        Container   namelessContainer   = namelessDialog.getContentPane();
        Object      result              = 
            ComponentFinder.getComponent( namelessContainer, jcNamePred );
        assertNull( result );
        result = ComponentFinder.getComponent( namelessDialog, jcNamePred );
        assertNull( result );
        result = ComponentFinder.getComponent( namelessFrame, jcNamePred );
        assertNull( result );
    }
    
    @Test
    public void testNPEThrowers()
    {
        Predicate<JFrame>           jfPred          = null;
        Predicate<JDialog>          jdPred          = null;
        Predicate<JComponent>       jcPred          = null;
        String                      nullStr         = null;
        Container                   nullContainer   = null;
        Class<NullPointerException> npe = NullPointerException.class;
        
        assertThrows( npe, () -> ComponentFinder.getTopLevelWindow( null ) );
        
        assertThrows( npe, () -> ComponentFinder.getFrame( jfPred, true) );
        assertThrows( npe, () -> ComponentFinder.getFrame( nullStr, true) );
        assertThrows( npe, () -> ComponentFinder.getFrame( nullStr ) );
        
        assertThrows( npe, () -> ComponentFinder.getDialog( jdPred, true) );
        assertThrows( npe, () -> ComponentFinder.getDialog( nullStr, true) );
        assertThrows( npe, () -> ComponentFinder.getDialog( nullStr ) );
        
        assertThrows( npe, () -> ComponentFinder.getComponent( null, "arg" ) );
        assertThrows( npe, () -> 
            ComponentFinder.getComponent( namelessDialog, nullStr ) 
        );
        assertThrows( npe, () -> 
            ComponentFinder.getComponent( nullContainer, c -> false ) 
        );
        assertThrows( npe, () -> 
            ComponentFinder.getComponent( new JPanel(), jcPred ) 
        );
    }

    /**
     * Instantiates a JFrame with a unique name
     * beginning with the given prefix.
     * Populates the JFrame with several JPanels.
     * 
     * @param prefix    the given prefix
     * 
     * @return  the instantiated JFrame
     * 
     * @see #buildPanel(String, int)
     */
    private static JFrame buildFrame( String prefix )
    {
        int     seqNum      = 0;
        JFrame  frame       = getFrame( prefix, winNum++ );
        JPanel  contentPane = buildPanel( prefix, seqNum++ );
        
        JPanel  panel1      = buildPanel( prefix, seqNum++ );
        JPanel  panel2      = buildPanel( prefix, seqNum++ );
        JPanel  panel21     = buildPanel( prefix, seqNum++ );
        
        panel2.add( panel21 );
        contentPane.add( panel1 );
        contentPane.add( panel2 );
        frame.setContentPane( contentPane );
        
        frame.pack();
        return frame;
    }
    
    /**
     * Construct a JFrame with no name and no title,
     * and whose constituent components have no name.
     * 
     * @return the constructed frame;
     */
    private static JFrame buildNamelessFrame()
    {
        JFrame  frame   = new JFrame();
        frame.setTitle( null );
        frame.setName( null );
        
        JPanel  contentPane = buildNamelessPanel();
        contentPane.add( buildNamelessPanel() );
        frame.setContentPane( contentPane );
        
        frame.pack();
        return frame;
    }
    
    /**
     * Construct a JDialog with no name and no title,
     * and whose constituent components have no name.
     * 
     * @return the constructed dialog;
     */
    private static JDialog buildNamelessDialog()
    {
        JDialog dialog  = new JDialog();
        dialog.setTitle( null );
        dialog.setName( null );
        
        JPanel  contentPane = buildNamelessPanel();
        contentPane.add( buildNamelessPanel() );
        dialog.setContentPane( contentPane );
        
        dialog.pack();
        return dialog;
    }
    
    private static JPanel buildNamelessPanel()
    {
        JPanel  panel   = new JPanel();
        panel.setName( null );
        IntStream.range( 0, 2 ).forEach( n -> {
            JLabel  label   = new JLabel();
            label.setName( null );
            panel.add( label );
        });
        return panel;
    }

    /**
     * Instantiates a JDialog with a unique name
     * beginning with the given prefix.
     * Populates the JDialog with several JPanels.
     * 
     * @param prefix    the given prefix
     * 
     * @return  the instantiated JDialog
     * 
     * @see #buildPanel(String, int)
     */
    private static JDialog buildDialog( String prefix )
    {
        int     seqNum      = 0;
        JDialog dialog      = getDialog( prefix, winNum++ );
        JPanel  contentPane = buildPanel( prefix, seqNum++ );
        
        JPanel  panel1      = buildPanel( prefix, seqNum++ );
        JPanel  panel2      = buildPanel( prefix, seqNum++ );
        JPanel  panel21     = buildPanel( prefix, seqNum++ );
        
        panel2.add( panel21 );
        contentPane.add( panel1 );
        contentPane.add( panel2 );
        dialog.setContentPane( contentPane );
        
        dialog.pack();
        return dialog;
    }
    
    /**
     * Instantiates a JFrame with a unique name and title
     * tied to the given prefix and sequence number.
     * Adds the new frame to the list of frames.
     * 
     * @param prefix    the given prefix
     * @param num       the given sequence number
     * 
     * @return  a JFrame with a unique name and title
     */
    private static JFrame getFrame( String prefix, int num )
    {
        String  name    = getName( prefix, "jframe", num );
        String  title   = getLabelStr( prefix, "jframe", num );
        JFrame  jFrame  = new JFrame();
        jFrame.setName( name );
        jFrame.setTitle( title );
        frames.add( jFrame );
        return jFrame;
    }
    
    /**
     * Instantiates a JDialog with a unique name and title
     * tied to the given prefix and sequence number.
     * Adds the new dialog to the list of dialogs.
     * 
     * @param prefix    the given prefix
     * @param num       the given sequence number
     * 
     * @return  a JDialog with a unique name and title
     */
    private static JDialog getDialog( String prefix, int num )
    {
        String  name    = getName( prefix, "jdialog", num );
        String  title   = getLabelStr( prefix, "jdialog", num );
        JDialog jDialog = new JDialog();
        jDialog.setName( name );
        jDialog.setTitle( title );
        dialogs.add( jDialog );
        return jDialog;
    }
    
    /**
     * Creates a JPanel with a unique name
     * tied to the given prefix and number,
     * and populates it with several JComponents
     * with unique names.
     * 
     * @param prefix    the given prefix
     * @param num       the given sequence number
     * 
     * @return a populated JPanel with a unique name
     */
    private static JPanel buildPanel( String prefix, int num )
    {
        int     seqNum      = 0;
        JPanel  panel       = getPanel( prefix, num );
        
        String  panelName   = "[" + panel.getName() + "]";
        for ( int inx = 0 ; inx < 3 ; ++inx )
        {
            JComponent  jButton = getButton( panelName, seqNum++ );
            JComponent  jLabel = getLabel( panelName, seqNum++ );
            panel.add( jButton );
            panel.add( jLabel );
        }
        
        return panel;
    }
    
    /**
     * Instantiates a JPanel with a unique name
     * tied to the given prefix and sequence number.
     * Adds the new panel to the JComponent map.
     * 
     * @param prefix    the given prefix
     * @param num       the given sequence number
     * 
     * @return  a populated JPanel with a unique name
     */
    private static JPanel getPanel( String prefix, int num )
    {
        JPanel  panel   = new JPanel();
        String  name    = getName( prefix, "jpanel", num );
        panel.setName( name );
        put( panel );
        return panel;
    }
    
    /**
     * Instantiates a JButton with a unique name
     * tied to the given prefix and sequence number.
     * Adds the new component to the JComponent map.
     * 
     * @param prefix    the given prefix
     * @param num       the given sequence number
     * 
     * @return  a JButton with a unique name
     */
    private static JComponent getButton( String prefix, int num )
    {
        String  name    = getName( prefix, "jbutton", num );
        String  label   = getLabelStr( prefix, "jbutton", num );
        JButton jButton = new JButton( label );
        jButton.setName( name );
        put( jButton );
        return jButton;
    }
    
    /**
     * Instantiates a JLabel with a unique name
     * tied to the given prefix and sequence number.
     * Adds the new component to the JComponent map.
     * 
     * @param prefix    the given prefix
     * @param num       the given sequence number
     * 
     * @return  a JLabel with a unique name
     */
    private static JComponent getLabel( String prefix, int num )
    {
        String  name    = getName( prefix, "jlabel", num );
        String  label   = getLabelStr( prefix, "jlabel", num );
        JLabel  jLabel  = new JLabel( label );
        jLabel.setName( name );
        put( jLabel );
        return jLabel;
    }
    
    /**
     * Adds a given JComponent to the JComponent map
     * using its name as the key.
     * Verifies that the name is unique and noon-null.
     * 
     * @param comp  the given JComponent
     */
    private static void put( JComponent comp )
    {
        String  name    = comp.getName();
        assertNotNull( name );
        assertFalse( name.isBlank() );
        assertFalse( jcMap.containsKey( name ), name );
        jcMap.put( name, comp );
    }
    
    /**
     * Gets a formatted name consisting of 
     * a given prefix, a given type, and a given sequence number.
     * 
     * @param prefix    the given prefix
     * @param type      the given type
     * @param num       the given sequence number
     * 
     * @return  a formatted name consisting of the given parts
     */
    private static String getName( String prefix, String type, int num )
    {
        final String fmt    = "%s+%s+%03d+Name";
        String  name    = String.format( fmt, prefix, type, num );
        return name;
    }
    
    
    /**
     * Gets a formatted label consisting of 
     * a given prefix, a given type, and a given sequence number.
     * 
     * @param prefix    the given prefix
     * @param type      the given type
     * @param num       the given sequence number
     * 
     * @return  a formatted label consisting of the given parts
     */
    private static String getLabelStr( String prefix, String type, int num )
    {
        final String fmt    = "%s+%s+%03d+Label";
        String  label   = String.format( fmt, prefix, type, num );
        return label;
    }
    
    /**
     * Compiles a list of all JComponents
     * in the hierarchy of a given JFrame.
     * 
     * @param parent    the given JFrame
     * 
     * @return  a list of all JComponents in the hierarchy of the given JFrame
     */
    private static List<JComponent> getAllDescendants( JFrame parent )
    {
        List<JComponent>    allDescendants  = new ArrayList<>();
        if ( parent.getContentPane() instanceof JComponent pane )
        {
            allDescendants.add( pane );
            allDescendants.addAll( getAllDescendants( pane  ));
        }
        return allDescendants;
    }
    
    /**
     * Compiles a list of all JComponents
     * in the hierarchy of a given JDialog.
     * 
     * @param parent    the given JDialog
     * 
     * @return  a list of all JComponents in the hierarchy of the given JDialog
     */
    private static List<JComponent> getAllDescendants( JDialog parent )
    {
        List<JComponent>    allDescendants  = new ArrayList<>();
        if ( parent.getContentPane() instanceof JComponent pane )
        {
            allDescendants.add( pane );
            allDescendants.addAll( getAllDescendants( pane  ));
        }
        return allDescendants;
    }
    
    /**
     * Compiles a list of all JComponents
     * in the hierarchy of a given JComponent.
     * 
     * @param parent    the given JComponent
     * 
     * @return  
     *      a list of all JComponents in the hierarchy of the given JComponent
     */
    private static List<JComponent> getAllDescendants( JComponent parent )
    {
        List<JComponent>    allDescendants  = new ArrayList<>();
        Arrays.stream( parent.getComponents() )
            .filter( c -> c instanceof JComponent )
            .map( c -> (JComponent)c )
            .forEach( jc -> {
                allDescendants.add( jc );
                allDescendants.addAll( getAllDescendants( jc ) );
            });
        return allDescendants;
    }
    
    /**
     * Gets a stream of all JPanels in the test configuration.
     * 
     * @return  stream of all JPanels in the test configuration
     */
    private static Stream<JPanel> testAllJPanels()
    {
        Collection<JComponent> values  = jcMap.values();
        Stream<JPanel>  stream  = values.stream()
            .filter( jc -> jc instanceof JPanel )
            .map( jc -> (JPanel)jc );
        return stream;
    }
    
    /**
     * Gets a stream of all top-level windows in the test configuration.
     * 
     * @return  stream of all top-level windows in the test configuration
     */
    private static Stream<Window> testAllWindows()
    {
        Stream<JDialog> allDialogs  =
            dialogs.stream()
            .filter( JDialog::isDisplayable );
        Stream<JFrame>  allFrames   =
            frames.stream()
            .filter( JFrame::isDisplayable );
        Stream<Window>  allWindows  = Stream.concat( allDialogs, allFrames );
        return allWindows;
    }
}
