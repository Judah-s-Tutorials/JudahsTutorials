package com.acmemail.judah.anonymous_classes.functional_interfaces;

import java.util.function.IntSupplier;
import java.util.stream.IntStream;

public class IntSupplierDemo
{

    public static void main(String[] args)
    {
        // Print a list of the first 100 prime numbers
        IntSupplier supplier    = new PrimeSupplier();
        for ( int inx = 0 ; inx < 100 ; ++inx )
            System.out.println( supplier.getAsInt() );
    }

    private static class OddSupplier implements IntSupplier
    {
        private int next;
        
        public OddSupplier()
        {
            this( 1 );
        }
        
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
