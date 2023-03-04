package com.acmemail.judah.anonymous_classes.streams;

import java.util.List;

import com.acmemail.judah.anonymous_classes.lambdas.ShowDog;

/**
 * This application
 * demonstrates the use
 * of the <em>filter</em> stream operation.
 * 
 * @author Jack Straub
 */
public class StreamFilterShowDogsDemo
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        List<ShowDog>   showDogs    = ShowDogGenerator.getShowDogs(  10 );
        showDogs.stream()
            .filter( d -> d.getAge() > 5 )
            .forEach( System.out::println );
    }
}
