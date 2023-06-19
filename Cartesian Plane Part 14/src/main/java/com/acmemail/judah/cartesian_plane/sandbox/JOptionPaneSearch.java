package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.function.Predicate;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentFinder;

/**
 * Program to display a JOptionPane message dialog,
 * and search it for the contained message
 * and OK button. 
 * Diagnostics are printed to stdout.
 * The dialog remains displayed for three seconds
 * before being dismissed.
 * 
 * @author Jack Straub
 */
public class JOptionPaneSearch
{
    /** Component finder for searching for components. */
    private static final ComponentFinder    finder  = new ComponentFinder();
    
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
        Thread  thread  = start( () -> showErrorDialog() );
        JButton button  = getButton( "OK" );
        getLabel( "error" );
        System.out.println( button );
        Thread.sleep( 3000 );
        
        if ( button != null )
            button.doClick();
        thread.join();
        System.exit( 0 );
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
     * for the first JLabel
     * containing the given text.
     * Prints an identifying message
     * to stdout.
     * 
     * @param   containedText    the given text
     * 
     * @return
     *      the first button with the given text found,
     *      or null if none
     */
    private static JLabel getLabel( String containedText )
    {
        Predicate<JComponent>   notNull     = jc -> jc != null;
        Predicate<JComponent>   isLabel     = jc -> jc instanceof JLabel;
        Predicate<JComponent>   contains    = 
            jc -> contains( containedText, ((JLabel)jc).getText() );
        Predicate<JComponent>   pred        = 
            notNull.and( isLabel ).and( contains );
        JComponent  comp    = finder.find( pred );
        JLabel      label   = null;
        if ( comp != null && comp instanceof JLabel )
            label = (JLabel)comp;
        
        if ( label != null )
            System.out.println( "Label: " + label.getText() );
        return label;
    }
    
    /**
     * Convenience method to indicate
     * whether a given string (container)
     * contains another given string (contained).
     * If either parameter is null
     * false will be returned.
     * 
     * @param contained     the text that might be found in the container
     * @param container     the container to search
     * 
     * @return 
     *      true if the given strings are not null
     *      and the <em>container</em> string contains
     *      the <em>contained</em> string.
     */
    private static boolean contains( String contained, String container )
    {
        boolean result  = false;
        if ( contained != null && container != null )
            result = container.contains( contained );
        return result;
    }
    
    /**
     * Starts the given Runnable
     * in a dedicated thread.
     * Pauses before returning
     * to allow the new thread
     * to initialize and execute.
     * 
     * @param funk  the given Runnable
     * 
     * @return  the created thread
     * 
     * @throws InterruptedException
     *      if raised by a Thread operation
     */
    private static Thread start( Runnable funk )
        throws InterruptedException
    {
        Thread          thread  = new Thread( funk );
        thread.start();
        Thread.sleep( 500 );
        
        return thread;
    }
    
    /**
     * Displays a message dialog.
     */
    private static void showErrorDialog()
    {
        String  message = "This is an error message";
        String  title   = "Error Dialog Title";
        int     type    = JOptionPane.ERROR_MESSAGE;
        JOptionPane.showMessageDialog(
            null,
            message,
            title,
            type
        );
    }
}
