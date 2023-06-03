package com.acmemail.judah.cartesian_plane.test_utils;

import java.awt.Container;
import java.awt.Window;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class ComponentFinder
{   
    private static final Predicate<Window>      isDialog        = 
        w -> w instanceof JDialog;
    private static final Predicate<Window>      isFrame         = 
        w -> w instanceof JFrame;
    private static final Predicate<Window>      isVisible       = 
        Window::isVisible;
    private static final Predicate<Window>      isDisplayable   = 
        Window::isDisplayable;
    private static final Predicate<Object>      isJComponent    = 
        c -> c instanceof JComponent;
    
    private boolean canBeFrame          = true;
    private boolean canBeDialog         = true;
    private boolean mustBeVisible       = true;
    private boolean mustBeDisplayable   = true;
    
    private Predicate<Window>   topWindowFilter;
    
    public ComponentFinder()
    {
        this( true, true, true );
    }

    public ComponentFinder( 
        boolean canBeFrame, 
        boolean canBeDialog, 
        boolean mustBeVisible
    )
    {
        this.canBeDialog = canBeDialog;
        this.canBeFrame = canBeFrame;
        this.mustBeVisible = mustBeVisible;
        setTopWindowFilter();
    }
    
    /**
     * @param canBeFrame the canBeFrame to set
     */
    public void setCanBeFrame(boolean canBeFrame)
    {
        this.canBeFrame = canBeFrame;
    }

    /**
     * @param canBeDialog the canBeDialog to set
     */
    public void setCanBeDialog(boolean canBeDialog)
    {
        this.canBeDialog = canBeDialog;
    }

    /**
     * @param mustBeVisible the mustBeVisible to set
     */
    public void setMustBeVisible(boolean mustBeVisible)
    {
        this.mustBeVisible = mustBeVisible;
    }

    /**
     * @param mustBeDisplayable the mustBeDisplayable to set
     */
    public void setMustBeDisplayable(boolean mustBeDisplayable)
    {
        this.mustBeDisplayable = mustBeDisplayable;
    }

    /**
     * @param topWindowFilter the topWindowFilter to set
     */
    public void setTopWindowFilter( Predicate<Window> topWindowFilter )
    {
        this.topWindowFilter = topWindowFilter;
    }
    
    public Window findTop( Predicate<Window> pred )
    {
        Window  window  = Arrays.stream( Window.getWindows() )
            .filter( topWindowFilter )
            .filter( pred )
            .findFirst()
            .orElse( null );
        return window;
    }
    
    public JComponent find( Predicate<JComponent> pred )
    {
       JComponent   comp    =  Arrays.stream( Window.getWindows() )
            .filter( topWindowFilter )
            .map( w -> find( w, pred ) )
            .findFirst()
            .orElse( null );
        return comp;
    }
    
    public JComponent find( Window window, Predicate<JComponent> pred )
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
    
    public JComponent find( JComponent container, Predicate<JComponent> pred )
    {
        JComponent  comp    = null;
        if ( pred.test( container ) )
            comp = container;
        else
            comp = Arrays.stream( container.getComponents() )
                .filter( isJComponent )
                .map( c -> find( (JComponent)c, pred ) )
                .findFirst()
                .orElse( null );
        return comp;
    }
    
    private Container getContentPane( Window window )
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

    private void setTopWindowFilter()
    {
        Predicate<Window>   finalPredicate  = w -> true;
        if ( canBeFrame )
        {
            Predicate<Window>   pred    = isFrame;
            if ( canBeDialog )
                finalPredicate = pred.or( isDialog );
        }
        else if ( canBeDialog )
            finalPredicate = isDialog;
        else
            ;
        
        if ( mustBeVisible )
            finalPredicate = finalPredicate.and( isVisible );
        if ( mustBeDisplayable )
            finalPredicate = finalPredicate.and( isDisplayable );
    }
}
