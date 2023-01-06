package com.acmemail.judah.cartesian_plane.sandbox;

/**
 * This application demonstrates that, using float values,
 * adding .99 10,000 times is <em>not</em> the same
 * as .99 * 10,000.
 * 
 * @author Jack Straub
 *
 */
public class CumulativeRoundingErrorDemo
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments; not used
     */
    public static void main(String[] args)
    {
        String  fmt     = "%8.2f %5d %8.2f %5d%n";
        float   next    = 0;
        float   incr    = .99f;
        int     repeat  = 10000;
        for ( int inx = 0 ; inx <= repeat ; ++inx )
        {
            float   calc        = inx * incr;
            int     calcRounded = (int)(calc + .5);
            int     nextRounded = (int)(next + .5);
            System.out.printf( fmt, next, nextRounded, calc, calcRounded );
            next += incr;
        }
    }
}
