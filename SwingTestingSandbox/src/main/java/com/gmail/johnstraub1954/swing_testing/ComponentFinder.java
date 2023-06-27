package com.gmail.johnstraub1954.swing_testing;

import java.awt.Container;
import java.awt.Window;
import java.util.Arrays;
import java.util.function.Predicate;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class ComponentFinder
{
    public static Window findWindow( Predicate<Window> pred )
    {
        Window  window  = Arrays.stream( Window.getWindows() )
            .filter( w -> pred.test( w ) )
            .findFirst()
            .orElse( null );
        return window;
    }
    
    public static JComponent find( Predicate<JComponent> pred )
    {
        JComponent  comp    = Arrays.stream( Window.getWindows() )
            .map( ComponentFinder::getContentPane )
            .filter( c -> c != null )
            .map( c -> find( c, pred) )
            .filter( c -> c != null )
            .findFirst()
            .orElse( null );
        return comp;
    }
    
    public static JComponent 
    find( JComponent comp, Predicate<JComponent> pred )
    {
        JComponent  result  = null;
        if ( pred.test( comp ) )
            result = comp;
        else
            result = Arrays.stream( comp.getComponents() )
                .filter( c -> (c instanceof JComponent) )
                .map( c -> (JComponent)c )
                .map( c -> find( c, pred ) )
                .filter( c -> c != null )
                .findFirst()    
                .orElse( null );
        return result;
    }
    
    private static JComponent getContentPane( Window window )
    {
        Container   container   = null;
        if ( window instanceof JFrame )
            container = ((JFrame)window).getContentPane();
        else if ( window instanceof JDialog )
            container = ((JDialog)window).getContentPane();
        else 
            container = null;
        
        JComponent  comp    = null;
        if ( container != null && container instanceof JComponent )
            comp = (JComponent)container;
        
        return comp;
    }
}
