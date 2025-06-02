package com.gmail.johnstraub1954.penrose.utils;

import java.awt.Color;
import java.util.HashMap;

public class ColorMap extends HashMap<Integer,Color>
{
    /**
     * Default serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     * Does nothing interesting.
     */
    public ColorMap()
    {
    }
    
    /**
     * Convenience constructor
     * for initializing a map
     * with four key/value pairs.
     * 
     * @param key1  first key
     * @param val1  first value
     * @param key2  second key
     * @param val2  second value
     * @param key3  third key
     * @param val3  third value
     * @param key4  fourth key
     * @param val4  fourth value
     */
    public ColorMap(
        Integer key1,
        Color   val1,
        Integer key2,
        Color   val2,
        Integer key3,
        Color   val3,
        Integer key4,
        Color   val4
    )
    {
        put( key1, val1 );
        put( key2, val2 );
        put( key3, val3 );
        put( key4, val4 );
    }
}
