package com.acmemail.judah.anonymous_classes.lambdas;

import java.util.ArrayList;
import java.util.List;

/**
 * This application is
 * a revision of the previous example,
 * {@linkplain MethodReferenceExample2}.
 * In this example
 * the lambdas used in the prior example
 * are replaced with lambdas
 * using method references.
 * 
 * @author Jack Straub
 *
 *@see MethodReferenceExample2
 */
public class MethodReferenceExample3
{    
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        List<ShowDog>    list    = getList();

        System.out.println( "*** sort by age ***" );
        list.sort( ShowDog::sortByAge );
        for ( ShowDog dog : list )
            System.out.println( dog );
        
        System.out.println( "*** sort by breed ***" );
        list.sort( ShowDog::sortByBreed );
        for ( ShowDog dog : list )
            System.out.println( dog );
    }

    /**
     * Gets a list of ShowDogs
     * for demonstration purposes.
     *  
     * @return  a list of ShowDogs
     */
    private static List<ShowDog> getList()
    {
        List<ShowDog>    list    = new ArrayList<>();
        
        list.add( new ShowDog( "Fido", 3, "Collie", 55555 ) );
        list.add( new ShowDog( "Shep", 2, "Collie", 22222 ) );
        list.add( new ShowDog( "Tipsy", 4, "Poodle", 33333 ) );
        list.add( new ShowDog( "Doodles", 5, "Shepherd", 77777 ) );
        list.add( new ShowDog( "Iggy", 2, "Poodle", 33333 ) );
        
        return list;
    }
}
