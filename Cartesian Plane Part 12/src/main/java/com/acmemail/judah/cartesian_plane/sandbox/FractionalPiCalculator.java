package com.acmemail.judah.cartesian_plane.sandbox;

public class FractionalPiCalculator
{

    public static void main(String[] args)
    {
        int     denom   = 16;
        for ( int num = 0 ; num < 2*denom ; ++num )
        {
            double  decimal = (num * Math.PI) / denom;
            System.out.printf( "(%2d x pi) / %d: %8.7f%n", num, denom, decimal );
        }
    }

}
