package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.Color;

/**
 * This is an application that demonstrates
 * how to remove the alpha bits 
 * from an integer color value.
 * 
 * @author Jack Straub
 */
public class SuppressAlphaBitsDemo
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        // Encode magenta as an integer.
        int     magentaInt  = 0xFF00FF;
        printInt( "magenta", magentaInt, true );
        
        // Create the alpha mask; 8 high order bits set, all other bits 0.
        int     alphaMask   = 0xFF000000;
        printInt( "alphaMask", alphaMask, false );
        
        // Calculate the complement of the alpha mask.
        printInt( "~alphaMask", ~alphaMask, false );
        
        // Get a color object for magenta
        Color   colorVal    = new Color( magentaInt );
        
        // Get the integer that encodes magenta in colorVal.
        int     rgb         = colorVal.getRGB();        
        printInt( "rgb with alpha", rgb, false );
        
        // Suppress the high order 8 bits of the integer encoded in colorVal.
        int     rgbNoAlpha  = rgb & ~alphaMask;
        printInt( "rgb without alpha", rgbNoAlpha, false );
    }
    
    /**
     * Print the value of a given integer 
     * in decimal, hexadecimal and binary.
     * Precede the integer values with a descriptive note.
     * Optionally print a header.
     * 
     * @param note      descriptive note     
     * @param iVal      given integer
     * @param header    true to print a header
     */
    private static void printInt( String note, int iVal, boolean header )
    {
        final String    headerFmt   = "%18s%11s %8s %32s%n";
        if ( header )
        {
            System.out.printf( headerFmt, " ", "Decimal", "Hex", "Binary" );
            System.out.printf( 
                headerFmt, 
                " ", 
                "===========", 
                "========", 
                "================================" 
            );
        }
            
        final String    fmt     = "%-18s%011d %08x %s%n";
        String  iValBin = Integer.toBinaryString( iVal );
        for ( int inx = iValBin.length() ; inx < 32 ; ++inx )
            iValBin = "0" + iValBin;
        System.out.printf( fmt, note, iVal, iVal, iValBin );
    }
}
