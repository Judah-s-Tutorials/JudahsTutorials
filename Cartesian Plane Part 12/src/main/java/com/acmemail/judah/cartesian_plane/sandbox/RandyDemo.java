package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RandyDemo
{
    private static final Random randy   = new Random( 1 );
    
    private record DoublePair( double num1, double num2 )
    {
        @Override
        public String  toString()
        {
            StringBuilder   bldr    = new StringBuilder();
            bldr.append( String.format( "(%5.3f,", num1 ) )
                .append( String.format( "%5.3f)", num2 ) );
            return bldr.toString();
        }
    }
    
    public static void main(String[] args)
    {
        Stream<DoublePair>  quad1   = generator( 0, 10, 0, 10 );
        Stream<DoublePair>  quad2   = generator( 0, 10, -10, 0 );
        Stream<DoublePair>  quad3   = generator( -10, 0, -10, 0 );
        Stream<DoublePair>  quad4   = generator( -10, 0, 0, 10 );
        Stream.of( quad1, quad2, quad3, quad4 )
            .flatMap( s -> s)
            .forEach( System.out::println );
    }

    private static Stream<DoublePair> generator( 
        double minX, 
        double maxX, 
        double minY, 
        double maxY 
    )
    {
        Stream<DoublePair> stream =
        IntStream.iterate( 0, i -> i < 10, i -> i + 1 )
            .mapToObj( 
                i -> new DoublePair(
                        randy.nextDouble( minX, maxX ),  
                        randy.nextDouble( minY, maxY )
                    )
            );
        return stream;
    }
}
