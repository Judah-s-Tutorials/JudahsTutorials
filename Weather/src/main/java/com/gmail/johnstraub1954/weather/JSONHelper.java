package com.gmail.johnstraub1954.weather;

import java.util.function.BiFunction;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONHelper
{
    private final JSONObject    data;
    
    public JSONHelper( String strData )
    {
        this.data = new JSONObject( strData );
    }
    
    public JSONHelper( JSONObject data )
    {
        this.data = data;
    }
    
    public Object getData( BiFunction<JSONObject,String,Object> func, String key, String def )
    {
        Object  result  = null;
        try
        {
            result = func.apply( data, key );
        }
        catch ( JSONException exc )
        {
            result = def;
        }
        return result;
    }
    
    public String getString( String key )
    {
        String  result  = getString( key, "" );
        return result;
    }
    
    public String getString( String key, String def )
    {
        String  result  = null;
        try
        {
            result = data.getString( key );
        }
        catch ( JSONException exc )
        {
            result = def;
        }
        return result;
    }
    
    public long getLong( String key )
    {
        long    result = getLong( key, -1 );
        return result;
    }
    
    public long getLong( String key, long def )
    {
        long    result  = 0;
        try
        {
            result = data.getLong( key );
        }
        catch ( JSONException exc )
        {
            result = def;
        }
        return result;
    }
    
    public int getInt( String key )
    {
        int    result = getInt( key, -1 );
        return result;
    }
    
    public int getInt( String key, int def )
    {
        int result  = 0;
        try
        {
            result = data.getInt( key );
        }
        catch ( JSONException exc )
        {
            result = def;
        }
        return result;
    }
    
    public boolean getBoolean( String key )
    {
        boolean result = getBoolean( key, false );
        return result;
    }
    
    public boolean getBoolean( String key, boolean def )
    {
        boolean result  = false;
        try
        {
            String  str = data.getString( key );
            result = str != null && !str.equals( "0" );
        }
        catch ( JSONException exc )
        {
            result = def;
        }
        return result;
    }
}
