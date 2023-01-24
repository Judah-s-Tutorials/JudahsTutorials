package com.acmemail.judah.cartesian_plane.sandbox;

import java.lang.reflect.Field;

import com.acmemail.judah.cartesian_plane.CPConstants;

/**
 * This application demonstrates how to find all the property name
 * constants and their associated default values
 * in the CPConstants class.
 * <p>
 * We start by assuming that the name of a constant field
 * containing a property name
 * has a suffix of <em>_PN</em>,
 * and the name of the constant field containing
 * the default value for that property
 * is the same as the property name constant
 * but with a suffix of <em>_DV</em>.
 * So...
 * </p>
 * <p>
 * ... for each constant field <em>xxx</em>_PN
 * locate the corresponding field <em>xxx</em>_DV
 * and print them out.
 * </p>
 * 
 * @author Jack Straub
 */
public class ReflectionCPConstantsDemo
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        String              fmt     = "%s: %s = ";
        Class<CPConstants>  clazz   = CPConstants.class;
        Field[]             fields  = clazz.getFields();
        try
        {
            for ( Field field : fields )
            {
                String  pName    = field.getName();
                if ( pName.endsWith( "_PN" ) )
                {
                    int         pNameLen    = pName.length();
                    String      pNameVal    = (String)field.get( null );
                    System.out.printf( fmt, pName, pNameVal );
                    String      dvName      = 
                        pName.substring( 0, pNameLen - 3 ) + "_DV";
                    try
                    {
                        Field       dvField     = clazz.getField( dvName );
                        String      dvNameVal   =(String)dvField.get( null );
                        System.out.println( dvNameVal );
                    }
                    catch ( NoSuchFieldException exc )
                    {
                        System.out.println( "\"" + dvName + "\" not found" );
                    }
                }
            }
        }
        catch ( IllegalAccessException exc )
        {
            exc.printStackTrace();
        }
    }
}
