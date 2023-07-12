package com.acmemail.judah.sandbox.test_utils;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;

import com.acmemail.judah.sandbox.PropertyManager;

public class PropertiesShadow
{
    private static final String[]   lineProperties  =
    {
        PropertyManager.STROKE,
        PropertyManager.LENGTH,
        PropertyManager.SPACING,
        PropertyManager.COLOR
    };
    private final PropertyManager   pMgr    = PropertyManager.instanceOf();
    
    private final Map<String, Optional<Object>> propMap = new HashMap<>();
    private OptionalDouble  stroke;
    private OptionalDouble  length;
    private OptionalDouble  spacing;
    private Optional<Color> color;

    public PropertiesShadow()
    {
        // TODO Auto-generated constructor stub
    }
    
//    private void putLineProps( String category )
//    {
//        Arrays.stream( lineProperties )
//            .map( pMgr.get)
//    }
//    
//    private void put( String major, String minor, Optional<?> val )
//    {
//        String  key = major + "." + minor;
//        propMap.put( key, val );
//    }

}
