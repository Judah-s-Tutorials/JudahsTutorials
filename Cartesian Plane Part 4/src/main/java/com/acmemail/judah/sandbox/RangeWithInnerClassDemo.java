package com.acmemail.judah.sandbox;

public class RangeWithInnerClassDemo
{
    public static void main(String[] args)
    {
        RangeWithInnerClass range   = new RangeWithInnerClass( -10, 10 );
        for ( int num : range )
            System.out.println( num );
    }
}
