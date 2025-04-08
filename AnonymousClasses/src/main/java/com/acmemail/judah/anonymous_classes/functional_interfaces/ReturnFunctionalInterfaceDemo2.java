package com.acmemail.judah.anonymous_classes.functional_interfaces;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.ToIntBiFunction;

import com.acmemail.judah.anonymous_classes.lambdas.ShowDog;

/**
 * This is a simple application to demonstrate how
 * a method can return a functional interface.
 * 
 * @author Jack Straub
 */
public class ReturnFunctionalInterfaceDemo2
{
    private static final List<ShowDog>  showDogs    = getList();
    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        int size    = showDogs.size();
        for ( int inx = 0 ; inx < size ; ++inx )
        {
            ShowDog dog1    = showDogs.get( inx );
            String  breed   = dog1.getBreed();
            String  name    = dog1.getName();
            int     age     = dog1.getAge();
            int     ownerID = dog1.getOwnerID();
            showDogs.add( 
                new ShowDog( name, age, breed, ownerID * 2 )
            );
            showDogs.add( 
                new ShowDog( name, 2 * age, breed, ownerID / 2 )
            );
        }
        Comparator<ShowDog> comp    =
            sortBy( "breed", "name", "age", "ownerID" );
        showDogs.sort( comp );
        showDogs.forEach( System.out::println );
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
    private static Comparator<ShowDog> sortBy( String... params )
    {
        List<Comparator<ShowDog>>   list    = new ArrayList<>();   
        for ( String param : params )
        {
            switch ( param  )
            {
            case "age":
                list.add( ShowDog::sortByAge );
                break;
            case "breed":
                list.add( ShowDog::sortByBreed );
                break;
            case "name":
                list.add( ShowDog::sortByName );
                break;
            case "ownerID":
                list.add( ShowDog::sortByOwnerID );
                break;
            default:
                break;
            }
        }
        if ( list.size() == 0 )
            list.add( ShowDog::sortByName );
        Comparator<ShowDog> comp    = (s1,s2) -> {
            int result  = 0;
            int size    = list.size();
            for ( int inx = 0 ; result == 0 && inx < size ; ++inx )
                result = list.get( inx ).compare( s1, s2 );
            return result;
        };
        return comp;
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
