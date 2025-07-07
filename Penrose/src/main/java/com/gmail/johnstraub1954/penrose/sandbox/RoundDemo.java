package com.gmail.johnstraub1954.penrose.sandbox;

import java.util.stream.Stream;

import com.gmail.johnstraub1954.penrose.utils.Utils;

public class RoundDemo
{

    public RoundDemo()
    {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args)
    {
        Stream.of( 3.14159, 2.51234, 2.456666 )
            .forEach( RoundDemo::printRound );

    }

    private static void printRound( double valIn )
    {
        double  rounded     = Utils.round( valIn );
        String  valInStr    = String.format( "%10.4f", valIn );
        String  roundedStr  = String.format( "%10.4f", rounded );
        System.out.println( valInStr + "->" + roundedStr );
    }
}
