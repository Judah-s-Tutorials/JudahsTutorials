package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.text.html.StyleSheet;

public class StyleSheetFromURLDemo
{
    private static final String urlStr  = 
        "https://judahstutorials.com/raw-pages/main.css";
    
    public static void main(String[] args)
    {
        StyleSheet  styleSheet  = getStyleSheetFromURL( urlStr );
        System.out.println( styleSheet );
    }
    
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
