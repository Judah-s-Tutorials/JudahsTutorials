package com.acmemail.judah.anonymous_classes.lambdas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class IntSorterA3
{
    public static void main(String[] args)
    {
        Random          randy   = new Random( 0 );
        List<Integer>   numbers = new ArrayList<>();
        for ( int inx = 0 ; inx < 25 ; ++inx )
            numbers.add( randy.nextInt( 200 ) - 100 );
        numbers.sort( (num1,num2) -> num2 - num1 );
        numbers.forEach( new Consumer<Integer>() {
            public void accept( Integer p ){
                System.out.println( p );
            }
        });
    }
}
