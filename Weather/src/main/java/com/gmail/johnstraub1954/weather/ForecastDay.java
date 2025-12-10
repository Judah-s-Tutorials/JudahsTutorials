package com.gmail.johnstraub1954.weather;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ForecastDay
{
    private final String        date;
    private final long          epoch;
    private final Day           day;
    private final Astro         astro;
    private final List<Hour>    hours;
    
    public ForecastDay( JSONObject obj )
    {
        hours = new ArrayList<>();
        date = obj.getString( "date" );
        epoch = obj.getLong( "date_epoch" );
        
        JSONObject  jsonDay     = obj.getJSONObject( "day" );
        JSONObject  jsonAstro   = obj.getJSONObject( "astro" );
        day = new Day( jsonDay );
        astro = new Astro( jsonAstro );
        
        JSONArray   jsonHours   = obj.getJSONArray( "hour" );
        int         numHours    = jsonHours.length();
        for ( int inx = 0 ; inx < numHours ; ++inx )
        {
            JSONObject  jsonHour    = jsonHours.getJSONObject( inx );
            Hour        hour        = new Hour( jsonHour );
            hours.add( hour );
        }
    }
    
    /**
     * @return the date
     */
    public String getDate()
    {
        return date;
    }

    /**
     * @return the epoch
     */
    public long getEpoch()
    {
        return epoch;
    }

    /**
     * @return the day
     */
    public Day getDay()
    {
        return day;
    }

    /**
     * @return the astro
     */
    public Astro getAstro()
    {
        return astro;
    }

    /**
     * @return the hours
     */
    public List<Hour> getHours()
    {
        return hours;
    }
    
    public Hour getHour( int index ) throws IndexOutOfBoundsException
    {
        Hour    hour    = hours.get( index );
        return hour;
    }

    public String toString()
    {
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( "date=" ).append( date ).append( "," );
        bldr.append( "epoch=" ).append( epoch ).append( "," );
        bldr.append( "day={" ).append( day ).append( "}," );
        bldr.append( "astro={" ).append( astro ).append( "}" );
        bldr.append( "hours=[" );
        for ( Hour hour : hours )
        {
            bldr.append( "{" ).append( hour ).append( "}," );
        }
        bldr.deleteCharAt( bldr.length() - 1 );
        
        bldr.append( "]" );
        return bldr.toString();
    }
}
