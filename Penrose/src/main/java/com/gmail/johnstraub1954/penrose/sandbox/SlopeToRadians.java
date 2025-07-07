package com.gmail.johnstraub1954.penrose.sandbox;

import java.awt.geom.Line2D;
import java.util.stream.Stream;

public class SlopeToRadians
{
    private static final double xier    = Math.PI / 180;
    public SlopeToRadians()
    {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args)
    {
        System.out.println( Math.PI / 2 );
        Line2D  line00  = new Line2D.Double( -5, 0, 5, 0 );
        Line2D  line01  = new Line2D.Double( -5, -5, 5, 5 );
        Line2D  line67  = new Line2D.Double( -5, -3.35, 5, 3.35 );
        Line2D  line90  = new Line2D.Double( -5, -1000000, 5, 1000000 );
        Line2D  lineInf = new Line2D.Double( 0, -5, 0, 5 );
        Stream.of( line00, line01, line67, line90, lineInf )
            .peek( SlopeToRadians::print )
            .map(  SlopeToRadians::invert )
            .forEach(  SlopeToRadians::print );
    }

    private static void print( Line2D line )
    {
        double  xco1        = line.getX1();
        double  yco1        = line.getY1();
        double  xco2        = line.getX2();
        double  yco2        = line.getY2();
        double  deltaX      = xco2 - xco1;
        double  deltaY      = yco2 - yco1;
        double  slope       = deltaY / deltaX;
        double  radians     = Math.atan( slope );
        String  fmt         = "%2.2f";
        String  strSlope    = String.format( fmt, slope );
        String  strRadians  = String.format( fmt, radians );
        String  message =
            "slope: " + strSlope
            + ", radians: " + strRadians;
        System.out.println( message );
    }
    
    private static Line2D invert( Line2D lineIn )
    {
        Line2D  lineOut = new Line2D.Double( lineIn.getP2(), lineIn.getP1() );
        return lineOut;
    }
}
