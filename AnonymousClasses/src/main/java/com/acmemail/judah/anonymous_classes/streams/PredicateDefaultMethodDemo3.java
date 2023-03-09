package com.acmemail.judah.anonymous_classes.streams;
import java.util.List;
import java.util.function.Predicate;

import com.acmemail.judah.anonymous_classes.lambdas.ShowDog;

/**
 * This is a simple application 
 * that demonstrates the use
 * of the default <em>or</em> method
 * of the <em>Predicate</em> interface.
 * It's the same as
 * {@linkplain com.acmemail.judah.anonymous_classes.functional_interfaces.PredicateDefaultMethodDemo1}
 * except that the for loop
 * in the previous demo
 * is replaced with a streaming operation.
 * 
 * @author Jack Straub
 */
public class PredicateDefaultMethodDemo3
{
    /** List of ShowDogs for demonstration purposes. */
    private static final List<ShowDog>  showDogs    = 
        ShowDogGenerator.getShowDogs( 10 );
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        showDogs.forEach( System.out::println );
        System.out.println( "****************" );
        
        Predicate<ShowDog>  lessThan    = d -> d.getAge() < 6;
        Predicate<ShowDog>  breedEquals = d -> d.getBreed().equals( "collie" );
        ShowDog showDog = getShowDog( lessThan.or( breedEquals ) );
        System.out.println( showDog );
    }
    
    /**
     * Returns the first ShowDog in the list
     * that satisfies the given Predicate.
     * Returns null if no suitable ShowDog is found.
     * 
     * @param tester    the given Predicate
     * 
     * @return
     *      the first ShowDog in the list
     *      that satisfies the given Predicate,
     *      or null if none found
     */
    private static ShowDog  getShowDog( Predicate<ShowDog> tester )
    {
        ShowDog showDog = 
            showDogs.stream().filter( tester ).findFirst().orElse( null );
        
        return showDog;
    }
}
