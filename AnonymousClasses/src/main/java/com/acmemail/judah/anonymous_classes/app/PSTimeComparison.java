package com.acmemail.judah.anonymous_classes.app;

/**
 * This application executes tests
 * comparing the time required
 * to execute a parallel algorithm
 * and a serial algorithm.
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
        StreamTimer serTimer    = new StreamTimer();
        StreamTimer parTimer    = new StreamTimer();
        
        serTimer.serialAverage();
        parTimer.parallelAverage();
        String  report          = 
            serTimer.toReportString( "Averaging", parTimer );
        System.out.println( report );
        
        serTimer.serialSort();
        parTimer.parallelSort();
        report = serTimer.toReportString( "Sorting", parTimer );
        System.out.print( report );
    }

}
