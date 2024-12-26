package com.gmail.johnstraub1954.penrose;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

public class PDart extends PShape
{
    public PDart( double longSide )
    {
        super( longSide );
    }
    
    public PDart( double longSide, double xco, double yco )
    {
        super( longSide );
        moveTo( xco, yco );
    }
    
    @Override
    public Path2D initPath(double longSide)
    {
        double  base    = longSide * ( Math.sin( D36 ) / Math.sin( D108 ) );
        
        System.out.println( longSide + ", " + base );
        double  rightXco    = longSide * Math.cos( D36 );
        double  midYco      = longSide * Math.sin( D36 );
        
        double  midXOff     = rightXco - base;
        double  midYOff     = midYco;
        double  rightXOff   = rightXco;
        double  bottomYOff  = 2 * midYOff;
        
        Path2D  path        = new Path2D.Double();
        path.reset();
        path.moveTo( 0, 0 );
        path.lineTo( rightXOff, midYOff );
        path.lineTo( 0, bottomYOff );
        path.lineTo( midXOff, midYOff );
        path.lineTo( 0, 0 );
        
        double      diam    = getDotDiam();
        double      half    = diam / 2;
        Ellipse2D   dot = 
            new Ellipse2D.Double( diam, diam + half, diam, diam );
        path.append( dot, false );
        dot = new Ellipse2D.Double( 
            diam, 
            bottomYOff - (diam + diam + half), 
            diam, 
            diam
        );
        path.append( dot, false );

        return path;
    }
}
