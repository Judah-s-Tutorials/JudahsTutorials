package com.acmemail.judah.sandbox;

import java.awt.Color;
import java.util.HashMap;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

@SuppressWarnings("serial")
public class PropertyManager extends HashMap<String,Object>
{
    public static final String  AXES        = "axis";
    public static final String  MAJOR       = "major";
    public static final String  MINOR       = "minor";
    public static final String  GRID        = "grid";
    
    public static final String  STROKE      = "stroke";
    public static final String  LENGTH      = "length";
    public static final String  SPACING     = "spacing";
    public static final String  COLOR       = "color";
    
    private static PropertyManager   instance    = null;
    
    public static synchronized PropertyManager instanceOf()
    {
        if ( instance == null )
            instance = new PropertyManager();
        return instance;
    }
    
    private PropertyManager()
    {
        put( AXES, STROKE, 7.0 );
        put( AXES, COLOR, Color.RED );
        
        put( MAJOR, STROKE, 5.0 );
        put( MAJOR, LENGTH, 11.0 );
        put( MAJOR, SPACING, 10.0 );
        put( MAJOR, COLOR, Color.BLUE );
        
        put( MINOR, STROKE, 3.0 );
        put( MINOR, LENGTH, 5.0 );
        put( MINOR, SPACING, 5.0 );
        put( MINOR, COLOR, Color.CYAN );
        
        put( GRID, STROKE, 1.0 );
        put( GRID, COLOR, Color.GREEN );
    }
    
    public Optional<Object> get( String major, String minor )
    {
        Optional<Object>    result  = Optional.empty();
        String              key     = major + "." + minor;
        Object              val     = get( key );
        if ( val != null )
            result = Optional.of( val );
        return result;
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
