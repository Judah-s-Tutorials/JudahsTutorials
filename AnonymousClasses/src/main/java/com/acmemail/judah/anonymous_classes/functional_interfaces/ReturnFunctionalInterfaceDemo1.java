package com.acmemail.judah.anonymous_classes.functional_interfaces;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.acmemail.judah.anonymous_classes.lambdas.ShowDog;
import com.acmemail.judah.anonymous_classes.streams.ShowDogGenerator;

/**
 * This is a simple application to demonstrate how
 * a method can return a functional interface.
 * 
 * @author Jack Straub
 */
public class ReturnFunctionalInterfaceDemo1
{
    private static final List<ShowDog>  showDogs    = getList();
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
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
    private static ShowDog  getShowDog( Predicate<ShowDog> tester )
    {
        for ( ShowDog showDog : showDogs )
            if ( tester.test( showDog ) )
                return showDog;
        
        return null;
    }
    
    /**
     * Creates a list of ShowDogs for demonstration purposes.
     * 
     * @return  a list of ShowDogs
     */
    private static List<ShowDog> getList()
    {
        List<ShowDog>    list    = new ArrayList<>();
        
        list.add( new ShowDog( "Fido", 7, "Collie", 55555 ) );
        list.add( new ShowDog( "Shep", 2, "Collie", 22222 ) );
        list.add( new ShowDog( "Tipsy", 4, "Poodle", 33333 ) );
        list.add( new ShowDog( "Doodles", 5, "Shepherd", 77777 ) );
        list.add( new ShowDog( "Iggy", 2, "Poodle", 33333 ) );
        
        return list;
    }
}
