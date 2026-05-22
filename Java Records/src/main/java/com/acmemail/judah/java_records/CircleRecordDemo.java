package com.acmemail.judah.java_records;

import java.awt.geom.Point2D;

/**
 * Sample application to show portray a traditional class,
 * and the same class implemented as a record.
 */
public class CircleRecordDemo
{
    /**
     * Application entry point.
     * 
     * @param args  command-line arguments, not used
     */
    public static void main(String[] args)
    {
        Point2D             center  = new Point2D.Double( 25, 25 );
        CircleTraditional   circleC = new CircleTraditional( 100, center );
        CircleRecord        circleR = new CircleRecord( 100, center );
        System.out.println( circleC.getDiameter() ); // traditional getter
        System.out.println( circleR.diameter() ); // record getter
        System.out.println( circleC );
        System.out.println( circleR );
    }
}
