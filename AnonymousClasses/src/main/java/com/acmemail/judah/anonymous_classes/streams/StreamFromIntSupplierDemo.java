package com.acmemail.judah.anonymous_classes.streams;
import java.util.stream.IntStream;

import com.acmemail.judah.anonymous_classes.functional_interfaces.PrimeSupplier;


/**
 * This application demonstrates
 * the use of the <em>Supplier</em> functional interface
 * in conjunction with 
 * the <em>generate</em> stream operation.
 * 
 * @author Jack Straub
 */
public class StreamFromIntSupplierDemo
{
    /**
     * Application entry point.
     * Produces a sequential stream of prime numbers.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        IntStream.generate( new PrimeSupplier() ).forEach( System.out::println );

    }

}
