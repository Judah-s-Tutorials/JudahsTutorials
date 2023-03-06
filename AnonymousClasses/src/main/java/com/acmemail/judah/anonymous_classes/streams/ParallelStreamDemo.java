package com.acmemail.judah.anonymous_classes.streams;

import java.util.Arrays;
import java.util.Random;

public class ParallelStreamDemo
{
    public static void main(String[] args)
    {
        Random      randy   = new Random( 0 );
        double[]    dArr    = randy.doubles().limit( 100000000 ).toArray();
        System.out.println( dArr.length );
        
        double  avgOf           = 0;
        long    numIterations   = 100;
        long    minimumTime     = Integer.MAX_VALUE;
        long    maximumTime     = 0;
        double  totalTime       = 0;
        double  averageTime     = 0;
        
        for ( int inx = 0 ; inx < numIterations ; ++inx )
        {
            long    start   = System.currentTimeMillis();
            avgOf =
                Arrays.stream( dArr ).parallel().average().getAsDouble();
            long    delta   = System.currentTimeMillis() - start;
            totalTime += delta;
            if ( delta < minimumTime )
                minimumTime = delta;
            if ( delta > maximumTime )
                maximumTime = delta;
        }
        
        System.out.println( "result=" + avgOf );
        
        averageTime = totalTime / numIterations;
        System.out.println( "min=" + minimumTime );
        System.out.println( "max=" + maximumTime );
        System.out.println( "avg=" + averageTime );
    }
}
