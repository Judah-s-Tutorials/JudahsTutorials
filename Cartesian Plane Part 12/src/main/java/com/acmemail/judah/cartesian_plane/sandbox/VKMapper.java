package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Prints the names and values of key codes
 * for the most common keyboard characters.
 * 
 * @author Jack Straub
 */
public class VKMapper
{
    private static final String fmt = "%-12s  %02x  %c%n";
    private static final Map<Integer,String>    vkMap   = new HashMap<>();
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        initMap();
        List<Integer>   keyList = new ArrayList<>( vkMap.keySet() );
        keyList.sort( Integer::compareTo );
        keyList.stream()
            .filter( i -> i >= '+' )
            .filter( i -> i <= 'Z' )
            .forEach( i -> System.out.printf( fmt, vkMap.get( i ), i, i ) );
    }

    /**
     * Initializes a map of all VK_... variables
     * in the KeyEvent class,
     * where the value of the variable
     * is the key
     * and the name of the variable
     * is the value.
     */
    private static void initMap()
    {
        Class<KeyEvent> vkClass = KeyEvent.class;
        Field[]         fields  = vkClass.getFields();
        Arrays.stream( fields )
            .filter( f -> f.getName().startsWith( "VK" ) )
            .forEach( f -> vkMap.put( getInt( f ), f.getName() ) );
    }
    
    /**
     * Do the work of Field.getInt(Object),
     * which throws an exception
     * so can't be used in a functional interface.
     * 
     * @param   field to interrogate
     * 
     * @return value returned by getInt method
     */
    private static int getInt( Field field )
    {
        int val = 0;
        try 
        {
            val = field.getInt( null );
        }
        catch ( IllegalAccessException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        return val;
    }
}
