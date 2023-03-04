package com.acmemail.judah.anonymous_classes.streams;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import com.acmemail.judah.anonymous_classes.lambdas.ShowDog;

/**
 * This application is a revision
 * of the previous application,
 * {@linkplain OptionalDemo1}.
 * The for loop
 * in the <em>getShowDog</em> method
 * of the previous application
 * is replaced with
 * a streaming operation.
 * 
 * @author Jack Straub
 */
public class OptionalDemo2
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
        
        Predicate<ShowDog>  lessThan    = d -> d.getAge() < 6;
        Predicate<ShowDog>  breedEquals = d -> d.getBreed().equals( "Collie" );
        
        // Get the first ShowDog that is less than 6,
        // or which is a Collie.
        Optional<ShowDog>   showDogOpt  = getShowDog( lessThan.or( breedEquals ) );
        if ( showDogOpt.isEmpty() )
            System.out.println( "no suitable ShowDog found" );
        else
            System.out.println( showDogOpt.get() );
    }
    
    /**
     * Returns an Optional 
     * containing the first show dog in the list
     * that satisfies the given predicate.
     * Returns an empty Optional
     * if no such show dog is found.
     * 
     * @param tester    the given predicate
     * 
     * @return
     *      Optional containing the first show dog in the list 
     *      that satisfies the given predicate,
     *      or an empty Optional if none found
     */
    private static Optional<ShowDog> getShowDog( Predicate<ShowDog> tester )
    {
        Optional<ShowDog>   firstShowDog    = showDogs.stream().findFirst();
        
        return firstShowDog;
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
