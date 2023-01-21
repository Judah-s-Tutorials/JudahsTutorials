package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.acmemail.judah.cartesian_plane.CPConstants;

/**
 * This class demonstrates 
 * how to load a properties file
 * from a given path.
 * using the "try with resources" strategy.
 * 
 * @author Jack Straub
 *
 * @see UserPropertiesDemo2
 * @see AppPropertiesDemo
 */
public class UserPropertiesDemo2
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        Properties  props       = new Properties();
        
        // Get the path to the user's properties file.
        String      propName    = CPConstants.USER_PROPERTIES_PN;
        String      propFile    = System.getProperty( propName );
        if ( propFile == null || propFile.isEmpty() )
            System.err.println( "Cant't get " + propName );
        else
        {
            System.out.println( "Attempting to load " + propFile );
            // IMPORTANT! the inStream variable MUST be declared
            // INSIDE the parentheses.
            try ( FileInputStream inStream = new FileInputStream( propFile ) )
            {
                // Because inStream is declared within the parentheses
                // following try, inStream.close() will be invoked
                // regardless of whether an exception is thrown.
                props.load( inStream );
            }
            catch ( IOException exc )
            {
                System.err.println( "Open file error: " + exc.getMessage() );
            }
            for ( Object name : props.keySet() )
            {
                Object  val = props.get( name );
                System.out.println( name + ": " + val );
            }
        }
    }
}
