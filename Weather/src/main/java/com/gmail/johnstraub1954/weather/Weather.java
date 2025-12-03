package com.gmail.johnstraub1954.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Weather
{
    private static final String apiKey  =   "cb4566e090b84696a7d161318252911";
    private static final String urlHost =   "api.weatherapi.com";
    private static final String urlPath =   "/v1/forecast.json";
    private static final String params  = 
        "key=" + apiKey + "&q=41.4,2.2&days=1";
    private static final String testURL =   
        "https://" + urlHost + urlPath + "?" + params;
    
    public static void main(String[] args)
    {
        Weather app     = new Weather();
        app.exec();
    }
    
    public Weather()
    {
    }
    public void exec()
    {
        List<String> list   = null;
        try
        {
            URI uri                     = new URI( testURL );
            URL url = uri.toURL();
            InputStreamReader inReader  = 
                new InputStreamReader( url.openStream()  );
            BufferedReader bufStream    = new BufferedReader( inReader );
            list = bufStream.lines().toList();
        }
        catch ( URISyntaxException | IOException exc  )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        System.out.println( list.size() );
        String line = list.get( 0 );
        System.out.println( line.length() );
        
        JSONObject  jsonObj     = new JSONObject( line );
        WeatherData data    = new WeatherData( jsonObj );
        System.out.println( data );
        /*
//        System.out.println( line );
        
        JSONObject  jsonLoc     = jsonObj.getJSONObject( "location" );
        JSONObject  jsonCurr    = jsonObj.getJSONObject( "current" );
        Location    location    = new Location( jsonLoc );
        BasicData     current     = new BasicData(  jsonCurr );
        System.out.println( location );
        System.out.println( current );
//        JSONObject  location    = jsonObj.getJSONObject( "location" );
//        JSONObject  current     = jsonObj.getJSONObject( "current" );
//        JSONObject  forecast    = jsonObj.getJSONObject( "forecast" );
//        JSONArray   forecastDay = forecast.getJSONArray( "forecastday" );
//        System.out.println( forecast );
//        
//        String[]    names   = JSONObject.getNames( jsonObj );
//        System.out.println( "array" );
//        for ( String name : names )
//            System.out.println( name );
//        System.out.println( "names" );
//        for ( Object obj : forecastDay )
//            System.out.println( obj.getClass().getSimpleName() );
//        System.out.println( "***************************" );
//        print( jsonObj );
 */
    }
    
    private void print( JSONObject root )
    {
        JSONObject  location    = root.getJSONObject( "location" );
        String      city        = location.getString( "name" );
        String      region      = location.getString( "region" );
        String      country     = location.getString( "country" );
        double      latitude    = location.getDouble( "lat" );
        double      longitude   = location.getDouble( "lon" );
        String      timeZone    = location.getString( "tz_id" );
        long        epoch       = location.getLong( "localtime_epoch" );
        String      time        = location.getString( "localtime" );

        System.out.println( "city:" + city );
        System.out.println( "region:" + region );
        System.out.println( "country:" + country );
        System.out.println( "latitude:" + latitude );
        System.out.println( "longitude:" + longitude );
        System.out.println( "time zone:" + timeZone );
        System.out.println( "epoch:" + epoch );
        System.out.println( "time:" + time );

    }
}
