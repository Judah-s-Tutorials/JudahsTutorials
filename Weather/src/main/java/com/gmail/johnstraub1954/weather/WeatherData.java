package com.gmail.johnstraub1954.weather;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherData
{
    private final Location          location;
    private final BasicData         current;
    private final List<ForecastDay> forecastDays;
    private final Astro             astro;
    
    public WeatherData( JSONObject data )
    {
        location = new Location( data.getJSONObject( "location" ) );
        current = new BasicData( data.getJSONObject( "current" ) );
        forecastDays = new ArrayList<>();
        
        JSONObject  forecast    = data.getJSONObject( "forecast" );
        JSONArray   jsonDays    = forecast.getJSONArray( "forecastday" );
        int         numDays     = jsonDays.length();
        for ( int inx = 0 ; inx < numDays ; ++inx )
        {
            JSONObject  jsonDay = jsonDays.getJSONObject( inx );
            ForecastDay day     = new ForecastDay( jsonDay );
            forecastDays.add( day );
        }
        astro = forecastDays.get( 0 ).getAstro();
    }
    
    /**
     * @return the location
     */
    public Location getLocation()
    {
        return location;
    }

    /**
     * @return the current
     */
    public BasicData getCurrent()
    {
        return current;
    }

    /**
     * @return the forecastDays
     */
    public List<ForecastDay> getForecastDays()
    {
        return forecastDays;
    }

    /**
     * @return the forecastDays
     */
    public ForecastDay getForecastDay( int index )
        throws IndexOutOfBoundsException
    {
        ForecastDay forecastDay = forecastDays.get( index );
        return forecastDay;
    }
    
    /**
     * Convenience method to get the Astro object
     * from the ForecastDay object
     * at the given index
     * of the ForecastDays list..
     * 
     * @param index the given index
     * 
     * @return the ForecastDay object at the given index
     */
    public Astro getAstro( int index ) throws IndexOutOfBoundsException
    {
        ForecastDay forecastDay = getForecastDay( index );
        Astro       astro       = forecastDay.getAstro();
        return astro;
    }
    
    /**
     * Convenience routine to get the city name
     * from the Location object.
     * 
     * @return  the city name from the Location object
     */
    public String getCity()
    {
        String  city    = getLocation().getCity();
        return city;
    }
    
    /**
     * Convenience routine to get the region name
     * from the Location object.
     * 
     * @return  the reegion name from the Location object
     */
    public String getRegion()
    {
        String  region  = getLocation().getRegion();
        return region;
    }
    
    /**
     * Convenience routine to get the country name
     * from the Location object.
     * 
     * @return  the country name from the Location object
     */
    public String getCountry()
    {
        String  country = getLocation().getCountry();
        return country;
    }
    
    /**
     * Convenience routine to get the latitude
     * from the Location object.
     * 
     * @return  the latitude from the Location object
     */
    public double getLatitude()
    {
        double  latitude    = getLocation().getLatitude();
        return latitude;
    }
    
    /**
     * Convenience routine to get the longitude
     * from the Location object.
     * 
     * @return  the longitude from the Location object
     */
    public double getLongitude()
    {
        double  longitude    = getLocation().getLongitude();
        return longitude;
    }
    
    /**
     * Convenience routine to get the time zone
     * from the Location object.
     * This data does not include
     * the UTC offset.
     * 
     * @return  the time zone from the Location object
     */
    public String getTimeZone()
    {
        String  timeZone    = getLocation().getTimeZone();
        return timeZone;
    }

    /**
     * Convenience method to get
     * the current temperature in Fahrenheit.
     *
     * @return the current temperature in Fahrenheit
     */
    public double getCurrentTempF()
    {
        double  temp    = getCurrent().getFahrenheit();
        return temp;
    }
    
    /**
     * Convenience method to get
     * the expected max temperature in Celsius.
     *
     * @return the current temperature in Celsius
     */
    public double getCurrentMaxTempC()
    {
        ForecastDay today   = getForecastDay( 0 );
        double      temp    = today.getDay().getMaxtempC();
        return temp;
    }
    
    
    /**
     * Convenience method to get
     * the expected max temperature in Fahrenheit.
     *
     * @return the current temperature in Fahrenheit
     */
    public double getCurrentMaxTempF()
    {
        ForecastDay today   = getForecastDay( 0 );
        double      temp    = today.getDay().getMaxtempF();
        return temp;
    }
    
    /**
     * Convenience method to get
     * the expected min temperature in Celsius.
     *
     * @return the current temperature in Celsius
     */
    public double getCurrentMinTempC()
    {
        ForecastDay today   = getForecastDay( 0 );
        double      temp    = today.getDay().getMintempC();
        return temp;
    }
    
    
    /**
     * Convenience method to get
     * the expected min temperature in Fahrenheit.
     *
     * @return the current temperature in Fahrenheit
     */
    public double getCurrentMinTempF()
    {
        ForecastDay today   = getForecastDay( 0 );
        double      temp    = today.getDay().getMintempF();
        return temp;
    }
    
    /**
     * Convenience method to get
     * the current temperature in Celsius.
     *
     * @return the current temperature in Celsius
     */
    public double getCurrentTempC()
    {
        double  temp    = getCurrent().getCelsius();
        return temp;
    }
    
    /**
     * Convenience method to get
     * the time from the current object.
     *
     * @return the current time
     */
    public String getCurrentTime()
    {
        String  time    = current.getLastUpdated();
        return time;
    }
    
    /**
     * Convenience method to get
     * the icon from the current object.
     *
     * @return the current temperature in Celsius
     */
    public Icon getCurrentIcon()
    {
        Icon    icon    = getCurrent().getCondition().getIcon();
        return icon;
    }
    
    /**
     * Convenience method to get
     * the sunrise from the current forecastday/astro object.
     *
     * @return the current forecastday sunrise
     */
    public String getSunrise()
    {
        String  sunrise = astro.getSunrise();
        return sunrise;
    }
    
    /**
     * Convenience method to get
     * the sunset from the current forecastday/astro object.
     *
     * @return the current forecastday sunset
     */
    public String getSunset()
    {
        String  sunset = astro.getSunset();
        return sunset;
    }
    
    /**
     * Convenience method to get
     * the moonrise from the current forecastday/astro object.
     *
     * @return the current forecastday moonrise
     */
    public String getMoonrise()
    {
        String  moonrise    = astro.getMoonrise();
        return moonrise;
    }
    
    /**
     * Convenience method to get
     * the sunrise from the current forecastday/astro object.
     *
     * @return the current forecastday sunrise
     */
    public String getMoonset()
    {
        String  moonset = astro.getMoonset();
        return moonset;
    }
    
    /**
     * Convenience method to get
     * the moon phase from the current forecastday/astro object.
     *
     * @return the current forecastday sunrise
     */
    public String getMoonPhase()
    {
        String  moonPhase   = astro.getMoonPhase();
        return moonPhase;
    }
    
    /**
     * Convenience method to get
     * the moon percent illumination
     * from the current forecastday/astro object.
     *
     * @return the current forecastday sunrise
     */
    public int getMoonIllumination()
    {
        int  moonIllumination   = astro.getMoonIllumination();
        return moonIllumination;
    }
    
    /**
     * Convenience method to get
     * the percent chance of rain
     * from the current forecastday/day object.
     *
     * @return the chance of rain for today
     */
    public int getCurrentRainChance()
    {
        Day today   = getForecastDay( 0 ).getDay();
        int chance  = today.getDailyChanceOfRain();
        return chance;
    }
    
    /**
     * Convenience method to get
     * the percent chance of snow
     * from the current forecastday/day object.
     *
     * @return the chance of snow for today
     */
    public int getCurrentSnowChance()
    {
        Day today   = getForecastDay( 0 ).getDay();
        int chance  = today.getDailyChanceOfSnow();
        return chance;
    }
    
    /**
     * Convenience method to get
     * the current condition text
     * from the current object.
     *
     * @return the current condition text
     */
    public String getCurrentConditionText()
    {
        String  text    = getCurrent().getCondition().getText();
        return text;
    }
    
    /**
     * Convenience method to get
     * the current precipitation in millimeters
     * from the current object.
     *
     * @return the current precipitation in millimeters
     */
    public double getCurrentPrecipMillis()
    {
        double  precip  = getCurrent().getPrecipMillis();
        return precip;
    }
    
    /**
     * Convenience method to get
     * the current precipitation in millimeters
     * from the current object.
     *
     * @return the current precipitation in millimeters
     */
    public double getCurrentPrecipInches()
    {
        double  precip  = getCurrent().getPrecipInches();
        return precip;
    }

    public String toString()
    {
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( "location={" ).append( location ).append( "}," );
        bldr.append( "current={" ).append( current ).append( "}," );
        bldr.append( "forecastDays=[" );
        
        for ( ForecastDay forcastDay : forecastDays )
        {
            bldr.append( "{" ).append( forcastDay ).append( "}," );
        }
        bldr.deleteCharAt( bldr.length() - 1 );
        
        bldr.append( "]" );
        return bldr.toString();
    }
}
