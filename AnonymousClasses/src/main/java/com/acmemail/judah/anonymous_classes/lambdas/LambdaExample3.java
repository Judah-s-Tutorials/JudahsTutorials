package com.acmemail.judah.anonymous_classes.lambdas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LambdaExample3
{        
    public static void main( String[] args )
    {
        List<Integer>   randomList  = new ArrayList<>();
        Random          randy       = new Random( 0 );
        for ( int inx = 0 ; inx < 100 ; ++inx )
            randomList.add( randy.nextInt( 100 ) );
        
        randomList.sort( (num1, num2) -> num2 - num1 );
        for ( Integer num : randomList )
            System.out.println( num );
    }
}
