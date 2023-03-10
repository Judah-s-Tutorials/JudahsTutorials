package com.acmemail.judah.anonymous_classes.functional_interfaces;

import java.util.function.IntSupplier;

/**
 * Demonstrates how to implement
 * an <em>IntSequence</em> functional interface.
 * Starting with a given value,
 * generates a sequence of odd numbers.
 * 
 * @author Jack Straub
 */
public class IntSupplierDemo
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        // Print a list of the first 100 prime numbers
        OddSupplier supplier    = new OddSupplier();
        for ( int inx = 0 ; inx < 100 ; ++inx )
            System.out.println( supplier.getAsInt() );
    }

    /**
     * Encapsulates an <em>IntSupplier</em> functional interface
     * that generates a sequence of odd numbers
     * from a given start point.
     */
    private static class OddSupplier implements IntSupplier
    {
        private int next;
        
        /**
         * Default constructor.
         * Generates a sequence of odd numbers
         * beginning with 0.
         */
        public OddSupplier()
        {
            this( 1 );
        }
        
        /**
         * Constructor.
         * Generates a sequence of odd numbers
         * beginning with given value;
         * the given value must be odd.
         * 
         * @param start the given value to start the sequence
         * 
         * @throws  IllegalArgumentException
         *          if the given start value
         *          is not odd
         */
        public OddSupplier( int start )
        {
            next = start;
        }
        
        @Override
        public int getAsInt()
        {
            int result  = next;
            next += 2;
            
            return result;
        }
    }
}
