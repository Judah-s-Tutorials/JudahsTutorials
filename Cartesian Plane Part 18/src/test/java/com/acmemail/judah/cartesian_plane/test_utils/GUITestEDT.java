package com.acmemail.judah.cartesian_plane.test_utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.acmemail.judah.cartesian_plane.graphics_utils.GUIUtils;

public class GUITestEDT
{
    public static void 
    setProperty( Consumer<Color> consumer, Color prop )
    {
        Consumer<Object>    objConsumer =
            o -> consumer.accept( (Color)o );
        setProperty( objConsumer, (Object)prop );
    }
    
    public static void 
    setProperty( Consumer<Double> consumer, double prop )
    {
        Consumer<Object>    objConsumer =
            o -> consumer.accept( (Double)o );
        setProperty( objConsumer, (Object)prop );
    }
    
    public static void 
    setProperty( Consumer<Integer> consumer, int prop )
    {
        Consumer<Object>    objConsumer =
            o -> consumer.accept( (Integer)o );
        setProperty( objConsumer, (Object)prop );
    }
    
    public static void 
    setProperty( Consumer<String> consumer, String prop )
    {
        Consumer<Object>    objConsumer =
            o -> consumer.accept( (String)o );
        setProperty( objConsumer, (Object)prop );
    }
    
    public static void 
    setProperty( Consumer<Boolean> consumer, boolean prop )
    {
        Consumer<Object>    objConsumer =
            o -> consumer.accept( (Boolean)o );
        setProperty( objConsumer, (Object)prop );
    }

    public static void 
    setProperty( Consumer<Object> consumer, Object prop )
    {
        GUIUtils.schedEDTAndWait( () -> consumer.accept( prop ) );
    }
    
    public static Color getColor( Supplier<Color> supplier )
    {
        Object  obj = getProperty( supplier );
        assertTrue( obj instanceof Color );
        return (Color)obj;
    }
    
    public static Double getDouble( Supplier<Double> supplier )
    {
        Object  obj = getProperty( supplier );
        assertTrue( obj instanceof Double );
        return (Double)obj;
    }
    
    public static int getInt( Supplier<Integer> supplier )
    {
        Object  obj = getProperty( supplier );
        assertTrue( obj instanceof Integer );
        return (int)obj;
    }
    
    public static Object getProperty( Supplier<?> supplier )
    {
        Object[]    obj = new Object[1];
        GUIUtils.schedEDTAndWait( () -> obj[0] = supplier.get() );
        assertNotNull( obj[0] );
        return obj[0];
    }
}
