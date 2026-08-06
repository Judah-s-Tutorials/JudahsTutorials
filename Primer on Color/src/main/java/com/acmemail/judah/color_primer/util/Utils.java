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
    
    /**
     * Returns the angle of a line drawn from point1 to point2.
     * Returned value is in radians in the range 0 &le; r &le; 2&pi;.
     * A horizontal line is at 0 radians; angles increase
     * in a counter-clockwise direction.
     * 
     * @param point1    given point1
     * @param point2    given point2
     * 
     * @return  angle in radians of a line drawn from point1 to point2
     */
    public static double getAngle( Point2D point1, Point2D point2 )
    {
        double      deltaX      = point2.getX() - point1.getX();
        // reverse delta-y measurement because
        // java y values increase moving downwards
        double      deltaY      = point1.getY() - point2.getY();
        double      radians     = Math.atan2( deltaY, deltaX );
//        if ( radians < 0 )
//            radians += 2 * Math.PI;
        return radians;
    }
    
    public static double getDegrees( Line2D line )
    {
        double      radians     = getAngle( line );
        double      degrees     = Math.toDegrees( radians );
        return degrees;
    }
}
