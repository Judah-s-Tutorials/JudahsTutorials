package com.gmail.johnstraub1954.orrery;
import static java.lang.Math.PI;
import static java.lang.Math.E;

public class UMath
{
    public static final double  R_PER_D     = 180 / PI;
    public static final double  D_PER_R     = PI / 180;
    public static final double  ZED         = Double.MIN_VALUE;
    
    public static double rev( double radians )
    {
        double  result  = radians / (2*PI);
        return result;
    }
    
    public static double decHours( double hours, double mins, double secs )
    {
        double  decHours    = hours + mins / 60. + secs / 3600.;
        return decHours;
    }
}
