package com.gmail.johnstraub1954.penrose;

import java.awt.geom.Path2D;

public class PKite extends PShape
{
    public PKite( double longSide )
    {
        super( longSide );
    }
    
    public PKite( double longSide, double xco, double yco )
    {
        super( longSide );
        moveTo( xco, yco );
    }
    
    public Path2D initPath( double longSide )
    {
        double  midXOff     = longSide * Math.cos( D36 );
        double  midYOff     = longSide * Math.sin( D36 );
        double  rightXOff   = longSide;
        double  bottomYOff  = 2 * midYOff;
        
        Path2D  path        = new Path2D.Double();
        path.reset();
        path.moveTo( 0, midYOff );
        path.lineTo( midXOff, 0 );
        path.lineTo( midXOff, 0 );
        path.lineTo( rightXOff, midYOff );
        path.lineTo( midXOff, bottomYOff );
        path.lineTo( 0, midYOff );
        return path;
    }
}
