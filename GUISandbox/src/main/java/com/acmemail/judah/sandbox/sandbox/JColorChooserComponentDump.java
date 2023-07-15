package com.acmemail.judah.sandbox.sandbox;

import java.awt.Color;

import javax.swing.JColorChooser;

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
public class JColorChooserComponentDump
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
        Thread      thread  = 
            new Thread( () -> JColorChooser.showDialog( null, "Title", Color.RED) );
        thread.start();
        Thread.sleep( 500 );
        RecursiveListComponents.dumpWindows();
        thread.join();
        
        System.exit( 0 );
    }
}
