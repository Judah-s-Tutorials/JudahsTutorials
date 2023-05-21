package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Prints the names and values
 * of all VK_... class variables
 * in the KeyEvent class.
 * 
 * @author Jack Straub
 */
public class VKPrint
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments
     * 
     * @throws IllegalAccessException
     *      if an attempt to access a field
     *      results in an exception
     */
    public static void main(String[] args)
        throws IllegalAccessException
    {
        List<VKMap>     list        = new ArrayList<>();
        Class<KeyEvent> vkClass     = KeyEvent.class;
        Field[]         fields      = vkClass.getFields();
        for ( Field field : fields )
        {
            String  name    = field.getName();
            if ( name.startsWith( "VK_" ) )
                list.add( new VKMap( field.getName(), field.getInt( null ) ) );
        }
        Collections.sort( list );
        list.forEach( System.out::println );
    }
    
    /**
     * Encapsulates the correspondence
     * between a variable name
     * to its value.
     * 
     * @author Jack Straub
     */
    private static class VKMap implements Comparable<VKMap>
    {
        final String    name;
        final int       val;
        
        /**
         * Constructor.
         * Establishes the name and value
         * of a variable.
         * 
         * @param name  the name of the encapsulated variable
         * @param val   the value of the encapsulated variable
         */
        public VKMap( String name, int val )
        {
            this.name = name;
            this.val = val;
        }
        
        @Override
        public String toString()
        {
            String  out = String.format( "%04x: %s", val, name );
            return out;
        }

        @Override
        public int compareTo( VKMap other )
        {
            int rcode   = val - other.val;
            return rcode;
        }
    }
}
