package com.gmail.johnstraub1954.penrose.utils;

public class Angle
{
    public static final double  TO_RADIANS  = Math.PI / 180;
    public static final double  TO_DEGREES  = 180 / Math.PI;
    public static final double  TWO_PI      = 2 * Math.PI;

    private final double    radians;
    private final double    degrees;
    /**
     * Default constructor; not used.
     */
    private Angle()
    {
        // not used
        radians = 0;
        degrees = 0;
    }

    private Angle( double value, boolean isRadians )
    {
        if ( isRadians )
        {
            radians = value;
            degrees = TO_DEGREES * value;
        }
        else
        {
            degrees = value;
            radians = value * TO_RADIANS;
        }
    } 
    
    public static Angle getInDegrees( double degrees )
    {
        Angle   angle   = new Angle( degrees, false );
        return angle;
    }
    
    public static Angle getInRadians( double radians )
    {
        Angle   angle   = new Angle( radians, true );
        return angle;
    }
    
    public double getDegrees()
    {
        return degrees;
    }
    
    public double getRadians()
    {
        return radians;
    }
}
