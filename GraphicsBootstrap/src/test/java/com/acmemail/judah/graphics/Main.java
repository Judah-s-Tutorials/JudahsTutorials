package com.acmemail.judah.graphics;

/**
 * This class contains a main method
 * that instantiates a Canvas object
 * and displays it in a Root window.
 * 
 * @author Jack Straub
 *
 */
public class Main
{
    /**
     * Creates a Canvas object
     * and displays it in a Root.
     * 
     * @param args	command-line arguments; not used.
     */
    public static void main(String[] args)
    {
        Canvas  canvas  = new Canvas( 400, 500 );
        Root    root    = new Root( canvas );
        root.start();
    }
}
