package com.acmemail.judah.cartesian_plane.sandbox;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * This class is intended to demonstrate
 * some of the uses
 * of the JUnit <em>@ParameterizedTest</em> tag.
 * 
 * @author Jack Straub
 *
 */
public class DemoParameterizedTest
{
    /**
     * Simple demo of @Parameterized test
     * using an array of ints.
     * 
     * @param testParam represents the current value to use in the test
     */
    @ParameterizedTest
    @ValueSource ( ints = {100, 200, 300} )
    public void generateIntParametersUsingValueSource( int testParam )
    {
        System.out.println( "int parameter " + testParam );
        assertTrue( testParam > 50 );
    }

    /**
     * Simple demo of @Parameterized test
     * using an array of strings.
     * 
     * @param testParam represents the current value to use in the test
     */
    @ParameterizedTest
    @ValueSource ( strings = {"manny", "moe", "jack"} )
    public void generateStringParametersUsingValueSource( String testParam )
    {
        System.out.println( "String parameter " + testParam );
        assertTrue( testParam.length() < 50 );
    }

    /**
     * Demo of @Parameterized test
     * using two parameters.
     * Values are established using @CsvSource
     * (CSV stands for Comma Separated Values).
     * Each element of the CSV array
     * is a string
     * containing multiple parameters
     * separated by commas.
     * 
     * @param testParam1    captures the first value in a CSV element
     * @param testParam2    captures the second value in a CSV element
     */
    @ParameterizedTest
    @CsvSource ( {"manny, 5", "moe, 3", "jack, 4"} )
    public void generateMultipleParametersUsingCsvSource( 
        String testParam1, 
        int    testParam2
    )
    {
        System.out.println( "two parameters " + testParam1 + ", " + testParam2 );
        assertTrue( testParam1.length() == testParam2 );
    }

    /**
     * Demo of @ParameterizedTest with <em>@MethodSource( methodName )</em>.
     * Each test is supplied with a single parameter.
     * The argument to @MethodSource
     * is the name of a method
     * that returns a <em>stream</em> of values;
     * for our purposes "stream" is satisfied
     * by an iterator.
     * 
     * @param testParam represents the current value to use in the test
     * 
     * @see #intGenerator()
     * @see IntGenerator
     */
    @ParameterizedTest
    @MethodSource ( "intGenerator" )
    public void generateParametersUsingMethodSource( int testParam )
    {
        System.out.println( "random parameters " + testParam );
        assertTrue( testParam >= 0 );
    }
    
    /**
     * Demo of @ParameterizedTest with <em>@MethodSource( methodName )</em>.
     * Each test is supplied with three parameters.
     * The argument to @MethodSource
     * is the name of a method
     * that returns a <em>stream</em> of <em>Arguments</em> objects;
     * for our purposes "stream" is satisfied
     * by Iterator&lt;Arguments&gt;. 
     * <em>Arguments</em> is a class
     * from the JUnit library.
     * We expect each Arguments object
     * to encapsulate three values
     * of type <em>String, int</em> and <em>double.</em>
     * 
     * @param arg1  String value derived from Arguments object
     * @param arg2  int value derived from Arguments object
     * @param arg3  double value derived from Arguments object.
     * 
     * @see #threeArgGenerator()
     * @see ThreeArgGenerator
     */
    @ParameterizedTest
    @MethodSource ( "threeArgGenerator" )
    public void generateMultipleParametersUsingMethodSource( 
        String arg1, 
        int    arg2, 
        double arg3
     )
    {
        String  str = arg1 + arg2 + ", " + arg3;
        System.out.println( str );
        assertTrue( arg2 < 1000 );
    }
    
    /**
     * Returns an Iterator&lt;Integer&gt; that generates five elements.
     * 
     * @return  an iterator that generates five Integers
     * 
     * @see IntGenerator
     */
    private static Iterator<Integer> intGenerator()
    {
        return new IntGenerator( 5 );
    }
    
    /**
     * Returns an Iterator&lt;Arguments&gt; that generates five elements.
     * 
     * @return  an iterator that generates five Arguments objects
     * 
     * @see ThreeArgGenerator
     */
    private static Iterator<Arguments> threeArgGenerator()
    {
        return new ThreeArgGenerator( 5 );
    }
    
    /**
     * Generates a series of random integers
     * in the range [0, 1000).
     * The number of integers to generate
     * is supplied by the user
     * in the constructor.
     * 
     * @author Jack Straub
     *
     * @see DemoParameterizedTest#intGenerator()
     */
    private static class IntGenerator implements Iterator<Integer>
    {
        private final Random    randy   = new Random();
        private final int       max;
        private int             count;
        
        /**
         * Constructor.
         * Determines the number of elements
         * generated by this iterator.
         * 
         * @param max   the number of elements 
         *              that should be generated by this iterator
         */
        public IntGenerator( int max )
        {
            this.max = max;
        }
        
        /**
         * Indicates whether or not this iterator
         * has at least one more element to generate.
         * 
         * @return  true if this iterator can provide  
         *          at least one more element  
         */
        @Override
        public boolean hasNext()
        {
            return count < max;
        }

        /**
         * Returns the next Integer in the sequence.
         * 
         * @return the next Integer in the sequence
         * 
         * @throws NoSuchElementException if this iterator is exhausted
         */
        @Override
        public Integer next()
        {
            if ( !hasNext() )
                throw new NoSuchElementException();
            count++;
            return randy.nextInt( 1000 );
        }
    }
    
    /**
     * Generates a series of Arguments objects
     * where each Arguments object contains
     * one String, one int and one double element
     * in that order.
     * The number of objects to generate 
     * is specified in the constructor.
     * 
     * @author Jack Straub
     * 
     * @see DemoParameterizedTest#threeArgGenerator()
     *
     */
    private static class ThreeArgGenerator implements Iterator<Arguments>
    {
        private final Random    randy   = new Random();
        private final int       max;
        private int             count   = 0;
        
        /**
         * Constructor.
         * Determines the maximum number of elements
         * to be generated by this iterator.
         * 
         * @param max   the maximum number of elements
         *              to be generated by this iterator
         */
        public ThreeArgGenerator( int max )
        {
            this.max = max;
        }
        
        /**
         * Indicates whether or not this iterator
         * has at least one more element to generate.
         * 
         * @return  true if this iterator can provide  
         *          at least one more element  
         */
        @Override
        public boolean hasNext()
        {
            return count < max;
        }

        /**
         * Returns the next Arguments object in the sequence.
         * 
         * @return the next Arguments object in the sequence
         * 
         * @throws NoSuchElementException if this iterator is exhausted
         */
        @Override
        public Arguments next()
        {
            if ( !hasNext() )
                throw new NoSuchElementException();
            ++count;
            String      arg1    = "Invocation " + count + ": ";
            int         arg2    = randy.nextInt( 500 );
            double      arg3    = Math.sqrt( arg2 );
            Arguments   args    = Arguments.of( arg1, arg2, arg3 );
            return args;
        }
    }
}
