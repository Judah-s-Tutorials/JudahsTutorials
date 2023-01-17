package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.acmemail.judah.cartesian_plane.PropertyManager;

public class AsResourceDemo
{
    private static final String         appPropertiesName   = 
        "appProperties.ini";
    private static final Properties     appProperties   = new Properties();

    public static void main(String[] args)
    {   
        getAppProperties();
        appProperties.forEach( (k, v) -> System.out.println( k + " = " + v ) );
        System.out.println( "size = " + appProperties.keySet().size() );
    }
    private static void getAppProperties()
    {
        Class<?>    clazz       = AsResourceDemo.class;
        ClassLoader loader      = clazz.getClassLoader();
        InputStream inStream    = 
            loader.getResourceAsStream( appPropertiesName );
        if ( inStream == null )
        {
            String  msg = "System properties file \"" 
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
                String  msg = "Error reading system properties file: \""
                    + appPropertiesName + "\"";
                System.err.println( msg );
                System.err.println( exc.getMessage() );
            }
        }
        
        if ( inStream != null )
        {
            try
            {
                inStream.close();
            }
            catch ( IOException exc )
            {
                String  msg = "Error closing system properties file: \""
                    + appPropertiesName + "\"";
                System.err.println( msg );
                System.err.println( exc.getMessage() );
            }
        }
    }

}
