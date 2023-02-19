package com.acmemail.judah.anonymous_classes.functional_interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.IntPredicate;

public class PredicateDemo
{
    public static void main(String[] args)
    {
        List<Integer>   scores  = getScores( 25 );
        score( scores, i -> i >= 65 );
    }
    
    private static void score( List<Integer> scores, IntPredicate pred )
    {
        for ( int score : scores )
        {
            String  grade   = pred.test( score ) ? "pass" : "fail";
            System.out.printf( "%3d -> %s%n", score, grade );
        }
    }

    private static List<Integer> getScores( int numScores )
    {
        List<Integer>   scores  = new ArrayList<>();
        Random          randy   = new Random( 1 );
        for ( int inx = 0 ; inx < numScores ; ++inx )
            scores.add( randy.nextInt( 61 ) + 40 );
        return scores;
    }
}
