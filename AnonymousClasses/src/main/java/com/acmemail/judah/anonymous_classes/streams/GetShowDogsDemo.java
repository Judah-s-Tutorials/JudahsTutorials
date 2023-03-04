package com.acmemail.judah.anonymous_classes.streams;

import java.util.List;

import com.acmemail.judah.anonymous_classes.lambdas.ShowDog;

/**
 * This application does nothing other
 * than demonstrating that the 
 * {@linkplain ShowDogGenerator}
 * works.
 * 
 * @author Jack Straub
 */
public class GetShowDogsDemo
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        List<ShowDog>   showDogs    = ShowDogGenerator.getShowDogs( 10 );
        showDogs.forEach( System.out::println );
    }
}
