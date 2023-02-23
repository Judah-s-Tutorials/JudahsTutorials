package com.acmemail.judah.anonymous_classes.lambdas;

import java.util.ArrayList;
import java.util.List;

public class MethodReferenceExample3
{    
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
