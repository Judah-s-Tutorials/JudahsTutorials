package com.acmemail.judah.cartesian_plane.graphics_utils;

import java.awt.Container;
import java.awt.Window;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Utility class to search for components
 * within an application's window hierarchy.
 * You can search for a top-level window
 * (limited to types JDialog and JFrame).
 * You can search for a component
 * (limited to type JComponent)
 * anywhere in the window hierarchy,
 * or nested within a particular container.
 * By default,
 * searches are limited
 * to "displayable" top-level windows,
 * but that can be configured.
 * Instances of this class
 * can be figured with the following filters:
 * <ul>
 * <li>Search only JDialogs</li>
 * <li>Search only JFrames</li>
 * <li>Search JDialogs and JFrames</li>
 * <li>Search only visible top-level windows
 * <li>Search displayable top-level window</li>
 * </li>
 * </ul>
 * <p>
 * In addition,
 * the built-in filters
 * can be overridden altogether
 * by a user-defined filter.
 * 
 * @author Jack Straub
 */
/**
 * @author Jack Straub
 */
public class ComponentFinder
{   
    /** 
     * Predicate that's satisfied when
     * a Window is an instance of a JDialog.
     */
    private static final Predicate<Window>      isDialog        = 
        w -> w instanceof JDialog;
    /** 
     * Predicate that's satisfied when
     * a Window is an instance of a JFrame.
     */
    private static final Predicate<Window>      isFrame         = 
        w -> w instanceof JFrame;
    /** Predicate that's satisfied when a top-level window is visible. */
    private static final Predicate<Window>      isVisible       = 
        Window::isVisible;
    /** Predicate that's satisfied when a top-level window is displayable. */
    private static final Predicate<Window>      isDisplayable   = 
        Window::isDisplayable;
    /** 
     * Predicate that's satisfied when
     * a Component is an instance of a JComponent.
     */
    private static final Predicate<Object>      isJComponent    = 
        c -> c instanceof JComponent;
    
    /** Indicates whether JFrames can be searched. */
    private boolean canBeFrame          = true;
    /** Indicates whether JDialogs can be searched. */
    private boolean canBeDialog         = true;
    /** Indicates that only visible top-level windows can be searched. */
    private boolean mustBeVisible       = true;
    /** Indicates that only displayable top-level windows can be searched. */
    private boolean mustBeDisplayable   = true;
    
    /** Predicate that's configured dynamically from the above Booleans. */
    private Predicate<Window>   topWindowFilter;
    
    /**
     * Default constructor.
     * Sets can-be-frame to true,
     * can-be-dialog to true,
     * and must-be-visible to true,
     * and must-be-displayable to true.
     */
    public ComponentFinder()
    {
        this( true, true, true );
    }

    /**
     * Constructor.
     * Sets can-be-frame, can-be-dialog and must-be-visible 
     * to the given values.
     * Sets must-be-displayable to true.
     * 
     * @param canBeFrame        the can-be-frame value
     * @param canBeDialog       the can-be-dialog value
     * @param mustBeVisible     the must-be-visible value
     */
    public ComponentFinder( 
        boolean canBeDialog, 
        boolean canBeFrame, 
        boolean mustBeVisible
    )
    {
        this.canBeDialog = canBeDialog;
        this.canBeFrame = canBeFrame;
        this.mustBeVisible = mustBeVisible;
        setTopWindowFilter();
    }
    
    /**
     * Sets the can-be-frame parameter.
     * 
     * @param value true to allow JFrames to be searched;
     *              false to eliminate JFrames from searches.
     * 
     */
    public void setCanBeFrame(boolean value)
    {
        setParameter( value, b -> canBeFrame = b );
    }

    /**
     * Sets the can-be-dialog parameter.
     * 
     * @param value     true to allow JDialogs to be searched;
     *                  false to eliminate JDialogs from searches.
     * 
     */
    public void setCanBeDialog(boolean value)
    {
        setParameter( value, b -> canBeDialog = b );
    }

    /**
     * Sets the must-be-visible parameter.
     * 
     * @param value true to allow only visible 
     *              top-level windows to be searched;
     *              false to allow any top-level window
     *              to be searched, whether its visible or not
     */
    public void setMustBeVisible(boolean value)
    {
        setParameter( value, b -> mustBeVisible = b );
    }

    /**
     * Sets the must-be-displayable parameter.
     * 
     * @param value 
     *      true to allow only displayable top-level windows to be searched;
     *      false to allow any top-level window to be searched, 
     *      whether its displayable or not
     */
    public void setMustBeDisplayable(boolean value)
    {
        setParameter( value, b -> mustBeDisplayable = b );
    }

    /**
     * Gets the value of the can-be-frame parameter.
     * 
     * @return the value of the can-be-frame parameter
     */
    public boolean isCanBeFrame()
    {
        return canBeFrame;
    }

    /**
     * Gets the value of the can-be-dialog parameter.
     * 
     * @return the value of the can-be-dialog parameter
     */
    public boolean isCanBeDialog()
    {
        return canBeDialog;
    }

    /**
     * Gets the value of the must-be-visible parameter.
     * 
     * @return the value of the must-be-visible parameter
     */
    public boolean isMustBeVisible()
    {
        return mustBeVisible;
    }

    /**
     * Gets the value of the must-be-displayable parameter.
     * 
     * @return the value of the must-be-displayable parameter
     */
    public boolean isMustBeDisplayable()
    {
        return mustBeDisplayable;
    }

    /**
     * Gets the value of the top window filter.
     * 
     * @return the value of the top window filter
     */
    public Predicate<Window> getTopWindowFilter()
    {
        return topWindowFilter;
    }

    /**
     * Sets the top-level window filter.
     * Setting this filter will override
     * any other parameters,
     * such as must-be-dialog and must-be-visible.
     * Only top-level windows that satisfy 
     * the given predicate will be searched.
     * 
     * @param topWindowFilter the given predicate
     */
    public void setTopWindowFilter( Predicate<Window> topWindowFilter )
    {
        this.topWindowFilter = topWindowFilter;
    }
    
    /**
     * Returns the first top-level window
     * that passes this instances default filters
     * and the given predicate.
     * 
     * @param pred  the given predicate
     * 
     * @return the first top-level window that passes
     *         instance and user filters
     */
    public Window findWindow( Predicate<Window> pred )
    {
        Window  window  = Arrays.stream( Window.getWindows() )
            .filter( topWindowFilter )
            .filter( pred )
            .findFirst()
            .orElse( null );
        return window;
    }
    
    /**
     * Beginning with the top-level windows,
     * searches the window hierarchy
     * for the first JComponent
     * that satisfies the given predicate.
     * Top-level windows to be searched
     * must satisfy all the built-in
     * search parameters,
     * such as can-be-dialog and must-be-visible.
     * 
     * @param pred  the given predicate
     * 
     * @return the first 
     *      JComponent in the window hierarchy
     *      that satisfies the given predicate
     */
    public JComponent find( Predicate<JComponent> pred )
    {
       JComponent   comp    =  Arrays.stream( Window.getWindows() )
            .filter( topWindowFilter )
            .map( w -> find( w, pred ) )
            .filter( c -> c != null )
            .findFirst()
            .orElse( null );
        return comp;
    }
    
    /**
     * Searches for a JComponent
     * present in the hierarchy
     * of a given top-level window.
     * The first such component
     * to satisfy a given predicate
     * will be returned.
     * 
     * @param window    the given top-level window
     * @param pred      the given predicate
     * 
     * @return 
     *      the first component, within the given hierarchy
     *      that satisfies the given predicate
     */
    public static JComponent 
    find( Window window, Predicate<JComponent> pred )
    {
        JComponent  comp        = null;
        Container   contentPane = getContentPane( window );
        JComponent  jPane       = 
            contentPane instanceof JComponent ?
            (JComponent)contentPane :
            null;
        
        if ( jPane != null )
            comp = find( jPane, pred );
        
        return comp;
    }
    
    /**
     * Searches for a JComponent
     * present in the hierarchy
     * of a given container.
     * The first such component
     * to satisfy a given predicate
     * will be returned.
     * 
     * @param container the given container
     * @param pred      the given predicate
     * 
     * @return 
     *      the first component, within the given hierarchy
     *      that satisfies the given predicate
     */
    public static JComponent 
    find( JComponent container, Predicate<JComponent> pred )
    {
        JComponent  comp    = null;
        if ( pred.test( container ) )
            comp = container;
        else
            comp = Arrays.stream( container.getComponents() )
                .filter( isJComponent )
                .map( c -> find( (JComponent)c, pred ) )
                .filter( c -> c != null )
                .findFirst()
                .orElse( null );
        return comp;
    }
    
    /**
     * Locates and disposes all top-level windows
     * in an application.
     * <p style="padding-left:3em;">
     *     SAFE TO INVOKE FROM ANY THREAD
     * </p>
     * <P>
     * Note that 
     * it is safe
     * to call this method
     * from the EDT
     * or any other thread.
     * If not called
     * from the EDT,
     * the dispose task
     * will be scheduled
     * to run on the EDT.
     * </p>
     */
    public static void disposeAll()
    {
        if ( SwingUtilities.isEventDispatchThread() )
            disposeAllEDT();
        else
            GUIUtils.schedEDTAndWait( () -> {
                disposeAllEDT();
            });
    }
    
    /**
     * Constructs a predicate
     * that describes a pushbutton
     * with a given label.
     * 
     * @param label the given label
     * 
     * @return  a predicate describing a pushbutton
     *          with a given label
     */
    public static Predicate<JComponent> getButtonPredicate( String label )
    {
        Predicate<JComponent>   isButton    = jc -> jc instanceof JButton;
        Predicate<JComponent>   hasLabel    = 
            jc -> label.equals( ((JButton)jc).getText() );
        Predicate<JComponent>   pred        = isButton.and( hasLabel );
        return pred;
    }
    
    /**
     * Constructs a predicate
     * that describes a top-level window
     * with a given title.
     * 
     * @param title the given title
     * 
     * @return  a predicate describing a top-level window
     *          with a given title
     */
    public static Predicate<Window> getWindowPredicate( String title )
    {
        Predicate<Window>   isDialog        = w -> (w instanceof JDialog);
        Predicate<Window>   isFrame         = w -> (w instanceof JFrame);
        Predicate<Window>   isDialogTitle   = w ->
            title.equals( ((JDialog)w).getTitle() );
        Predicate<Window>   isFrameTitle    =  w ->
            title.equals( ((JFrame)w).getTitle() );
        Predicate<Window>   pred            =
            (isDialog.and( isDialogTitle )).or(isFrame.and( isFrameTitle ));
        return pred;
    }
    
    /**
     * Locates and disposes all top-level windows
     * in an application.
     * It is assumed
     * that this method
     * is being invoked
     * from the EDT.
     * 
     * @see #disposeAll()
     */
    private static void disposeAllEDT()
    {
        Arrays.stream( Window.getWindows() ).forEach( Window::dispose );
    }
    
    /**
     * Convenience method to set a top-level search parameter,
     * and to perform the necessary configuration
     * when such a parameter changes.
     * 
     * @param value the value of the parameter
     * @param dest  the destination of the given value
     */
    private void setParameter( boolean value, Consumer<Boolean> dest )
    {
        dest.accept( value );
        setTopWindowFilter();
    }
    
    /**
     * Assuming that a given Window
     * is a JFrame or a JDialog,
     * obtains the Window's content pane.
     * If the window is neither
     * a JDialog nor a JFrame
     * null is returned.
     * 
     * @param window    the given Window
     * 
     * @return  
     *      the given Window's content pane,
     *      or null if the operation fails
     */
    private static Container getContentPane( Window window )
    {
        Container   contentPane;
        if ( window instanceof JDialog )
            contentPane = ((JDialog)window).getContentPane();
        else if ( window instanceof JFrame )
            contentPane = ((JFrame)window).getContentPane();
        else
            contentPane = null;
        return contentPane;
    }

    /**
     * Convenience method to configure
     * the default top-level window filter
     * from the default parameters.
     */
    private void setTopWindowFilter()
    {
        Predicate<Window>   finalPredicate  = w -> true;
        if ( canBeFrame )
        {
            finalPredicate = isFrame;
            if ( canBeDialog )
                finalPredicate = finalPredicate.or( isDialog );
        }
        else if ( canBeDialog )
            finalPredicate = isDialog;
        else
            ;
        
        if ( mustBeVisible )
            finalPredicate = finalPredicate.and( isVisible );
        if ( mustBeDisplayable )
            finalPredicate = finalPredicate.and( isDisplayable );
        topWindowFilter = finalPredicate;
    }
}
