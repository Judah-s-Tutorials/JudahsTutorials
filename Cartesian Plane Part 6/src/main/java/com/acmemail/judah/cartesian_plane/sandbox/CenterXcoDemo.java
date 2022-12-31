package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.geom.Rectangle2D;

/**
 * Verify that Rectangle2D.getCenterX/.getCenterY
 * return the expected value.
 * 
 * @author Jack Straub
 *
 */
public class CenterXcoDemo
{
    /**
     * Application entry point.
     * 
     * @param args  command line arguments, not used
     */
    public static void main(String[] args)
    {
        printResult( new Rectangle2D.Double( 0, 0, 256, 128 ) );
        printResult( new Rectangle2D.Double( 0, 0, 257, 129 ) );
        printResult( new Rectangle2D.Double( 0, 0, 3, 3 ) );
        printResult( new Rectangle2D.Double( 0, 0, 4, 4 ) );
    }
    
    private static void printResult( Rectangle2D rect )
    {
        String  expFmtX     = "expected center xco = %f%n";
        String  actFmtX     = "width= %f, centerX=%f%n";
        String  expFmtY     = "expected center yco = %f%n";
        String  actFmtY     = "height=%f, centerY=%f%n";
        double  width       = rect.getWidth();
        double  actCenterX  = rect.getCenterX();
        double  expCenterX  = (width - 1) / 2;
        double  height      = rect.getHeight();
        double  actCenterY  = rect.getCenterY();
        double  expCenterY  = (height - 1) / 2;
        
        System.out.printf( expFmtX, expCenterX );
        System.out.printf( actFmtX, width, actCenterX );
        System.out.printf( expFmtY, expCenterY );
        System.out.printf( actFmtY, width, actCenterY );
        System.out.println( "*********************" );
    }

}
