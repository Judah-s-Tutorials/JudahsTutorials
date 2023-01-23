package com.acmemail.judah.cartesian_plane.sandbox;

import java.lang.reflect.Field;

import com.acmemail.judah.cartesian_plane.CPConstants;

public class ReflectionDemo
{
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
                    String      pNameVal    = (String)field.get(null );
                    System.out.printf( fmt, pName, pNameVal );
                    String      dvName      = 
                        pName.substring( 0, pNameLen - 3 ) + "_DV";
                    try
                    {
                        Field       dvField     = clazz.getField( dvName );
                        String      dvNameVal   =(String)dvField.get(null );
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
