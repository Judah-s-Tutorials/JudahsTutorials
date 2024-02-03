package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Component;
import java.util.stream.Stream;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * This is a brief program that demonstrates how to traverse
 * the containment hierarchy of a JMenu.
 * 
 * @author Jack Straub
 */
public class MenuTraversalDemo
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        JMenu   subMenu1    = new JMenu( "Upload..." );
        subMenu1.add( new JMenuItem( "http" ) );
        subMenu1.add( new JMenuItem( "ftp" ) );
        
        JMenu   subMenu2    = new JMenu( "Save As..." );
        subMenu2.add( new JMenuItem( ".gif" ) );
        subMenu2.add( new JMenuItem( ".png" ) );
        subMenu2.add( new JMenuItem( ".jpg" ) );

        JMenu   menu   = new JMenu( "File" );
        menu.add( new JMenuItem( "Open") );
        menu.add( subMenu1 );
        menu.add( new JMenuItem( "Save") );
        menu.add( subMenu2 );
        menu.add( new JMenuItem( "Exit") );
        
        dumpMenu( menu, "" );
    }

    /**
     * Recursively traverse a given JMenu hierarchy,
     * printing the text of each JMenuItem.
     * 
     * @param menu      the given JMenu
     * @param indent
     *      number of spaces to indent prior to printing the 
     *      JMenuItem text; increased by four spaces with each
     *      recursive invocation
     */
    private static void dumpMenu( JMenu menu, String indent )
    {
        System.out.println( indent + menu.getText() );
        String  nextIndent  = indent + "    ";
        
        Component[] comps   = menu.getMenuComponents();
        Stream.of( comps )
            .filter( c -> (c instanceof JMenuItem) )
            .map( c -> (JMenuItem)c )
            .forEach( jc -> {
                if ( jc instanceof JMenu )
                    dumpMenu( (JMenu)jc, nextIndent );
                else
                    System.out.println( nextIndent + jc.getText() );
            });
    }
}