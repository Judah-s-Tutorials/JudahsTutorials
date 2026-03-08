package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Color;

/**
 * This class demonstrates that
 * the value encoded in a Color object
 * is not necessarily 
 * the value you used to instantiate the object.
 * The reason for this
 * is that you specify the value
 * of the lower-order three byte
 * of a (4 byte) <em>int,</em>
 * but then Java tweaks the value
 * of the fourth byte.
 * 
 * @author Jack Straub
 *
 * @see <a href="https://judahstutorials.blogspot.com/p/java-color-primer.html">
 *     Color Primer
 * </a>
 */
public class GetRGBDemo
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        int     colorValIn  = 0xFF00FF;
        Color   color       = new Color( colorValIn );
        int     colorValOut = color.getRGB();
        
        String  fmt         = "color in:  %08X%ncolor out: %08X%n";
        System.out.printf( fmt, colorValIn, colorValOut );
    }
    
    // output:
    // color in:  00FF00FF
    // color out: FFFF00FF
}
