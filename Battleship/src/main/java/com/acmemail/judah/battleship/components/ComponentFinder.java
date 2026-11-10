package com.acmemail.judah.battleship.components;

import java.awt.Container;
import java.awt.Window;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * Utility class to search for components
 * within an application's window hierarchy.
 * You can search for a top-level window
 * (limited to types JDialog and JFrame).
 * You can search for a component
 * (limited to type JComponent)
 * anywhere in the window hierarchy,
 * or nested within a particular container.
 * 
 * @author Jack
 */
public class ComponentFinder
{
    /**
     * Default constructor; not used.
     */
    private ComponentFinder()
    {
        // not used
    }
    
    /**
     * Returns the first JFrame that satisfies the given predicate.
     * If {@code mustBeDisplayable} is true,
     * only displayable frames will be considered.
     * 
     * @param pred              the given predicate
     * @param mustBeDisplayable true, to filter on displayable frames 
     * 
     * @return  the first JFrame that satisfies the given predicate
     * 
     * @throws NullPointerException if pred is null
     */
    public static JFrame getFrame( 
        Predicate<JFrame> pred,
        boolean mustBeDisplayable
    )
    {
        Objects.requireNonNull( pred, "pred" );
        Predicate<JFrame>   displayable = JFrame::isDisplayable;
        Predicate<JFrame>   filter      = mustBeDisplayable ?
            pred.and( displayable ) : pred;
        JFrame              frame       = 
            Arrays.stream( Window.getWindows() )
                .filter( w -> w instanceof JFrame )
                .map( w -> (JFrame)w )
                .filter( filter )
                .findFirst().orElse( null );
        return frame;
    }
    
    /**
     * Gets the first JFrame with the given title.
     * If {@code mustBeDisplayable} is true,
     * only displayable frames will be considered.

     * @param title             the given title
     * @param mustBeDisplayable true, to filter on displayable frames
     * 
     * @return the first JFrame with the given title
     * 
     * @throws NullPointerException if title is null
     */
    public static JFrame getFrame( String title, boolean mustBeDisplayable )
    {
        JFrame  frame   = getFrame( jfHasTitle( title ), mustBeDisplayable );
        return frame;
    }
    
    /**
     * Gets the first displayable JFrame with the given title.

     * @param title             the given title
     * 
     * @return the first displayable JFrame with the given title
     * 
     * @throws NullPointerException if pred is null
     */
    public static JFrame getFrame( String title )
    {
        JFrame  frame   = getFrame( title, true );
        return frame;
    }
    
    /**
     * Returns the first JDialog that satisfies the given predicate.
     * If {@code mustBeDisplayable} is true,
     * only displayable dialogs will be considered.
     * 
     * @param pred              the given predicate
     * @param mustBeDisplayable true, to filter on displayable dialogs 
     * 
     * @return  the first JDialog that satisfies the given predicate
     * 
     * @throws NullPointerException if pred is null
     */
    public static JDialog getDialog( 
        Predicate<JDialog> pred,
        boolean mustBeDisplayable
    )
    {
        Objects.requireNonNull( pred, "pred" );
        Predicate<JDialog>  displayable = JDialog::isDisplayable;
        Predicate<JDialog>  filter      = mustBeDisplayable ?
            pred.and( displayable ) : pred;
        JDialog             frame       = 
            Arrays.stream( Window.getWindows() )
                .filter( w -> w instanceof JDialog )
                .map( w -> (JDialog)w )
                .filter( filter )
                .findFirst().orElse( null );
        return frame;
    }
    
    /**
     * Returns the first JDialog with the given title.
     * If {@code mustBeDisplayable} is true,
     * only displayable dialogs will be considered.
     * 
     * @param title             the given title
     * @param mustBeDisplayable true, to filter on displayable dialogs 
     * 
     * @return  the first JDialog with the given title
     * 
     * @throws NullPointerException if title is null
     */
    public static JDialog getDialog( String title, boolean mustBeDisplayable )
    {
        JDialog dialog  = getDialog( jdHasTitle( title ), mustBeDisplayable );
        return dialog;
    }
    
    /**
     * Returns the first displayable JDialog with the given title.
     * 
     * @param title             the given title
     * 
     * @return  the first displayable JDialog with the given title
     * 
     * @throws NullPointerException if title is null
     */
    public static JDialog getDialog( String title )
    {
        JDialog dialog  = getDialog( title, true );
        return dialog;
    }
    
    /**
     * Returns the first top-level window
     * that satisfies the given predicate.
     * 
     * @param pred  the given predicate
     * 
     * @return the first top-level window that satisfies the given predicate
     * 
     * @throws NullPointerException if pred is null
     */
    public static Window getTopLevelWindow( Predicate<Window> pred )
    {
        Objects.requireNonNull( pred, "pred" );
        Window  window  = 
            Arrays.stream( Window.getWindows() )
                .filter( w -> (w instanceof JFrame) || (w instanceof JDialog) )
                .filter( pred )
                .findFirst().orElse( null );
        return window;
    }
    
    /**
     * Finds the first JComponent child
     * of the given top-level window
     * to satisfy the given predicate.
     * 
     * @param window    the given top-level window
     * @param pred      the given predicate
     * 
     * @return  the first JComponent child of window that satisfies pred
     * 
     * @throws NullPointerException if pred is null
     * @throws NullPointerException if window is null
     */
    public static JComponent getComponent( 
        Window  window, 
        Predicate<JComponent> pred 
    )
    {
        Objects.requireNonNull( window, "window" );
        Objects.requireNonNull( pred, "pred" );
        JComponent  comp;
        if ( window instanceof JFrame frame )
            comp = getComponent( frame.getContentPane(), pred );
        else if ( window instanceof JDialog dialog )
            comp = getComponent( dialog.getContentPane(), pred );
        else
            comp = null;
        
        return comp;
    }
    
    /**
     * Finds the first JComponent child
     * of the given top-level window
     * with the given name.
     * 
     * @param window    the given top-level window
     * @param name      the given name
     * 
     * @return  the first JComponent child of window with the given name
     * 
     * @throws NullPointerException if pred is null
     */
    public static JComponent getComponent( 
        Window  window, 
        String name 
    )
    {
        JComponent  comp    = 
            getComponent( window, jcHasName( name ) );
        return comp;
    }
    
    /**
     * Finds the first JComponent child
     * of the given container
     * that satisfies the given predicate.
     * 
     * @param container the given container
     * @param pred      the given predicate
     * 
     * @return  the first JComponent child of window with satisfies pred
     * 
     * @throws NullPointerException if container is null
     * @throws NullPointerException if pred is null
     */
    public static JComponent getComponent( 
        Container container, 
        Predicate<JComponent> pred 
    )
    {
        Objects.requireNonNull( pred, "pred" );
        Objects.requireNonNull( container, "container" );
        JComponent  component   = null;
        if ( (container instanceof JComponent self) && pred.test( self ) )
                component = self;
        else
        {
            component = Arrays.stream( container.getComponents() )
                .filter( c -> c instanceof JComponent )
                .map( c -> (JComponent)c )
                .map( jc -> getComponent( jc, pred ) )
                .filter( jc -> jc != null )
                .findFirst().orElse( null );
        }
        
        return component;
    }
    
    /**
     * Disposes all top-level windows in the component hierarchy.
     */
    public static void disposeAll()
    {
        ComponentUtils.invokeAndWait( () -> {
            Arrays.stream( Window.getWindows() ).forEach( Window::dispose );
        });
    }
    
    /**
     * Compiles a predicate that tests true
     * if the name of a JFrame is non-null
     * and equal to the given name.
     * 
     * @param name  the given name
     * 
     * @return  a predicate that compares a JFrame name to the given name
     * 
     * @throws NullPointerException if name is null
     */
    public static Predicate<JFrame> jfHasName( String name )
    {
        Objects.requireNonNull( name, "name" );
        Predicate<JFrame> predicate = jf -> name.equals( jf.getName() );
        return predicate;
    }
    
    /**
     * Compiles a predicate that tests true
     * if the name of a JDialog is non-null
     * and equal to the given name.
     * 
     * @param name  the given name
     * 
     * @return  a predicate that compares a JDialog name to the given name
     * 
     * @throws NullPointerException if name is null
     */
    public static Predicate<JDialog> jdHasName( String name )
    {
        Objects.requireNonNull( name, "name" );
        Predicate<JDialog>  predicate = jd -> name.equals( jd.getName() );
        return predicate;
    }
    
    /**
     * Compiles a predicate that tests true
     * if the title of a JFrame is non-null
     * and equal to the given title.
     * 
     * @param title  the given title
     * 
     * @return  a predicate that compares a JFrame title to the given title
     * 
     * @throws NullPointerException if title is null
     */
    public static Predicate<JFrame> jfHasTitle( String title )
    {
        Objects.requireNonNull( title, "title" );
        Predicate<JFrame> predicate = jf -> title.equals( jf.getTitle() );
        return predicate;
    }
    
    /**
     * Compiles a predicate that tests true
     * if the title of a JDialog is non-null
     * and equal to the given title.
     * 
     * @param title  the given title
     * 
     * @return  a predicate that compares a JDialog title to the given title
     * 
     * @throws NullPointerException if title is null
     */
    public static Predicate<JDialog> jdHasTitle( String title )
    {
        Objects.requireNonNull( title, "title" );
        Predicate<JDialog>  predicate = jd -> title.equals( jd.getTitle() );
        return predicate;
    }
    
    /**
     * Compiles a predicate that tests true
     * if the name of a JComponent is non-null
     * and equal to the given name.
     * 
     * @param name  the given name
     * 
     * @return  a predicate that compares a JComponent name to the given name
     * 
     * @throws NullPointerException if name is null
     */
    public static Predicate<JComponent> jcHasName( String name )
    {
        Objects.requireNonNull( name, "name" );
        Predicate<JComponent>  predicate = jc -> name.equals( jc.getName() );
        return predicate;
    }
}
