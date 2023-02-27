package com.acmemail.judah.anonymous_classes.streams;

import java.util.stream.IntStream;

public class StreamGenerationDemo1
{
    public static void main(String[] args)
    {
        
        IntStream.range( 10, 15 ).forEach( System.out::println );
    }
}