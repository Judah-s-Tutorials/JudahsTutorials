package com.acmemail.judah.cartesian_plane.sandbox.profile;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.acmemail.judah.cartesian_plane.components.LinePropertySet;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetAxes;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetGridLines;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMajor;
import com.acmemail.judah.cartesian_plane.components.LinePropertySetTicMinor;
import com.acmemail.judah.cartesian_plane.graphics_utils.ComponentException;

public class Profile
{
    private final Map<String,LinePropertySet>   linePropertySetMap  =
        new HashMap<>();
    
    private void  initLinePropertySetMap()
    {
        Map<String,LinePropertySet> map = new HashMap<>();
        Stream.of(
            LinePropertySetAxes.class,
            LinePropertySetGridLines.class,
            LinePropertySetTicMajor.class,
            LinePropertySetTicMinor.class
        ).forEach( this::putClass );
    }
    
    public Profile()
    {
        initLinePropertySetMap();
    }

    private void putClass( Class<? extends LinePropertySet> clazz )
    {
        try
        {
            String          name    = clazz.getSimpleName();
            LinePropertySet set     = 
                clazz.getDeclaredConstructor().newInstance();
            linePropertySetMap.put( name, set );
        }
        catch ( 
            NoSuchMethodException | 
            SecurityException |
            InvocationTargetException |
            IllegalAccessException |
            InstantiationException exc
        )
        {
            String  msg =
                "Could not instantiate " + clazz.getSimpleName();
            throw new ComponentException( msg );
        }
    }
}
