package com.gmail.johnstraub1954.penrose;

import java.awt.geom.Point2D;

public class RotateContext
{
    private final double    pinXco;
    private final double    pinYco;
    private final double    cosine;
    private final double    sine;
    
    public RotateContext( double pinXco, double pinYco, double radians )
    {
        this.pinXco = pinXco;
        this.pinYco = pinYco;
        cosine = Math.cos( radians );
        sine = Math.sin( radians );
    }
    
    public Point2D rotate( double xco, double yco )
    {
        double  transXco    = xco - pinXco;
        double  transYco    = yco - pinYco;
        double  newXco      = transXco * cosine - transYco * sine; 
        double  newYco      = transXco * sine + transYco * cosine; 
        
        newXco += pinXco;
        newYco += pinYco;
        Point2D point   = new Point2D.Double( newXco, newYco );
        return point;
    }
}
