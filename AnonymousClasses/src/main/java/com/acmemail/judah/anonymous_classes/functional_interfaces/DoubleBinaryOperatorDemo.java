package com.acmemail.judah.anonymous_classes.functional_interfaces;

import java.util.Random;
import java.util.function.DoubleBinaryOperator;

public class DoubleBinaryOperatorDemo
{
    private static final Random randy1  = new Random( 1 );
    private static final Random randy2  = new Random( 2 );
    
    public static void main(String[] args)
    {
        combine(  10, (d1,d2) -> d1 * d2 );
    }

    private static void combine( int numOps, DoubleBinaryOperator funk)
    {
        for ( int inx = 0 ; inx < numOps ; ++inx )
        {
            double  arg1    = randy1.nextDouble() * 100;
            double  arg2    = randy2.nextDouble() * 100;
            double  result  = funk.applyAsDouble( arg1, arg2 );
            System.out.printf( "%f, %f -> %f%n", arg1, arg2, result );
        }
    }
}
