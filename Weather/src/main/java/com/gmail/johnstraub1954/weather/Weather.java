package com.gmail.johnstraub1954.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.swing.JOptionPane;

import org.json.JSONObject;

public class Weather
{
    private static final String apiKey      =   "cb4566e090b84696a7d161318252911";
    private static final String urlHost     =   "api.weatherapi.com";
    private static final String urlPath     =   "/v1/forecast.json";
    
    private WeatherData data;
    
    public static void main(String[] args)
    {
        Weather app     = new Weather();
        app.exec();
        System.exit( 0 );
    }
    
    public Weather()
    {
    }
    
    public void exec()
    {
        String  location    = Input.askLocation();
        String  strData     = null;
        if ( location == null )
            ;
        else if ( (strData = getData( location, 6 ) ) == null )
            ;
        else
        {
            System.out.println( strData.length() );
            
            JSONObject  jsonObj = new JSONObject( strData );
            data    = new WeatherData( jsonObj );
            String      title   = data.getCity() + ", " + data.getCountry();
            System.out.println( strData );
            
            WeatherDialog   dialog  = 
                WeatherDialog.of( null, title, data );
            dialog.show();
        }
    }
    
    private static String getData( String location, int days )
    {
        String  encodedLocation = 
            URLEncoder.encode( location, StandardCharsets.UTF_8 );
        String  params          = 
            "key=" + apiKey + "&q=" + encodedLocation + "&days=" + days;
        String  urlStr          = "https://" + urlHost + urlPath + "?" + params;
        String  data            = null;
        try
        {
            System.out.println( urlStr );
            URI uri = new URI( urlStr );
            URL url = uri.toURL();
            InputStreamReader inReader  = 
                new InputStreamReader( url.openStream()  );
            BufferedReader bufStream    = new BufferedReader( inReader );
            data = bufStream.readLine();
        }
        catch ( URISyntaxException | IOException exc  )
        {
            exc.printStackTrace();
            String  title   = "Unable to get data for " + location;
            JOptionPane.showMessageDialog( 
                null, 
                exc.getMessage(), 
                title, 
                JOptionPane.ERROR_MESSAGE
            );
        }
        return data;
    }
    
    private static String getTimeZone( String location, int days )
    {
        String  encodedLocation = 
            URLEncoder.encode( location, StandardCharsets.UTF_8 );
        String  params          = 
            "key=" + apiKey + "&q=" + encodedLocation + "&days=" + days;
        String  urlStr          = "https://" + urlHost + urlPath + "?" + params;
        String  data            = null;
        try
        {
            System.out.println( urlStr );
            URI uri = new URI( urlStr );
            URL url = uri.toURL();
            InputStreamReader inReader  = 
                new InputStreamReader( url.openStream()  );
            BufferedReader bufStream    = new BufferedReader( inReader );
            data = bufStream.readLine();
        }
        catch ( URISyntaxException | IOException exc  )
        {
            exc.printStackTrace();
            String  title   = "Unable to get data for " + location;
            JOptionPane.showMessageDialog( 
                null, 
                exc.getMessage(), 
                title, 
                JOptionPane.ERROR_MESSAGE
            );
        }
        return data;
    }
}
