package com.acmemail.judah.cartesian_plane.app;

import java.util.stream.Stream;

/**
 * Demonstrates how to use the Polynomial class.
 */
public class PolynomialDemo
{
    /**
     * Application entry point.
     * 
     * @param args  command-line arguments, not used
     */
    public static void main(String[] args)
    {
        // Calculates 2x**3 + x**2 + 3x + 1
        Polynomial  poly    = new Polynomial( 2, 1, 3, 1 );
        Stream.of( 1, 2, 3, 4, 5 )
            .map( poly::applyAsDouble )
            .forEach( System.out::println );
    }
}
