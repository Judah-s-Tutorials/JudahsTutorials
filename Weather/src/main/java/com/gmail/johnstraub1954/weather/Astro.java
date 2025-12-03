package com.gmail.johnstraub1954.weather;

import org.json.JSONObject;

public class Astro
{
    private final String    sunrise;
    private final String    sunset;
    private final String    moonrise;
    private final String    moonset;
    private final String    moonPhase;
    private final int       moonIllumination;
    private final boolean   isMoonUp;
    private final boolean   isSunUp;
    
    public Astro( JSONObject astro )
    {
        sunrise = astro.getString( "sunrise" );
        sunset = astro.getString( "sunset" );
        moonrise = astro.getString( "moonrise" );
        moonset = astro.getString( "moonset" );
        moonPhase = astro.getString( "moon_phase" );
        moonIllumination = astro.getInt( "moon_illumination" );
        isMoonUp = getBoolean( astro, "is_moon_up" );
        isSunUp = getBoolean( astro, "is_sun_up" );
    }
    
    public String toString()
    {
        StringBuilder   bldr    = new StringBuilder();
        bldr.append( "sunrise=" ).append( sunrise ).append( "," );
        bldr.append( "sunset=" ).append( sunset ).append( "," );
        bldr.append( "moonrise=" ).append( moonrise ).append( "," );
        bldr.append( "moonset=" ).append( moonset ).append( "," );
        bldr.append( "moonPhase=" ).append( moonPhase ).append( "," );
        bldr.append( "moonIllumination=" ).append( moonIllumination ).append( "," );
        bldr.append( "isMoonUp=" ).append( isMoonUp ).append( "," );
        bldr.append( "isSunUp=" ).append( isSunUp );
        return bldr.toString();
    }
    
    private static boolean getBoolean( JSONObject obj, String key )
    {
        int     asInt       = obj.getInt( key );
        boolean asBoolean   = !(asInt == 0);
        return asBoolean;
    }
}
