package com.gmail.johnstraub1954.weather;

import org.json.JSONObject;

public class Day
{
    private final double    maxtempC;
    private final double    maxtempF;
    private final double    mintempC;
    private final double    mintempF;
    private final double    avgtempC;
    private final double    avgtempF;
    private final double    maxwindMPH;
    private final double    maxwindKPH;
    private final double    totalprecipMMs;
    private final double    totalprecipInches;
    private final double    totalsnowCM;
    private final double    avgvisKMs;
    private final double    avgvisMiles;
    private final int       avgHumidity;
    private final boolean   dailyWillItRain;
    private final int       dailyChanceOfRain;
    private final boolean   dailyWillItSnow;
    private final int       dailyChanceOfSnow;
    private final Condition condition;
    private final double    uvIndex;
    
    public Day( JSONObject day )
    {
        maxtempC = day.getInt( "maxtemp_c" );
        maxtempF = day.getInt( "maxtemp_f" );
        mintempC = day.getInt( "mintemp_c" );
        mintempF = day.getInt( "mintemp_f" );
        avgtempC = day.getInt( "avgtemp_c" );
        avgtempF = day.getInt( "avgtemp_f" );
        maxwindKPH = day.getDouble( "maxwind_kph" );
        maxwindMPH = day.getDouble( "maxwind_mph" );
        totalprecipMMs = day.getDouble( "totalprecip_mm" );
        totalprecipInches = day.getDouble( "totalprecip_in" );
        totalsnowCM = day.getDouble( "totalsnow_cm" );
        avgvisKMs = day.getDouble( "avgvis_km" );
        avgvisMiles = day.getDouble( "avgvis_miles" );
        avgHumidity = day.getInt( "avghumidity" );
        dailyWillItRain = getBoolean( day, "daily_will_it_rain" );
        dailyChanceOfRain = day.getInt( "daily_chance_of_rain" );
        dailyWillItSnow = getBoolean( day, "daily_will_it_snow" );
        dailyChanceOfSnow = day.getInt( "daily_chance_of_snow" );
        uvIndex = day.getDouble( "uv" );
        JSONObject  jsonCondition   = day.getJSONObject( "condition" );
        condition = new Condition( jsonCondition );
    }
    
    public String toString()
    {
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( "maxtempC=" ).append( maxtempC ).append( "," );
        bldr.append( "maxtempF=" ).append( maxtempF ).append( "," );
        bldr.append( "maxtempC=" ).append( maxtempC ).append( "," );
        bldr.append( "mintempC=" ).append( mintempC ).append( "," );
        bldr.append( "mintempF=" ).append( mintempF ).append( "," );
        bldr.append( "avgtempC=" ).append( avgtempC ).append( "," );
        bldr.append( "avgtempF=" ).append( avgtempF ).append( "," );
        bldr.append( "maxwindMPH=" ).append( maxwindMPH ).append( "," );
        bldr.append( "maxwindKPH=" ).append( maxwindKPH ).append( "," );
        bldr.append( "totalprecipMMs=" ).append( totalprecipMMs ).append( "," );
        bldr.append( "totalprecipInches=" ).append( totalprecipInches ).append( "," );
        bldr.append( "totalsnowCM=" ).append( totalsnowCM ).append( "," );
        bldr.append( "avgvisKMs=" ).append( avgvisKMs ).append( "," );
        bldr.append( "avgvisMiles=" ).append( avgvisMiles ).append( "," );
        bldr.append( "avgHumidity=" ).append( avgHumidity ).append( "," );
        bldr.append( "dailyWillItRain=" ).append( dailyWillItRain ).append( "," );
        bldr.append( "dailyChanceOfRain=" ).append( dailyChanceOfRain ).append( "," );
        bldr.append( "dailyWillItSnow=" ).append( dailyWillItSnow ).append( "," );
        bldr.append( "dailyChanceOfSnow=" ).append( dailyChanceOfSnow ).append( "," );
        bldr.append( "condition={" ).append( condition ).append( "}," );
        bldr.append( "uvIndex=" ).append( uvIndex ).append( "," );
        
        return bldr.toString();
    }
    
    private static boolean getBoolean( JSONObject obj, String key )
    {
        int     asInt       = obj.getInt( key );
        boolean asBoolean   = !(asInt == 0);
        return asBoolean;
    }
}
