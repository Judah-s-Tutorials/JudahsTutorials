package com.acmemail.judah.anonymous_classes.streams;
import java.util.List;
import java.util.function.Predicate;

import com.acmemail.judah.anonymous_classes.lambdas.ShowDog;

/**
 * This is a simple application to demonstrate how
 * a method can return a functional interface.
 * 
 * @author Jack Straub
 */
/**
 * @author Jack Straub
 *
 */
public class ReturnFunctionalInterfaceDemo2
{
    private static final List<ShowDog>  showDogs    = 
        ShowDogGenerator.getShowDogs( 10 );
    
    public static void main(String[] args)
    {
        showDogs.forEach( System.out::println );
        System.out.println( "****************" );
        
        Predicate<ShowDog>  lessThan    = ageLessThan( 6 );
        Predicate<ShowDog>  breedEquals = breedEquals( "collie" );
        ShowDog showDog = getShowDog( lessThan.or( breedEquals ) );
        System.out.println( showDog );
    }
    
    /**
     * Returns a Predicate asserting
     * that a ShowDog is less than a given age.
     * 
     * @param age   the given age
     * 
     * @return  
     *      a Predicate asserting
     *      that a ShowDog is less than a given age
     */
    private static Predicate<ShowDog> ageLessThan( int age )
    {
        Predicate<ShowDog>  pred    = d -> d.getAge() < age;
        return pred;
    }
    
    
    /**
     * Returns a Predicate asserting
     * that a ShowDog belongs to a given breed
     * 
     * @param age   the given age
     * 
     * @return  
     *      a Predicate asserting
     *      that a ShowDog belongs to a given breed
     */
    private static Predicate<ShowDog> breedEquals( String breed )
    {
        Predicate<ShowDog>  pred    = d -> d.getBreed().equals( breed );
        return pred;
    }
    
    /**
     * Returns the first ShowDog that passes a given Predicate,
     * or null if there is no such ShowDog.
     * 
     * @param tester    the given Predicate
     * 
     * @return  
     *      the first ShowDog that passes a given Predicate,
     *      or null if there is no such ShowDog
     */
    private static ShowDog getShowDog( Predicate<ShowDog> tester )
    {
        // Remember findFirst returns a possibly empty Optional...
        // ... if you call get on an empty Optional it will throw an exception
        // ... instead use orElse, which handles the empty Optional.
        ShowDog showDog = 
            showDogs.stream().filter( tester ).findFirst().orElse( null );
        
        return showDog;
    }
}
