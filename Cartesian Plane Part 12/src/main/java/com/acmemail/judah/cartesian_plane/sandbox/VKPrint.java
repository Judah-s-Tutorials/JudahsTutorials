package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VKPrint
{
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
    
    private static class VKMap implements Comparable<VKMap>
    {
        final String    name;
        final int       val;
        
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
        public int compareTo( VKMap o )
        {
            int rcode   = val - o.val;
            return rcode;
        }
    }
}
