package com.acmemail.judah.anonymous_classes.functional_interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.IntPredicate;

/**
 * Simple application to demonstrate
 * the use of the <em>Predicate</em> functional interface.
 * 
 * @author Jack Straub
 */
public class PredicateDemo
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        List<Integer>   scores  = getScores( 25 );
        score( scores, i -> i >= 65 );
    }
    
    /**
     * Prints "pass" or "fail"
     * for each score in a list.
     * Pass/fail is determined by
     * a given Predicate.
     * 
     * @param scores    the scores to examine
     * @param pred      the given predicate
     */
    private static void score( List<Integer> scores, IntPredicate pred )
    {
        for ( int score : scores )
        {
            String  grade   = pred.test( score ) ? "pass" : "fail";
            System.out.printf( "%3d -> %s%n", score, grade );
        }
    }

    /**
     * Generates a list of random scores
     * between 60 and 100.
     * 
     * @param numScores the number of scores to generate
     * 
     * @return  the generated list of scores
     */
    private static List<Integer> getScores( int numScores )
    {
        List<Integer>   scores  = new ArrayList<>();
        Random          randy   = new Random( 1 );
        for ( int inx = 0 ; inx < numScores ; ++inx )
            scores.add( randy.nextInt( 61 ) + 40 );
        return scores;
    }
}
