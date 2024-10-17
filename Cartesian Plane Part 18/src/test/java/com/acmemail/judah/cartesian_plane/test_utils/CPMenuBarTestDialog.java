package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Window;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.swing.AbstractButton;
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

/**
 * This class encapsulates a dialog
 * that contains a CPMenuBar.
 * It's purpose is to facilitate testing
 * of the CPMenuBar class.
 * It simplifies access to components
 * of the CPMenuBar,
 * and to the dialogs that the CPMenuBar posts.
 * Operations on components
 * are always executed on the Event Dispatch Thread,
 * relieving the test class of this responsibility.
 * 
 * @author Jack Straub
 */
public class CPMenuBarTestDialog
{
    /** Singleton for this class. */
    private static CPMenuBarTestDialog testDialog  = null;
    
    /** The encapsulated dialog. */
    private final JDialog       dialog;
    /** The encapsulated menu bar. */
    private final CPMenuBar     menuBar;
    
    /** The encapsulated line property dialog. */
    private final JDialog       lineDialog;
    /** The encapsulated graph property dialog. */
    private final JDialog       graphDialog;
    /** The encapsulated Profile editor dialog. */
    private final JDialog       profileEditorDialog;
    /** The encapsulated About dialog. */
    private final JDialog       aboutDialog;
    
    /**************************************************
     * Ad hoc fields are for the convenience of methods
     * utilizing lambdas, where modification of a local
     * variable is forbidden.
     * 
     * The value of an ad hoc variable is unpredictable
     * outside of the narrow context within which it is
     * used in a method.
     **************************************************/
    /** 
     * Instance variable to be used as needed when a JMenu object
     * is assigned in a lambda.
     */
    private JMenu       adHocJMenu1;
    /** 
     * Instance variable to be used as needed when a JMenuItem object
     * is assigned in a lambda.
     */
    private JMenuItem   adHocJMenuItem1;
    /** 
     * Instance variable to be used as needed when a Boolean
     * variable is assigned in a lambda.
     */
    private boolean     adHocBoolean1;
    
    /**
     * Constructor.
     * Configures and makes visible a dialog
     * containing a CPMenuBar.
     * 
     * @see #CPMenuBarTestDialog()
     */
    private CPMenuBarTestDialog()
    {
        menuBar = new CPMenuBar( null );
        dialog = makeDialog();
        
        graphDialog = getDialog( "Graph" );
        lineDialog = getDialog( "Line" );
        aboutDialog = getDialog( "About" );
        profileEditorDialog = getDialog( "Profile Editor" );
    }

    /**
     * Method to obtain the sole instance
     * of this class.
     * 
     * @return  the sole instance of this class
     */
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
    
    /**
     * Returns true if the encapsulated dialog
     * has focus.
     * The work performed by this method
     * is always executed on the EDT.
     * 
     * @return  true if the encapsulated dialog has focus.
     */
    public boolean hasFocus()
    {
        GUIUtils.schedEDTAndWait( 
            () -> adHocBoolean1 = dialog.hasFocus()
        );
        return adHocBoolean1;
    }
    
    /**
     * Obtains the JMenu object containing the given text.
     * This method may be called on any thread.
     * If necessary, the work performed by this method
     * is delegated to the EDT.
     * 
     * @param text  the given text
     * 
     * @return  the JMenu object containing the given text
     */
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
    
    /**
     * Gets the JMenuItem with the given menu hierarchy.
     * For example,
     * <em>tem("Help", "Math Help", "Math is Fun")</em>
     * will return the item with the text "Math is Fun"
     * from the "Math Help" submenu
     * of the "Help" menu.
     * The work performed by this method
     * is always executed on the EDT.
     * 
     * @param labels
     * @return
     */
    public JMenuItem getMenuItem( String... labels )
    {
        GUIUtils.schedEDTAndWait( 
            () -> adHocJMenuItem1 = getMenuItemEDT( labels )
        );
        return adHocJMenuItem1;
    }
    
    /**
     * Gets the dialog that contains the LinePropertiesPanel
     * which is spawned by the Window menu.
     * 
     * @return  the dialog that contains the LinePropertiesPanel
     */
    public JDialog getLineDialog()
    {
        return lineDialog;
    }
    
    /**
     * Gets the dialog that contains the GraphPropertiesPanel
     * which is spawned by the Window menu.
     * 
     * @return  the dialog that contains the GraphPropertiesPanel
     */
    public JDialog getGraphDialog()
    {
        return graphDialog;
    }
    
    /**
     * Gets the dialog that contains the GraphPropertiesPanel
     * which is spawned by the Window menu.
     * 
     * @return  the dialog that contains the GraphPropertiesPanel
     */
    public JDialog getProfileEditorDialog()
    {
        return profileEditorDialog;
    }
    
    /**
     * Gets the About dialog 
     * which is spawned by the Help menu.
     * 
     * @return  the dialog that contains the LinePropertiesPanel
     */
    public JDialog getAboutDialog()
    {
        return aboutDialog;
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
     * Spawns a thread that will click the given button.
     * The actual click operation will be delegated to the EDT.
     * This method will pause for a brief time
     * before returning the spawned Thread object
     * to the caller.
     * The method will pause for a brief time
     * before returning to the caller.
     * <p>
     * A typical use of this method
     * is to click a button
     * that posts a modal dialog.
     * When the test procedure is complete
     * the dialog can be closed,
     * and the thread will terminate:
     * <pre>
     *     Thread thread = tester.doClickInThread( aboutMenuItem );
     *     assertTrue( tester.isVisible( aboutDialog ) );
     *     tester.setVisible( aboutDialog, false );
     *     Utils.join( thread );
     *     assertFalse( tester.isVisible( aboutDialog ) );</pre>
     * 
     * @param button    the given button
     * 
     * @return  the Thread object to which the operation is delegated
     */
    public Thread doClickInThread( AbstractButton button )
    {
        Runnable    doClick = () -> button.doClick();
        Runnable    asEDT   = 
            () -> SwingUtilities.invokeLater( doClick );
        Thread      thread  = new Thread( asEDT );
        thread.start();
        Utils.pause( 500 );
        return thread;
    }
    
    /**
     * Determines whether the given component is visible.
     * This process is always delegated to the EDT.
     * 
     * @param comp  the given component
     * 
     * @return  true if the given component is visible
     */
    public boolean isVisible( Component comp )
    {
        GUIUtils.schedEDTAndWait( () -> adHocBoolean1 = comp.isVisible() );
        return adHocBoolean1;
    }
    
    /**
     * Sets the visibility of the given component.
     * This process is always delegated to the EDT.
     * 
     * @param comp  the given component
     * 
     * @param visible   true to make the component visible
     */
    public void setVisible( Component comp, boolean visible )
    {
        GUIUtils.schedEDTAndWait( () -> comp.setVisible( visible ) );
    }
    
    /**
     * Sets the enabled state of the given component.
     * 
     * @param comp      the given component
     * @param enabled   true to enable the given component
     */
    public void setEnabled( Component comp, boolean enabled )
    {
        GUIUtils.schedEDTAndWait( () -> comp.setEnabled( enabled ) );
    }
    
    /**
     * Gets the enabled state of the given component.
     * 
     * @param comp      the given component
     * 
     * @return true if the given component is enabled
     */
    public boolean getEnabled( Component comp )
    {
        GUIUtils.schedEDTAndWait( () -> 
            adHocBoolean1 = comp.isEnabled() );
        return adHocBoolean1;
    }
    
    /**
     * Finds the JMenu with the given text.
     * It is assumed that this method is invoked
     * from the EDT.
     * 
     * @param text  the given text
     * 
     * @return  the JMenu with the given text
     */
    private JMenu getMenuEDT( String text )
    {
        Predicate<JComponent>   isMenu  = c -> (c instanceof JMenu);
        Predicate<JComponent>   hasText = 
            c -> text.equals( ((JMenu)c).getText() );
        Predicate<JComponent>   pred    = isMenu.and( hasText );
        JComponent              comp    =
            ComponentFinder.find( menuBar, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JMenu );
        return (JMenu)comp;
    }
    
    /**
     * Finds the menu item with the given text
     * within the given menu hierarchy.
     * It is assumed that this method is invoked
     * from the EDT.
     * 
     * @param labels  the given menu hierarchy
     * 
     * @return  the menu item with the given menu hierarchy
     * 
     * @see #getMenuItem(String...)
     * @see #getMenuItem(JMenu, String)
     */
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
    
    /**
     * Finds the item within a given JMenu
     * with the given text.
     * It is assumed that this method
     * is invoked on the EDT.
     * 
     * @param menu  the given JMenu
     * @param text  the given text
     * 
     * @return  
     *      the item within the given JMenu with the given text
     */
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
    
    /**
     * Gets the dialog with the title
     * that contains the given text.
     * It is assumed that this method
     * is invoked on the EDT.
     * 
     * @param titleFragment the given text
     * 
     * @return
     *      the dialog with the title that contains the given text
     */
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
    
    /**
     * Creates and shows the JDialog
     * that will contain the CPMenuBar under test.
     * It is assumed that this method
     * is invoked on the EDT.
     * 
     * @return  the created dialog
     */
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
