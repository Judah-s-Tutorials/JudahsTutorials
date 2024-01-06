package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.text.html.StyleSheet;

import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;

/**
 * Demonstrates how to populate a StyleSheet
 * by reading a resource.
 * 
 * @author Jack Straub
 */
public class StyleSheetFromResourceDemo
{
    public static void main(String[] args)
    {
        StyleSheet  styleSheet  = 
            getStyleSheetFromResource( "SandboxDocs/Jabberwocky.css" );
        System.out.println( styleSheet );
    }
    
    private static StyleSheet getStyleSheetFromResource( String resource )
    {
        StyleSheet  styleSheet  = new StyleSheet();
        try ( 
            InputStream inStream = getResourceAsStream( resource );
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
    
    private static InputStream getResourceAsStream( String resource )
    {
        ClassLoader loader      = PropertyManager.class.getClassLoader();
        InputStream inStream    = loader.getResourceAsStream( resource );
        if ( inStream == null )
        {
            String  msg = "Resource file \"" 
                + resource + "\" not found";
            System.err.println( msg );
            throw new ComponentException( msg );
        }
        return inStream;
    }
}
