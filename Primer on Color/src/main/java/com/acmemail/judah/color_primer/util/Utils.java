package com.acmemail.judah.color_primer.util;

import java.awt.geom.Line2D;

public class Utils
{
    public static double getAngle( Line2D line )
    {
        double      deltaX      = line.getX2() - line.getX1();
        // reverse delta-y measurement because
        // java y values increase moving downwards
        double      deltaY      = line.getY1() - line.getY2();
        double      radians     = Math.atan2( deltaY, deltaX );
        return radians;
    }
    
    public static double getDegrees( Line2D line )
    {
        double      radians     = getAngle( line );
        double      degrees     = Math.toDegrees( radians );
        return degrees;
    }
}
