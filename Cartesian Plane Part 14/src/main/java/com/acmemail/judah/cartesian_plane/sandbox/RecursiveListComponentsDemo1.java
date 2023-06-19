package com.acmemail.judah.cartesian_plane.sandbox;

import com.acmemail.judah.cartesian_plane.input.ItemSelectionDialog;

/**
 * Application to demonstrate
 * recursive traversal 
 * of a window hierarchy.
 * Display a single top-level window
 * and prints out details
 * about the resulting window hierarchy.
 * 
 * @author Jack Straub
 */
public class RecursiveListComponentsDemo1
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     * 
     * @throws InterruptedException
     *      if raised by a Thread operation
     */
    public static void main(String[] args) throws InterruptedException
    {
        String[]    items   = { "Item 1", "Item 2", "Item 3" };
        ItemSelectionDialog dialog  =
            new ItemSelectionDialog( "RecursiveListComponentsDemo1", items );
        Thread      thread  = new Thread( () -> dialog.show() );
        thread.start();
        RecursiveListComponents.dumpWindows();
        thread.join();
        
        System.exit( 0 );
    }
}
