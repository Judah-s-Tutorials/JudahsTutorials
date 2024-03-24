package com.acmemail.judah.cartesian_plane.sandbox;

import javax.swing.UIManager;

public class UIManagerKeyDemo
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        System.out.println( "Begin" );
        UIManager.getDefaults().keySet().stream()
            .filter( k -> k.toString().contains( "Formatted") )
            .forEach( System.out::println );
    }
}
