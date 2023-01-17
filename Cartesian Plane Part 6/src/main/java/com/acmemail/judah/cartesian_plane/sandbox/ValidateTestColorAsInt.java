package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Color;

/**
 * Benchest to confirm that we know how
 * to mask the alpha bits
 * out of a Color value.
 * 
 * @author Jack Straub
 *
 */
public class ValidateTestColorAsInt
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        int     cyanInt     = 0x00FFFF;
        int     magentaInt  = 0xFF00FF;
        int     yellowInt   = 0xFFFF00;

        Color   cyan        = new Color( cyanInt );
        Color   magenta     = new Color( magentaInt );
        Color   yellow      = new Color( yellowInt );
        
        testColorAsInt( cyanInt, cyan ); 
        testColorAsInt( magentaInt, magenta ); 
        testColorAsInt( yellowInt, yellow ); 
    }
    
    /**
     * Algorithm for testing an expected color value
     * (without an alpha component)
     * with an actual color value
     * (possibly with an alpha component).
     * 
     * @param expVal    expected color value
     * @param color     actual color
     */
    private static void testColorAsInt( int expVal, Color color )
    {
        int rgb     = color.getRGB();
        int actVal  = rgb & ~0xFF000000;
        
        System.out.printf( "rgb without masking: %08x%n", rgb );
        System.out.printf( "rgb after masking:   %08x%n", actVal );
        System.out.printf( "expected value:      %08x%n", expVal );
        System.out.println( "expected == actual? " + (expVal == actVal) );
        System.out.println( "=============================" );
    }
}
