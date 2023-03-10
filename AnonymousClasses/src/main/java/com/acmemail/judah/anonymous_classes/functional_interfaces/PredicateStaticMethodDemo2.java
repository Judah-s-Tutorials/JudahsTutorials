package com.acmemail.judah.anonymous_classes.functional_interfaces;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.acmemail.judah.anonymous_classes.lambdas.ShowDog;

/**
 * Simple application to demonstrate the use of the
 * Predicate interface <em>not</em> static method.
 * 
 * @author Jack Straub
 */
public class PredicateStaticMethodDemo2
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
        
        // Get the first ShowDog that is not a Collie
        ShowDog showDog = getShowDog( 
            Predicate.not( d -> d.getBreed().equals( "Collie" ) )
        );
        System.out.println( showDog );
    }
    
    /**
     * Returns the first show dog in the list
     * that satisfies the given predicate.
     * Returns null if no such show dog is found.
     * 
     * @param tester    the given predicate
     * 
     * @return
     *      the first show dog in the list that satisfies the given predicate,
     *      or null if none found
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
