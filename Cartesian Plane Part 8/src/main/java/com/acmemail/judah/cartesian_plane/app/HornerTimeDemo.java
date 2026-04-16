package com.acmemail.judah.cartesian_plane.app;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

/**
 * This application compares the efficiency of
 * Horner's method for polynomial evaluation
 * to the method originally implemented
 * in the Polynomial class.
 * 
 * @see Polynomial
 * @see PolynomialOrig
 */
public class HornerTimeDemo
{
    /**
     * Application entry point.
     * @param args  command-line arguments, not used
     */
    public static void main(String[] args)
    {
        double[]            coeff       = { -3.5, 2.5, -1.5, .5 };
        int                 loops       = 10;
        List<Long>          horner      = new ArrayList<>();
        List<Long>          orig        = new ArrayList<>();
        Polynomial          polyHorner  = new Polynomial( coeff );
        PolynomialOrig      polyOrig    = new PolynomialOrig( coeff );
        for ( int inx = 0 ; inx < loops ; ++inx )
        {
            long    time    = eval( polyOrig );
            orig.add( time );
            time = eval( polyHorner );
            horner.add( time );
        }
        int                 len         = horner.size();
        for ( int inx = 0 ; inx < len ; ++inx )
        {
            long    hornerTime      = horner.get( inx );
            long    origTime        = orig.get( inx );
            String  feedback        =
                String.format( "%4d %4d", hornerTime, origTime );
            System.out.println( feedback );
        }
        
        Stats   hStats  = getStats( horner );
        Stats   nStats  = getStats( orig );
        System.out.println( "        " + hStats.getHeader() );
        System.out.println( "Horner: " + hStats );
        System.out.println( "  Orig: " + nStats );
    }
    
    /**
     * Given a list of long values,
     * calculate the minimum, maximum, and average of the values
     * and compose a Stats object containing them.
     *  
     * @param list  the given list
     * 
     * @return  Stats object containing the calculated 
     *          minimum. maximum, and average of the values in the list
     */
    private static Stats getStats( List<Long> list )
    {
        long    min = 
            list.stream()
               .mapToLong( Long::longValue )
               .min().orElse( 0 );
        long    max = 
            list.stream()
               .mapToLong( Long::longValue )
               .max().orElse( 0 );
        double  avg = 
            list.stream()
               .mapToLong( Long::longValue )
               .average().orElse( 0 );
        Stats   stats   = new Stats( min, max, avg );
        return stats;
    }

    /**
     * Executes the given function against
     * a range of x values
     * and measure the elapsed time.
     * 
     * @param funk  the given function
     * 
     * @return  the time consumed by the operation
     */
    private static long eval( DoubleUnaryOperator funk )
    {
        long    begin   = System.currentTimeMillis();
        for ( int inx = 0 ; inx < 10000 ; ++inx )
        {
            for ( double xco = -5 ; xco <= 5 ; xco += .001 )
            {
                funk.applyAsDouble( xco );
            }
        }
        long    elapsed = System.currentTimeMillis() - begin;
        return elapsed;
    }
    
    /**
     * Encapsulates the minimum, maximum, and average values
     * of some operation.
     */
    private static class Stats
    {
        /** The encapsulated minimum value. */
        public final long   min;
        /** The encapsulated maximum value. */
        public final long   max;
        /** The encapsulated average value. */
        public final double avg;
        
        /**
         * Constructor.
         * Initializes the encapsulated values.
         * 
         * @param min   initializer for the minimum value
         * @param max   initializer for the maximum value
         * @param avg   initializer for the average value
         */
        public Stats( long min, long max, double avg )
        {
            this.min = min;
            this.max = max;
            this.avg = avg;
        }
        
        /**
         * Gets the string containing the "Min Max Avg"
         * column headers.
         * The length of each column header
         * is calculated to align 
         * with the expected lengths of the strings
         * returned by getMin(), getMax(), and getAvg.
         * 
         * @return  the string containing the "Min Max Avg" column headers
         */
        public String getHeader()
        {
            String  header  = 
                String.format( "%5s%5s%8s", "Min", "Max", "Avg" );
            return header;
        }
        
        /**
         * Gets a formatted string containing the encapsulated
         * minimum value.
         * The length of the string is estimated
         * in an effort to favor printing the returned values
         * in a right-justified column.
         * 
         * @return
         *      formatted string containing the encapsulated
         *      minimum value
         */
        public String getMin()
        {
            String  val = String.format( "%5d", min );
            return val;
        }
        
        /**
         * Gets a formatted string containing the encapsulated
         * maximum value.
         * The length of the string is estimated
         * in an effort to favor printing the returned values
         * in a right-justified column.
         * 
         * @return
         *      formatted string containing the encapsulated
         *      maximum value
         */
        public String getMax()
        {
            String  val = String.format( "%5d", max );
            return val;
        }
        
        /**
         * Gets a formatted string containing the encapsulated
     * averleftage value.
         * The length of the string is estimated
         * in an effort to favor printing the returned values
         * in a right-justified column.
         * 
         * @return
         *      formatted string containing the encapsulated
         *      minimum value
         */
        public String getAvg()
        {
            String  val = String.format( "%8.2f", avg );
            return val;
        }
        
        @Override
        public String toString()
        {
            StringBuilder   bldr    = new StringBuilder();
            bldr.append( getMin() ).append( getMax() ).append( getAvg() );
            return bldr.toString();
        }
    }
}
