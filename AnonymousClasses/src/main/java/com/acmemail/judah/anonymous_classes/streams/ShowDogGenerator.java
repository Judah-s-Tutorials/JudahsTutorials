package com.acmemail.judah.anonymous_classes.streams;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.acmemail.judah.anonymous_classes.lambdas.ShowDog;

public class ShowDogGenerator
{
    private static final String names[] =
    {
        "fido",
        "spot",
        "sky",
        "miracle",
        "bozo",
        "browie",
        "blackie",
        "foxy",
        "wolf",
        "bowser",
        "bruiser",
        "noisy",
        "killer",
        "remedy",
        "rarebit",
        "prince",
        "king",
        "princess",
        "queenie",
        "sorceress",
        "slumber"
    };
    
    private static final String     breeds[]    =
    {
        "terrier",
        "bloodhound",
        "sheepdog",
        "dachsund",
        "pointer",
        "collie",
        "setter",
        "doberman",
        "spaniel",
        "poodle"
    };
    
    private static final int    minAge      = 1;
    private static final int    maxAge      = 10;
    
    private static final int    maxOwnerID  = 5000;
    
    public static List<ShowDog> getShowDogs( int maxCount )
    {
        final   int     numNames    = names.length;
        final   int     numBreeds   = breeds.length;
        final   int     ageRange    = maxAge - minAge;
        final Random    randy       = new Random( 0 );
        
        List<ShowDog>   list    =
            Stream.generate( ShowDog::new )
                .limit( maxCount )
                .map( s -> s.putName( names[randy.nextInt( numNames )] ) )
                .map( s -> s.putBreed( breeds[randy.nextInt( numBreeds )] ) )
                .map( s -> s.putAge( randy.nextInt( ageRange ) + minAge ) )
                .map( s -> s.putOwnerID( randy.nextInt( maxOwnerID + 1 ) ) )
                .collect( Collectors.toList() );
        return list;
    }
}
