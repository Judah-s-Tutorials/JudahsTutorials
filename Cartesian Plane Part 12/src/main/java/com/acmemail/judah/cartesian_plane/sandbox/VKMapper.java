package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VKMapper
{
    private static final String fmt = "%-12s  %02x  %c%n";
    private static final Map<Integer,String>    vkMap   = new HashMap<>();
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
