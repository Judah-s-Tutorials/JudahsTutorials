package com.acmemail.judah.color_primer.util;

import java.awt.Component;
import java.awt.Container;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * This class is under development.
 * It searches an application's GUI component hierarchy
 * for JComponents matching some criteria,
 * having a given name, for example.
 * An instance of this class is tied to a specific JFrame,
 * but there are many class methods
 * that can be used to perform more flexible searches.
 * <p>
 * At the moment functionality is restricted
 * to JFrames and JComponents.
 * JDialogs must be supported in the near future.
 */
public class ComponentFinder
{
    /** The JFrame tied to an instance of this class. */
    private final JFrame    frame;
    
    public ComponentFinder( JFrame frame )
    {
        this.frame = frame;
    }
    
    /**
     * Search the encapsulated JFrame for a JComponent
     * with a given name.
     * 
     * @param name  the given name
     * 
     * @return  the target JComponent, or null if not found
     */
    public JComponent getComponentByName( String name )
    {
        JComponent  component   = getComponentByName( frame, name );
        return component;
    }
    
    /**
     * Searches all open JFrames and JDialogs
     * for a JComponent with the given name.
     * 
     * @param name  the given name
     * 
     * @return    the target JComponent, or null if not found
     */
    public static JComponent globalGetComponentByName( String name )
    {
        JComponent  component   = getJFrames()
            .map( f -> getComponentByName( f, name ) )
            .filter( c -> c != null )
            .findFirst().orElse( null );
        return component;
    }
    
    /**
     * Searches for a JFrame with the given name.
     * 
     * @param name  the given name
     * 
     * @return  the target JFram, or null if not found
     */
    public static JFrame getJFrameByName( String name )
    {
        Predicate<JFrame>   pred    = f -> name.equals( f.getName() );
        JFrame  frame   = getJFrames()
            .filter( pred::test )
            .findFirst().orElse( null );
        return frame;
    }
    
    /**
     * Search the given JFrame for a JComponent with the given name.
     * 
     * @param frame the given JFrame
     * @param name  the given name
     * 
     * @return  the target JComponent, or null if not found
     */
    public static JComponent getComponentByName( JFrame frame, String name )
    {
        Container   contentPane = frame.getContentPane();
        JComponent  component   = null;
        if ( contentPane instanceof JComponent jComp )
            component = getComponentByName( jComp, name );
        return component;
    }
    
    /**
     * Search the hierarchy of a given JComponent for a JComponent
     * with the given name.
     * 
     * @param jComp the given JComponent
     * @param name  the given name
     * 
     * @return  the target JComponent, or null if not found
     */
    public static JComponent 
    getComponentByName( JComponent jComp, String name )
    {
        Objects.requireNonNull( jComp, "jComp" );
        Objects.requireNonNull( name, "name" );
        
        // when testing equality of name and c.getName,
        // use "name.equals" because c.getName coule return null.
        final Predicate<JComponent> pred    =
            c -> c != null && name.equals( c.getName() );
        JComponent  result  = getComponent( jComp, pred );
        return result;
    }
    
    /**
     * Search the hierarchy of a given JComponent for a JComponent
     * that satisfies the given predicate.
     * 
     * @param jComp the given JComponent
     * @param pred  the given predicate
     * 
     * @return  the target JComponent, or null if not found
     */
    public static JComponent 
    getComponent( JComponent jComp, Predicate<JComponent> pred )
    {
        JComponent  result  = null;
        if ( pred.test( jComp ) )
            result = jComp;
        else
        {
            Component[] children    = jComp.getComponents();
            int         len         = children.length;
            for ( int inx = 0 ; inx < len && result == null ; ++inx )
            {
                if ( children[inx] instanceof JComponent nextJComp )
                {
                    result = getComponent( nextJComp, pred );
                }
            }
        }
        return result;
    }
    
    /**
     * Get a stream of all JFrames in an application.
     * 
     * @return  a stream of all JFrames in an application
     */
    public static Stream<JFrame> getJFrames()
    {
        Stream<JFrame>  stream  =
            Arrays.stream( JFrame.getFrames() )
            .filter( w -> w.isDisplayable() )
            .filter( f -> f instanceof JFrame )
            .map( f -> (JFrame)f );
        return stream;
    }
}
