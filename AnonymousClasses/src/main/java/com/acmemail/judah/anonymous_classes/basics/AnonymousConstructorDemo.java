package com.acmemail.judah.anonymous_classes.basics;

import java.util.ArrayList;
import java.util.List;

public class AnonymousConstructorDemo
{
    @SuppressWarnings("serial")
    private static final List<Integer> testData    = new ArrayList<>()
    {
        {
            add( 10 );
            add( 20 );
            add( 30 );
        }
    };
    
    public static void main(String[] args)
    {
        for ( Integer datum : testData )
            System.out.println( datum );

    }
}
