package com.acmemail.judah.cartesian_plane.sandbox.app;

import java.awt.geom.Rectangle2D;

import com.acmemail.judah.cartesian_plane.LineGenerator;

public class LineGeneratorDemo1
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        int             dim         = 601;
        int             gridUnit    = 100;
        Rectangle2D     rect        = 
            new Rectangle2D.Double( 0, 0, dim, dim );
        LineGenerator   lineGen     = 
            new LineGenerator( rect, gridUnit, 1 );
        lineGen.forEach( l -> {
            String  str = "GL: " + l.getP1() + "/" + l.getP2();
            System.out.println( str );
        });
        
        lineGen = new LineGenerator( rect, 513, 1 );
        lineGen.forEach( l -> {
            String  str = "AX: " + l.getP1() + "/" + l.getP2();
            System.out.println( str );
        });
    }
    
}
