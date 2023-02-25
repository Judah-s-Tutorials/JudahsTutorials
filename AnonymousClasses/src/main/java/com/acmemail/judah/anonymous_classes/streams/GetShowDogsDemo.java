package com.acmemail.judah.anonymous_classes.streams;

import java.util.List;

import com.acmemail.judah.anonymous_classes.lambdas.ShowDog;

public class GetShowDogsDemo
{
    public static void main(String[] args)
    {
        List<ShowDog>   showDogs    = ShowDogGenerator.getShowDogs( 10 );
        showDogs.forEach( System.out::println );
    }
}
