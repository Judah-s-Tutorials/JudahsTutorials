package com.gmail.johnstraub1954.weather;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherData
{
    private final Location          location;
    private final BasicData         current;
    private final List<ForecastDay> forecastDays;
    
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
