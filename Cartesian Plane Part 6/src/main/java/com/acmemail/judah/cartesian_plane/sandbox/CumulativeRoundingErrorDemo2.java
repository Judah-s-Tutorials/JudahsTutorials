package com.acmemail.judah.cartesian_plane.sandbox;

/**
 * This application demonstrates that, using float values,
 * adding .99 10,000 times is <em>not</em> the same
 * as .99 * 10,000.
 * 
 * @author Jack Straub
 *
 */
public class CumulativeRoundingErrorDemo2
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        String  fmt     = "sum: %8.2f product %8.2f%n";
        float   sum     = 0;
        float   incr    = .99f;
        int     repeat  = 10000;
        for ( int inx = 1 ; inx <= repeat ; ++inx )
        {
            sum += incr;
            float   product     = inx * incr;
            System.out.printf( fmt, sum, product );
        }
    }
}
