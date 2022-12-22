package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.geom.Line2D;
import java.util.Random;
import java.util.stream.IntStream;

public class RoundDemo
{
    public static void main(String[] args)
    {
        String  fmt = "%5.4f -> %5.4f%n";
        for ( float var = 1.001f ; var < 1.1f ; var += .001f )
        {
            float   rounded =   roundToHundredths( var );
            System.out.printf( fmt, var, rounded );
        }
        
        Random  randy   = new Random( 5 );
        IntStream.range( 0, 10 ).forEach( inx -> 
        {
            float   xco1    = randy.nextFloat() * 10f;
            float   yco1    = randy.nextFloat() * 10f;
            float   xco2    = randy.nextFloat() * 10f + 1;
            float   yco2    = randy.nextFloat() * 10f + 1;
            Line2D  line1   = new Line2D.Float( xco1, yco1, xco2, yco2 );
            Line2D  line2   = roundToHundredths( line1 );
            print( line1 );
            print( line2 );
            System.out.println( "===================" );
        }
        );
    }

    private static Line2D roundToHundredths( Line2D lineIn )
    {
        float   xco1    = roundToHundredths( lineIn.getX1() );
        float   yco1    = roundToHundredths( lineIn.getY1() );
        float   xco2    = roundToHundredths( lineIn.getX2() );
        float   yco2    = roundToHundredths( lineIn.getY2() );
        Line2D  lineOut = new Line2D.Float( xco1, yco1, xco2, yco2 );
        return lineOut;
    }
    
    private static float roundToHundredths( double fVar )
    {
        float   varOut  = (int)(fVar * 100 + .5);
        varOut /= 100;
        return varOut;
    }
    
    private static void print( Line2D line )
    {
        String  fmt = "%6.4f %6.4f %6.4f %6.4f%n";
        System.out.printf(
            fmt,
            line.getX1(),
            line.getY1(),
            line.getX2(),
            line.getY2()
        );
    }
}
