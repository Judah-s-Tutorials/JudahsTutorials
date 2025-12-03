package com.gmail.johnstraub1954.weather;

import org.json.JSONObject;

public class Hour
{
    private final long      timeEpoch;
    private final String    time;
    private final double    centigrade;
    private final double    fahrenheit;
    private final boolean   isDay;          // show day or night icon
    private final Condition condition;
    private final double    windMPH;
    private final double    windKPH;
    private final int       windDegree;     // Wind Direction in degrees
    private final String    windDir;        // Wind Direction compass point
    private final double    pressureMBs;
    private final double    pressureInches;
    private final double    precipMMs;
    private final double    precipInches;
    private final double    snowCM;
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
    private final boolean   willItRain;
    private final int       chanceOfRain;
    private final boolean   willItSnow;
    private final int       chanceOfSnow;
    private final double    visibilityKM;
    private final double    visibilityMiles;
    private final double    gustMPH;
    private final double    gustKPH;
    private final double    uvIndex;
    private final double    shortRad;   // short-range radar
    private final double    diffRad;    // doppler radar
    private final double    dni;        // Direct Normal Irradiance
    private final double    gti;        // Geopotential Height
    
    
    public Hour( JSONObject hour )
    {
        JSONObject  conditionObj    = hour.getJSONObject( "condition" );
        
        int isDayInt    = hour.getInt( "is_day" );
        timeEpoch = hour.getLong( "time_epoch" );
        time = hour.getString( "time" );
        centigrade = hour.getDouble( "temp_c" );
        fahrenheit = hour.getDouble( "temp_f" );
        isDay = isDayInt != 0;
        condition = new Condition( conditionObj );
        windMPH = hour.getDouble( "wind_mph" );
        windKPH = hour.getDouble( "wind_kph" );
        windDegree = hour.getInt( "wind_degree" );
        windDir = hour.getString( "wind_dir" );
        pressureMBs = hour.getDouble( "pressure_mb" );
        pressureInches = hour.getDouble( "pressure_in" );
        precipMMs = hour.getDouble( "precip_mm" );
        precipInches = hour.getDouble( "precip_in" );
        snowCM = hour.getDouble( "snow_cm" );
        humidity = hour.getInt( "humidity" );
        cloudCover = hour.getInt( "cloud" );
        feelsLikeC = hour.getDouble( "feelslike_c" );
        feelsLikeF = hour.getDouble( "feelslike_f" );
        windChillC = hour.getDouble( "windchill_c" );
        windChillF = hour.getDouble( "windchill_f" );
        heatIndexC = hour.getDouble( "heatindex_c" );
        heatIndexF = hour.getDouble( "heatindex_f" );
        dewPointC = hour.getDouble( "dewpoint_c" );
        dewPointF = hour.getDouble( "dewpoint_f" );
        willItRain = getBoolean( hour, "will_it_rain" );
        chanceOfRain = hour.getInt( "chance_of_rain" );
        willItSnow = getBoolean( hour, "will_it_snow" );
        chanceOfSnow = hour.getInt( "chance_of_snow" );
        visibilityKM = hour.getDouble( "vis_km" );
        visibilityMiles = hour.getDouble( "vis_miles" );
        gustKPH = hour.getDouble( "gust_kph" );
        gustMPH = hour.getDouble( "gust_mph" );
        uvIndex = hour.getDouble( "uv" );
        shortRad = hour.getDouble( "short_rad" );
        diffRad = hour.getDouble( "diff_rad" );
        dni = hour.getDouble( "dni" );
        gti = hour.getDouble( "gti" );
    }

    @Override
    public String toString()
    {
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( "timeEpoch=" ).append( timeEpoch ).append( "," )
            .append( "time=" ).append( time ).append( "," )
            .append( "centigrade=" ).append( centigrade ).append( "," )
            .append( "fahrenheit=" ).append( fahrenheit ).append( "," )
            .append( "isDay=" ).append( isDay ).append( "," )
            .append( "condition={" ).append( condition ).append( "}," )
            .append( "windKPH=" ).append( windKPH ).append( "," )
            .append( "windMPH=" ).append( windMPH ).append( "," )
            .append( "windDegree=" ).append( windDegree ).append( "," )
            .append( "windDir=" ).append( windDir ).append( "," )
            .append( "pressureMBs=" ).append( pressureMBs ).append( "," )
            .append( "pressureInches=" ).append( pressureInches ).append( "," )
            .append( "precipMMs=" ).append( precipMMs ).append( "," )
            .append( "precipInches=" ).append( precipInches ).append( "," )
            .append( "snowCM=" ).append( snowCM ).append( "," )
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
            .append( "willItRain=" ).append( willItRain ).append( "," )
            .append( "chanceOfRain=" ).append( chanceOfRain ).append( "," )
            .append( "willItSnow=" ).append( willItSnow ).append( "," )
            .append( "chanceOfSnow=" ).append( chanceOfSnow ).append( "," )
            .append( "visibilityKM=" ).append( visibilityKM ).append( "," )
            .append( "visibilityMiles=" ).append( visibilityMiles ).append( "," )
            .append( "gustKPH=" ).append( gustKPH ).append( "," )
            .append( "gustMPH=" ).append( gustMPH ).append( "," )
            .append( "uvIndex=" ).append( uvIndex ).append( "," )
            .append( "shortRad=" ).append( shortRad ).append( "," )
            .append( "diffRad=" ).append( diffRad ).append( "," )
            .append( "dni=" ).append( dni ).append( "," )
            .append( "gti=" ).append( gti );
        return bldr.toString();
    }
    
    private static boolean getBoolean( JSONObject obj, String key )
    {
        int     asInt       = obj.getInt( key );
        boolean asBoolean   = !(asInt == 0);
        return asBoolean;
    }

}
