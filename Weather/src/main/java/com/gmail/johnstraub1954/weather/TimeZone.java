package com.gmail.johnstraub1954.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import javax.swing.JOptionPane;

import org.json.JSONObject;

public class TimeZone
{
    private static final String apiKey      = "VNS12YSM9CHM";
    private static final String urlHost     = "api.timezonedb.com";
    private static final String tzPath      = "/v2.1/get-time-zone";
    
    private final String        responseStatus;
    private final String        responseMessage;
    private final String        countryCode;
    private final String        countryName;
    private final String        regionName;
    private final String        cityName;
    private final String        zoneName;
    private final String        zoneAbbreviation;
    private final int           offsetSeconds;
    private final boolean       dst;
    private final long          zoneStart;
    private final long          zoneEnd;
    private final String        nextZoneAbbreviation;
    private final LocalDateTime timeStamp;
    
    public static void main( String[] args )
    {
        TimeZone    zone    = TimeZone.of( 48.858890, 2.320041 );
        System.out.println( zone );
    }
    
    public static TimeZone of( WeatherData weatherData )
    {
        double      latitude    = weatherData.getLatitude();
        double      longitude   = weatherData.getLongitude();
        TimeZone    timeZone    = of( latitude, longitude );
        return timeZone;
    }
    
    public static TimeZone of( double latitude, double longitude )
    {
        String  params          = 
            "key=" + apiKey 
            + "&format=json"
            + "&by=position"
            + "&lat=" + latitude
            + "&lng=" + longitude;
        String  urlStr          = 
            "https://" + urlHost + tzPath + "?" + params;
        String  data            = null;
        URL     url             = null;
        try
        {
            System.out.println( urlStr );
            URI uri = new URI( urlStr );
            url = uri.toURL();
            HttpURLConnection   conn = 
                (HttpURLConnection)url.openConnection();
            conn.setRequestMethod( "GET" );
            int status = conn.getResponseCode();
            if ( status != HttpURLConnection.HTTP_OK ) {
                throw new IOException( "HTTP error code: " + status );
            }
        }
        catch ( URISyntaxException | IOException exc  )
        {
            reportError( exc, params );
        }
        try ( 
            InputStream inStream = url.openStream(); 
            InputStreamReader inReader = new InputStreamReader( inStream  );
        )
        {
            BufferedReader  bufStream   = new BufferedReader( inReader );
            StringBuilder   bldr        = new StringBuilder();
            String line;

            while ((line = bufStream.readLine()) != null) {
                bldr.append(line);
            }
            data = bldr.toString();
            System.out.println( data );
        }
        catch ( IOException exc  )
        {
            reportError( exc, params );
        }
        
        TimeZone    timeZone    = new TimeZone( data );
        return timeZone;
    }
    
    private TimeZone( String strData )
    {
        JSONObject  data    = new JSONObject( strData );
        JSONHelper  helper  = new JSONHelper( data );
        responseStatus = helper.getString( "status" );
        responseMessage = helper.getString( "status" );
        countryCode = helper.getString( "countryCode" );
        countryName = helper.getString( "countryName" );
        regionName = helper.getString( "regionName" );
        cityName = helper.getString( "cityName" );
        zoneName = helper.getString( "zoneName" );
        zoneAbbreviation = helper.getString( "abbreviation" );
        offsetSeconds = helper.getInt( "gmtOffset" );
        dst = helper.getBoolean( "dst" );
        zoneStart = helper.getLong( "zoneStart", -1 );
        zoneEnd = helper.getLong( "zoneEnd", -1 );
        nextZoneAbbreviation = helper.getString( "nextAbbreviation" );
        timeStamp = getEpoch( data, "timestamp" );
    }
    
    /**
     * @return the apikey
     */
    public static String getApikey()
    {
        return apiKey;
    }

    /**
     * @return the urlhost
     */
    public static String getUrlhost()
    {
        return urlHost;
    }

    /**
     * @return the tzpath
     */
    public static String getTzpath()
    {
        return tzPath;
    }

    /**
     * @return the responseStatus
     */
    public String getResponseStatus()
    {
        return responseStatus;
    }

    /**
     * @return the responseMessage
     */
    public String getResponseMessage()
    {
        return responseMessage;
    }

    /**
     * @return the countryCode
     */
    public String getCountryCode()
    {
        return countryCode;
    }

    /**
     * @return the countryName
     */
    public String getCountryName()
    {
        return countryName;
    }

    /**
     * @return the regionName
     */
    public String getRegionName()
    {
        return regionName;
    }

    /**
     * @return the cityName
     */
    public String getCityName()
    {
        return cityName;
    }

    /**
     * @return the zoneName
     */
    public String getZoneName()
    {
        return zoneName;
    }

    /**
     * @return the zoneAbbreviation
     */
    public String getZoneAbbreviation()
    {
        return zoneAbbreviation;
    }

    /**
     * @return the offsetSeconds
     */
    public int getOffsetSeconds()
    {
        return offsetSeconds;
    }

    /**
     * @return the dst
     */
    public boolean isDst()
    {
        return dst;
    }

    /**
     * @return the zoneStart
     */
    public long getZoneStart()
    {
        return zoneStart;
    }

    /**
     * @return the zoneEnd
     */
    public long getZoneEnd()
    {
        return zoneEnd;
    }

    /**
     * @return the nextZoneAbbreviation
     */
    public String getNextZoneAbbreviation()
    {
        return nextZoneAbbreviation;
    }

    /**
     * @return the timeStamp
     */
    public LocalDateTime getTimeStamp()
    {
        return timeStamp;
    }

    public String toString()
    {
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( "responseStatus=" ).append( responseStatus )
                .append( "," )
            .append( "responseMessage=" ).append( responseMessage )
                .append( "," )
            .append( "countryCode=" ).append( countryCode ).append( "," )
            .append( "countryName=" ).append( countryName ).append( "," )
            .append( "regionName=" ).append( regionName ).append( "," )
            .append( "cityName=" ).append( cityName ).append( "," )
            .append( "zoneName=" ).append( zoneName ).append( "," )
            .append( "zoneAbbreviation=" ).append( zoneAbbreviation )
                .append( "," )
            .append( "offsetSeconds=" ).append( offsetSeconds ).append( "," )
            .append( "dst=" ).append( dst ).append( "," )
            .append( "zoneStart=" ).append( zoneStart ).append( "," )
            .append( "zoneEnd=" ).append( zoneEnd ).append( "," )
            .append( "nextZoneAbbreviation=" ).append( nextZoneAbbreviation )
                .append( "," )
            .append( "timeStamp={").append( timeStamp ).append( timeStamp )
                .append( "}" );
        return bldr.toString();
    }
    
    private static LocalDateTime getEpoch( JSONObject data, String key )
    {
        long            epoch       = data.getLong( key );
        LocalDateTime   dateTime    = 
            LocalDateTime.ofEpochSecond( epoch, 0, ZoneOffset.UTC );
        return dateTime;
    }
    
    private static void reportError( Exception exc, String location )
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
}
