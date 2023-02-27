package com.acmemail.judah.anonymous_classes.streams;
import java.util.List;
import java.util.function.Predicate;

import com.acmemail.judah.anonymous_classes.lambdas.ShowDog;

public class PredicateStaticMethodDemo3
{
    private static final List<ShowDog>  showDogs    = 
        ShowDogGenerator.getShowDogs( 10 );
    public static void main(String[] args)
    {
        showDogs.forEach( System.out::println );
        System.out.println( "****************" );
        
        Predicate<ShowDog>  lessThan    = d -> d.getAge() < 6;
        Predicate<ShowDog>  breedEquals = d -> d.getBreed().equals( "collie" );
        ShowDog showDog = getShowDog( lessThan.or( breedEquals ) );
        System.out.println( showDog );
    }
    
    private static ShowDog  getShowDog( Predicate<ShowDog> tester )
    {
        ShowDog showDog = 
            showDogs.stream().filter( tester ).findFirst().orElse( null );
        
        return showDog;
    }
}
