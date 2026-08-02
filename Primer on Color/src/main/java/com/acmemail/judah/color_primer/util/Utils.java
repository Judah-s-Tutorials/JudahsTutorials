package com.acmemail.judah.color_primer.util;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Utils
{
    public static double getAngle( Line2D line )
    {
        double  radians = getAngle( line.getP1(), line.getP2() );
        return radians;
    }
    
    public static double getAngle( Point2D point1, Point2D point2 )
    {
        double      deltaX      = point2.getX() - point1.getX();
        // reverse delta-y measurement because
        // java y values increase moving downwards
        double      deltaY      = point1.getY() - point2.getY();
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
