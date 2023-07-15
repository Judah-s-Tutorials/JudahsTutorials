package com.acmemail.judah.sandbox.sandbox;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.acmemail.judah.sandbox.ComponentException;

/**
 * Utility to traverse an application's window hierarchy.
 * 
 * @author Jack Straub
 */
public class RecursiveListComponents
{
    /**
     * Locates all the components
     * in an application window's hierarchy,
     * and print identifying details.
     * 
     * @throws ComponentException if an error occurs
     */
    public static void dumpWindows()
    {
        Predicate<Window>   isDialog        = w -> w instanceof JDialog;
        Predicate<Window>   isFrame         = w -> w instanceof JFrame;
        Predicate<Window>   isDialogOrFrame = isDialog.or( isFrame );
        Arrays.stream( Window.getWindows() )
            .filter( isDialogOrFrame )
            .peek( RecursiveListComponents::printFrame )
            .map( RecursiveListComponents::getContentPane )
            .forEach( c -> dumpContainer( c, 1 ) );
    }
    
    /**
     * Prints identifying details 
     * of a given top-level window.
     * 
     * @param window    the given window
     * 
     * @throws 
     *      ComponentException if the given window
     *      is not a JDialog or a JFrame.
     */
    private static void printFrame( Window window )
    {
        System.out.print( "class name: " );
        System.out.println( window.getClass().getName() );
        System.out.print( "window name: " );
        System.out.println( window.getName() );
        System.out.print( "title: " );
        if ( window instanceof JFrame )
            System.out.println( ((JFrame)window).getTitle() );
        else if ( window instanceof JDialog )
            System.out.println( ((JDialog)window).getTitle() );
        else 
            throw new ComponentException( "Unexpected Window type" );
    }
    
    /**
     * Gets the content pane
     * of a given top-level window.
     * 
     * @param window    the given window
     * 
     * @return the content pane of the given window
     * 
     * @throws 
     *      ComponentException if the given window
     *      is not a JDialog or a JFrame.
     */
    private static Container getContentPane( Window window )
    {
        Container   container   = null;
        if ( window instanceof JDialog )
            container = ((JDialog)window).getContentPane();
        else if ( window instanceof JFrame )
            container = ((JFrame)window).getContentPane();
        else
        {
            String  name    = window.getClass().getName();
            String  message = "Not JDialog or JFrame: " + name;
            throw new ComponentException( message );
        }
        return container;
    }
    
    /**
     * Recursively prints identifying details
     * of a given Container
     * and all of its nested components.
     * 
     * @param container     the given Container
     * @param indentNum     
     *      the nesting level; 
     *      used to indent the output
     */
    private static void dumpContainer( Container container, int indentNum )
    {
        String          spaces  = "    ";
        StringBuilder   bldr    = new StringBuilder();
        IntStream.range( 0, indentNum ).forEach( i -> bldr.append( spaces ) );
        String          indent  = bldr.toString();
        
        Component[]     children    = container.getComponents();
        printComponent( container, indent );
        System.out.println( indent + "num children: " + children.length );
        System.out.println( indent + "====================" );
        for ( Component child : children )
        {
            if ( child instanceof Container )
                dumpContainer( (Container)child, indentNum + 1 );
            else
                printComponent( child, indent + spaces );
        }
    }
        
    /**
     * Prints identifying information
     * of a given Component.
     * 
     * @param comp      the given Component
     * @param indent    prefix used to indent each line of output
     */
    private static void printComponent( Component comp, String indent )
    {
        System.out.println( indent + comp.getClass().getName() );
        System.out.println( indent + "name: " + comp.getName() );
        String  text    = null;
        if ( comp instanceof JLabel )
            text = ((JLabel)comp).getText();
        else if ( comp instanceof JButton )
            text = ((JButton)comp).getText();
        else
            ;
        System.out.println( indent + "text: " + text );
    }
}
