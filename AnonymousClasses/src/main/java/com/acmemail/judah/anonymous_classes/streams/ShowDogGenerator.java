package com.acmemail.judah.anonymous_classes.streams;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.acmemail.judah.anonymous_classes.lambdas.ShowDog;

/**
 * This class is used
 * to generate a random list of ShowDogs
 * for demonstration purposes.
 * 
 * @author Jack Straub
 */
public class ShowDogGenerator
{
    /** List of show dog names to be chosen at random. */
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
    
    /** List of show dog breeds to be chosen at random. */
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
    
    /** Minimum, randomly generated age of a show dog. */
    private static final int    minAge      = 1;
    /** Maximum, randomly generated age of a show dog. */
    private static final int    maxAge      = 10;
    
    /** 
     * The maximum owner ID.
     * An owner ID between 1 and maxOwnerID,
     * inclusive,
     * is generated for each show dog.
     */
    private static final int    maxOwnerID  = 5000;
    
    /**
     * Gets a list of randomly generated ShowDogs.
     * 
     * @param maxCount  the number of ShowDogs to generate
     * 
     * @return  a list of randomly generated ShowDogs
     */
    public static List<ShowDog> getShowDogs( int maxCount )
    {
        final int       numNames    = names.length;
        final int       numBreeds   = breeds.length;
        final int       ageRange    = maxAge - minAge;
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
