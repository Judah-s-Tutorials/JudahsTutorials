package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Window;
import java.io.File;
import java.text.ParseException;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.components.CPFrame;
import com.acmemail.judah.cartesian_plane.components.NamePanel;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;
import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.FileManager;

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
    
    /** The file chooser dialog. */
    private JDialog                 chooserDialog;
    /** Save button in the file chooser dialog. */
    private JButton                 chooserSaveButton;
    /** Open button in the file chooser dialog. */
    private JButton                 chooserOpenButton;
    /** Cancel button in the file chooser dialog. */
    private JButton                 chooserCancelButton;
    /** Text field in the file chooser dialog. */
    private JTextField              chooserTextField;
    
    /** The text field containing the equation name. */
    private final JFormattedTextField   eqNameField;

    /** The object that encapsulates the File menu on the menu bar. */
    private final JMenu             fileMenu;
    /** The menu item that launches a "new" operation. */
    private final JMenuItem         newItem;
    /** The menu item that launches an "open" operation. */
    private final JMenuItem         openItem;
    /** The menu item that launches a "save" operation. */
    private final JMenuItem         saveItem;
    /** The menu item that launches a "save as" operation. */
    private final JMenuItem         saveAsItem;
    /** The menu item that launches a "close" operation. */
    private final JMenuItem         closeItem;
    /** The menu item that launches a "delete" operation. */
    private final JMenuItem         deleteItem;
    
    /** This CPMenuBarDMTestUtils object. */
    private static CPMenuBarDMTestUtils utils;
    
    /** Variable for limited use in lambdas. */
    private String  adHocString1;
    
    /**
     * Returns this class's singleton,
     * creating it if necessary.
     * 
     * @return  this class's singleton
     */
    public static CPMenuBarDMTestUtils getUtils()
    {
        if ( utils == null )
        {
            if ( SwingUtilities.isEventDispatchThread() )
                utils = new CPMenuBarDMTestUtils();
            else
                GUIUtils.schedEDTAndWait( () -> 
                    utils = new CPMenuBarDMTestUtils() );
        }
        return utils;
    }
    
    /**
     * Constructor.
     * Creates a CPFrame object,
     * and mostly initializes this object.
     * Object initialization is completed
     * by the ComponentListener ChooserInit.
     * This constructor must be executed
     * in the context of the EDT.
     * 
     * @see ChooserInit
     * @see #chooserInit
     * @see #getUtils()
     */
    private CPMenuBarDMTestUtils()
    {
        cpFrame = new CPFrame();
        utils = this;
        
        fileMenu = getFileMenu();
        newItem = getMenuItem( "New" );
        openItem = getMenuItem( "Open" );
        saveItem = getMenuItem( "Save" );
        saveAsItem = getMenuItem( "Save As" );
        deleteItem = getMenuItem( "Delete" );
        closeItem = getMenuItem( "Close" );
        eqNameField = getEquationNameField();
    }
    
    /**
     * In the file chooser,
     * set the default directory 
     * for file operations.
     * 
     * @param path  the default directory for file operations
     */
    public void setRelativePath( File path )
    {
        FileManager.chooser.setCurrentDirectory( path );
    }
    
    /**
     * Tests the enabled state of all the data management
     * menu items. 
     * The user provides the expected state
     * of the "Save," "Save As" and "Delete" items;
     * all other items are
     * expected to be enabled.
     * This method is guaranteed
     * to run in the context of the EDT.
     * 
     * @param expSave   the expected state of the "Save" item
     * @param expSaveAs the expected state of the "Save As" item
     * @param expClose the expected state of the "Close" item
     * @param expDelete the expected state of the "Delete" item
     */
    public void testEnablement( 
        boolean expSave, 
        boolean expSaveAs, 
        boolean expClose,
        boolean expDelete
    )
    {
        // Debugging aid: collect the value of the enabled properties
        // of all the buttons prior to testing them. See also below.
        class State
        {
            boolean newItem;
            boolean openItem;
            boolean saveItem;
            boolean saveAsItem;
            boolean closeItem;
            boolean deleteItem;
        }
        State   state   = new State();
        
        // The schedEDTAndWait task formerly contained all the assertions.
        // But that means, if an assertion fails, we get an 
        // InvaocationTargetException and we have to explore the (long)
        // stack trace in the exception to find the failure. Instead,
        // use schedEDTAndWait to collect all the properties, then put
        // the assertions after the schedEDTAndWait.
        GUIUtils.schedEDTAndWait( () -> {
            state.newItem =  newItem.isEnabled();
            state.openItem =  openItem.isEnabled();
            state.saveItem =  saveItem.isEnabled();
            state.saveAsItem = saveAsItem.isEnabled();
            state.closeItem = closeItem.isEnabled();
            state.deleteItem = deleteItem.isEnabled();
        });
        assertTrue( state.newItem );
        assertTrue( state.openItem );
        assertEquals( expSave, state.saveItem );
        assertEquals( expSaveAs, state.saveAsItem );
        assertEquals( expClose, state.closeItem );
        assertEquals( expDelete, state.deleteItem );
    }
    
    /**
     * Sets a name in the main GUI's equation name field,
     * then posts an action event.
     * 
     * @param name  the text to set in the equation name field
     */
    public void setEquationName( String name )
    {
        GUIUtils.schedEDTAndWait( () -> {
            try
            {
                eqNameField.setText( name );
                eqNameField.commitEdit();
            }
            catch ( ParseException exc )
            {
                fail( exc );
            }
        });
    }
    
    /**
     * Gets the text from the main GUI's equation name field.
     * 
     * @return  the text from the equation name field
     */
    public String getEquationName()
    {
        GUIUtils.schedEDTAndWait( () -> 
            adHocString1 = eqNameField.getText()
        );
        return adHocString1;
    }
    
    /**
     * Clicks the Save button on the File menu.
     */
    public void save()
    {
        doClick( saveItem );
    }
    
    /**
     * Exercise the close feature;
     */
    public void close()
    {
        doClick( closeItem );
    }
    
    /**
     * Exercise the delete feature;
     */
    public void delete()
    {
        doClick( deleteItem );
    }
    
    /**
     * Click the SaveAs button, 
     * bringing up a JFileChooser dialog.
     * Enter the given path into
     * the dialog's text field.
     * If the given boolean is true
     * click the dialog's Save button,
     * else click the cancel button.
     * 
     * @param path  the given path
     * @param save  
     *      true to dismiss dialog with Save,
     *      false to dismiss dialog with Cancel
     */
    public void saveAs( File path, boolean save )
    {
        Thread  thread  = showFileChooser( saveAsItem );
        GUIUtils.schedEDTAndWait( () -> {
            chooserTextField.setText( path.getName() );
            chooserSaveButton = getChooserButton( "Save" );
        });
        JButton terminator  = save ? 
            chooserSaveButton : chooserCancelButton;
        doClick( terminator );
        Utils.join( thread );
    }
    
    /**
     * Click the Open button, 
     * bringing up a JFileChooser dialog.
     * Enter the given path into
     * the dialog's text field.
     * If the given boolean is true
     * click the dialog's Open button,
     * else click the cancel button.
     * 
     * @param path  the given path
     * @param open  
     *      true to dismiss dialog with Open,
     *      false to dismiss dialog with Cancel
     */
    public void open( File path, boolean open )
    {
        Thread  thread  = showFileChooser( openItem );
        GUIUtils.schedEDTAndWait( () -> {
            chooserTextField.setText( path.getName() );
            chooserOpenButton = getChooserButton( "Open" );
        });
        JButton terminator  = open ? 
            chooserOpenButton : chooserCancelButton;
        doClick( terminator );
        Utils.join( thread );
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
        GUIUtils.schedEDTAndWait( () -> button.doClick() );
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
    
    /**
     * Gets the JTextField
     * from the main application frame
     * that contains the name 
     * of an equation.
     * 
     * @return  the JTextField that contains the equation name
     */
    private JFormattedTextField getEquationNameField()
    {
        // Get the panel that contains the name field.
        Predicate<JComponent>   panelPred   = 
            c -> (c instanceof NamePanel);
        JComponent              panel       = 
            ComponentFinder.find( cpFrame, panelPred );
        assertNotNull( panel );
        
        // Find the text field in the NamePanel
        Predicate<JComponent>   pred        = 
            c -> (c instanceof JFormattedTextField);
        JComponent              comp        =
            ComponentFinder.find( panel, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JFormattedTextField);
        JFormattedTextField     textField   = (JFormattedTextField)comp;
        return textField;
    }
    
    /**
     * Finds the JDiaog used by the file chooser.
     * <p>
     * Precondition: The chooser variable is initialized
     * <p>
     * Precondition: This method must be called from the EDT.
     * 
     * @return  
     *      the JDiaog used by the file chooser
     */
    private JDialog getChooserDialog()
    {
        Predicate<Window>   pred    = w -> (w instanceof JDialog);
        // can be dialog = true, can be frame = false, 
        // must be visible = false
        ComponentFinder     finder  = 
            new ComponentFinder( true, false, true );
        Window              window  = finder.findWindow( pred );
        assertNotNull( window );
        assertTrue( window instanceof JDialog);
        JDialog             dialog  = (JDialog)window;
        return dialog;
    }
    
    /**
     * Finds the button with the given text
     * in the file chooser dialog.
     * <p>
     * Precondition: The chooserDialog variable is initialized
     * <p>
     * Precondition: This method must be called from the EDT.
     * 
     * @param text  the given text
     * 
     * @return  
     *      the button in the file chooser dialog with the given text
     */
    private JButton getChooserButton( String text )
    {
        Predicate<JComponent>   pred    = 
            ComponentFinder.getButtonPredicate( text );
        JComponent              comp    =
            ComponentFinder.find( chooserDialog, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JButton );
        JButton button  = (JButton)comp;
        return button;
    }

    /**
     * Finds the text field in the file chooser dialog.
     * <p>
     * Precondition: The chooserDialog variable is initialized
     * <p>
     * Precondition: This method must be called from the EDT.
     * 
     * @return  
     *      the text field in the file chooser dialog
     */
    private JTextField getChooserTextField()
    {
        Predicate<JComponent>   pred    = c -> (c instanceof JTextField);
        JComponent              comp    =
            ComponentFinder.find( chooserDialog, pred );
        assertNotNull( comp );
        assertTrue( comp instanceof JTextField );
        JTextField  textField   = (JTextField)comp;
        return textField;
    }
    
    /**
     * Activates the given button in a dedicated thread
     * thereby starting the FileChooser dialog.
     * Obtains references to the FileChooserDialog,
     * and its text field and Cancel button.
     * Returns the thread ID to the caller.
     * 
     * @param button    the given button
     * 
     * @return the ID of the thread used to start the dialog
     * 
     * @see #chooserDialog
     * @see #chooserCancelButton
     * @see #chooserTextField
     */
    private Thread showFileChooser( AbstractButton button )
    {
        Thread  thread  = new Thread( () -> doClick( button ) );
        thread.start();
        Utils.pause( 500 );
        
        chooserDialog = getChooserDialog();
        chooserCancelButton = getChooserButton( "Cancel" );
        chooserTextField = getChooserTextField();
        return thread;
    }
}
