package com.acmemail.judah.anonymous_classes.basics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class SortingExampleInteger4
{
    public static void main(String[] args)
    {
        List<Integer>   randomList  = new ArrayList<>();
        Random          randy       = new Random( 0 );
        for ( int inx = 0 ; inx < 100 ; ++inx )
            randomList.add( randy.nextInt( 100 ) );
        
    randomList.sort(new Comparator<Integer>(){
        public int compare( Integer num1, Integer num2 ){
        int     rcode   = num2 - num1; return rcode;}});
        
        for ( Integer num : randomList )
            System.out.println( num );
    }
}
