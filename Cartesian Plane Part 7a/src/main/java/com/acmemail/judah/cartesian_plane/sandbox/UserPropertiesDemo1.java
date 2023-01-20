package com.acmemail.judah.cartesian_plane.sandbox;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.acmemail.judah.cartesian_plane.CPConstants;

public class UserPropertiesDemo1
{
    public static void main(String[] args)
    {
        Properties  props       = new Properties();
        String      propName    = CPConstants.USER_PROPERTIES_PN;
        String      propFiles   = System.getProperty( propName );
        if ( propFiles == null )
            System.err.println( "Cant't get " + propName );
        else
        {
            FileInputStream inStream    = null;
            try
            {
                inStream = new FileInputStream( propName );
                props.load( inStream );
            }
            catch ( IOException exc )
            {
                System.err.println( "Open file error: " + exc.getMessage() );
                System.exit( 1 );
            }
            finally
            {
                try
                {
                    inStream.close();
                }
                catch ( IOException exc )
                {
                    System.err.println( "Close file error: " + exc.getMessage() );
                    System.exit( 1 );
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
