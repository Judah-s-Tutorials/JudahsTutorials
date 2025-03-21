package com.acmemail.judah.anonymous_classes;

import java.awt.Point;
import java.util.Random;

public class Utils
{
    private static final Random randomXco   = new Random( 0 );
    private static final Random randomYco   = new Random( 1 );

    public static Point randomPoint()
    {
        int     xco     = randomXco.nextInt( 201 ) - 100;
        int     yco     = randomYco.nextInt( 201 ) - 100;
        Point   point   = new Point( xco, yco );
        return point;
    }
}
