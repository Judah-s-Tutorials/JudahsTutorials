package com.acmemail.judah.anonymous_classes.app;

/**
 * This application executes tests
 * comparing the time required
 * to execute a parallel algorithm
 * and a sequential algorithm.
 * 
 * @author Jack Straub
 * 
 * @see StreamTimer
 */
public class PSTimeComparison
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        StreamTimer seqTimer    = new StreamTimer();
        StreamTimer parTimer    = new StreamTimer();
        
        seqTimer.sequentialAverage();
        parTimer.parallelAverage();
        String  report          = 
            seqTimer.toReportString( "Averaging", parTimer );
        System.out.println( report );
        
        seqTimer.sequentialSort();
        parTimer.parallelSort();
        report = seqTimer.toReportString( "Sorting", parTimer );
        System.out.print( report );
    }

}
