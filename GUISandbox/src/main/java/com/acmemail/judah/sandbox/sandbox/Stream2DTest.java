package com.acmemail.judah.sandbox.sandbox;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Stream2DTest
{
    private static String   newl    = System.lineSeparator();
    public static void main(String[] args)
    {
        int[][] intArr  = new int[10][5];
        for ( int row = 0 ; row < intArr.length ; ++row )
        {
            for ( int col = 0 ; col < intArr[row].length ; ++col )
            {
                int val = col;
                intArr[row][col] = 42;
            }
        }
        intArr[3][3] = 50;
        
//        long count = IntStream.range(0, intArr.length)
//            .flatMap( r -> Arrays.stream( intArr[r] ) )
//            .distinct()
//            .count();
//        System.out.println( count );
        
        long count = IntStream.range(0, intArr.length)
            .flatMap( r -> IntStream.range( 0, intArr[r].length ) )
//            .map( c -> intArr[r][c] )
            .distinct()
            .peek( System.out::println )
            .count();
        System.out.println( count );
    }
}
