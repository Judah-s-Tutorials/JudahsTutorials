package com.acmemail.judah.anonymous_classes.streams;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import com.acmemail.judah.anonymous_classes.lambdas.ShowDog;

/**
 * This application
 * demonstrates the use
 * of miscellaneous reduction operations
 * available directly
 * from the Java library.
 * 
 * @author Jack Straub
 */
public class MiscReductionExamples
{
    private static List<ShowDog>    showDogs    = 
        ShowDogGenerator.getShowDogs( 25 );
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        countDemo();
        countDemo( d -> d.getBreed().equals( "collie" ) );
        averageDemo();
        maxDemo();
    }

    /**
     * Demonstrates the use of the
     * <em>count</em> reduction operation.
     * Counts the elements in a stream.
     */
    private static void countDemo()
    {
        long    count   =
            showDogs
                .stream()
                .count();
        System.out.println( "there are " + count + " show dogs" );
    }

    /**
     * Demonstrates the use of the
     * <em>count</em> reduction operation.
     * Counts the elements in a stream
     * that match a given predicate.
     * 
     * @param   pred    the given predicate
     */
    private static void countDemo( Predicate<ShowDog> pred )
    {
        long    count   =
            showDogs
                .stream()
                .filter( pred )
                .count();
        String  feedback    =
            "there are " + count + " show dogs "
            + "matching the predicate";
        System.out.println( feedback );
    }

    /**
     * Demonstrates the use of the
     * <em>average</em> reduction operation.
     * Find the average age
     * of a ShowDog in the showDogs list.
     */
    private static void averageDemo()
    {
        // Note: .average() returns an Optional
        double  average =
            showDogs
                .stream()
                .mapToDouble( ShowDog::getAge )
                .average()
                .orElse( 0 );
        System.out.println( "the average age is: " + average );
    }

    /**
     * Demonstrates the use of the
     * <em>average</em> reduction operation.
     * Find the oldest ShowDog in the showDogs list.
     */
    private static void maxDemo()
    {
        // Note: .average() returns an Optional
        Optional<ShowDog>    oldest  =
            showDogs
                .stream()
                .max( (d1,d2) -> d1.getAge() - d2.getAge() );
        
        String  name    = "none";
        if ( oldest.isPresent() )
            name = oldest.get().getName();
        System.out.println( "the oldest show dog is " + name );
    }
}
