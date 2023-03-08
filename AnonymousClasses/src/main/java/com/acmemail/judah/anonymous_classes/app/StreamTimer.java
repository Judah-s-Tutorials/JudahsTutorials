package com.acmemail.judah.anonymous_classes.app;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

/**
 * This is a utility class
 * to execute timed tests.
 * The choice of tests is limited;
 * they are:
 * <ul>
 * <li>
 *      One test to compute
 *      the average of an array of double values,
 *      using parallel decomposition;
 * </li>
 * <li>
 *      One test to compute
 *      the average of an array of double values,
 *      without using parallel processing;
 * </li>
 * <li>
 *      One test to sort
 *      an array of double values,
 *      using parallel decomposition;
 * </li>
 * <li>
 *      One test to sort
 *      an array of double values,
 *      without using parallel processing.
 * </li>
 * </ul>
 * <p>
 * Each test is executed
 * using the same large array
 * of randomly-generated double values.
 * Each test is run iteratively,
 * and the minimum, maximum and average time
 * required for each iteration
 * is recorded.
 * The size of the array
 * and the number of iterations
 * cannot be controlled
 * by the user.
 * </p>
 * 
 * @author Jack Straub
 */
public class StreamTimer
{
    /** Line-separator for this platform. */
    private static final String newl        = System.lineSeparator();
    /** 
     * Label for the minimum result in the report produced by
     * {@inkplain #toReportString(StreamTimer)}.
     */
    private static final String minLabel    = "Minimum: ";
    /** 
     * Label for the maximum result in the report produced by
     * {@inkplain #toReportString(StreamTimer)}.
     */
    private static final String maxLabel    = "Maximum: ";
    /** 
     * Label for the maximum result in the report produced by
     * {@inkplain #toReportString(StreamTimer)}.
     */
    private static final String avgLabel    = "Average: ";
    
    /** Number of iteration of each test to run. */
    private final int       numIterations   = 100;
    /** Total number of test values generated. */
    private final int       numTestValues   = 10_000_000;
    /** Num values to use in sort test. */
    private final int       numSortValues   = numTestValues;
    /** Test values for each test. */
    private final double[]  dArr     = 
        new Random( 0 )
            .doubles()
            .limit( numTestValues )
            .toArray();

    private long    minimumTime;
    private long    maximumTime;
    private double  averageTime;
    private boolean isParallel;
    
    /**
     * Run a timed test,
     * computing the average
     * of double values in an array,
     * and recording
     * the minimum, maximum and average time.
     * A parallel traversal is employed.
     * 
     * @return  the computed average
     */
    public double parallelAverage()
    {
        init( true );
        double  totalTime       = 0;
        double  avgOf           = 0;
        
        for ( int inx = 0 ; inx < numIterations ; ++inx )
        {
            long    start   = System.currentTimeMillis();
            avgOf =
                Arrays.stream( dArr ).parallel().average().getAsDouble();
            long    delta   = System.currentTimeMillis() - start;
            totalTime += delta;
            if ( delta < minimumTime )
                minimumTime = delta;
            if ( delta > maximumTime )
                maximumTime = delta;
        }        
        averageTime = totalTime / numIterations;
        
        return avgOf;
    }
    
    /**
     * Run a timed test,
     * sorting the
     * double values in an array,
     * and recording
     * the minimum, maximum and average time.
     * A parallel traversal is employed.
     */
    public void parallelSort()
    {
        init( true );
        double  totalTime       = 0;
        
        for ( int inx = 0 ; inx < numIterations ; ++inx )
        {
            double[]    workingArr  = Arrays.copyOf( dArr, numSortValues );
            long    start   = System.currentTimeMillis();
            Arrays.parallelSort( workingArr );
            long    delta   = System.currentTimeMillis() - start;
            totalTime += delta;
            if ( delta < minimumTime )
                minimumTime = delta;
            if ( delta > maximumTime )
                maximumTime = delta;
        }        
        averageTime = totalTime / numIterations;
    }

    /**
     * Run a timed test,
     * computing the average
     * of double values in an array,
     * and recording
     * the minimum, maximum and average time.
     * A serial (non-parallel) traversal is employed.
     * 
     * @return  the computed average
     */
    public double serialAverage()
    {
        init( false );
        double  totalTime       = 0;
        double  avgOf           = 0;
        
        for ( int inx = 0 ; inx < numIterations ; ++inx )
        {
            long    start   = System.currentTimeMillis();
            avgOf =
                Arrays.stream( dArr ).average().getAsDouble();
            long    delta   = System.currentTimeMillis() - start;
            totalTime += delta;
            if ( delta < minimumTime )
                minimumTime = delta;
            if ( delta > maximumTime )
                maximumTime = delta;
        }        
        averageTime = totalTime / numIterations;
        
        return avgOf;
    }
    
    /**
     * Run a timed test,
     * sorting the
     * double values in an array,
     * and recording
     * the minimum, maximum and average time.
     * A serial (non-parallel) traversal is employed.
     */
    public void serialSort()
    {
        init( false );
        double  totalTime       = 0;
        
        for ( int inx = 0 ; inx < numIterations ; ++inx )
        {
            double[]    workingArr  = Arrays.copyOf( dArr, numSortValues );
            long    start   = System.currentTimeMillis();
            Arrays.sort( workingArr );
            long    delta   = System.currentTimeMillis() - start;
            totalTime += delta;
            if ( delta < minimumTime )
                minimumTime = delta;
            if ( delta > maximumTime )
                maximumTime = delta;
        }        
        averageTime = totalTime / numIterations;
    }
    
    /**
     * Returns the minimum time,
     * in milliseconds,
     * to execute one iteration 
     * of the test.
     * 
     * @return 
     *      the minimum time to execute one iteration 
     *      of the test
     */
    public long getMinimumTime()
    {
        return minimumTime;
    }

    /**
     * Returns the maximum time,
     * in milliseconds,
     * to execute one iteration 
     * of the test.
     * 
     * @return 
     *      the maximum time to execute one iteration 
     *      of the test
     */
    public long getMaximumTime()
    {
        return maximumTime;
    }

    /**
     * Returns the average time,
     * in milliseconds,
     * to execute the test loop.
     * 
     * @return 
     *      the average time to execute the test loop
     */
    public double getAverageTime()
    {
        return averageTime;
    }

    /**
     * Indicates whether or not
     * this test was
     * run using parallel decomposition.
     * 
     * @return true 
     *      if this test was run
     *      using parallel decomposition
     */
    public boolean isParallel()
    {
        return isParallel;
    }
    
    /**
     * Produces a report
     * comparing the timing recorded
     * in this StreamTimer
     * with the timing recorded
     * in another StreamTimer.
     * 
     * @param other the other StreamTimer
     * 
     * @return  
     *      a report comparing the timings
     *      in this object with another
     */
    public String toReportString( String lead, StreamTimer other )
    {
        String[]        labelArr    = getLabels();
        Stream<String>  labelStream = Arrays.stream( labelArr );
        Stream<String>  thisResult  = getVals( this );
        Stream<String>  thatResult  = getVals( other );
        
        String[]        strArr      =
            merge( labelStream, thisResult, (s1, s2) -> s1 + s2 );
        Stream<String>  temp        = Arrays.stream( strArr );
        strArr = 
            merge( temp, thatResult, (s1, s2) -> s1 + "  " + s2 );
        String          colHeadings =
            getColHeadings( labelArr[0], strArr[0], other );
        StringBuilder   bldr        = new StringBuilder();
        bldr.append( lead ).append( newl )
            .append( colHeadings ).append( newl );
        Arrays.stream( strArr ).forEach( s -> bldr.append( s ).append( newl ) );
        return bldr.toString();
    }
    
    /**
     * Gets a stream consisting of the 
     * minimum, maximum and average values
     * in the given StreamTimer instance,
     * in that order.
     * Each value is converted to a String,
     * and formatted in such a way
     * that each String will be
     * the same length,
     * and the values
     * will be right-justified
     * in the strings.
     * 
     * @param   timer   the given StreamTimer instance
     * 
     * @return
     *      stream consisting of the 
     *      minimum, maximum and average values
     *      in the given StreamTimer instance
     */
    private static Stream<String> getVals( StreamTimer timer )
    {
        Stream.Builder<String>  bldr    = Stream.builder();
        bldr
            .add( String.format( "%6d", timer.minimumTime ) )
            .add( String.format( "%6d", timer.maximumTime ) )
            .add( String.format( "%6.2f", timer.averageTime ) );
        Stream<String>          stream  = bldr.build();
        return stream;
    }
    
    /**
     * Gets a stream consisting of  
     * the labels for 
     * the minimum, maximum and average values,
     * in that order,
     * in the report produced by
     * {@linkplain #toReportString(StreamTimer)};
     * Each value 
     * is formatted in such a way
     * that each String will be
     * the same length,
     * and the values
     * will be left-justified
     * in the strings.
     * 
     * @return
     *      stream consisting of the 
     *      minimum, maximum and average labels
     *      required to produce a report
     */
    private static String[] getLabels()
    {
        // get length of longest label
        int     maxLen  = 
            Stream.of( minLabel, maxLabel, avgLabel )
                .mapToInt(String::length)
                .max()
                .getAsInt();
        // force all label strings to be the same length
        String  fmt     = "%-" + maxLen + "s";
        
        Stream.Builder<String>  bldr    = Stream.builder();
        bldr.add( String.format( fmt, minLabel ) )
            .add( String.format( fmt, maxLabel ) )
            .add( String.format( fmt, avgLabel ) );
        String[]    strArr  =
            bldr.build()
                .toArray( String[]::new );
//        Stream<String>          stream  = bldr.build();
//        return stream;
        return strArr;
    }

    /** 
     * Merge two streams of Strings,
     * returning a single String result.
     * The user provides a BinaryOperator<String>
     * which determines
     * how corresponding elements
     * of the two streams are to be combined.
     * It is generally expected
     * that the two streams
     * will be of equal length.
     * In the event 
     * that streamA is longer than streamB
     * the remaining elements
     * of stream A
     * will be combined with the empty string.
     * In the event 
     * that streamB is longer than streamA
     * excess elements in streamB
     * will be ignored.
     * An array of Strings is returned,
     * the length of which
     * will be equal to the length
     * of streamA.
     * Sequential elements of the array
     * will consist of strings
     * produced by the provided combiner.
     * 
     * @param streamA   the first stream to merge
     * @param streamB   the second stream to merge
     * @param joiner    the provided combiner 
     * 
     * @return
     *      an array of strings consisting of
     *      the combined elements of the two streams
     */
    private static String[] merge(
        Stream<String> streamA, 
        Stream<String> streamB, 
        BinaryOperator<String> joiner
    )
    {
        Iterator<String>    iterB   = streamB.iterator();
        String[]            result  =
            streamA.map( s -> 
                joiner.apply(s, iterB.hasNext() ? iterB.next() : "") )
            .toArray( String[]::new );
        return result;
    }
    
    private String 
    getColHeadings( String label, String line, StreamTimer other )
    {
        final String    spaces  =
            "                                        "
            + "                                        ";
        
        int             col1Start   = label.length();
        int             itemLen     = line.length() - col1Start;
        int             col2Start   = col1Start + (int)(itemLen / 2.0 + .5);
        StringBuilder   bldr        = new StringBuilder();
        bldr.append( spaces.substring( 0, col1Start ) );
        bldr.append( isParallel ? "parall " : "sequen  " );
        bldr.append( spaces.substring( 0, col2Start - bldr.length() + 1 ) );
        bldr.append( other.isParallel ? "parall " : "sequen  " );
        
        return bldr.toString();
    }

    /**
     * Helper method for initializing
     * the test parameters.
     * 
     * @param isParallel    indicates whether this is a parallel test
     */
    private void init( boolean isParallel )
    {
        minimumTime = Integer.MAX_VALUE;
        maximumTime = 0;
        averageTime = 0;
        this.isParallel = isParallel;
    }
}
