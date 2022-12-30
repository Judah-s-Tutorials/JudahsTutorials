package com.acmemail.judah.cartesian_plane.app;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;

/**
 * This class encapsulates logic to instantiate
 * and display a CartesianPlane.
 *
 * @author Jack Straub
 */
public class Main
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        CartesianPlane   canvas  = new CartesianPlane();
        Root            root    = new Root( canvas );
        root.start();
    }
}
