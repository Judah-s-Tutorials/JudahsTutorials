package com.acmemail.judah.color_primer;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class LineExt
{
    private Line2D  line2D  = new Line2D.Double();
    
    public LineExt()
    {
    }
    
    public LineExt( Line2D line )
    {
        line2D.setLine( line );
    }
    
    public LineExt( double xco1, double yco1, double xco2, double yco2 )
    {
        line2D.setLine( xco1, yco1, xco2, yco2 );
    }
    
    public void setLine( Line2D line )
    {
        line2D.setLine( line );
    }
    
    public void setLine( double xco1, double yco1, double xco2, double yco2 )
    {
        line2D.setLine( xco1, yco1, xco2, yco2 );
    }
    
    public void setP2( double xco2, double yco2 )
    {
        Point2D p1  = line2D.getP1();
        Point2D p2  = new Point2D.Double( xco2, yco2 );
        line2D.setLine( p1, p2 );
    }
    
    public Rectangle2D getBounds2D()
    {
        return line2D.getBounds2D();
    }
    
    public void draw( Graphics2D gtx )
    {
        gtx.draw( line2D );
    }
    
    public Line2D getLine2D()
    {
        return line2D;
    }
    
    public double getLength()
    {
        double  length  = line2D.getP1().distance( line2D.getP2() );
        return length;
    }
    
    public double getAngle()
    {
        double      deltaX      = line2D.getX2() - line2D.getX1();
        // reverse delta-y measurement because
        // java y values increase moving downwards
        double      deltaY      = line2D.getY1() - line2D.getY2();
        double      radians     = Math.atan2( deltaY, deltaX );
        return radians;
    }
    
    public double getDegrees()
    {
        double      radians     = getAngle();
        double      degrees     = Math.toDegrees( radians );
        return degrees;
    }
}
