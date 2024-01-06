package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.text.html.StyleSheet;

public class StyleSheetFromStringDemo
{
    private static final String sampleCSS   =
        "h1{font-size:125%;}"
        + "p{background-color:#ff0000; color:#ffff00;}"
        + "li{font-family:monospaced;}";
    
    public static void main(String[] args)
    {
        StyleSheet  styleSheet  = getStyleSheetFromString( sampleCSS );
        System.out.println( styleSheet );
    }
    
    private static StyleSheet getStyleSheetFromString( String cssString )
    {
        StyleSheet  styleSheet  = new StyleSheet();
        
        byte[]  buffer  = cssString.getBytes();
        try ( 
            ByteArrayInputStream inStream = 
                new ByteArrayInputStream( buffer );
            InputStreamReader inReader = new InputStreamReader( inStream );
        )
        {
            styleSheet.loadRules( inReader, null );
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        return styleSheet;
    }
}
