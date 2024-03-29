package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.acmemail.judah.cartesian_plane.CPConstants;

/**
 * This class demonstrates 
 * how to load a properties file
 * from a given path.
 * The strategy employed
 * uses the most traditional
 * IO error processing.
 * There's another example,
 * {@linkplain UserPropertiesDemo2},
 * that uses the "try with resources" strategy.
 * 
 * @author Jack Straub
 *
 * @see UserPropertiesDemo2
 */
public class UserPropertiesDemo1
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
            // This variable needs to be declared before the try block
            // because its scope must extend to the finally block.
            FileInputStream inStream    = null;
            try
            {
                // Once opened we must remember to eventually close the
                // input stream. This is done in the finally block which
                // is executed regardless of whether an exception is thrown.
                inStream = new FileInputStream( propFile );
                props.load( inStream );
            }
            catch ( IOException exc )
            {
                System.err.println( "Open file error: " + exc.getMessage() );
            }
            finally
            {
                // Annoyingly, closing a stream might also cause an
                // IOException, which must be caught.
                try
                {
                    if ( inStream != null )
                        inStream.close();
                }
                catch ( IOException exc )
                {
                    System.err.println( "Close file error: " + exc.getMessage() );
                }
            }
            for ( Object name : props.keySet() )
            {
                Object  val = props.get( name );
                System.out.println( name + ": " + val );
            }
        }
    }
}
