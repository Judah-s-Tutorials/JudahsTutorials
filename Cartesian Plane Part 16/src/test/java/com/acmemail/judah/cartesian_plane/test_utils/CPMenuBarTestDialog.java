package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Window;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.components.CPMenuBar;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;

public class CPMenuBarTestDialog
{
    /** Singleton for this class. */
    private static CPMenuBarTestDialog testDialog  = null;
    
    /** The encapsulated dialog. */
    private final JDialog       dialog;
    /** The encapsulated menu bar. */
    private final CPMenuBar     menuBar;
    /** The encapsulated File menu. */
    private final JMenu         fileMenu;
    /** The encapsulated Window menu. */
    private final JMenu         windowMenu;
    /** The encapsulated Help menu. */
    private final JMenu         helpMenu;
    
    /** The encapsulated line property dialog. */
    private final JDialog       lineDialog;
    /** The encapsulated graph property dialog. */
    private final JDialog       graphDialog;
    /** The encapsulated About dialog. */
    private final JDialog       aboutDialog;
    
    /** 
     * Ad hoc field for the convenience of methods
     * utilizing lambdas, where modification
     * of a local variable
     * is forbidden.
     * The value of this variable
     * is unpredictable outside of
     * the narrow context within which
     * it is used in a method.
     */
    private Object      adHocObject1;
    private JMenu       adHocJMenu1;
    private JMenuItem   adHocJMenuItem1;
    
    private CPMenuBarTestDialog()
    {
        menuBar = new CPMenuBar( null );
        dialog = makeDialog();
        fileMenu = getMenu( "File" );
        windowMenu = getMenu( "Window" );
        helpMenu = getMenu( "Help" );
        
        graphDialog = getDialog( "Graph" );
        lineDialog = getDialog( "Line" );
        aboutDialog = getDialog( "About" );
    }
    
    public static CPMenuBarTestDialog getTestDialog()
    {
        if ( testDialog == null )
        {
            GUIUtils.schedEDTAndWait( 
                () -> testDialog = new CPMenuBarTestDialog()
            );
        }
        return testDialog;
    }
    
    public JMenu getMenu( String text )
    {
        if ( SwingUtilities.isEventDispatchThread() )
            adHocJMenu1 = getMenuEDT( text );
        else
            GUIUtils.schedEDTAndWait( 
                () -> adHocJMenu1 = getMenuEDT( text )
            );
        return adHocJMenu1;
    }
    
    public JMenu getMenuEDT( String text )
    {
        Predicate<JComponent>   isMenu  = c -> (c instanceof JMenu);
        Predicate<JComponent>   hasText = 
            c -> text.equals( ((JMenu)c).getText() );
        Predicate<JComponent>   pred    = isMenu.and( hasText );
        JComponent              comp    =
            ComponentFinder.find( dialog, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JMenu );
        return (JMenu)comp;
    }
    
    public JMenuItem getMenuItem( String... labels )
    {
        GUIUtils.schedEDTAndWait( 
            () -> adHocJMenuItem1 = getMenuItemEDT( labels )
        );
        return adHocJMenuItem1;
    }
    
    private JMenuItem getMenuItemEDT( String... labels )
    {
        JMenuItem   item    = getMenu( labels[0] );
        for ( int inx = 1 ; item != null && inx < labels.length ; ++inx  )
        {
            if ( !(item instanceof JMenu) )
                item = null;
            else
                item = getMenuItem( (JMenu)item, labels[inx] );
        }
        return item;
    }
    
    private JMenuItem getMenuItem( JMenu menu, String text )
    {
        JMenuItem   item    =
            Stream.of( menu.getMenuComponents() )
                .filter( c -> (c instanceof JMenuItem) )
                .map( c -> (JMenuItem)c )
                .filter( i -> text.equalsIgnoreCase( i.getText() ) )
                .findFirst()
                .orElse( null );
        return item;
    }
    
    private JDialog getDialog( String titleFragment )
    {
        ComponentFinder     finder      = 
            new ComponentFinder( true, false, false );
        Predicate<Window>   isDialog    = w -> (w instanceof JDialog);
        Predicate<Window>   hasText     = 
            w -> ((JDialog)w).getTitle().contains( titleFragment );
        Predicate<Window>   pred        = isDialog.and( hasText );
        Window              window      = finder.findWindow( pred );
        assertNotNull( window );
        assertTrue( window instanceof JDialog );
        return (JDialog)window;
    }
    
    private JDialog makeDialog()
    {
        String  title       = "Menu Bar Test Dialog";
        JDialog dialog      = new JDialog( (Window)null, title );
        JPanel  contentPane = new JPanel( new BorderLayout() );
        
        String  placeHolder = "CPMenuBar Test Dialog";
        JLabel  centerLabel = new JLabel( placeHolder );
        Font    origFont    = centerLabel.getFont();
        float   origSize    = origFont.getSize();
        float   newSize     = 3 * origSize;
        Font    newFont     = origFont.deriveFont( newSize );
        centerLabel.setFont( newFont );
        contentPane.add( centerLabel, BorderLayout.CENTER );
        contentPane.add( menuBar, BorderLayout.NORTH );
        
        dialog.setContentPane( contentPane );
        dialog.setLocation( 200, 200 );
        dialog.pack();
        dialog.setVisible( true );
        
        return dialog;
    }
}
