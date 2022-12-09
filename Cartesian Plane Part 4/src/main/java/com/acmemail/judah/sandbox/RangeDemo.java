package com.acmemail.judah.sandbox;

public class RangeDemo
{
    public static void main(String[] args)
    {
        Range   range   = new Range( -10, 10 );
        for ( int num : range )
            System.out.println( num );
    }
}
