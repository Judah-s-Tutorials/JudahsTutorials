package com.acmemail.judah.anonymous_classes.functional_interfaces;

import java.util.function.IntSupplier;

/**
 * Sample implementation of the
 * <em>IntSupplier</em> functional interface.
 * Generates an effectively infinite
 * sequence of prime numbers.
 * 
 * @author Jack Straub
 */
public class PrimeSupplier implements IntSupplier
{
    private int next    = 2;
    
    @Override
    public int getAsInt()
    {
        int result  = next;
        nextPrime();
        
        return result;
    }
    
    /**
     * Finds the next prime number in the sequence.
     */
    private void nextPrime()
    {
        if ( next == 2 )
            next = 3;
        else
        {
            int test;
            for ( test = next + 2 ; !isPrime( test ) ; test += 2 )
                ;
            next = test;
        }
    }
    
    /**
     * Determines whether or not
     * a given number is prime.
     * 
     * @param num   the given number
     * 
     * @return  true if the given number is prime
     */
    private static boolean isPrime( int num )
    {
        boolean result  = true;
        if ( num < 2 )
            result = false;
        else if ( num == 2 )
            result = true;
        else if ( num % 2 == 0 )
            result = false;
        else
        {
            int limit   = (int)Math.sqrt( num ) + 1;
            for ( int test = 3 ; result && test <= limit ; test += 2 )
                result = num % test != 0;
        }
        return result;
    }
}