package com.acmemail.judah.anonymous_classes.streams;

import java.util.stream.Stream;

public class GenerateDemo1
{
    public static void main(String[] args)
    {
        Stream.generate( () ->Math.random() )
            .limit( 10 )
            .forEach( System.out::println );
    }

}
