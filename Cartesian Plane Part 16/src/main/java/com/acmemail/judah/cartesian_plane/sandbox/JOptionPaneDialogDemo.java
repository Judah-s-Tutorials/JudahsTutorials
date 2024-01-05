package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

import com.acmemail.judah.cartesian_plane.PropertyManager;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;

public class JOptionPaneDialogDemo
{
    /** System line separator. */
    private static final String newLine     = System.lineSeparator();
    
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        String  text    = getTextFromResource( "MenuBarDocs/gpl-3.0.txt" );
        JOptionPane.showMessageDialog( null, text );
    }

    /**
     * Concatenate the lines from a given resource
     * into a single string, separated by 
     * platform-appropriate line separators.
     * 
     * @param resource  the given resource
     * 
     * @return  the compiled string
     */
    private static String getTextFromResource( String resource )
    {
        String       text        = null;
        try (
            InputStream inStream = getInputStreamFromResource( resource );
            InputStreamReader reader = new InputStreamReader( inStream );
            BufferedReader bufReader = new BufferedReader( reader );
        )
        {
            StringBuilder    bldr    = new StringBuilder();
            bufReader.lines()
                .map( bldr::append )
                .forEach( b -> b.append( newLine ) );
            text = bldr.toString();
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            throw new ComponentException( exc );
        }
        return text;
    }
    
    /**
     * Obtain an InputStream with a given resource as a source.
     * 
     * @param resource  the given resource
     * 
     * @return  the InputStream obtained from the given resource.
     */
    private static InputStream getInputStreamFromResource( String resource )
    {
        ClassLoader loader      = PropertyManager.class.getClassLoader();
        InputStream inStream    = loader.getResourceAsStream( resource );
        if ( inStream == null )
        {
            String  msg = "Resource file \"" 
                + resource + "\" not found";
            throw new ComponentException( msg );
        }
        return inStream;
    }
}
