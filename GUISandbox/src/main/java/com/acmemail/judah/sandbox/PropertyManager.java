package com.acmemail.judah.sandbox;

import java.awt.Color;
import java.util.HashMap;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

@SuppressWarnings("serial")
public class PropertyManager extends HashMap<String,Object>
{
    public static final String  AXIS        = "axis";
    public static final String  MAJOR       = "major";
    public static final String  MINOR       = "minor";
    public static final String  GRID        = "grid";
    
    public static final String  STROKE      = "stroke";
    public static final String  LENGTH      = "length";
    public static final String  SPACING     = "spacing";
    public static final String  COLOR       = "color";
    
    public PropertyManager()
    {
        put( AXIS, STROKE, 5 );
        put( AXIS, COLOR, Color.BLACK );
        
        put( MAJOR, STROKE, 5 );
        put( MAJOR, LENGTH, 11 );
        put( MAJOR, SPACING, 10 );
        put( MAJOR, COLOR, Color.BLACK );
        
        put( MINOR, STROKE, 3 );
        put( MINOR, LENGTH, 5 );
        put( MINOR, SPACING, 5 );
        put( MINOR, COLOR, Color.BLACK );
        
        put( GRID, STROKE, 5 );
        put( GRID, COLOR, Color.LIGHT_GRAY );
    }
    
    public OptionalInt getAsInt( String major, String minor )
    {
        OptionalInt     result  = OptionalInt.empty();
        String          key     = major + "." + minor;
        Object          val     = get( key );
        if ( val != null && (val instanceof Integer) )
            result = OptionalInt.of( (Integer)val );
        return result;
    }
    
    public OptionalDouble getAsDouble( String major, String minor )
    {
        OptionalDouble  result  = OptionalDouble.empty();
        String          key     = major + "." + minor;
        Object          val     = get( key );
        if ( val != null && (val instanceof Double) )
            result = OptionalDouble.of( (Double)val );
        return result;
    }
    
    public Optional<Color> getAsColor( String major, String minor )
    {
        Optional<Color> result  = Optional.empty();
        String          key     = major + "." + minor;
        Object          val     = get( key );
        if ( val != null && (val instanceof Color) )
            result = Optional.of( (Color)val );
        return result;
    }
    
    public void put( String major, String minor, int val )
    {
        String  key = major + "." + minor;
        put( key, val );
    }
    
    public void put( String major, String minor, Color val )
    {
        String  key = major + "." + minor;
        put( key, val );
    }
    
    public void put( String major, String minor, double val )
    {
        String  key = major + "." + minor;
        put( key, val );
    }
}
