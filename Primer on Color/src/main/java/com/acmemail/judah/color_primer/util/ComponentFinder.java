package com.acmemail.judah.color_primer.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class ComponentFinder
{
    private final JFrame    frame;
    
    public ComponentFinder( JFrame frame )
    {
        this.frame = frame;
    }
    
    public JComponent getComponentByName( String name )
    {
        JComponent  component   = getComponentByName( frame, name );
        return component;
    }
    
    public static JComponent globalGetComponentByName( String name )
    {
        JComponent  component   = getJFrames()
            .map( f -> getComponentByName( f, name ) )
            .filter( c -> c != null )
            .findFirst().orElse( null );
        return component;
    }
    
    public static JFrame getJFrameByName( String name )
    {
        Predicate<JFrame>   pred    = f -> name.equals( f.getName() );
        JFrame  frame   = getJFrames()
            .filter( pred::test )
            .findFirst().orElse( null );
        return frame;
    }
    
    public static JComponent getComponentByName( JFrame frame, String name )
    {
        Container   contentPane = frame.getContentPane();
        JComponent  component   = null;
        if ( contentPane instanceof JComponent jComp )
            component = getComponentByName( jComp, name );
        return component;
    }
    
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
    
    public static Stream<JFrame> getJFrames()
    {
        Stream<JFrame>  stream  =
            Arrays.stream( JFrame.getFrames() )
            .filter( f -> f instanceof JFrame )
            .map( f -> (JFrame)f );
        return stream;
    }
}
