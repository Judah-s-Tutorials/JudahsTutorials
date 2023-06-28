package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Window;
import java.util.function.Predicate;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;

/**
 * Program to demonstrate how to search a JFileChooser dialog
 * for the control buttons (save/open/cancel)
 * and path name text field.
 * <p>
 * A save dialog is displayed. 
 * The text field for entering the path,
 * and the control buttons,
 * save/open/cancel are located,
 * and, if found, 
 * their hashcodes are printed to stdout.
 * When the save dialog is displayed
 * you should expect the open button
 * not to be found
 * (null will be printed).
 * <p>
 * Next an open dialog is displayed,
 * and the procedure above is repeated.
 * When the open dialog is displayed
 * you should expect the save button
 * not to be found
 * (null will be printed).
 * <p>
 * In both of the above cases
 * a diagnostic will be displayed
 * in the the text field,
 * and the dialog will remain open
 * for three seconds
 * so that it can be examined
 * by the operator.
 * After three seconds
 * the dialog will be cancelled.
 * 
 * @author Jack Straub
 */
public class JFileChooserSearch
{
    /** Component finder for searching for components. */
    private static final ComponentFinder finder = new ComponentFinder();
    /** File-chooser dialog being displayed and interrogated. */
    private static final JFileChooser    chooser = new JFileChooser();
    
    /**
     *  Used as part of the feedback displayed in the file chooser's
     *  text field. Incremented after each use, 
     *  serves as an indication to the operator
     *  that a new string is displayed
     *  each time the dialog is shown.
     */
    private static int  count   = 0;
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     * 
     * @throws InterruptedException
     *      if raised during one of the Thread operations
     *      sleep or join
     */
    public static void main(String[] args) throws InterruptedException
    {
        printIds( () -> chooser.showSaveDialog( null ) );
        printIds( () -> chooser.showOpenDialog( null ) );
        System.exit( 0 );
    }
    
    /**
     * Displays the file chooser,
     * and searches for the components
     * needed to control the dialog.
     * 
     * @param runner
     *      functional interface used to show the file chooser;
     *      presumably a reference to either <em>chooser.showSavedialog</em>
     *      or <em>chooser.showOpendialog</em>
     *      
     * @throws InterruptedException
     *      if raised by a Thread operation
     */
    private static void printIds( Runnable runner )
        throws InterruptedException
    {
        Thread  thread          = start( runner );
        getJDialog();
        JTextField textField    = getTextField();
        JButton cancelButton    = getButton( "Cancel" );
        getButton( "Save" );
        getButton( "Open" );
        System.out.println( "====================" );
        
        SwingUtilities.invokeLater( () -> 
            textField.setText( "Path goes here: " + ++count )
        );
        Thread.sleep( 3000 );
        if ( cancelButton != null )
            SwingUtilities.invokeLater( () -> cancelButton.doClick() );
        thread.join();
    }
    
    /**
     * Searches the application window hierarchy
     * for the first visible dialog.
     * Prints an identifying message
     * to stdout.
     * 
     * @return
     *      the first visible dialog found,
     *      or null if none
     */
    private static JDialog getJDialog()
    {
        Window  window  = finder.findWindow( w -> true );
        String  ident   = null;
        JDialog dialog  = null;
        
        if ( window == null )
            ;
        else if ( !(window instanceof JDialog) )
            ident = "??? " + window.getClass().getName();
        else
        {
            dialog = (JDialog)window;
            ident = Integer.toHexString( window.hashCode() ) + " ";
            ident += window.getClass().getName();
        }
        System.out.println( "JDialog: " + ident );
        return dialog;
    }
    
    /**
     * Searches the application window hierarchy
     * for the first JButton
     * with the given text.
     * Prints an identifying message
     * to stdout.
     * 
     * @param   text    the given text
     * 
     * @return
     *      the first button with the given text found,
     *      or null if none
     */
    private static JButton getButton( String text )
    {
        Predicate<JComponent>   pred    =
            ComponentFinder.getButtonPredicate( text );
        String                  ident   = "?????";
        JButton                 button  = null;
        
        JComponent              comp    = finder.find( pred );
        if ( comp == null )
            ident = "null";
        else if ( !(comp instanceof JButton) )
            ident = comp.getClass().getName();
        else
        {
            button = (JButton)comp;
            ident = Integer.toHexString( comp.hashCode() );
        }
        
        System.out.println( text + ": " + ident );
        return button;
    }
    
    /**
     * Searches the application window hierarchy
     * for the first JTextField.
     * Prints an identifying message
     * to stdout.
     * 
     * @return
     *      the first JTextField found,
     *      or null if none
     */
    private static JTextField getTextField()
    {
        JComponent  comp    = finder.find( c -> (c instanceof JTextField) );
        JTextField  text    = null;
        String      ident   = null;
        if ( comp != null && comp instanceof JTextField ) 
        {
            text = (JTextField)comp;
            ident = Integer.toHexString( text.hashCode() );
        }
        System.out.println( "TextField: " + ident );
        return text;
    }
    
    /**
     * Starts the given Runnable
     * in a dedicated thread.
     * 
     * @param funk  the given runnable
     * 
     * @return  the created thread
     * 
     * @throws InterruptedException
     *      if raised by Thread.sleep
     */
    private static Thread start( Runnable funk )
        throws InterruptedException
    {
        Thread          thread  = new Thread( funk );
        thread.start();
        Thread.sleep( 500 );
        
        return thread;
    }
}
