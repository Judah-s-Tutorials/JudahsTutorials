package com.gmail.johnstraub1954.penrose2;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Vertex
{
    private static final double twoPI   = 2 * Math.PI;
    
    private final Point2D   coords;
    private final double    angle;
    private final double    adjSide;
    private final boolean   isDotted;
    public Vertex( 
        Point2D coords, 
        double  angle, 
        double  adjSide, 
        boolean isDotted 
    )
    {
        this.coords = coords;
        this.angle = angle;
        this.adjSide = adjSide;
        this.isDotted = isDotted;
    }
    
    public Vertex( 
        Vertex from, 
        double angle, 
        double adjSide, 
        boolean isDotted
    )
    {
        this.coords = from.getNext();
        this.angle = (from.angle + angle) % twoPI;
        this.adjSide = adjSide;
        this.isDotted = isDotted;
    }
    
    public Point2D getCoords()
    {
        return coords;
    }

    public double getAngle()
    {
        return angle;
    }
    
    public double getAdjSide()
    {
        return adjSide;
    }
    
    public boolean isDotted()
    {
        return isDotted;
    }
    
    public Point2D getNext()
    {
        double  xco     = coords.getX() + Math.cos( angle ) * adjSide;
        double  yco     = coords.getY() - Math.sin( angle ) * adjSide;
        Point2D end     = new Point2D.Double( xco, yco );
        return end;
    }

    public Line2D getAdjLine()
    {
        double  xco     = coords.getX() + Math.cos( angle ) * adjSide;
        double  yco     = coords.getY() - Math.sin( angle ) * adjSide;
        Point2D end     = new Point2D.Double( xco, yco );
        Line2D  line    = new Line2D.Double( coords, end );
        return line;
    }
}
