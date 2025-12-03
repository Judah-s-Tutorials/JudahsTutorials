package com.gmail.johnstraub1954.weather;

import org.json.JSONObject;

public class BasicData
{
    private final long      epoch;
    private final String    lastUpdated;
    private final double    celsius;
    private final double    fahrenheit;
    private final boolean   isDay;          // show day or night icon
    private final Condition condition;
    private final double    windMPH;
    private final double    windKPH;
    private final int       windDegree;     // Wind Direction in degrees
    private final String    windDir;        // Wind Direction compass point
    private final double    pressureMBs;
    private final double    pressureInches;
    private final double    precipMillis;
    private final double    precipInches;
    private final int       humidity;
    private final int       cloudCover;     // percent
    private final double    feelsLikeC;
    private final double    feelsLikeF;
    private final double    windChillC;
    private final double    windChillF;
    private final double    heatIndexC;
    private final double    heatIndexF;
    private final double    dewPointC;
    private final double    dewPointF;
    private final double    visibilityKM;
    private final double    visibilityMiles;
    private final double    uvIndex;
    private final double    gustKPH;
    private final double    gustMPH;
    private final double    shortRad;   // short-range radar
    private final double    diffRad;    // doppler radar
    private final double    dni;        // Direct Normal Irradiance
    private final double    gti;        // Geopotential Height
    
    public BasicData( JSONObject current )
    {
        JSONObject  conditionObj    = current.getJSONObject( "condition" );
        
        int isDayInt    = current.getInt( "is_day" );
        epoch = current.getLong( "last_updated_epoch" );
        lastUpdated = current.getString( "last_updated" );
        celsius = current.getDouble( "temp_c" );
        fahrenheit = current.getDouble( "temp_f" );
        isDay = isDayInt != 0;
        condition = new Condition( conditionObj );
        windMPH = current.getDouble( "wind_mph" );
        windKPH = current.getDouble( "wind_kph" );
        windDegree = current.getInt( "wind_degree" );
        windDir = current.getString( "wind_dir" );
        pressureMBs = current.getDouble( "pressure_mb" );
        pressureInches = current.getDouble( "pressure_in" );
        precipMillis = current.getDouble( "precip_mm" );
        precipInches = current.getDouble( "precip_in" );
        humidity = current.getInt( "humidity" );
        cloudCover = current.getInt( "cloud" );
        feelsLikeC = current.getDouble( "feelslike_c" );
        feelsLikeF = current.getDouble( "feelslike_f" );
        windChillC = current.getDouble( "windchill_c" );
        windChillF = current.getDouble( "windchill_f" );
        heatIndexC = current.getDouble( "heatindex_c" );
        heatIndexF = current.getDouble( "heatindex_f" );
        dewPointC = current.getDouble( "dewpoint_c" );
        dewPointF = current.getDouble( "dewpoint_f" );
        visibilityKM = current.getDouble( "vis_km" );
        visibilityMiles = current.getDouble( "vis_miles" );
        uvIndex = current.getDouble( "uv" );
        gustKPH = current.getDouble( "gust_kph" );
        gustMPH = current.getDouble( "gust_mph" );
        shortRad = current.getDouble( "short_rad" );
        diffRad = current.getDouble( "diff_rad" );
        dni = current.getDouble( "dni" );
        gti = current.getDouble( "gti" );
    }

    @Override
    public String toString()
    {
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( "epoch=" ).append( epoch ).append( "," )
            .append( "lastUpdated=" ).append( lastUpdated ).append( "," )
            .append( "celsius=" ).append( celsius ).append( "," )
            .append( "fahrenheit=" ).append( fahrenheit ).append( "," )
            .append( "isDay=" ).append( isDay ).append( "," )
            .append( "condition={" ).append( condition ).append( "}," )
            .append( "windKPH=" ).append( windKPH ).append( "," )
            .append( "windMPH=" ).append( windMPH ).append( "," )
            .append( "windDegree=" ).append( windDegree ).append( "," )
            .append( "windDir=" ).append( windDir ).append( "," )
            .append( "pressureMBs=" ).append( pressureMBs ).append( "," )
            .append( "pressureInches=" ).append( pressureInches ).append( "," )
            .append( "precipMillis=" ).append( precipMillis ).append( "," )
            .append( "precipInches=" ).append( precipInches ).append( "," )
            .append( "humidity=" ).append( humidity ).append( "," )
            .append( "cloudCover=" ).append( cloudCover ).append( "%," )
            .append( "feelsLikeC=" ).append( feelsLikeC ).append( "," )
            .append( "feelsLikeF=" ).append( feelsLikeF ).append( "," )
            .append( "windChillC=" ).append( windChillC ).append( "," )
            .append( "windChillF=" ).append( windChillF ).append( "," )
            .append( "heatIndexC=" ).append( heatIndexC ).append( "," )
            .append( "heatIndexF=" ).append( heatIndexF ).append( "," )
            .append( "dewPointC=" ).append( dewPointC ).append( "," )
            .append( "dewPointF=" ).append( dewPointF ).append( "," )
            .append( "visibilityKM=" ).append( visibilityKM ).append( "," )
            .append( "visibilityMiles=" ).append( visibilityMiles ).append( "," )
            .append( "uvIndex=" ).append( uvIndex ).append( "," )
            .append( "gustKPH=" ).append( gustKPH ).append( "," )
            .append( "gustMPH=" ).append( gustMPH ).append( "," )
            .append( "shortRad=" ).append( shortRad ).append( "," )
            .append( "diffRad=" ).append( diffRad ).append( "," )
            .append( "dni=" ).append( dni ).append( "," )
            .append( "gti=" ).append( gti );
        return bldr.toString();
    }
    
    
}
