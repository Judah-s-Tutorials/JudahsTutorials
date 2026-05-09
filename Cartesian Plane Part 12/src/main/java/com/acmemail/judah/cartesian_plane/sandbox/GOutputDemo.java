package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.Locale;

public class GOutputDemo
{
    public static void main(String[] args)
    {
        double  dNum1   = 1.2;
        double  dNum2   = 1.234e-40;
        double  dNum3   = .00000000000000000000000000000000000000001234;
        double  dNum4   = 123456;
        System.out.println( dNum1 );
        System.out.println( dNum2 );
        System.out.println( dNum3 );
        System.out.println( dNum1 + " " + dNum2 );
        
        String  str     = String.format( "%g, %g", dNum2, dNum1 );
        String  str2    = String.format( "%g", dNum2 );
        System.out.println( str );
        double  dNum5   = Double.parseDouble( str2 );
        System.out.println( dNum5 );
        String  str3    = String.format( "%s", dNum3 );
        System.out.println( str3 );
        String  str4    = String.format( "%s", dNum4 );
        System.out.println( str4 );
    }
}
