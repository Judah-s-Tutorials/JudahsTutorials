package com.gmail.johnstraub1954.weather;

import org.json.JSONObject;

public class Location
{
    private final String      city;
    private final String      region;
    private final String      country;
    private final double      latitude;
    private final double      longitude;
    private final String      timeZone;
    private final long        epoch;
    private final String      time;

    public Location( JSONObject location )
    {
        city        = location.getString( "name" );
        region      = location.getString( "region" );
        country     = location.getString( "country" );
        latitude    = location.getDouble( "lat" );
        longitude   = location.getDouble( "lon" );
        timeZone    = location.getString( "tz_id" );
        epoch       = location.getLong( "localtime_epoch" );
        time        = location.getString( "localtime" );
    }
    
    /**
     * @return the city
     */
    public String getCity()
    {
        return city;
    }

    /**
     * @return the region
     */
    public String getRegion()
    {
        return region;
    }

    /**
     * @return the country
     */
    public String getCountry()
    {
        return country;
    }

    /**
     * @return the latitude
     */
    public double getLatitude()
    {
        return latitude;
    }

    /**
     * @return the longitude
     */
    public double getLongitude()
    {
        return longitude;
    }

    /**
     * @return the timeZone
     */
    public String getTimeZone()
    {
        return timeZone;
    }

    /**
     * @return the epoch
     */
    public long getEpoch()
    {
        return epoch;
    }

    /**
     * @return the time
     */
    public String getTime()
    {
        return time;
    }

    public String toString()
    {
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( "city=" ).append( city ).append( ";" );
        bldr.append( "region=" ).append( region ).append( ";" );
        bldr.append( "country=" ).append( country ).append( ";" );
        bldr.append( "latitude=" ).append( latitude ).append( ";" );
        bldr.append( "longitude=" ).append( longitude ).append( ";" );
        bldr.append( "timeZone=" ).append( timeZone ).append( ";" );
        bldr.append( "epoch=" ).append( epoch ).append( ";" );
        bldr.append( "time=" ).append( time );
        
        return bldr.toString();
    }
}
