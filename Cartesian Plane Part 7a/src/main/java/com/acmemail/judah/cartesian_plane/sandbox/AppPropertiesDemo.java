package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.acmemail.judah.cartesian_plane.PropertyManager;

/**
 * This class demonstrates how to 
 * read a properties file 
 * from the project resources directory.
 * 
 * @author Jack Straub
 * 
 * @see UserPropertiesDemo2
 *
 */
public class AppPropertiesDemo
{
    private static final String     appPropertiesName   = "appProperties.ini";
    private static final Properties appProperties       = new Properties();

    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {   
        getAppProperties();
        for ( Object name : appProperties.keySet() )
        {
            Object  val = appProperties.get( name );
            System.out.println( name + ": " + val );
        }
    }
    
    /**
     * Loads the application properties file
     * from the resources directory.
     */
    private static void getAppProperties()
    {
        ClassLoader loader      = AppPropertiesDemo.class.getClassLoader();
        InputStream inStream    = 
            loader.getResourceAsStream( appPropertiesName );
        if ( inStream == null )
        {
            String  msg = "Application properties file \"" 
                + appPropertiesName + "\" not found";
            System.err.println( msg );
        }
        else
        {
            try
            {
                appProperties.load( inStream );
            }
            catch ( IOException exc )
            {
                String  msg = "Error reading application properties file: \""
                    + appPropertiesName + "\"";
                System.err.println( msg );
                System.err.println( exc.getMessage() );
            }
        }
        
        try
        {
            inStream.close();
        }
        catch ( IOException exc )
        {
            String  msg = "Error closing application properties file: \""
                + appPropertiesName + "\"";
            System.err.println( msg );
            System.err.println( exc.getMessage() );
        }
    }
}
