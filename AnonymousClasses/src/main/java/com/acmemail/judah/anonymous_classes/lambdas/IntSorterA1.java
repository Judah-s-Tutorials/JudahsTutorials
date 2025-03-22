package com.acmemail.judah.anonymous_classes.lambdas;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class IntSorterA1
{
    public static void main(String[] args)
    {
        Random          randy   = new Random( 0 );
        List<Integer>   numbers = new ArrayList<>();
        for ( int inx = 0 ; inx < 25 ; ++inx )
            numbers.add( randy.nextInt( 200 ) - 100 );
        numbers.sort( (num1,num2) -> {
            return num2 - num1;
        });
        for ( Integer num : numbers )
            System.out.println( num );
    }
}
