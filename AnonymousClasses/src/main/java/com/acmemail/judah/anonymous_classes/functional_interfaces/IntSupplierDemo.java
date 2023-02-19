package com.acmemail.judah.anonymous_classes.functional_interfaces;

import java.util.function.IntSupplier;

public class IntSupplierDemo
{

    public static void main(String[] args)
    {
        PrimeSupplier   supplier    = new PrimeSupplier();
        for ( int inx = 0 ; inx < 100 ; ++inx )
            System.out.println( supplier.getAsInt() );
    }

    private static class PrimeSupplier implements IntSupplier
    {
        int next    = 2;
        
        @Override
        public int getAsInt()
        {
            int result  = next;
            nextPrime();
            
            return result;
        }
        
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
}
