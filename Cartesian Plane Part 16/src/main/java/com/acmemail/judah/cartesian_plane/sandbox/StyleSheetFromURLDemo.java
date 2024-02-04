package com.acmemail.judah.cartesian_plane.sandbox;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.text.html.StyleSheet;

/**
 * Application to demonstrate how to
 * initialize a StyleSheet from a URL.
 * 
 * @author Jack Straub
 */
public class StyleSheetFromURLDemo
{
    /** URL from which to obtain style data. */
    private static final String urlStr  = 
        "https://judahstutorials.com/raw-pages/main.css";
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        StyleSheet  styleSheet  = getStyleSheetFromURL( urlStr );
        System.out.println( styleSheet );
    }
    
    /**
     * Creates a StyleSheet, and initializes it from a given URL.
     * The URL most likely refers to a CSS file.
     * 
     * @param urlStr    the given URL
     * 
     * @return  the initialized StyleSheet
     */
    private static StyleSheet getStyleSheetFromURL( String urlStr )
    {
        StyleSheet  styleSheet  = new StyleSheet();
        try
        {
            URL         url         = new URL( urlStr );
            styleSheet.importStyleSheet( url );
        }
        catch ( MalformedURLException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        return styleSheet;
    }
}
